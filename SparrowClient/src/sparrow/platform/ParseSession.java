package sparrow.platform;

import java.util.Map;

import sparrow.protocol.Protocol;
import sparrow.protocol.ProtocolParser;

public class ParseSession {
    private Map<String, String> mMap;
    private int mSequenceNo;
    private int mColor;
    private int[][] mBoard;
    private int[] mMove;
    private int[] mPreviousMove;

    public ParseSession() {
    }

    public void readMessage(ProtocolParser parser, String message) {
        mMap = parser.getValues(message);
        mSequenceNo = Integer.valueOf(mMap.get(Protocol.KEY.SN));
        mColor = Integer.valueOf(mMap.get(Protocol.KEY.COLOR));
        mBoard = parser.parseBoard(mMap.get(Protocol.KEY.BOARD));
        if (mMap.containsKey(Protocol.KEY.PREVIOUS_MOVE)) {
            mPreviousMove = parser.parsePreviousMove(mMap.get(Protocol.KEY.PREVIOUS_MOVE));
        }
    }

    public int getColor() {
        return mColor;
    }

    public int getSN() {
        return mSequenceNo;
    }

    public int[][] getBoard() {
        return mBoard;
    }

    public int[] getMove() {
        return mMove;
    }

    public int[] getPreviousMove() {
        return mPreviousMove;
    }
}
