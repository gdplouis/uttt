package com.uttt.common.analytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.uttt.common.StackFrameUtil;
import com.uttt.common.board.Token;
import com.uttt.common.game.Game;
import com.uttt.common.game.Game.GameStatus;
import com.uttt.common.game.GameImpl;
import com.uttt.common.game.PlayerUnpredictable;

public class MonteCarlo {

	private static final Logger log = StackFrameUtil.methodLogger();
	
	private final int runs;
	private final int height;
	private final int size;

	private final List<Game> games;
	private final AtomicInteger numCompletedGames;

	private long tic;
	private long toc;

	public MonteCarlo(int height, int size) {
		this(genNumRuns(height, size), height, size);
	}
	
	public MonteCarlo(int runs, int height, int size) {
		this.runs = runs;
		this.height = height;
		this.size = size;

		games = new ArrayList<>(runs);
		numCompletedGames = new AtomicInteger(0);
	}
	
	public MonteCarlo init() {
		for (int i = 0; i < runs; i++) {
			games.add(new GameImpl());
		}
		
		games.parallelStream().forEach(
				game -> {
					game.initGame(
							PlayerUnpredictable.create(Token.PLAYER_AAA),
							PlayerUnpredictable.create(Token.PLAYER_BBB),
							height, size);
				});
		
		return this;
	}

	public void run() {

		log.info("");
		log.info("***********");
		log.info("");
		log.info(String.format("Runs=%d, Board Height=%d, Board Size=%d", runs, height, size));
		log.info("Baking...");

		tic = System.currentTimeMillis();

		games.parallelStream().forEach(
				game -> {
					game.play();
					final int num = numCompletedGames.incrementAndGet();
					logPrecentageStatus(num, "%.0f%% ");
				});

		toc = System.currentTimeMillis();

		analyze();
	}

	private void analyze() {

		log.info("Serving...");
		
		final List<Game.GameStatus> statuses = Collections.synchronizedList(new ArrayList<>(runs));
		IntStream.range(0, runs).boxed().forEach(i -> {
			statuses.add(i, games.get(i).getStatus());
		});
		
		final long tiedGames = statuses.parallelStream()
				.filter(status -> status == GameStatus.GAME_ENDED_TIE)
				.count();
		final long playerAAAGames = statuses.parallelStream()
				.filter(status -> status == GameStatus.GAME_ENDED_PLAYER_AAA)
				.count();
		final long playerBBBGames = statuses.parallelStream()
				.filter(status -> status == GameStatus.GAME_ENDED_PLAYER_BBB)
				.count();

		final double percentageTied = percentage(tiedGames, runs) * 100.0;
		final double percentageAAA = percentage(playerAAAGames, runs) * 100.0;
		final double percentageBBB = percentage(playerBBBGames, runs) * 100.0;

		log.info(String.format("Total time=%s", prettyDuration(tic, toc)));
		log.info(String.format("Tied games=%.2f%%", percentageTied));
		log.info(String.format("Player AAA games=%.2f%%", percentageAAA));
		log.info(String.format("Player BBB games=%.2f%%", percentageBBB));
	}

	private void logPrecentageStatus(int i, String message) {
		if (runs > 100) {
			if (i % (runs / 100) == 0) {
				log.debug(String.format(message, percentage(i, runs) * 100.0) );
			}
		}
	}

	private static double percentage(long numerator, long denominator) {
		return percentage((double) numerator, (double) denominator);
	}

	private static double percentage(double numerator, double denominator) {
		return numerator / denominator;
	}

	private static String prettyDuration(long begin, long end) {
		final Duration duration = new Duration(end - begin); // in milliseconds
		final PeriodFormatter formatter = new PeriodFormatterBuilder()
				.appendDays().appendSuffix("d")
				.appendHours().appendSuffix("h")
				.appendMinutes().appendSuffix("m")
				.appendSeconds().appendSuffix("s")
				.appendMillis().appendSuffix("ms")
				.toFormatter();
		return formatter.print(duration.toPeriod());
	}
	
	private static int genNumRuns(int height, int size) {
		// Probably sufficient for Monte Carlo...?
		return (int) Math.pow(Math.pow(Math.pow(10, size), height), 0.5);
	}

	public static void main(String[] args) {
		for (int height = 1; height <= 3; height++) {
			for (int size = 2; size <= 5; size++) {
				System.gc();
				new MonteCarlo(height, size).init().run();
			}
		}
	}
}
