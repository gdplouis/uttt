package com.uttt.core.analytics;

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
import com.uttt.core.game.Game;
import com.uttt.core.game.PlayerUnpredictable;
import com.uttt.core.game.PlayerWeight;
import com.uttt.core.game.Game.GameStatus;
import com.uttt.core.player.Player;

public class MonteCarlo {

	private static final Logger log = StackFrameUtil.methodLogger();

	private final int runs;
	private final int height;
	private final int size;

	private final List<Game> games;
	private final AtomicInteger numCompletedGames;

	private long tic;
	private long toc;

	private MonteCarlo(int runs, int height, int size) {

		if (runs % 2 != 0) {
			throw new RuntimeException("number of runs must be even");
		}

		this.runs = runs;
		this.height = height;
		this.size = size;

		games = new ArrayList<>(runs);
		numCompletedGames = new AtomicInteger(0);
	}

	public static MonteCarlo init(Class<? extends Player> klass1, Class<? extends Player> klass2, int height, int size) {
		return init(klass1, klass2, genNumRuns(height, size), height, size);
	}

	public static MonteCarlo init(Class<? extends Player> klass1, Class<? extends Player> klass2, int runs, int height, int size) {
		final MonteCarlo mc = new MonteCarlo(runs, height, size);
		mc.init(klass1, klass2);
		return mc;
	}

	public void run() {

		log.info("");
		log.info("***********");
		log.info("");
		log.info(String.format("Runs=%d, Board Height=%d, Board Size=%d", runs, height, size));
		log.info("Baking...");

		tic = System.currentTimeMillis();

		games.stream().forEach(game -> {
			game.play();
			 final int num = numCompletedGames.incrementAndGet();
			 logPrecentageStatus(num, "%.0f%% ");
		});

		toc = System.currentTimeMillis();

		analyze();
	}

	private void init(Class<? extends Player> klass1, Class<? extends Player> klass2) {

		if (!games.isEmpty()) {
			log.info("Already initialized, continuing");
			return;
		}

		for (int i = 0; i < runs; i++) {
			games.add(new Game());
		}
		games.subList(0, runs / 2).stream().forEach(game -> {
			try {
				game.initGame(klass1, klass2, height, size);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		games.subList(runs / 2, runs).stream().forEach(game -> {
			try {
				game.initGame(klass2, klass1, height, size);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void analyze() {

		log.info("Serving...");

		final List<Game.GameStatus> statuses1 = Collections.synchronizedList(new ArrayList<>(runs / 2));
		final List<Game.GameStatus> statuses2 = Collections.synchronizedList(new ArrayList<>(runs / 2));
		IntStream.range(0, runs / 2).boxed().forEach(i -> {
			statuses1.add(i, games.get(i).getStatus());
			statuses2.add(i, games.get(i + runs / 2).getStatus());
		});

		final long tiedGames = statuses1.stream().filter(status -> status == GameStatus.GAME_ENDED_TIE).count() + statuses2.stream().filter(status -> status == GameStatus.GAME_ENDED_TIE).count();
		final long player1Games = statuses1.stream().filter(status -> status == GameStatus.GAME_ENDED_PLAYER_AAA).count()
				+ statuses2.stream().filter(status -> status == GameStatus.GAME_ENDED_PLAYER_BBB).count();
		final long player2Games = statuses1.stream().filter(status -> status == GameStatus.GAME_ENDED_PLAYER_BBB).count()
				+ statuses2.stream().filter(status -> status == GameStatus.GAME_ENDED_PLAYER_AAA).count();

		final double percentageTied = percentage(tiedGames, runs) * 100.0;
		final double percentageAAA = percentage(player1Games, runs) * 100.0;
		final double percentageBBB = percentage(player2Games, runs) * 100.0;

		log.info(String.format("Total time=%s", prettyDuration(tic, toc)));
		log.info(String.format("Tied games=%.2f%%", percentageTied));
		log.info(String.format("%s games=%.2f%%", games.get(0).getPlayerAAA().getClass().getSimpleName(), percentageAAA));
		log.info(String.format("%s games=%.2f%%", games.get(0).getPlayerBBB().getClass().getSimpleName(), percentageBBB));
	}

    private void logPrecentageStatus(int i, String message) {
        if (log.isTraceEnabled()) {
            if (runs > 100) {
                if (i % (runs / 100) == 0) {
                    log.trace(String.format(message, percentage(i, runs) * 100.0));
                }
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
		final PeriodFormatter formatter = new PeriodFormatterBuilder().appendDays().appendSuffix("d").appendHours().appendSuffix("h").appendMinutes().appendSuffix("m").appendSeconds()
				.appendSuffix("s").appendMillis().appendSuffix("ms").toFormatter();
		return formatter.print(duration.toPeriod());
	}

	private static int genNumRuns(int height, int size) {
		// Probably sufficient for Monte Carlo...?
		switch (height) {
		case 1:
			return 10000;
		case 2:
			return 5000;
		case 3:
			return 2000;
		default:
			return 1000;
		}
	}

	public static void main(String[] args) {
		for (int height = 1; height <= 3; height++) {
			for (int size = 2; size <= 5; size++) {
				System.gc();
				MonteCarlo.init(PlayerWeight.class, PlayerUnpredictable.class, height, size).run();
			}
		}
	}
}
