/**
 * Classes that define the game API exposed to implementations of the {@code Player} interface.
 *
 * <P>An Ultimate Tic-Tac-Toe game, or just UTTT, is an ehnancement of the classic Tic-Tac-Toe game, with three
 * additional features: height, board size flexibility, and move contraints.
 *
 * <P><I>Height</I> is achieved by making the boards "recursive," in that the game consists of "boards-of-boards" or
 * "boards-of-boards-of-boards," etc.
 *
 * <P><I>Size Flexibility</I> means that the boards are not limited to the classic 3x3, but can be any square of at
 * least 2. Further, the size is the same for all boards in a game, from the top all the way down to each bottom board.
 *
 * <P><I>Move Constraints</I> means that the placing of a player's token (on some bottom board) creates a constraint on
 * the next player's move, limiting the choices of play. There are several variations of how such contraints are
 * determined, see {@link com.uttt.core.game.Rules.Constraint Rules.Constraint}, although a given implementation might
 * not support them all.
 *
 * <P>Finally, other game play enhancements <I>may</I> be supported supported in the future, such as:
 *
 * <UL>
 *
 * <LI>Rotating Boards - Since the win conditions for each board is rotationally symmetric, this wouldn't impact any
 * single board move, but may interact with move constraints in novel ways.
 *
 * <LI>Bombs - UTTT is naturally a game with a limited number of maximum moves (one for each position in all bottom
 * boards). Perhaps players start with a number of "bombs" that can be used to clear out boards. Or perhaps bombs are
 * earned by winning bottom boards. Bombs might only apply to bottom boards, or perhaps to any board that is in a "draw"
 * state (i.e. no playable spots remaining and no winner).
 *
 * <LI>Capture - Winning a board might yield points by way of number of opponents tokens "taken." Points might be scaled
 * by the height of the winning board.
 *
 * <LI>Non-Empty Start - Possibly to handicap a better player, or just to see what happens, the starting configuration
 * might not be empty. This might be a scatter of "extra" starting tokens either randomly or in a well-defined pattern,
 * or just a particular partial game that has been discovered to be "interesting" to continue.
 *
 * <LI>Limited Tokens - Something closer to checkers, with players limited to a maximum number of tokens. After the max
 * is reached, a placement would require the removal of token somewhere else to maintain balance. Such removal might be
 * unrestricted, or somehow be tied into the placement constraint.
 *
 * <LI>Etc., Etc., Etc.
 *
 * </UL>
 *
 * <P><DIV style="padding-left:2em"><I><B>NOTE:</B> Some of these proposals might interact in subtle ways, such as
 * variants that remove pieces (e.g. Bombs or Capture) and those that limit cardinality (e.g. Limited Tokens).</I></DIV>
 *
 * <!-- :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: -->
 *
 * <H2>Core Abstractions</H2>
 *
 * <P><B>Game:</B>
 *
 * A {@link com.uttt.core.game.Game Game} is comprised of only a few core elements: a configuration, an official
 * (private) board, and two players. The game mechanism is responsible for negotiating the configuration between the
 * players, obtaining moves from players, applying the moves to the board, and then reporting the results of moves to
 * all players. Additionally, a game implementation <I>may</I> also provide move history, export, import (including of
 * an incomplete game), resource usage tracking & reporting (e.g. CPU time, memory, etc.), resource usage restrictions,
 * inter-game storage, tracing / troubleshooting / debugging aids, etc. Further, it <I>may</I> also provide information
 * about the board state (or any of it's sub-boards) that a play could derive itself, but is more convenient for the
 * game API to provide, e.g. total move count, count of specific player's tokens within a specific (sub)-board, list of
 * open positions on a given board, etc.
 *
 * <P><I><B>NOTE:</B> The game <B>should not</B> provide derived information about the board state that is of direct aid
 * in forming a player's move strategy. Thus, providing a list of open spots is fine, but not one of "recommended"
 * positions, or "best next move" (even if limited to the scope of a single board's positions).</I>
 *
 * <P><B>Node:</B>
 *
 * Each {@link com.uttt.core.board.Node Node} represents a single position within some parent board - unless this is a
 * top-board node, in which case it's the whole kit-and-kaboodle. If a board is considered a kind of tree structure,
 * then nodes are all elements in the tree, from root (top-board) to leaf (token).
 *
 * <P><B>Field:</B>
 *
 * The (square) grid of nodes that make up a board's positions. In order to facilitate visual representations of board
 * states, the field is indexed in row-major fashion, with (row=0,col=0) representing the "upper left" and
 * (row=size,col=size) the "exclusive" bound position just outside the "lower right" position.
 *
 * <P>In some contexts, it's convenient to use a kind of "compass" coordinates, where for size=3: North~(0,0),
 * South~(2,2), East~(0,2), and West(2,0). The midway points then naturally become NE~(0,1), NW~(1,0), SE~(1,2), and
 * SW~(2,1). Finally, the center can be CP~(1,1). <I>NOTE: This notation is <B>not</B> supported by the API, but is
 * sometimes useful to represent test cases or, more generally, describe move sequences without a soup of small
 * numbers.</I>
 *
 * <P><B>Token (extends Node):</B>
 *
 * A {@link com.uttt.core.board.Token Token} holds a spot as controlled (owned, taken) by a specific player. Tokens
 * may only be placed on bottom-boards. As a leaf in a board tree, tokens always have height=0.
 *
 * <P><B>Board (extends Node):</B>
 *
 * A {@link com.uttt.core.board.Board Board} is a square grid of {@code Node} elements, which can either be
 * {@code Token} values or (subordinate) {@code Board} instances. For a given board, all of it's nodes are of the same
 * kind, tokens or sub-boards. Further, if a board-of-boards, all the sub-boards must have the same <B>height</B> (see
 * below).
 *
 * <UL>
 *
 * <LI>Top-Board - The highest board in a game, which itself has no parent board. Except when height=1, all its grid
 * nodes will be (sub) boards.
 *
 * <LI>Bottom-Board - The lowest board(s) in a game, whose grid nodes are all tokens.
 *
 * <LI>Mid-Board - A board between the top and bottom boards.
 *
 * </UL>
 *
 * <P><DIV style="padding-left:2em"><I><B>NOTE:</B> For height=1 games, the top board is the (single) bottom board. For
 * height=2, there are no mid-boards at all. </I></DIV>
 *
 * <P><B>Size:</B>
 *
 * All boards within a single game, whether a bottom-, mid-, or top-board, are of the same square size. The minimum size
 * is 2. Although that is almost degenerately small, games of size 2 are useful for testing game mechanics and (simple)
 * aspects of player strategy, so they are allowed. In theory, any size above 2 is "interesting," but because the state
 * space of the game grows exponentially with the size, a given implementation is free to limit the maximum size,
 * possibly as a function of the height.
 *
 * <P><B>Height:</B>
 *
 * The height of any token is always zero (0), of all bottom-boards is always one (1), and for all other boards is one
 * more than the (uniform) height of the nodes in its field grid. The height of the top-board is also the height of the
 * game.
 *
 * <P><B>Lineage:</B>
 *
 * A sequence of boards from the top-board through any mid-boards until reaching some particular position. The lineage
 * of the top-board is, by definition, empty (null), while that for a token spot has a board sequence exactly as long as
 * the height of the top-board. In genealogical terms, it's the parent board of a position, and that board's parent, and
 * so on until the top-board (which has no parent).
 *
 * <P><B>Move:</B>
 *
 * A sequence of (row,col) pairs that, starting from the top-board, select a position with a board's field, one such
 * pair for each level of the board until a token spot is reached. Note that each spot in the game has exactly one such
 * sequence that identifies it, and every valid sequence identifies exactly one spot - where "valid" means with a length
 * matching the game height, and with all (row,col) values within the semi-open range [0,size).
 *
 * <P><B>Move Constraint:</B>
 *
 * A move constraint is a restriction that may be placed upon a player's current move, based on the immediately
 * preceding move of the opponent. There are several strategies that can be used to generate such constraints (see
 * {@link com.uttt.core.game.Rules.Constraint Constraint}), but they all share common aspects:
 *
 * <NL>
 *
 * <LI>A constraint may be null, meaning that a player may choose freely from all available spots
 *
 * <LI>It may, at most, specify one less (row,col) pairs than the height of the game.
 *
 * <LI>All boards reach through the (row,col) sequence must be open (playable). Although not strictly required, the
 * normal way this would be achieved is by examining a putative constraint for playability, if needed, successively
 * truncating it until it is playable or becomes the null constraint. Such behavior could, of course, be modified by
 * rule variation. Until such a variant is established, "truncation-until-playable" is the normative approach.
 *
 * </NL>
 *
 * Thus, a constraint might be at most as narrow as a specific bottom-board, but may also be a wide as a mid-board (of
 * any level), or even the whole game (when null).
 *
 * <P><B>Board Status: Open / Taken / Draw -</B>
 *
 * Each node will always be in one of several possible {@link com.uttt.core.board.Node.Status Status} states,
 * indicating that the node is "open" for play, has been "taken" by either token placement or sub-board win, or is in a
 * "draw" state where neither player has won and no spots are available for token placement within that board's
 * reachable leaf nodes. <I><B>NOTE:</B> The "draw" status is normally only possible for board nodes - not token nodes,
 * since a token position is either empty or taken by a player. Rule variations may, however, introduce a "blocking"
 * token which would behave like a draw, i.e. close a spot for play without giving control to either player.</I>
 *
 * <P><B>Meet:</B>
 *
 * While a Game takes a single board from initial configuration (typically empty), a "meet" is a series of games between
 * two given players. Such a meet should run several iterations for each of several variants and report result
 * statistics as appropriate.
 *
 * <P><B>Total Board:</B>
 *
 * Total set of all token spots across all bottom-boards in a given game. The number of such spots may be calculated by
 * <NOBR>[size ^ (2 * height)]</NOBR>. For "h1s3" (the classic), this gives 9. For the "basic UTTT" with "h2s3" we get
 * 81. For a monstrous game like "h4s5" there would be 390,625 token spots, while for the smaller sized "h4s3" we have
 * something more workable with a mere 6,561 token spots.
 *
 * <P><B>Win Conditions:</B>
 *
 * As in the classic "h1s3" configuration, a win for any board (top-, mid-, or bottom-board) is achieved by completely
 * controlling any single row, or col, or either diagonal. Once a board satisfies this win condition, no more tokens may
 * be played within it, even for empty spots.
 *
 * <P><B>Playable Spot:</B>
 *
 * A token may only be played on a bottom-board position (a "spot") that is empty and whose entire lineage of boards
 * have "open" status.
 *
 * <P><B>Spot:</B>
 *
 * A position on a bottom-board where a player's token may be placed, thus "taking" the spot for that player. This is
 * distinguished from the more general "position," which might be a sub-board or a spot.
 *
 * <P><B>Playability:</B>
 *
 * {@link com.uttt.core.board.Playable#isPlayable}.
 *
 * <HR>
 *
 * <P><B>QQQ:</B>
 *
 * Ipsum lorum.
 *
 * <P><B>QQQ:</B>
 *
 * Ipsum lorum.
 *
 * <P><B>QQQ:</B>
 *
 * Ipsum lorum.
 *
 * <P><B>QQQ:</B>
 *
 * Ipsum lorum.
 *
 * <P><B>QQQ:</B>
 *
 * Ipsum lorum.
 *
 */
package com.uttt.core.game;