package sparrow.protocol;

import java.util.HashMap;

public interface ProtocolParser {
    public String getMessageId(String message);

    public String getValue(String message, String key);

    public HashMap<String, String> getValues(String message);

    public int[] parseMove(String value);

    public int[][] parseBoard(String value);

    public int[] parsePreviousMove(String value);
}
