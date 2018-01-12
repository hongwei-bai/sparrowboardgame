package sparrow.protocol;

import java.util.HashMap;

abstract public class Protocol implements ProtocolMessage, ProtocolParser {
    public final static String INTER_PAIR_SEPARATOR = " ";
    public final static String INNNER_PAIR_SEPARATOR = "=";
    private String mMessage = null;

    public Protocol() {
        mMessage = "";
    }

    public interface MSGID {
        public final static String CONNECTION = "connection";
        public final static String MOVE = "move";
    }

    public interface KEY {
        public final static String MSGID = "msgid";
        public final static String PLAYER_NAME = "player_name";
        public final static String SN = "sn";
        public final static String BOARD = "board";
        public final static String PREVIOUS_MOVE = "previous_move";
        public final static String COLOR = "color";
        public final static String MOVE = "move";
    }

    protected static String appendSeparator(String message) {
        if (!message.isEmpty()) {
            return message + INTER_PAIR_SEPARATOR;
        }
        return message;
    }

    public static ProtocolMessage buildMessage() {
        return new FiveProtocol();
    }

    @Override
    public String getMessage() {
        return mMessage;
    }

    protected ProtocolMessage appendPair(String key, String value) {
        this.mMessage = appendSeparator(this.mMessage);
        this.mMessage += key + INNNER_PAIR_SEPARATOR;
        this.mMessage += value;
        return this;
    }

    @Override
    public String getValue(String message, String key) {
        String[] pairs = message.split(INTER_PAIR_SEPARATOR);
        for (String pair : pairs) {
            String[] col = pair.split(INNNER_PAIR_SEPARATOR);
            if (col[0].equals(key)) {
                return col[1];
            }
        }
        return null;
    }

    @Override
    public HashMap<String, String> getValues(String message) {
        HashMap<String, String> hashMap = new HashMap<>();
        String[] pairs = message.split(INTER_PAIR_SEPARATOR);
        for (String pair : pairs) {
            String[] col = pair.split(INNNER_PAIR_SEPARATOR);
            hashMap.put(col[0], col[1]);
        }
        return hashMap;
    }

    @Override
    public ProtocolMessage setMessageId(String msgid) {
        this.mMessage += KEY.MSGID + INNNER_PAIR_SEPARATOR + msgid;
        return this;
    }

    @Override
    public String getMessageId(String message) {
        String[] pairs = message.split(INTER_PAIR_SEPARATOR);
        String[] col = pairs[0].split(INNNER_PAIR_SEPARATOR);
        if (col[0].equals(KEY.MSGID)) {
            return col[1];
        }
        return null;
    }

    @Override
    public ProtocolMessage addPlayerName(String name) {
        return appendPair(KEY.PLAYER_NAME, name);
    }

    public static class MoveRecord {
        public int seqNo;
        public int color;
        public int type;
        public int[] from;
        public int[] to;

        public MoveRecord(int sn, int color, int[] to) {
            this.seqNo = sn;
            this.color = color;
            this.to = to;
        }
    }
}
