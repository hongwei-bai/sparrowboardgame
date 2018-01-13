package sparrow.five;

import sparrow.model.ModelBase;

public class FiveModel extends ModelBase implements FiveConstants {
    private int[][] mGridData = new int[NUMBER_GRID][NUMBER_GRID];
    private boolean mNextMoveBlack = true;
    private FiveBoard mFiveBoard = null;

    public void setView(FiveBoard v) {
        mFiveBoard = v;
    }

    public boolean put(int color, int[] to) {
        int gridColor = mGridData[to[0]][to[1]];
        if (gridColor > STONE_NONE) {
            return false;
        }

        mGridData[to[0]][to[1]] = color;
        mNextMoveBlack = !mNextMoveBlack;
        addToMoveRecord(color, to);
        FiveJudge.judgeWin(this, mGridData, color, to[0], to[1]);
        return true;
    }

    public int[][] get() {
        return mGridData;
    }

    @Override
    public boolean move(int color, int[] from, int[] to) {
        boolean success = put(color, to);
        if (success) {
            mFiveBoard.repaint();
        }
        return success;
    }

    @Override
    public int getNextMovePlayer() {
        return mNextMoveBlack ? 0 : 1;
    }

    public int getNextMoveColor() {
        return mNextMoveBlack ? STONE_BLACK : STONE_WHITE;
    }

    @Override
    public int[][] getBoard() {
        return mGridData;
    }

    @Override
    public int getMaximumPlayerNumber() {
        return 2;
    }

    @Override
    public int getMinimumPlayerNumber() {
        return 2;
    }

    @Override
    public String getColorDescription(int color) {
        return color == STONE_BLACK ? "Black" : "White";
    }

}
