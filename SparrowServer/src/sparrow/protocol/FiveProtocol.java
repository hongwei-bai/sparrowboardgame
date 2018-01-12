package sparrow.protocol;

public class FiveProtocol extends Protocol {
    public FiveProtocol() {

    }

    @Override
    public ProtocolMessage addColor(int color) {
        return appendPair(KEY.COLOR, Integer.toString(color));
    }

    @Override
    public int[][] parseBoard(String value) {
        int rowL = (int) Math.sqrt(value.length());
        int[][] r = new int[rowL][rowL];
        for (int i = 0; i < rowL; i++) {
            for (int j = 0; j < rowL; j++) {
                r[i][j] = value.indexOf(i * rowL + j);
            }
        }
        return r;
    }

    @Override
    public ProtocolMessage addBoard(int[][] board) {
        String string = "";
        for (int[] row : board) {
            for (int i : row) {
                string += i;
            }
        }
        return appendPair(KEY.BOARD, string);
    }

    @Override
    public ProtocolMessage addPreviousMove(MoveRecord moveRecord) {
        if (null == moveRecord) {
            return this;
        }
        return appendPair(KEY.PREVIOUS_MOVE, moveRecord.to[0] + "," + moveRecord.to[1]);
    }

    @Override
    public int[] parsePreviousMove(String value) {
        return parseMove(value);
    }

    @Override
    public ProtocolMessage addMove(int[] point) {
        return appendPair(KEY.MOVE, point[0] + "," + point[1]);
    }

    @Override
    public int[] parseMove(String value) {
        int[] r = new int[2];
        String[] s = value.split(",");
        r[0] = Integer.valueOf(s[0]);
        r[1] = Integer.valueOf(s[1]);
        return r;
    }

    @Override
    public ProtocolMessage addSN(int sn) {
        return appendPair(KEY.SN, Integer.toString(sn));
    }
}
