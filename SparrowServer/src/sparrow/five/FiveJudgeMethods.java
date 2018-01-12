package sparrow.five;

import java.util.ArrayList;

import sparrow.util.Log;

public class FiveJudgeMethods {
    final private static int NUMBER_GRID = FiveModel.NUMBER_GRID;
    final private static int STONE_NONE = FiveModel.STONE_NONE;
    final private static int STONE_BLACK = FiveModel.STONE_BLACK;
    final private static int STONE_WHITE = FiveModel.STONE_WHITE;
    final private static String[] CHAR_ARRAY = FiveBoard.CHAR_ARRAY;

    public static int countStoneInRow(int[][] board, int color, int x, int y) {
        int px = x + 1;
        int count = 1;
        while (px < NUMBER_GRID && board[px][y] == color) {
            px++;
            count++;
        }
        px = x - 1;
        while (px >= 0 && board[px][y] == color) {
            px--;
            count++;
        }
        return count;
    }

    public static int countStoneInColumn(int[][] board, int color, int x, int y) {
        int py = y + 1;
        int count = 1;
        while (py < NUMBER_GRID && board[x][py] == color) {
            py++;
            count++;
        }
        py = y - 1;
        while (py >= 0 && board[x][py] == color) {
            py--;
            count++;
        }
        return count;
    }

    public static int countStoneInClock2(int[][] board, int color, int x, int y) {
        int px = x + 1;
        int py = y + 1;
        int count = 1;
        while (px < NUMBER_GRID && py < NUMBER_GRID && board[px][py] == color) {
            px++;
            py++;
            count++;
        }
        px = x - 1;
        py = y - 1;
        while (px >= 0 && py >= 0 && board[px][py] == color) {
            px--;
            py--;
            count++;
        }
        return count;
    }

    public static int countStoneInClock4(int[][] board, int color, int x, int y) {
        int px = x - 1;
        int py = y + 1;
        int count = 1;
        while (px >= 0 && py < NUMBER_GRID && board[px][py] == color) {
            px--;
            py++;
            count++;
        }
        px = x + 1;
        py = y - 1;
        while (px < NUMBER_GRID && py >= 0 && board[px][py] == color) {
            px++;
            py--;
            count++;
        }
        return count;
    }

    public static ArrayList<Pattern> findStonesInRow(int[][] board, int color, int x, int y) {
        ArrayList<Pattern> listPattern = new ArrayList<>();

        for (int row = 0; row < NUMBER_GRID; row++) {
            int count = 0;
            for (int col = 0; col < NUMBER_GRID; col++) {
                int data = board[col][row];
                if (color == data) {
                    count++;
                } else {
                    if (count == 4) {
                        listPattern.add(new Pattern(4, col, row, 1, 0));
                    } else if (count == 3) {
                        listPattern.add(new Pattern(3, col, row, 1, 0));
                    } else if (count == 2) {
                        listPattern.add(new Pattern(2, col, row, 1, 0));
                    }
                    count = 0;
                }
            }
            if (count == 4) {
                listPattern.add(new Pattern(4, NUMBER_GRID, row, 1, 0));
            } else if (count == 3) {
                listPattern.add(new Pattern(3, NUMBER_GRID, row, 1, 0));
            }
        }

        return listPattern;
    }

    public static ArrayList<Pattern> findStonesInColumn(int[][] board, int color, int x, int y) {
        ArrayList<Pattern> listPattern = new ArrayList<>();

        for (int col = 0; col < NUMBER_GRID; col++) {
            int count = 0;
            for (int row = 0; row < NUMBER_GRID; row++) {
                int data = board[col][row];
                if (color == data) {
                    count++;
                } else {
                    if (count == 4) {
                        listPattern.add(new Pattern(4, col, row, 0, 1));
                    } else if (count == 3) {
                        listPattern.add(new Pattern(3, col, row, 0, 1));
                    } else if (count == 2) {
                        listPattern.add(new Pattern(2, col, row, 0, 1));
                    }
                    count = 0;
                }
            }
            if (count == 4) {
                listPattern.add(new Pattern(4, col, NUMBER_GRID, 0, 1));
            } else if (count == 3) {
                listPattern.add(new Pattern(3, col, NUMBER_GRID, 0, 1));
            }
        }

        return listPattern;
    }

    public static ArrayList<Pattern> findStonesInClock2(int[][] board, int color, int x, int y) {
        ArrayList<Pattern> listPattern = new ArrayList<>();

        for (int colBase = 0; colBase < NUMBER_GRID; colBase++) {
            int count = 0;
            int row, col;
            for (row = 0, col = colBase; row < NUMBER_GRID && col < NUMBER_GRID; row++, col++) {
                int data = board[col][row];
                if (color == data) {
                    count++;
                } else {
                    if (count == 4) {
                        listPattern.add(new Pattern(4, col, row, 1, 1));
                    } else if (count == 3) {
                        listPattern.add(new Pattern(3, col, row, 1, 1));
                    } else if (count == 2) {
                        listPattern.add(new Pattern(2, col, row, 1, 1));
                    }
                    count = 0;
                }
            }
            if (count == 4) {
                listPattern.add(new Pattern(4, col, row, 1, 1));
            } else if (count == 3) {
                listPattern.add(new Pattern(3, col, row, 1, 1));
            }
        }

        for (int rowBase = 1; rowBase < NUMBER_GRID; rowBase++) {
            int count = 0;
            int row, col;
            for (row = rowBase, col = 0; row < NUMBER_GRID && col < NUMBER_GRID; row++, col++) {
                int data = board[col][row];
                if (color == data) {
                    count++;
                } else {
                    if (count == 4) {
                        listPattern.add(new Pattern(4, col, row, 1, 1));
                    } else if (count == 3) {
                        listPattern.add(new Pattern(3, col, row, 1, 1));
                    } else if (count == 2) {
                        listPattern.add(new Pattern(2, col, row, 1, 1));
                    }
                    count = 0;
                }
            }
            if (count == 4) {
                listPattern.add(new Pattern(4, col, row, 1, 1));
            } else if (count == 3) {
                listPattern.add(new Pattern(3, col, row, 1, 1));
            }
        }

        return listPattern;
    }

    public static ArrayList<Pattern> findStonesInClock4(int[][] board, int color, int x, int y) {
        ArrayList<Pattern> listPattern = new ArrayList<>();

        for (int colBase = 0; colBase < NUMBER_GRID; colBase++) {
            int count = 0;
            int row, col;
            for (row = NUMBER_GRID - 1, col = colBase; row >= 0
                    && col < NUMBER_GRID; row--, col++) {
                int data = board[col][row];
                if (color == data) {
                    count++;
                } else {
                    if (count == 4) {
                        listPattern.add(new Pattern(4, col, row, 1, -1));
                    } else if (count == 3) {
                        listPattern.add(new Pattern(3, col, row, 1, -1));
                    } else if (count == 2) {
                        listPattern.add(new Pattern(2, col, row, 1, -1));
                    }
                    count = 0;
                }
            }
            if (count == 4) {
                listPattern.add(new Pattern(4, col, row, 1, -1));
            } else if (count == 3) {
                listPattern.add(new Pattern(3, col, row, 1, -1));
            }
        }

        for (int rowBase = NUMBER_GRID - 2; rowBase >= 0; rowBase--) {
            int count = 0;
            int row, col;
            for (row = rowBase, col = 0; row >= 0 && col < NUMBER_GRID; row--, col++) {
                int data = board[col][row];
                if (color == data) {
                    count++;
                } else {
                    if (count == 4) {
                        listPattern.add(new Pattern(4, col, row, 1, -1));
                    } else if (count == 3) {
                        listPattern.add(new Pattern(3, col, row, 1, -1));
                    } else if (count == 2) {
                        listPattern.add(new Pattern(2, col, row, 1, -1));
                    }
                    count = 0;
                }
            }
            if (count == 4) {
                listPattern.add(new Pattern(4, col, row, 1, -1));
            } else if (count == 3) {
                listPattern.add(new Pattern(3, col, row, 1, -1));
            }
        }

        return listPattern;
    }

    public static ArrayList<Pattern> findJumpInList(int[][] board, int color, int x, int y,
            ArrayList<Pattern> list) {
        for (Pattern pattern : list) {
            if (pattern.pattern == Pattern.PATTERN_STRAIGHT && pattern.straight == 3) {
                if (isJumpFourUp(board, color, pattern.stones)) {
                    pattern.straight++;
                    pattern.pattern = Pattern.PATTERN_JUMP;
                    pattern.stones.add(getJumpUpStone(pattern.stones));
                } else if (isJumpFourDown(board, color, pattern.stones)) {
                    pattern.straight++;
                    pattern.pattern = Pattern.PATTERN_JUMP;
                    pattern.stones.add(getJumpDownStone(pattern.stones));
                    pattern.stones = sort(pattern.stones);
                }
            }

            if (pattern.pattern == Pattern.PATTERN_STRAIGHT && pattern.straight == 2) {
                if (isJumpFourUp(board, color, pattern.stones)) {
                    pattern.straight++;
                    pattern.pattern = Pattern.PATTERN_JUMP;
                    pattern.stones.add(getJumpUpStone(pattern.stones));
                } else if (isJumpFourDown(board, color, pattern.stones)) {
                    pattern.straight++;
                    pattern.pattern = Pattern.PATTERN_JUMP;
                    pattern.stones.add(getJumpDownStone(pattern.stones));
                    pattern.stones = sort(pattern.stones);
                }
            }
        }
        return list;
    }

    public static ArrayList<Pattern> filterLiveFour(int[][] board, int color,
            ArrayList<Pattern> list) {
        ArrayList<Pattern> listFilter = new ArrayList<>();
        for (Pattern pattern : list) {
            if (isFourRush(board, color, pattern.stones)) {
                listFilter.add(pattern);
            }
        }
        return listFilter;
    }

    public static ArrayList<Pattern> filterLiveThree(int[][] board, int color,
            ArrayList<Pattern> list) {
        ArrayList<Pattern> listFilter = new ArrayList<>();
        for (Pattern pattern : list) {
            if (isLiveThree(board, color, pattern.stones)) {
                listFilter.add(pattern);
            }
        }
        return listFilter;
    }

    public static boolean hasCommonPoint(ArrayList<Pattern> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                Pattern p1 = list.get(i);
                Pattern p2 = list.get(j);
                if (hasCommonPoint(p1, p2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasCommonPoint(Pattern p1, Pattern p2) {
        for (Stone s1 : p1.stones) {
            for (Stone s2 : p2.stones) {
                if (s1.equals(s2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isJumpFourUp(int[][] board, int color, ArrayList<Stone> threeStones) {
        Stone stoneUp = getAdjcentUpStone(threeStones);
        Stone stoneUp2 = getJumpUpStone(threeStones);
        Stone stoneUp3 = get2JumpUpStone(threeStones);
        return isNonePoint(board, stoneUp) && isSameColor(board, color, stoneUp2)
                && !isSameColor(board, color, stoneUp3);
    }

    private static boolean isJumpFourDown(int[][] board, int color, ArrayList<Stone> threeStones) {
        Stone stoneDown = getAdjcentDownStone(threeStones);
        Stone stoneDown2 = getJumpDownStone(threeStones);
        Stone stoneDown3 = get2JumpDownStone(threeStones);
        return isNonePoint(board, stoneDown) && isSameColor(board, color, stoneDown2)
                && !isSameColor(board, color, stoneDown3);
    }

    private static boolean isLiveThree(int[][] board, int color, ArrayList<Stone> stones) {
        if (stones.size() != 3) {
            return false;
        }

        Log.d(Pattern.stonesToString(stones));
        if (isContinuous(stones)) {
            Stone stoneUp = getAdjcentUpStone(stones);
            ArrayList<Stone> stonePlusUp = new ArrayList<>();
            stonePlusUp.addAll(stones);
            stonePlusUp.add(stoneUp);
            boolean liveUp = isNonePoint(board, stoneUp)
                    && isStraightFourLive(board, color, stonePlusUp);

            Stone stoneDown = getAdjcentDownStone(stones);
            ArrayList<Stone> stonePlusDown = new ArrayList<>();
            stonePlusDown.addAll(stones);
            stonePlusDown.add(stoneDown);
            boolean liveDown = isNonePoint(board, stoneDown)
                    && isStraightFourLive(board, color, stonePlusDown);

            return liveUp || liveDown;
        } else {
            Stone gapStone = getGapStone(stones);
            ArrayList<Stone> stonePlusMid = new ArrayList<>();
            stonePlusMid.addAll(stones);
            stonePlusMid.add(gapStone);
            return isStraightFourLive(board, color, stonePlusMid);
        }
    }

    private static boolean isFourRush(int[][] board, int color, ArrayList<Stone> stones) {
        if (stones.size() != 4) {
            return false;
        }

        if (isContinuous(stones)) {
            return isStraightFourRush(board, color, stones);
        }
        return true;
    }

    private static boolean isStraightFourRush(int[][] board, int color, ArrayList<Stone> stones) {
        Stone stoneUp = getAdjcentUpStone(stones);
        Stone stoneDown = getAdjcentDownStone(stones);
        Stone stoneUp2 = getJumpUpStone(stones);
        Stone stoneDown2 = getJumpDownStone(stones);

        boolean liveUp = isNonePoint(board, stoneUp) && !isSameColor(board, color, stoneUp2);
        boolean liveDown = isNonePoint(board, stoneDown) && !isSameColor(board, color, stoneDown2);
        boolean deadUp = isBlocked(board, color, stoneUp);
        boolean deadDown = isBlocked(board, color, stoneDown);

        return (liveUp && deadDown) || (liveDown && deadUp);
    }

    private static boolean isStraightFourLive(int[][] board, int color, ArrayList<Stone> stones) {
        stones = sort(stones);
        Stone stoneUp = getAdjcentUpStone(stones);
        Stone stoneDown = getAdjcentDownStone(stones);
        Stone stoneUp2 = getJumpUpStone(stones);
        Stone stoneDown2 = getJumpDownStone(stones);

        boolean liveUp = isNonePoint(board, stoneUp) && !isSameColor(board, color, stoneUp2);
        boolean liveDown = isNonePoint(board, stoneDown) && !isSameColor(board, color, stoneDown2);
        return liveUp && liveDown;
    }

    private static boolean isNonePoint(int[][] board, Stone stone) {
        if (stone.p[0] < NUMBER_GRID && stone.p[1] < NUMBER_GRID && stone.p[0] >= 0
                && stone.p[1] >= 0) {
            if (board[stone.p[0]][stone.p[1]] == STONE_NONE) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSameColor(int[][] board, int color, Stone stone) {
        if (stone.p[0] < NUMBER_GRID && stone.p[1] < NUMBER_GRID && stone.p[0] >= 0
                && stone.p[1] >= 0) {
            if (board[stone.p[0]][stone.p[1]] == color) {
                return true;
            }
        }
        return false;
    }

    private static boolean isBlocked(int[][] board, int color, Stone stone) {
        if (stone.p[0] < NUMBER_GRID && stone.p[1] < NUMBER_GRID && stone.p[0] >= 0
                && stone.p[1] >= 0) {
            if (board[stone.p[0]][stone.p[1]] == getOpponent(color)) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private static int getOpponent(int color) {
        return STONE_BLACK == color ? STONE_WHITE : STONE_BLACK;
    }

    private static Stone getAdjcentUpStone(ArrayList<Stone> sorted) {
        int xInc = sorted.get(1).p[0] - sorted.get(0).p[0];
        int yInc = sorted.get(1).p[1] - sorted.get(0).p[1];
        int xUp = sorted.get(sorted.size() - 1).p[0] + xInc;
        int yUp = sorted.get(sorted.size() - 1).p[1] + yInc;
        return new Stone(xUp, yUp);
    }

    private static Stone getAdjcentDownStone(ArrayList<Stone> sorted) {
        int xInc = sorted.get(1).p[0] - sorted.get(0).p[0];
        int yInc = sorted.get(1).p[1] - sorted.get(0).p[1];
        int xDown = sorted.get(0).p[0] - xInc;
        int yDown = sorted.get(0).p[1] - yInc;
        return new Stone(xDown, yDown);
    }

    private static Stone getJumpUpStone(ArrayList<Stone> sorted) {
        int xInc = sorted.get(1).p[0] - sorted.get(0).p[0];
        int yInc = sorted.get(1).p[1] - sorted.get(0).p[1];
        int xUp2 = sorted.get(sorted.size() - 1).p[0] + xInc + xInc;
        int yUp2 = sorted.get(sorted.size() - 1).p[1] + yInc + yInc;
        return new Stone(xUp2, yUp2);
    }

    private static Stone getJumpDownStone(ArrayList<Stone> sorted) {
        int xInc = sorted.get(1).p[0] - sorted.get(0).p[0];
        int yInc = sorted.get(1).p[1] - sorted.get(0).p[1];
        int xDown2 = sorted.get(0).p[0] - xInc - xInc;
        int yDown2 = sorted.get(0).p[1] - yInc - yInc;
        return new Stone(xDown2, yDown2);
    }

    private static Stone get2JumpUpStone(ArrayList<Stone> sorted) {
        int xInc = sorted.get(1).p[0] - sorted.get(0).p[0];
        int yInc = sorted.get(1).p[1] - sorted.get(0).p[1];
        int xUp3 = sorted.get(sorted.size() - 1).p[0] + xInc + xInc + xInc;
        int yUp3 = sorted.get(sorted.size() - 1).p[1] + yInc + yInc + yInc;
        return new Stone(xUp3, yUp3);
    }

    private static Stone get2JumpDownStone(ArrayList<Stone> sorted) {
        int xInc = sorted.get(1).p[0] - sorted.get(0).p[0];
        int yInc = sorted.get(1).p[1] - sorted.get(0).p[1];
        int xDown3 = sorted.get(0).p[0] - xInc - xInc - xInc;
        int yDown3 = sorted.get(0).p[1] - yInc - yInc - yInc;
        return new Stone(xDown3, yDown3);
    }

    private static boolean isContinuous(ArrayList<Stone> sorted) {
        for (int i = 1; i < sorted.size(); i++) {
            if (isVertical(sorted)) {
                if (sorted.get(i).p[1] != sorted.get(i - 1).p[1] + 1) {
                    return false;
                }
            } else {
                if (sorted.get(i).p[0] != sorted.get(i - 1).p[0] + 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Stone getGapStone(ArrayList<Stone> sorted) {
        int i = 1;
        for (; i < sorted.size(); i++) {
            if (isVertical(sorted)) {
                if (sorted.get(i).p[1] == sorted.get(i - 1).p[1] + 2) {
                    break;
                }
            } else {
                if (sorted.get(i).p[0] == sorted.get(i - 1).p[0] + 2) {
                    break;
                }
            }
        }
        int x = (sorted.get(i).p[0] + sorted.get(i - 1).p[0]) >>> 1;
        int y = (sorted.get(i).p[1] + sorted.get(i - 1).p[1]) >>> 1;
        return new Stone(x, y);
    }

    private static ArrayList<Stone> sort(ArrayList<Stone> stones) {
        if (stones.size() <= 1) {
            Log.e("sort error.");
        }

        if (isVertical(stones)) {
            stones = insertSortY(stones);
        } else {
            stones = insertSortX(stones);
        }

        return stones;
    }

    private static boolean isVertical(ArrayList<Stone> stones) {
        return stones.get(0).p[0] == stones.get(1).p[0];
    }

    private static ArrayList<Stone> insertSortY(ArrayList<Stone> a) {
        for (int i = 0; i < a.size(); i++) {
            for (int j = i; j > 0; j--) {
                if (a.get(j).p[1] < a.get(j - 1).p[1]) {
                    Stone t = a.get(j - 1);
                    a.set(j - 1, a.get(j));
                    a.set(j, t);
                }
            }
        }
        return a;
    }

    private static ArrayList<Stone> insertSortX(ArrayList<Stone> a) {
        for (int i = 0; i < a.size(); i++) {
            for (int j = i; j > 0; j--) {
                if (a.get(j).p[0] < a.get(j - 1).p[0]) {
                    Stone t = a.get(j - 1);
                    a.set(j - 1, a.get(j));
                    a.set(j, t);
                }
            }
        }
        return a;
    }

    public static class Pattern {
        public static int PATTERN_STRAIGHT = 1;
        public static int PATTERN_JUMP = 2;
        public static String[] PATTERN_DESC = { "", "Straight", "Jump" };

        public int pattern;
        public int straight;
        public ArrayList<Stone> stones = new ArrayList<>();

        public Pattern(int straight, int lastXPlus, int lastYPlus, int incX, int incY) {
            pattern = PATTERN_STRAIGHT;
            this.straight = straight;
            for (int i = straight; i > 0; i--) {
                int x = lastXPlus - incX * i;
                int y = lastYPlus - incY * i;
                stones.add(new Stone(x, y));
            }
        }

        public String toString() {
            String string = "";
            string += PATTERN_DESC[pattern];
            string += " x" + straight;
            for (Stone stone : stones) {
                string += " " + stone.toString();
            }
            return string;
        }

        public static String stonesToString(ArrayList<Stone> l) {
            String string = "";
            for (Stone stone : l) {
                string += stone.toString() + " ";
            }
            return string;
        }
    }

    public static class Stone {
        public int[] p = new int[2];

        public Stone(int x, int y) {
            p[0] = x;
            p[1] = y;
        }

        @Override
        public boolean equals(Object s) {
            if (s instanceof Stone) {
                return p[0] == ((Stone) s).p[0] && p[1] == ((Stone) s).p[1];
            }
            return false;
        }

        public String toString() {
            String xString = CHAR_ARRAY[p[0]];
            int yStringInt = p[1] + 1;
            return xString + yStringInt;
        }
    }
}
