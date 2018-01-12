package sparrow.protocol;

import sparrow.protocol.Protocol.MoveRecord;

public interface ProtocolMessage {

    public String getMessage();

    public ProtocolMessage setMessageId(String msgid);

    public ProtocolMessage addPlayerName(String name);

    public ProtocolMessage addSN(int sn);

    public ProtocolMessage addColor(int color);

    public ProtocolMessage addBoard(int[][] board);

    public ProtocolMessage addPreviousMove(MoveRecord moveRecord);

    public ProtocolMessage addMove(int[] point);
}
