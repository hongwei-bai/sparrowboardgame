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
}
