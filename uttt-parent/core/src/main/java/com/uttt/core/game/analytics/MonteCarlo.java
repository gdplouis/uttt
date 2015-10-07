package com.uttt.core.game.analytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.uttt.common.Foreachable;
import com.uttt.common.StackFrameUtil;
import com.uttt.core.game.Game;
import com.uttt.core.game.Game.GameStatus;

/**
 * Runs multiple instances of {@link Game} and reports results. To help mitigate 1st player advantage, half the games
 * will be ran with Player A going first and the other half of the game with Player B going first.
 * <p>
 * usage:
 * <p>
 * [-1 Player1] [-2 Player2]
 * <p>
 * Where both -1 and -2 are optional, and Player1 and Player2 are the names of Player implementations under the package
 * com.uttt.core.player.impl. If either args are omitted, PlayerUnpredictable is used for that player.
 */
public class MonteCarlo {

    private static final Logger log = StackFrameUtil.methodLogger();

    private final int           runs;
    private final int           height;
    private final int           size;

    private final List<Game>    games;
    private final AtomicInteger numCompletedGames;

    private long                tic;
    private long                toc;

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

    public static MonteCarlo init(int runCount, String player1, String player2, int height, int size) {
        return init(player1, player2, runCount, height, size);
    }

    public static MonteCarlo init(String player1, String player2, int runs, int height, int size) {
        final MonteCarlo mc = new MonteCarlo(runs, height, size);
        mc.init(player1, player2);
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

    private void init(String player1, String player2) {

        if (!games.isEmpty()) {
            log.info("Already initialized, continuing");
            return;
        }

        for (int i = 0; i < runs; i++) {
            games.add(new Game());
        }
        games.subList(0, runs / 2).stream().forEach(game -> {
            try {
                game.initGame(player1, player2, height, size);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        games.subList(runs / 2, runs).stream().forEach(game -> {
            try {
                game.initGame(player2, player1, height, size);
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

        final long tiedGames = statuses1.stream().filter(status -> status == GameStatus.GAME_ENDED_TIE).count()
                + statuses2.stream().filter(status -> status == GameStatus.GAME_ENDED_TIE).count();
        final long player1Games = statuses1.stream().filter(status -> status == GameStatus.GAME_ENDED_PLAYER_AAA)
                .count()
                + statuses2.stream().filter(status -> status == GameStatus.GAME_ENDED_PLAYER_BBB).count();
        final long player2Games = statuses1.stream().filter(status -> status == GameStatus.GAME_ENDED_PLAYER_BBB)
                .count()
                + statuses2.stream().filter(status -> status == GameStatus.GAME_ENDED_PLAYER_AAA).count();

        final double percentageTied = percentage(tiedGames, runs) * 100.0;
        final double percentageAAA = percentage(player1Games, runs) * 100.0;
        final double percentageBBB = percentage(player2Games, runs) * 100.0;

        log.info(String.format("Total time=%s", prettyDuration(tic, toc)));
        log.info(String.format("Tied games=%.2f%%", percentageTied));
        log.info(String
                .format("%s games=%.2f%%", games.get(0).getPlayerAAA().getClass().getSimpleName(), percentageAAA));
        log.info(String
                .format("%s games=%.2f%%", games.get(0).getPlayerBBB().getClass().getSimpleName(), percentageBBB));
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
        final PeriodFormatter formatter = new PeriodFormatterBuilder().appendDays().appendSuffix("d").appendHours()
                .appendSuffix("h").appendMinutes().appendSuffix("m").appendSeconds().appendSuffix("s").appendMillis()
                .appendSuffix("ms").toFormatter();
        return formatter.print(duration.toPeriod());
    }

    /**
	 * @param height
	 * @param size (unused)
	 */
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

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {

        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("1")
                .withDescription("Player 1 class name [default: PlayerUnpredictable]").hasArg()
                .withArgName("CLASS_NAME").create());
        options.addOption(OptionBuilder.withLongOpt("2")
                .withDescription("Player 2 class name [default: PlayerUnpredictable]").hasArg()
                .withArgName("CLASS_NAME").create());
        options.addOption(OptionBuilder.withLongOpt("h").withDescription("Max board height [default: 3]").hasArg()
                .withArgName("HEIGHT").create());
        options.addOption(OptionBuilder.withLongOpt("s").withDescription("Max board size [default: 5]").hasArg()
                .withArgName("SIZE").create());
        options.addOption(OptionBuilder.withLongOpt("n").withDescription("Number of game runs to execute").hasArg()
                .withArgName("NUMRUNS").create());

        final String p1String;
        final String p2String;
        final int maxHeight;
        final int maxSize;
        final int runCount;
        try {
            CommandLine line = new BasicParser().parse(options, args);

            p1String = line.getOptionValue("1", "PlayerUnpredictable");
            p2String = line.getOptionValue("2", "PlayerUnpredictable");

            maxHeight = Integer.valueOf(line.getOptionValue("h", "1"));
            maxSize   = Integer.valueOf(line.getOptionValue("s", "3"));

            runCount = Integer.valueOf(line.getOptionValue("n", (""+genNumRuns(maxHeight, maxSize))));
        }
        catch (ParseException exp) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.setOptionComparator(new Comparator<Option>() {
                private static final String OPTS_ORDER = "12hsn";

                @Override
                public int compare(Option o1, Option o2) {
                    return OPTS_ORDER.indexOf(o1.getLongOpt()) - OPTS_ORDER.indexOf(o2.getLongOpt());
                }
            });
            formatter
                    .printHelp(
                            120,
                            "game",
                            "Run game",
                            options,
                            "Concrete player classes must be placed in com.uttt.core.player[.*]. Board height must be in [1..3] and board size must be in [2..5]",
                            true);
            throw exp;
        }

        log.info("  Player 1: " + p1String);
        log.info("  Player 2: " + p2String);
        log.info("Max Height: " + maxHeight);
        log.info("Max Size  : " + maxSize);

        for (int height : Foreachable.to(1, maxHeight)) {
            for (int size : Foreachable.to(2, maxSize)) {
                System.gc();
                MonteCarlo.init(runCount, p1String, p2String, height, size).run();
            }
        }
    }
}
