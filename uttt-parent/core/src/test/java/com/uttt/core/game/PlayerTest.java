package com.uttt.core.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.uttt.common.StackFrameUtil;
import com.uttt.core.board.Board;
import com.uttt.core.board.Position;
import com.uttt.core.board.Token;
import com.uttt.core.game.Move;
import com.uttt.core.game.PlayerPredictable;
import com.uttt.core.player.Player;

public class PlayerTest {

    @Test
    public void random_h1s3() {

        final Logger log = StackFrameUtil.methodLogger();
        log.trace("-------------------------------------------------------------");

        Board  board     = new Board(1,3);
        Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
        Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

        Player player = playerAAA;
        while (board.isPlayable()) {
            Move     move       = player.makeMove(log, board, null);
            Position position   = move.toPosition(board);
            Position constraint = position.place(player.getToken());

            assertNull(constraint);

            player = (player == playerAAA) ? playerBBB : playerAAA;

            log.trace("after placing:\n" + board.fieldAsPrintableString());
        }

        String expected = "" //
                + "XXXXXXX\n" //
                + "Xx|o|xX\n" //
                + "X-----X\n" //
                + "Xo|x|oX\n" //
                + "X-----X\n" //
                + "X.|.|xX\n" //
                + "XXXXXXX\n" //
                + "TOP.\n" //
        ;

        assertEquals(expected, board.fieldAsPrintableString());
    }

    @Test
    public void random_h2s3_ignoreConstraints() {

        final Logger log = StackFrameUtil.methodLogger();
        log.trace("-------------------------------------------------------------");

        Board  board     = new Board(2,3); // 81 token positions
        Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
        Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

        Player player = playerAAA;
        for (int i = 0; board.isPlayable() && (i < 100); ++i) {
            log.trace("#"+i);

            Move     move       = player.makeMove(log, board, null);
            log.trace(move.toString() + " by " + player.getToken());

            Position position   = move.toPosition(board);
            Position constraint = position.place(player.getToken());
            log.trace("Next move constraint (ignored): " + constraint);

            log.trace("after placing:\n" + board.fieldAsPrintableString());

            player = (player == playerAAA) ? playerBBB : playerAAA;
        }

        String expected = "" //
                + "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
                + "X???????||       ||XXXXXXXX\n" //
                + "X?x|o|x?|| o|.|. ||Xx|x|xXX\n" //
                + "X?-----?|| ----- ||X-----XX\n" //
                + "X?o|x|x?|| x|o|o ||Xx|x|.XX\n" //
                + "X?-----?|| ----- ||X-----XX\n" //
                + "X?o|x|o?|| o|.|. ||X.|o|xXX\n" //
                + "X???????||       ||XXXXXXXX\n" //
                + "X-------------------------X\n" //
                + "X-------------------------X\n" //
                + "XOOOOOOO||XXXXXXX||       X\n" //
                + "XOo|o|xO||Xx|.|oX|| .|x|o X\n" //
                + "XO-----O||X-----X|| ----- X\n" //
                + "XOx|x|.O||Xx|x|oX|| .|x|o X\n" //
                + "XO-----O||X-----X|| ----- X\n" //
                + "XOo|o|oO||Xx|o|xX|| .|o|. X\n" //
                + "XOOOOOOO||XXXXXXX||       X\n" //
                + "X-------------------------X\n" //
                + "X-------------------------X\n" //
                + "XXXXXXXX||XXXXXXX||OOOOOOOX\n" //
                + "XXo|.|xX||Xx|.|oX||Oo|x|xOX\n" //
                + "XX-----X||X-----X||O-----OX\n" //
                + "XXo|o|xX||Xx|o|oX||Oo|.|.OX\n" //
                + "XX-----X||X-----X||O-----OX\n" //
                + "XX.|x|xX||Xx|x|.X||Oo|o|oOX\n" //
                + "XXXXXXXX||XXXXXXX||OOOOOOOX\n" //
                + "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
                + "TOP.\n" //
        ;

        assertEquals(expected, board.fieldAsPrintableString());
    }

    @Test
    public void random_h2s3_followConstraints() {

        final Logger log = StackFrameUtil.methodLogger();
        log.trace("-------------------------------------------------------------");

        Board  board     = new Board(2,3); // 81 token positions
        Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
        Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

        Player player = playerAAA;
        Position constraint = null;
        for (int i = 0; board.isPlayable() && (i < 100); ++i) {
            log.trace("#"+i +": constraint=[" + constraint + "]");

            Move     move       = player.makeMove(log, board, constraint);
            Position position   = move.toPosition(board);
            log.trace(move.toString() + " by " + player.getToken());

            constraint = position.place(player.getToken());
            log.trace("Next move constraint: " + constraint);

            log.trace("after placing:\n" + board.fieldAsPrintableString());

            player = (player == playerAAA) ? playerBBB : playerAAA;
        }

        String expected = "" //
                + "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
                + "XXXXXXXX||XXXXXXX||XXXXXXXX\n" //
                + "XXo|.|xX||Xo|.|oX||Xx|o|oXX\n" //
                + "XX-----X||X-----X||X-----XX\n" //
                + "XX.|o|xX||Xx|x|xX||X.|x|xXX\n" //
                + "XX-----X||X-----X||X-----XX\n" //
                + "XX.|.|xX||Xo|o|xX||X.|o|xXX\n" //
                + "XXXXXXXX||XXXXXXX||XXXXXXXX\n" //
                + "X-------------------------X\n" //
                + "X-------------------------X\n" //
                + "XOOOOOOO||       ||OOOOOOOX\n" //
                + "XOx|x|oO|| x|o|x ||Ox|.|oOX\n" //
                + "XO-----O|| ----- ||O-----OX\n" //
                + "XOx|.|oO|| x|.|. ||Oo|o|.OX\n" //
                + "XO-----O|| ----- ||O-----OX\n" //
                + "XOo|.|oO|| o|x|o ||Oo|o|oOX\n" //
                + "XOOOOOOO||       ||OOOOOOOX\n" //
                + "X-------------------------X\n" //
                + "X-------------------------X\n" //
                + "XXXXXXXX||OOOOOOO||XXXXXXXX\n" //
                + "XXo|x|xX||Oo|x|.O||X.|o|oXX\n" //
                + "XX-----X||O-----O||X-----XX\n" //
                + "XX.|o|xX||Oo|x|xO||X.|.|.XX\n" //
                + "XX-----X||O-----O||X-----XX\n" //
                + "XX.|x|xX||Oo|.|.O||Xx|x|xXX\n" //
                + "XXXXXXXX||OOOOOOO||XXXXXXXX\n" //
                + "XXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
                + "TOP.\n" //
        ;

        assertEquals(expected, board.fieldAsPrintableString());
    }

    @Test
    public void random_h3s2_ignoreConstraints() {

        final Logger log = StackFrameUtil.methodLogger();
        log.trace("-------------------------------------------------------------");

        Board  board     = new Board(3,2); // 64 token positions
        Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
        Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

        Player player = playerAAA;
        for (int i = 0; board.isPlayable() && (i < 70); ++i) {
            log.trace("#" + i);

            Move     move       = player.makeMove(log, board, null);
            log.trace(move.toString() + " by " + player.getToken());

            Position position   = move.toPosition(board);
            Position constraint = position.place(player.getToken());

            log.trace("Next move constraint (ignored): " + constraint);
            log.trace("after placing:\n" + board.fieldAsPrintableString());

            player = (player == playerAAA) ? playerBBB : playerAAA;
        }

        String expected = "" //
                + "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
                + "O              |||OOOOOOOOOOOOOOO\n" //
                + "O      ||      |||OOOOOO||OOOOOOO\n" //
                + "O  x|o || .|.  |||OO.|.O||O.|.OOO\n" //
                + "O  --- || ---  |||OO---O||O---OOO\n" //
                + "O  .|. || .|.  |||OOo|oO||Oo|oOOO\n" //
                + "O      ||      |||OOOOOO||OOOOOOO\n" //
                + "O ------------ |||O------------OO\n" //
                + "O ------------ |||O------------OO\n" //
                + "O OOOOO||      |||O     ||XXXXXOO\n" //
                + "O O.|oO|| .|.  |||O .|. ||X.|.XOO\n" //
                + "O O---O|| ---  |||O --- ||X---XOO\n" //
                + "O Oo|.O|| x|.  |||O o|x ||Xx|xXOO\n" //
                + "O OOOOO||      |||O     ||XXXXXOO\n" //
                + "O              |||OOOOOOOOOOOOOOO\n" //
                + "O-------------------------------O\n" //
                + "O-------------------------------O\n" //
                + "O-------------------------------O\n" //
                + "OXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOO\n" //
                + "OXXXXXX||     X|||OXXXXX||OOOOOOO\n" //
                + "OXX.|xX|| .|. X|||OX.|xX||Ox|oOOO\n" //
                + "OXX---X|| --- X|||OX---X||O---OOO\n" //
                + "OXXx|.X|| .|. X|||OX.|xX||O.|oOOO\n" //
                + "OXXXXXX||     X|||OXXXXX||OOOOOOO\n" //
                + "OX------------X|||O------------OO\n" //
                + "OX------------X|||O------------OO\n" //
                + "OXXXXXX||     X|||O     ||OOOOOOO\n" //
                + "OXX.|xX|| x|o X|||O .|. ||O.|oOOO\n" //
                + "OXX---X|| --- X|||O --- ||O---OOO\n" //
                + "OXXx|.X|| .|. X|||O .|. ||Oo|.OOO\n" //
                + "OXXXXXX||     X|||O     ||OOOOOOO\n" //
                + "OXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOO\n" //
                + "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n" //
                + "TOP.\n" //
        ;

        assertEquals(expected, board.fieldAsPrintableString());
    }

    @Test
    public void random_h3s2_followConstraints() {

        final Logger log = StackFrameUtil.methodLogger();
        log.trace("-------------------------------------------------------------");

        Board  board     = new Board(3,2); // 64 token positions
        Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
        Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

        Player player = playerAAA;
        Position constraint = null;
        for (int i = 0; board.isPlayable() && (i < 80); ++i) {
            log.trace("#" + i);

            Move     move       = player.makeMove(log, board, constraint);
            Position position   = move.toPosition(board);

            log.trace(move.toString() + " by " + player.getToken());

            constraint = position.place(player.getToken());

            log.trace("Next move constraint: " + constraint);
            log.trace("after placing:\n" + board.fieldAsPrintableString());

            player = (player == playerAAA) ? playerBBB : playerAAA;
        }

        String expected = "" //
                + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
                + "XOOOOOOOOOOOOOO|||              X\n" //
                + "XOOOOOO||OOOOOO|||      ||      X\n" //
                + "XOOx|.O||Ox|oOO|||  .|. || .|.  X\n" //
                + "XOO---O||O---OO|||  --- || ---  X\n" //
                + "XOOo|oO||Oo|.OO|||  .|. || .|.  X\n" //
                + "XOOOOOO||OOOOOO|||      ||      X\n" //
                + "XO------------O||| ------------ X\n" //
                + "XO------------O||| ------------ X\n" //
                + "XO     ||     O|||      ||      X\n" //
                + "XO .|x || o|. O|||  .|. || .|.  X\n" //
                + "XO --- || --- O|||  --- || ---  X\n" //
                + "XO .|. || .|x O|||  .|. || .|.  X\n" //
                + "XO     ||     O|||      ||      X\n" //
                + "XOOOOOOOOOOOOOO|||              X\n" //
                + "X-------------------------------X\n" //
                + "X-------------------------------X\n" //
                + "X-------------------------------X\n" //
                + "XXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXX\n" //
                + "XX     ||XXXXXX|||XXXXXX||XXXXXXX\n" //
                + "XX .|x ||Xx|oXX|||XXx|.X||Xo|.XXX\n" //
                + "XX --- ||X---XX|||XX---X||X---XXX\n" //
                + "XX o|. ||X.|xXX|||XX.|xX||Xx|xXXX\n" //
                + "XX     ||XXXXXX|||XXXXXX||XXXXXXX\n" //
                + "XX------------X|||X------------XX\n" //
                + "XX------------X|||X------------XX\n" //
                + "XXXXXXX||OOOOOX|||X     ||OOOOOXX\n" //
                + "XXX.|.X||O.|oOX|||X .|o ||Oo|.OXX\n" //
                + "XXX---X||O---OX|||X --- ||O---OXX\n" //
                + "XXXx|xX||Oo|.OX|||X .|x ||Oo|.OXX\n" //
                + "XXXXXXX||OOOOOX|||X     ||OOOOOXX\n" //
                + "XXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXX\n" //
                + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
                + "TOP.\n" //
        ;

        assertEquals(expected, board.fieldAsPrintableString());
    }

    @Test
    public void random_h3s3_ignoreConstraints() {

        final Logger log = StackFrameUtil.methodLogger();
        log.trace("-------------------------------------------------------------");

        Board  board     = new Board(3,3); // 729 token locations
        Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
        Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

        Player player = playerAAA;
        for (int i = 0; board.isPlayable() && (i < 800); ++i) {
            log.trace("#" + i);

            Move     move       = player.makeMove(log, board, null);
            log.trace(move.toString() + " by " + player.getToken());

            Position position   = move.toPosition(board);
            Position constraint = position.place(player.getToken());

            log.trace("Next move constraint (ignored): " + constraint);
            log.trace(board.fieldAsPrintableString());

            player = (player == playerAAA) ? playerBBB : playerAAA;
        }

        String expected = "" //
                + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
                + "XXXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||???????????????????????????X\n" //
                + "XXXXXXXXX||       ||       X|||XOOOOOOO||       ||XXXXXXXX|||?XXXXXXX||OOOOOOO||OOOOOOO?X\n" //
                + "XXXx|x|.X|| .|o|. || o|x|. X|||XO.|o|.O|| o|x|x ||Xo|.|oXX|||?Xx|o|xX||Ox|.|.O||Oo|.|oO?X\n" //
                + "XXX-----X|| ----- || ----- X|||XO-----O|| ----- ||X-----XX|||?X-----X||O-----O||O-----O?X\n" //
                + "XXXo|x|.X|| .|.|x || x|o|x X|||XO.|o|oO|| x|o|o ||Xx|.|.XX|||?Xo|x|oX||O.|.|oO||Ox|o|oO?X\n" //
                + "XXX-----X|| ----- || ----- X|||XO-----O|| ----- ||X-----XX|||?X-----X||O-----O||O-----O?X\n" //
                + "XXXo|x|.X|| .|.|o || .|.|. X|||XO.|o|.O|| x|x|. ||Xx|x|xXX|||?Xo|o|xX||Oo|o|oO||Ox|x|oO?X\n" //
                + "XXXXXXXXX||       ||       X|||XOOOOOOO||       ||XXXXXXXX|||?XXXXXXX||OOOOOOO||OOOOOOO?X\n" //
                + "XX-------------------------X|||X-------------------------X|||?-------------------------?X\n" //
                + "XX-------------------------X|||X-------------------------X|||?-------------------------?X\n" //
                + "XX       ||XXXXXXX||XXXXXXXX|||XXXXXXXX||XXXXXXX||       X|||????????||XXXXXXX||OOOOOOO?X\n" //
                + "XX o|.|o ||Xo|.|xX||Xx|x|.XX|||XXx|o|xX||Xx|x|oX|| .|x|. X|||??x|o|x?||Xx|.|xX||O.|.|xO?X\n" //
                + "XX ----- ||X-----X||X-----XX|||XX-----X||X-----X|| ----- X|||??-----?||X-----X||O-----O?X\n" //
                + "XX .|x|x ||X.|o|.X||X.|x|oXX|||XXx|o|xX||X.|x|xX|| o|.|o X|||??o|o|x?||Xx|.|oX||O.|.|oO?X\n" //
                + "XX ----- ||X-----X||X-----XX|||XX-----X||X-----X|| ----- X|||??-----?||X-----X||O-----O?X\n" //
                + "XX .|x|o ||Xx|x|xX||X.|x|.XX|||XXx|x|oX||Xo|.|xX|| .|o|x X|||??x|x|o?||Xx|o|xX||Oo|o|oO?X\n" //
                + "XX       ||XXXXXXX||XXXXXXXX|||XXXXXXXX||XXXXXXX||       X|||????????||XXXXXXX||OOOOOOO?X\n" //
                + "XX-------------------------X|||X-------------------------X|||?-------------------------?X\n" //
                + "XX-------------------------X|||X-------------------------X|||?-------------------------?X\n" //
                + "XX       ||XXXXXXX||XXXXXXXX|||XXXXXXXX||OOOOOOO||       X|||?OOOOOOO||XXXXXXX||????????X\n" //
                + "XX .|o|x ||Xx|x|.X||Xo|o|.XX|||XX.|o|oX||Ox|o|.O|| o|x|. X|||?Oo|o|oO||Xx|x|xX||?o|x|o??X\n" //
                + "XX ----- ||X-----X||X-----XX|||XX-----X||O-----O|| ----- X|||?O-----O||X-----X||?-----??X\n" //
                + "XX .|.|. ||Xx|x|oX||Xo|.|xXX|||XX.|.|xX||Oo|o|.O|| .|o|o X|||?Oo|x|xO||Xo|o|xX||?x|x|o??X\n" //
                + "XX ----- ||X-----X||X-----XX|||XX-----X||O-----O|| ----- X|||?O-----O||X-----X||?-----??X\n" //
                + "XX x|.|. ||X.|.|xX||Xx|x|xXX|||XXx|x|xX||Oo|o|oO|| o|o|. X|||?Ox|x|oO||Xo|x|.X||?o|o|x??X\n" //
                + "XX       ||XXXXXXX||XXXXXXXX|||XXXXXXXX||OOOOOOO||       X|||?OOOOOOO||XXXXXXX||????????X\n" //
                + "XXXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||???????????????????????????X\n" //
                + "X---------------------------------------------------------------------------------------X\n" //
                + "X---------------------------------------------------------------------------------------X\n" //
                + "X---------------------------------------------------------------------------------------X\n" //
                + "XOOOOOOOOOOOOOOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||                           X\n" //
                + "XOOOOOOOO||OOOOOOO||OOOOOOOO|||XXXXXXXX||       ||XXXXXXXX||| OOOOOOO||XXXXXXX||OOOOOOO X\n" //
                + "XOOx|o|oO||Oo|.|.O||Ox|o|.OO|||XXx|.|.X|| .|o|x ||X.|o|oXX||| Oo|x|oO||X.|x|xX||Oo|.|xO X\n" //
                + "XOO-----O||O-----O||O-----OO|||XX-----X|| ----- ||X-----XX||| O-----O||X-----X||O-----O X\n" //
                + "XOO.|o|.O||Oo|.|xO||Oo|o|.OO|||XXo|x|xX|| .|.|o ||Xx|x|oXX||| O.|o|xO||X.|x|xX||Oo|o|.O X\n" //
                + "XOO-----O||O-----O||O-----OO|||XX-----X|| ----- ||X-----XX||| O-----O||X-----X||O-----O X\n" //
                + "XOOx|o|.O||Oo|o|xO||O.|o|.OO|||XXo|o|xX|| .|.|. ||Xx|x|xXX||| Oo|x|xO||Xx|o|oX||O.|o|oO X\n" //
                + "XOOOOOOOO||OOOOOOO||OOOOOOOO|||XXXXXXXX||       ||XXXXXXXX||| OOOOOOO||XXXXXXX||OOOOOOO X\n" //
                + "XO-------------------------O|||X-------------------------X||| ------------------------- X\n" //
                + "XO-------------------------O|||X-------------------------X||| ------------------------- X\n" //
                + "XOXXXXXXX||       ||OOOOOOOO|||XXXXXXXX||       ||???????X|||        ||       ||XXXXXXX X\n" //
                + "XOX.|.|xX|| o|.|o ||O.|.|.OO|||XXx|o|oX|| .|o|x ||?o|x|o?X|||  .|o|o || x|o|x ||Xo|x|.X X\n" //
                + "XOX-----X|| ----- ||O-----OO|||XX-----X|| ----- ||?-----?X|||  ----- || ----- ||X-----X X\n" //
                + "XOX.|.|xX|| x|x|o ||Oo|o|oOO|||XXx|.|.X|| x|.|. ||?o|x|x?X|||  o|x|x || x|.|o ||Xo|x|.X X\n" //
                + "XOX-----X|| ----- ||O-----OO|||XX-----X|| ----- ||?-----?X|||  ----- || ----- ||X-----X X\n" //
                + "XOXx|o|xX|| o|o|x ||O.|o|xOO|||XXx|x|xX|| o|o|x ||?x|o|o?X|||  o|x|o || .|o|o ||Xx|x|.X X\n" //
                + "XOXXXXXXX||       ||OOOOOOOO|||XXXXXXXX||       ||???????X|||        ||       ||XXXXXXX X\n" //
                + "XO-------------------------O|||X-------------------------X||| ------------------------- X\n" //
                + "XO-------------------------O|||X-------------------------X||| ------------------------- X\n" //
                + "XOXXXXXXX||       ||XXXXXXXO|||XXXXXXXX||XXXXXXX||       X||| XXXXXXX||OOOOOOO||OOOOOOO X\n" //
                + "XOXx|o|oX|| .|.|. ||Xx|.|.XO|||XXx|.|oX||Xo|o|.X|| x|.|. X||| Xx|x|.X||O.|.|oO||Ox|o|.O X\n" //
                + "XOX-----X|| ----- ||X-----XO|||XX-----X||X-----X|| ----- X||| X-----X||O-----O||O-----O X\n" //
                + "XOXo|x|xX|| .|.|. ||Xx|.|oXO|||XXx|x|.X||Xo|.|oX|| o|.|. X||| Xx|x|xX||Ox|o|oO||Oo|.|.O X\n" //
                + "XOX-----X|| ----- ||X-----XO|||XX-----X||X-----X|| ----- X||| X-----X||O-----O||O-----O X\n" //
                + "XOX.|.|xX|| o|.|. ||Xx|.|oXO|||XXo|o|xX||Xx|x|xX|| x|x|o X||| X.|.|.X||Ox|x|oO||Oo|o|oO X\n" //
                + "XOXXXXXXX||       ||XXXXXXXO|||XXXXXXXX||XXXXXXX||       X||| XXXXXXX||OOOOOOO||OOOOOOO X\n" //
                + "XOOOOOOOOOOOOOOOOOOOOOOOOOOO|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||                           X\n" //
                + "X---------------------------------------------------------------------------------------X\n" //
                + "X---------------------------------------------------------------------------------------X\n" //
                + "X---------------------------------------------------------------------------------------X\n" //
                + "X???????????????????????????|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
                + "X?OOOOOOO||OOOOOOO||XXXXXXX?|||XXXXXXXX||???????||       X|||XXXXXXXX||OOOOOOO||OOOOOOOXX\n" //
                + "X?Oo|.|.O||Oo|.|.O||Xo|x|xX?|||XXx|x|xX||?o|x|o?|| o|x|. X|||XX.|x|oX||Oo|.|oO||Oo|x|oOXX\n" //
                + "X?O-----O||O-----O||X-----X?|||XX-----X||?-----?|| ----- X|||XX-----X||O-----O||O-----OXX\n" //
                + "X?O.|o|xO||Ox|x|oO||Xx|x|oX?|||XXo|o|xX||?x|o|o?|| .|.|o X|||XXx|x|oX||Oo|x|oO||Oo|o|xOXX\n" //
                + "X?O-----O||O-----O||X-----X?|||XX-----X||?-----?|| ----- X|||XX-----X||O-----O||O-----OXX\n" //
                + "X?Oo|x|oO||Oo|o|oO||Xo|x|xX?|||XX.|x|oX||?x|o|x?|| o|.|x X|||XX.|x|.X||Ox|o|oO||Oo|.|xOXX\n" //
                + "X?OOOOOOO||OOOOOOO||XXXXXXX?|||XXXXXXXX||???????||       X|||XXXXXXXX||OOOOOOO||OOOOOOOXX\n" //
                + "X?-------------------------?|||X-------------------------X|||X-------------------------XX\n" //
                + "X?-------------------------?|||X-------------------------X|||X-------------------------XX\n" //
                + "X?XXXXXXX||XXXXXXX||OOOOOOO?|||XOOOOOOO||XXXXXXX||XXXXXXXX|||XOOOOOOO||XXXXXXX||OOOOOOOXX\n" //
                + "X?Xx|o|xX||Xx|o|oX||O.|.|oO?|||XOo|o|oO||X.|x|xX||Xx|o|oXX|||XOo|o|oO||Xo|x|xX||Ox|.|.OXX\n" //
                + "X?X-----X||X-----X||O-----O?|||XO-----O||X-----X||X-----XX|||XO-----O||X-----X||O-----OXX\n" //
                + "X?Xo|.|oX||X.|x|.X||Ox|o|xO?|||XOx|o|.O||Xo|.|xX||Xx|x|oXX|||XOx|x|oO||Xx|o|xX||Oo|o|oOXX\n" //
                + "X?X-----X||X-----X||O-----O?|||XO-----O||X-----X||X-----XX|||XO-----O||X-----X||O-----OXX\n" //
                + "X?Xx|x|xX||Xo|x|xX||Oo|x|oO?|||XO.|.|.O||Xo|.|xX||Xx|.|.XX|||XOo|o|.O||Xo|.|xX||O.|x|oOXX\n" //
                + "X?XXXXXXX||XXXXXXX||OOOOOOO?|||XOOOOOOO||XXXXXXX||XXXXXXXX|||XOOOOOOO||XXXXXXX||OOOOOOOXX\n" //
                + "X?-------------------------?|||X-------------------------X|||X-------------------------XX\n" //
                + "X?-------------------------?|||X-------------------------X|||X-------------------------XX\n" //
                + "X?OOOOOOO||XXXXXXX||XXXXXXX?|||X       ||       ||XXXXXXXX|||XOOOOOOO||XXXXXXX||XXXXXXXXX\n" //
                + "X?Oo|o|oO||Xx|x|xX||Xx|o|xX?|||X x|o|. || .|x|. ||X.|.|oXX|||XOo|.|.O||X.|.|xX||Xx|x|xXXX\n" //
                + "X?O-----O||X-----X||X-----X?|||X ----- || ----- ||X-----XX|||XO-----O||X-----X||X-----XXX\n" //
                + "X?O.|x|xO||X.|x|.X||Xo|x|.X?|||X x|.|o || .|o|. ||Xx|x|xXX|||XOo|o|.O||X.|x|xX||Xx|x|oXXX\n" //
                + "X?O-----O||X-----X||X-----X?|||X ----- || ----- ||X-----XX|||XO-----O||X-----X||X-----XXX\n" //
                + "X?Oo|x|oO||Xo|.|.X||Xx|x|oX?|||X .|x|. || x|.|. ||X.|x|oXX|||XOx|o|oO||Xx|o|oX||Xo|o|xXXX\n" //
                + "X?OOOOOOO||XXXXXXX||XXXXXXX?|||X       ||       ||XXXXXXXX|||XOOOOOOO||XXXXXXX||XXXXXXXXX\n" //
                + "X???????????????????????????|||XXXXXXXXXXXXXXXXXXXXXXXXXXX|||XXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
                + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" //
                + "TOP.\n" //
        ;

        assertEquals(expected, board.fieldAsPrintableString());
    }

    @Test
    public void random_h3s3_followConstraints() {

        final Logger log = StackFrameUtil.methodLogger();
        log.trace("-------------------------------------------------------------");

        Board  board     = new Board(3,3); // 729 token locations
        Player playerAAA = Player.create(PlayerPredictable.class, Token.PLAYER_AAA);
        Player playerBBB = Player.create(PlayerPredictable.class, Token.PLAYER_BBB);

        Player   player     = playerAAA;
        Position constraint = null;
        int      depth     = 0;
        for (int i = 0; board.isPlayable() && (i < 800); ++i) {
            depth = (constraint == null) ? 0 : constraint.depth();
            log.trace("#" + i);
            log.trace("Current constraint: [" + constraint + "]; depth=["+depth+"]");

            Move     move       = player.makeMove(log, board, constraint);
            Position position   = move.toPosition(board);

            log.trace(move.toString() + " by " + player.getToken());

            constraint = position.place(player.getToken());

            log.trace("Next move constraint: [" + constraint + "]");
            log.trace(board.fieldAsPrintableString());

            player = (player == playerAAA) ? playerBBB : playerAAA;
        }

        String expected = "" //
                + "?????????????????????????????????????????????????????????????????????????????????????????\n" //
                + "?OOOOOOOOOOOOOOOOOOOOOOOOOOO|||???????????????????????????|||XXXXXXXXXXXXXXXXXXXXXXXXXXX?\n" //
                + "?OXXXXXXX||OOOOOOO||OOOOOOOO|||?OOOOOOO||XXXXXXX||XXXXXXX?|||X       ||XXXXXXX||OOOOOOOX?\n" //
                + "?OX.|x|xX||Oo|x|.O||Ox|x|oOO|||?O.|o|xO||X.|x|oX||Xx|o|oX?|||X x|o|o ||X.|x|.X||Ox|o|xOX?\n" //
                + "?OX-----X||O-----O||O-----OO|||?O-----O||X-----X||X-----X?|||X ----- ||X-----X||O-----OX?\n" //
                + "?OX.|x|.X||Ox|o|xO||Oo|x|xOO|||?Ox|o|.O||X.|o|.X||Xx|x|xX?|||X x|.|. ||Xx|x|oX||Oo|o|.OX?\n" //
                + "?OX-----X||O-----O||O-----OO|||?O-----O||X-----X||X-----X?|||X ----- ||X-----X||O-----OX?\n" //
                + "?OXx|.|oX||Oo|o|oO||Oo|o|oOO|||?Oo|o|xO||Xx|x|xX||Xo|x|xX?|||X o|x|o ||X.|x|.X||Ox|o|.OX?\n" //
                + "?OXXXXXXX||OOOOOOO||OOOOOOOO|||?OOOOOOO||XXXXXXX||XXXXXXX?|||X       ||XXXXXXX||OOOOOOOX?\n" //
                + "?O-------------------------O|||?-------------------------?|||X-------------------------X?\n" //
                + "?O-------------------------O|||?-------------------------?|||X-------------------------X?\n" //
                + "?O       ||OOOOOOO||OOOOOOOO|||?XXXXXXX||???????||OOOOOOO?|||XOOOOOOO||       ||       X?\n" //
                + "?O .|.|x ||O.|.|xO||Oo|.|.OO|||?Xx|x|.X||?x|x|o?||Ox|o|oO?|||XO.|.|xO|| o|o|x || .|.|o X?\n" //
                + "?O ----- ||O-----O||O-----OO|||?X-----X||?-----?||O-----O?|||XO-----O|| ----- || ----- X?\n" //
                + "?O x|o|x ||Oo|o|oO||O.|o|.OO|||?Xx|o|.X||?o|o|x?||Oo|.|oO?|||XOx|.|xO|| o|.|. || .|x|. X?\n" //
                + "?O ----- ||O-----O||O-----OO|||?X-----X||?-----?||O-----O?|||XO-----O|| ----- || ----- X?\n" //
                + "?O .|o|o ||O.|.|.O||O.|x|oOO|||?Xx|.|xX||?x|x|o?||O.|.|oO?|||XOo|o|oO|| x|.|o || x|.|o X?\n" //
                + "?O       ||OOOOOOO||OOOOOOOO|||?XXXXXXX||???????||OOOOOOO?|||XOOOOOOO||       ||       X?\n" //
                + "?O-------------------------O|||?-------------------------?|||X-------------------------X?\n" //
                + "?O-------------------------O|||?-------------------------?|||X-------------------------X?\n" //
                + "?OOOOOOOO||XXXXXXX||XXXXXXXO|||?OOOOOOO||OOOOOOO||XXXXXXX?|||XXXXXXXX||XXXXXXX||XXXXXXXX?\n" //
                + "?OOo|.|xO||Xo|o|xX||X.|x|oXO|||?O.|.|oO||Oo|.|.O||Xo|o|xX?|||XXx|x|xX||Xx|o|oX||Xo|o|.XX?\n" //
                + "?OO-----O||X-----X||X-----XO|||?O-----O||O-----O||X-----X?|||XX-----X||X-----X||X-----XX?\n" //
                + "?OO.|o|xO||X.|x|xX||Xx|x|.XO|||?Oo|x|oO||O.|o|.O||Xo|x|oX?|||XXo|.|oX||Xx|x|.X||Xx|x|xXX?\n" //
                + "?OO-----O||X-----X||X-----XO|||?O-----O||O-----O||X-----X?|||XX-----X||X-----X||X-----XX?\n" //
                + "?OO.|x|oO||Xo|.|xX||X.|x|.XO|||?Oo|x|oO||O.|x|oO||Xx|.|xX?|||XXo|o|xX||Xo|o|xX||Xx|x|oXX?\n" //
                + "?OOOOOOOO||XXXXXXX||XXXXXXXO|||?OOOOOOO||OOOOOOO||XXXXXXX?|||XXXXXXXX||XXXXXXX||XXXXXXXX?\n" //
                + "?OOOOOOOOOOOOOOOOOOOOOOOOOOO|||???????????????????????????|||XXXXXXXXXXXXXXXXXXXXXXXXXXX?\n" //
                + "?---------------------------------------------------------------------------------------?\n" //
                + "?---------------------------------------------------------------------------------------?\n" //
                + "?---------------------------------------------------------------------------------------?\n" //
                + "?XXXXXXXXXXXXXXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||OOOOOOOOOOOOOOOOOOOOOOOOOOO?\n" //
                + "?XXXXXXXX||OOOOOOO||OOOOOOOX|||OOOOOOOO||OOOOOOO||       O|||OOOOOOOO||       ||XXXXXXXO?\n" //
                + "?XXx|x|oX||Oo|o|.O||Ox|.|oOX|||OOx|.|.O||Oo|x|.O|| o|o|. O|||OO.|x|oO|| x|.|x ||X.|x|.XO?\n" //
                + "?XX-----X||O-----O||O-----OX|||OO-----O||O-----O|| ----- O|||OO-----O|| ----- ||X-----XO?\n" //
                + "?XX.|x|oX||Ox|o|oO||O.|o|xOX|||OOo|o|oO||Ox|o|oO|| x|x|. O|||OO.|.|oO|| o|o|. ||Xx|x|.XO?\n" //
                + "?XX-----X||O-----O||O-----OX|||OO-----O||O-----O|| ----- O|||OO-----O|| ----- ||X-----XO?\n" //
                + "?XX.|x|.X||O.|o|.O||Oo|x|.OX|||OO.|x|.O||Oo|.|oO|| o|x|. O|||OO.|x|oO|| o|o|x ||X.|x|.XO?\n" //
                + "?XXXXXXXX||OOOOOOO||OOOOOOOX|||OOOOOOOO||OOOOOOO||       O|||OOOOOOOO||       ||XXXXXXXO?\n" //
                + "?X-------------------------X|||O-------------------------O|||O-------------------------O?\n" //
                + "?X-------------------------X|||O-------------------------O|||O-------------------------O?\n" //
                + "?XOOOOOOO||XXXXXXX||OOOOOOOX|||OXXXXXXX||OOOOOOO||XXXXXXXO|||OOOOOOOO||XXXXXXX||XXXXXXXO?\n" //
                + "?XOo|o|.O||X.|x|xX||Oo|x|xOX|||OXx|x|xX||Ox|x|oO||X.|x|oXO|||OO.|x|oO||Xo|o|xX||Xx|.|.XO?\n" //
                + "?XO-----O||X-----X||O-----OX|||OX-----X||O-----O||X-----XO|||OO-----O||X-----X||X-----XO?\n" //
                + "?XOx|o|xO||X.|.|xX||Oo|o|.OX|||OXx|o|.X||Oo|o|oO||Xx|x|oXO|||OOx|o|oO||Xo|x|xX||Xx|x|.XO?\n" //
                + "?XO-----O||X-----X||O-----OX|||OX-----X||O-----O||X-----XO|||OO-----O||X-----X||X-----XO?\n" //
                + "?XOo|x|oO||Xx|.|xX||Oo|.|.OX|||OXx|o|oX||Oo|o|xO||Xx|x|xXO|||OOo|x|xO||Xx|o|oX||Xx|.|.XO?\n" //
                + "?XOOOOOOO||XXXXXXX||OOOOOOOX|||OXXXXXXX||OOOOOOO||XXXXXXXO|||OOOOOOOO||XXXXXXX||XXXXXXXO?\n" //
                + "?X-------------------------X|||O-------------------------O|||O-------------------------O?\n" //
                + "?X-------------------------X|||O-------------------------O|||O-------------------------O?\n" //
                + "?XXXXXXXX||OOOOOOO||XXXXXXXX|||O???????||OOOOOOO||XXXXXXXO|||OOOOOOOO||XXXXXXX||OOOOOOOO?\n" //
                + "?XXo|x|xX||Oo|.|oO||Xo|x|xXX|||O?x|o|o?||Oo|o|.O||Xo|x|xXO|||OO.|x|.O||X.|o|oX||Ox|o|oOO?\n" //
                + "?XX-----X||O-----O||X-----XX|||O?-----?||O-----O||X-----XO|||OO-----O||X-----X||O-----OO?\n" //
                + "?XXx|o|xX||Ox|o|xO||Xo|x|oXX|||O?o|x|x?||Oo|x|xO||X.|x|oXO|||OO.|.|.O||Xo|x|oX||Oo|o|oOO?\n" //
                + "?XX-----X||O-----O||X-----XX|||O?-----?||O-----O||X-----XO|||OO-----O||X-----X||O-----OO?\n" //
                + "?XXx|o|xX||Oo|x|xO||Xx|o|oXX|||O?x|x|o?||Oo|.|xO||Xo|x|.XO|||OOo|o|oO||Xx|x|xX||Oo|x|xOO?\n" //
                + "?XXXXXXXX||OOOOOOO||XXXXXXXX|||O???????||OOOOOOO||XXXXXXXO|||OOOOOOOO||XXXXXXX||OOOOOOOO?\n" //
                + "?XXXXXXXXXXXXXXXXXXXXXXXXXXX|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||OOOOOOOOOOOOOOOOOOOOOOOOOOO?\n" //
                + "?---------------------------------------------------------------------------------------?\n" //
                + "?---------------------------------------------------------------------------------------?\n" //
                + "?---------------------------------------------------------------------------------------?\n" //
                + "????????????????????????????|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||????????????????????????????\n" //
                + "??XXXXXXX||XXXXXXX||OOOOOOO?|||OOOOOOOO||OOOOOOO||OOOOOOOO|||?XXXXXXX||OOOOOOO||OOOOOOO??\n" //
                + "??X.|o|xX||Xx|o|xX||Oo|.|.O?|||OO.|x|xO||Oo|x|xO||O.|x|.OO|||?Xx|x|.X||Oo|o|oO||Oo|x|xO??\n" //
                + "??X-----X||X-----X||O-----O?|||OO-----O||O-----O||O-----OO|||?X-----X||O-----O||O-----O??\n" //
                + "??Xo|x|oX||X.|x|.X||Oo|.|xO?|||OOo|o|oO||Oo|o|oO||Oo|x|.OO|||?X.|x|xX||Oo|.|xO||Ox|o|oO??\n" //
                + "??X-----X||X-----X||O-----O?|||OO-----O||O-----O||O-----OO|||?X-----X||O-----O||O-----O??\n" //
                + "??Xx|o|oX||Xx|x|.X||Oo|.|oO?|||OOo|.|xO||O.|o|oO||Oo|o|oOO|||?X.|.|xX||Oo|o|.O||Ox|o|oO??\n" //
                + "??XXXXXXX||XXXXXXX||OOOOOOO?|||OOOOOOOO||OOOOOOO||OOOOOOOO|||?XXXXXXX||OOOOOOO||OOOOOOO??\n" //
                + "??-------------------------?|||O-------------------------O|||?-------------------------??\n" //
                + "??-------------------------?|||O-------------------------O|||?-------------------------??\n" //
                + "??XXXXXXX||OOOOOOO||OOOOOOO?|||OXXXXXXX||OOOOOOO||OOOOOOOO|||?XXXXXXX||XXXXXXX||OOOOOOO??\n" //
                + "??X.|.|xX||Ox|o|.O||Ox|o|oO?|||OXo|.|.X||Ox|o|xO||Ox|.|oOO|||?Xo|x|xX||Xo|x|xX||Oo|.|xO??\n" //
                + "??X-----X||O-----O||O-----O?|||OX-----X||O-----O||O-----OO|||?X-----X||X-----X||O-----O??\n" //
                + "??Xo|.|xX||O.|x|oO||Oo|o|xO?|||OXx|x|xX||Oo|o|xO||Oo|o|xOO|||?Xo|x|oX||Xx|o|xX||Oo|o|.O??\n" //
                + "??X-----X||O-----O||O-----O?|||OX-----X||O-----O||O-----OO|||?X-----X||X-----X||O-----O??\n" //
                + "??Xx|.|xX||Oo|o|oO||Oo|x|oO?|||OX.|.|xX||Ox|o|oO||Oo|o|xOO|||?Xx|x|oX||Xo|o|xX||Ox|x|oO??\n" //
                + "??XXXXXXX||OOOOOOO||OOOOOOO?|||OXXXXXXX||OOOOOOO||OOOOOOOO|||?XXXXXXX||XXXXXXX||OOOOOOO??\n" //
                + "??-------------------------?|||O-------------------------O|||?-------------------------??\n" //
                + "??-------------------------?|||O-------------------------O|||?-------------------------??\n" //
                + "?????????||OOOOOOO||XXXXXXX?|||OXXXXXXX||       ||       O|||?OOOOOOO||XXXXXXX||?????????\n" //
                + "???x|o|x?||Oo|x|oO||Xx|o|xX?|||OXx|x|xX|| x|.|x || o|.|o O|||?Oo|.|oO||X.|.|.X||?o|x|o???\n" //
                + "???-----?||O-----O||X-----X?|||OX-----X|| ----- || ----- O|||?O-----O||X-----X||?-----???\n" //
                + "???o|x|x?||Ox|o|.O||Xx|.|.X?|||OX.|x|.X|| x|.|. || o|x|o O|||?O.|.|oO||Xx|x|xX||?o|o|x???\n" //
                + "???-----?||O-----O||X-----X?|||OX-----X|| ----- || ----- O|||?O-----O||X-----X||?-----???\n" //
                + "???o|x|o?||Ox|x|oO||Xx|.|.X?|||OX.|.|.X|| o|.|x || x|x|. O|||?Ox|x|oO||X.|.|.X||?x|o|x???\n" //
                + "?????????||OOOOOOO||XXXXXXX?|||OXXXXXXX||       ||       O|||?OOOOOOO||XXXXXXX||?????????\n" //
                + "????????????????????????????|||OOOOOOOOOOOOOOOOOOOOOOOOOOO|||????????????????????????????\n" //
                + "?????????????????????????????????????????????????????????????????????????????????????????\n" //
                + "TOP.\n" //
        ;

        assertEquals(expected, board.fieldAsPrintableString());
    }
}
