package sparrow.constants;

public class Protocol {
    public interface MSGID {
        public final static String CONNECTION = "[CONN]";
        public final static String PLAYER_MOVE = "[MOVE]";
    }

    public interface KEY {
        public final static String PLAYER_NAME = "player_name";
    }

    public static String buildPairString(String key, String value) {
        return key + "=" + value;
    }

    public static String getMessageBody(String message) {
        return message.substring(message.indexOf("]") + 1);
    }

    public static String getValue(String message, String key) {
        String[] pairs = getMessageBody(message).split(" ");
        for (String pair : pairs) {
            String[] col = pair.split("=");
            if (col[0].equals(key)) {
                return col[1];
            }
        }
        return null;
    }
}
