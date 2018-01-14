package sparrow.model;

import java.util.ArrayList;

import sparrow.protocol.Protocol.MoveRecord;

abstract public class ModelBase {
    protected ArrayList<MoveRecord> mMoveRecords = new ArrayList<>();
    private int mSequenceNo = 1;
    public ResultDeliverObserver mResultDeliverObserver = null;

    public interface ResultDeliverObserver {
        public void onWin(int color, String info);

        public void onFoul(int color, String info);
    }

    public void setResultDeliverObserver(ResultDeliverObserver o) {
        mResultDeliverObserver = o;
    }

    public void notifyWin(int color, String info) {
        if (mResultDeliverObserver != null) {
            mResultDeliverObserver.onWin(color, info);
        }
    }

    public void notifyFoul(int color, String info) {
        if (mResultDeliverObserver != null) {
            mResultDeliverObserver.onFoul(color, info);
        }
    }

    abstract public boolean move(int color, int[] from, int[] to);

    public boolean move(int color, int[] point) {
        return move(color, null, point);
    }

    abstract public int getNextMovePlayer();

    public int getNextMoveColor() {
        return getColor(getNextMovePlayer());
    }

    public int getColor(int playerId) {
        return playerId + 1;
    }

    public int getPlayerId(int color) {
        return color - 1;
    }

    abstract public int[][] getBoard();

    public MoveRecord getPreviousMove() {
        if (!mMoveRecords.isEmpty()) {
            return mMoveRecords.get(mMoveRecords.size() - 1);
        }
        return null;
    }

    protected void addToMoveRecord(int color, int[] to) {
        mMoveRecords.add(new MoveRecord(mSequenceNo, color, to));
    }

    public int getMoveSN() {
        return mSequenceNo;
    }

    public void incSN() {
        mSequenceNo++;
    }

    abstract public int getMaximumPlayerNumber();

    abstract public int getMinimumPlayerNumber();

    abstract public String getColorDescription(int color);

    abstract public String getMoveDescription(int[] from, int[] to);

    public String getMoveDescription(int[] to) {
        return getMoveDescription(null, to);
    }

    public static class MoveData {
        public int id;
        public int[] from;
        public int[] to;
    }

}
