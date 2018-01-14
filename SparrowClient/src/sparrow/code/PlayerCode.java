package sparrow.code;

import java.util.ArrayList;

import sparrow.five.FiveJudgeMethods;
import sparrow.five.FiveJudgeMethods.Pattern;
import sparrow.platform.PlayerInterface;

public class PlayerCode implements PlayerInterface {
    final private int INFINITY = 20000;
    private int[] mMove = new int[2];

    /* Depth less than 5 is not meaningful */
    /* 3-level-search is able to deal with rush four and jump four */
    /* 5-level-search is able to deal with live three */
    private int MAX_DEPTH = 5;

    @Override
    public String getPlayerName() {
        return "SNAB.Java";// Simple-Negamax-AlphaBeta
    }

    @Override
    public int[] move(int sn, int color, int[][] board, int[] prev) {
        if (1 == sn) {
            resetMove();
            return mMove;
        }

        resetMove();
        alphabeta(color, board, MAX_DEPTH, -INFINITY, INFINITY);
        return mMove;
    }

    private static int debugCount = 0;

    private int alphabeta(int color, int[][] board, int depth, int alpha, int beta) {
        if (depth < MAX_DEPTH) {
            int e = evaluate(color, board);
            if (e == INFINITY) {
                if (debugCount++ == 0) {
                    printBoard(board);
                }
                return INFINITY - (20 - depth);
            }

            if (e == -INFINITY) {
                return -INFINITY + (20 - depth);
            }

            if (depth <= 0) {
                return e;
            }
        }

        ArrayList<Move> movelist = generateMoves(board);
        for (Move m : movelist) {

            makeMove(board, m.x, m.y, color);
            int score = -alphabeta(getOpponent(color), board, depth - 1, -beta, -alpha);
            unMakeMove(board, m.x, m.y);

            if (score > alpha) {
                alpha = score;
                if (depth == MAX_DEPTH) {
                    setMove(m.x, m.y);
                }
            }

            if (alpha >= beta) {
                break;
            }
        }
        return alpha;
    }

    private void makeMove(int[][] board, int x, int y, int color) {
        board[x][y] = color;
    }

    private void unMakeMove(int[][] board, int x, int y) {
        board[x][y] = 0;
    }

    private void setMove(int x, int y) {
        mMove[0] = x;
        mMove[1] = y;
    }

    private ArrayList<Move> generateMoves(int[][] board) {
        ArrayList<Move> list = new ArrayList<>();
        int minx = 7;
        int miny = 7;
        int maxx = 7;
        int maxy = 7;
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                int data = board[x][y];
                if (data > 0) {
                    if (x < minx)
                        minx = x;
                    if (x > maxx)
                        maxx = x;
                    if (y < miny)
                        miny = y;
                    if (y > maxy)
                        maxy = y;
                }
            }
        }

        int x0 = Math.max(0, minx - 2);
        int x1 = Math.min(15, maxx + 2);
        int y0 = Math.max(0, miny - 2);
        int y1 = Math.min(15, maxy + 2);

        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                if (0 == board[x][y]) {
                    list.add(new Move(x, y));
                }
            }
        }
        return list;
    }

    public class Move {
        public int x;
        public int y;

        public Move(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private int evaluate(int color, int[][] board) {
        ArrayList<Pattern> list = FiveJudgeMethods.findStonesInRow(board, color);
        list.addAll(FiveJudgeMethods.findStonesInColumn(board, color));
        list.addAll(FiveJudgeMethods.findStonesInClock2(board, color));
        list.addAll(FiveJudgeMethods.findStonesInClock4(board, color));

        int e = 0;
        for (Pattern pattern : list) {
            if (pattern.straight == 5) {
                return INFINITY;
            } else if (pattern.straight > 5) {
                if (color == 1) {
                    return -INFINITY;
                } else {
                    return INFINITY;
                }
            }
            // else if (pattern.straight == 4 && pattern.pattern ==
            // Pattern.PATTERN_STRAIGHT) {
            // e += 60;
            // } else if (pattern.straight == 4 && pattern.pattern ==
            // Pattern.PATTERN_JUMP) {
            // e += 60;
            // } else if (pattern.straight == 3 && pattern.pattern ==
            // Pattern.PATTERN_STRAIGHT) {
            // e += 40;
            // } else if (pattern.straight == 3 && pattern.pattern ==
            // Pattern.PATTERN_JUMP) {
            // e += 30;
            // } else if (pattern.straight == 2 && pattern.pattern ==
            // Pattern.PATTERN_STRAIGHT) {
            // e += 10;
            // } else if (pattern.straight == 2 && pattern.pattern ==
            // Pattern.PATTERN_JUMP) {
            // e += 5;
            // }
        }

        // list = FiveJudgeMethods.findJumpInList(board, color, list);
        // ArrayList<Pattern> list4 = FiveJudgeMethods.filterRushFour(board,
        // color, list);
        // ArrayList<Pattern> list3 = FiveJudgeMethods.filterLiveThree(board,
        // color, list);
        //
        // if (FiveJudgeMethods.hasCommonPoint(list4)) {
        // if (color == 1) {
        // return -INFINITY + (20 - depth);
        // }
        // }

        // if (FiveJudgeMethods.hasCommonPoint(list3)) {
        // if (color == 1) {
        // return -INFINITY + (20 - depth);
        // }
        // }

        // e += list4.size() * 120 + list3.size() * 120;

        // for (int i = 0; i < 15; i++) {
        // for (int j = 0; j < 15; j++) {
        // if (board[i][j] == color) {
        // e += WEIGHT[i][j];
        // }
        // }
        // }
        return e;
    }

    private int getOpponent(int color) {
        return color == 1 ? 2 : 1;
    }

    private void resetMove() {
        mMove[0] = 7;
        mMove[1] = 7;
    }

    public static void printBoard(int[][] board) {
        for (int i = 14; i >= 0; i--) {
            for (int j = 0; j < 15; j++) {
                if (board[j][i] > 0) {
                    System.out.print(board[j][i] == 1 ? "*" : "o");
                } else {
                    System.out.print("-");
                }
            }
            System.out.println("");
        }
    }

    public static int[][] WEIGHT = { { 0, 1, 2, 3, 4, 5, 6, 7, 6, 5, 4, 3, 2, 1, 0 },
            { 1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1 },
            { 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2 },
            { 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3 },
            { 4, 5, 6, 7, 8, 9, 10, 11, 10, 9, 8, 7, 6, 5, 4 },
            { 5, 6, 7, 8, 9, 10, 11, 12, 11, 10, 9, 8, 7, 6, 5 },
            { 6, 7, 8, 9, 10, 11, 12, 13, 12, 11, 10, 9, 8, 7, 6 },
            { 7, 8, 9, 10, 11, 12, 13, 14, 13, 12, 11, 10, 9, 8, 7 },
            { 6, 7, 8, 9, 10, 11, 12, 13, 12, 11, 10, 9, 8, 7, 6 },
            { 5, 6, 7, 8, 9, 10, 11, 12, 11, 10, 9, 8, 7, 6, 5 },
            { 4, 5, 6, 7, 8, 9, 10, 11, 10, 9, 8, 7, 6, 5, 4 },
            { 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3 },
            { 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2 },
            { 1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1 },
            { 0, 1, 2, 3, 4, 5, 6, 7, 6, 5, 4, 3, 2, 1, 0 }, };

}
