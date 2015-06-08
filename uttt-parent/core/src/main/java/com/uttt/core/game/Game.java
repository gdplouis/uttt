package com.uttt.core.game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.reflections.Reflections;

import com.uttt.common.ExUtil;
import com.uttt.common.StackFrameUtil;
import com.uttt.common.UtttException;
import com.uttt.core.board.Board;
import com.uttt.core.board.Position;
import com.uttt.core.board.Token;
import com.uttt.core.player.Player;

/**
 * This class is responsible for a single session of a tic-tac-toe Game.
 * <p>
 * usage:
 * <p>
 * [-x PlayerX] [-o PlayerO]
 * <p>
 * Where both -x and -o are optional, and PlayerX and PlayerO are the names of Player implementations under the package
 * com.uttt.core.player.impl. If either args are omitted, PlayerUnpredictable is used for that player.
 * <p>
 */
public class Game {

    public enum GameStatus {
        PLAYER_AAA_TURN, PLAYER_BBB_TURN, GAME_ENDED_TIE, GAME_ENDED_PLAYER_AAA, GAME_ENDED_PLAYER_BBB,
    }

    private Pair<Player, Player>                      players;
    private Player                                    currentPlayer;
    private Board                                     board;
    private Logger                                    log;
    private GameStatus                                status;

    static final Map<String, Class<? extends Player>> cachedPlayerClasses;
    static {
        final Map<String, Class<? extends Player>> map = new HashMap<>();
        final Reflections reflections = new Reflections("com.uttt.core.player.impl");
        for (Class<? extends Player> playerClass : reflections.getSubTypesOf(Player.class)) {
            if (!Modifier.isAbstract(playerClass.getModifiers())) {
                map.put(playerClass.getSimpleName(), playerClass);
            }
        }
        cachedPlayerClasses = Collections.unmodifiableMap(map);
    }

    public Game initGame(String stringAAA, String stringBBB, int height, int size) throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
        validatePlayerString(stringAAA);
        validatePlayerString(stringBBB);
        return initGame(cachedPlayerClasses.get(stringAAA), cachedPlayerClasses.get(stringBBB), height, size);
    }

    public Game initGame(Class<? extends Player> classAAA, Class<? extends Player> classBBB, int height, int size)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            SecurityException {
        return initGame(Player.create(classAAA, Token.PLAYER_AAA), Player.create(classBBB, Token.PLAYER_BBB), height,
                size);
    }

    public Game initGame(Player playerAAA, Player playerBBB, int height, int size) {

        if (playerAAA.getToken() == playerBBB.getToken()) {
            throw ExUtil.create(UtttException.UnuniquePlayers.class)
                    .append(String.format("found two players with '%s' token", playerAAA.getToken())).build();
        }
        if (playerAAA.getToken() == Token.EMPTY) {
            throw ExUtil.create(UtttException.UnuseableToken.class)
                    .append(String.format("player AAA is using '%s' token", playerAAA.getToken())).build();
        }
        if (playerBBB.getToken() == Token.EMPTY) {
            throw ExUtil.create(UtttException.UnuseableToken.class)
                    .append(String.format("player BBB is using '%s' token", playerBBB.getToken())).build();
        }

        players = Pair.of(playerAAA, playerBBB);
        board = new Board(height, size);
        log = StackFrameUtil.methodLogger();
        switchTurn();

        return this;
    }

    public void play() {

        Position constraint = null;

        while (true) {

            final Move move = currentPlayer.makeMove(log, board.copyDeep(), constraint);
            final Position position = move.toPosition(board);
            constraint = position.place(currentPlayer.getToken());

            log.trace("after placing:\n" + board.fieldAsPrintableString());

            if (!board.isPlayable()) {
                break;
            }

            switchTurn();
        }

        switch (board.getStatus()) {
        case DRAW:
            status = GameStatus.GAME_ENDED_TIE;
            break;
        case WINNER_AAA:
            status = GameStatus.GAME_ENDED_PLAYER_AAA;
            break;
        case WINNER_BBB:
            status = GameStatus.GAME_ENDED_PLAYER_BBB;
            break;
        default:
            throw new RuntimeException("Unexpected game status '" + board.getStatus() + "' when game supposedly ended");
        }
    }

    public GameStatus getStatus() {
        return status;
    }

    public Board getBoard() {
        return board.copyDeep();
    }

    public Player getPlayerAAA() {
        return players.getLeft();
    }

    public Player getPlayerBBB() {
        return players.getRight();
    }

    private void switchTurn() {
        currentPlayer = currentPlayer == null || currentPlayer == getPlayerBBB() ? getPlayerAAA() : getPlayerBBB();
        status = currentPlayer.getToken() == Token.PLAYER_AAA ? GameStatus.PLAYER_AAA_TURN : GameStatus.PLAYER_BBB_TURN;
    }

    private static void validatePlayerString(String playerString) {
        if (!cachedPlayerClasses.containsKey(playerString)) {
            throw new RuntimeException(playerString
                    + " is either not in the correct package or is not a concrete Player implementation.");
        }
    }

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {

        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("x")
                .withDescription("Player 1 class name [default: PlayerUnpredictable]").hasArg()
                .withArgName("CLASS_NAME").create());
        options.addOption(OptionBuilder.withLongOpt("o")
                .withDescription("Player 2 class name [default: PlayerUnpredictable]").hasArg()
                .withArgName("CLASS_NAME").create());
        options.addOption(OptionBuilder.withLongOpt("h").withDescription("Board height [default: 1]").hasArg()
                .withArgName("HEIGHT").create());
        options.addOption(OptionBuilder.withLongOpt("s").withDescription("Board size [default: 3]").hasArg()
                .withArgName("SIZE").create());

        final String p1String;
        final String p2String;
        final int height;
        final int size;
        try {
            CommandLine line = new BasicParser().parse(options, args);
            p1String = line.getOptionValue("x", "PlayerUnpredictable");
            p2String = line.getOptionValue("o", "PlayerUnpredictable");
            height = Integer.valueOf(line.getOptionValue("h", "1"));
            size = Integer.valueOf(line.getOptionValue("s", "3"));

        } catch (ParseException exp) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.setOptionComparator(new Comparator<Option>() {
                private static final String OPTS_ORDER = "xohs";

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

        final Logger log = StackFrameUtil.methodLogger();

        log.info("     X: " + p1String);
        log.info("     O: " + p2String);
        log.info("Height: " + height);
        log.info("  Size: " + size);

        final Game game = new Game();
        game.initGame(p1String, p2String, height, size).play();

        switch (game.getStatus()) {
        case GAME_ENDED_TIE:
            log.info("Game Tied");
            break;
        case GAME_ENDED_PLAYER_AAA:
            log.info("Player X Wins!");
            break;
        case GAME_ENDED_PLAYER_BBB:
            log.info("Player O Wins!");
            break;
        default:
            break;
        }

        log.info(game.getBoard().fieldAsPrintableString());
    }
    
    @Override
    public String toString() {
        return getPlayerAAA().toString() + " vs. " + getPlayerBBB().toString();  
    }
}
