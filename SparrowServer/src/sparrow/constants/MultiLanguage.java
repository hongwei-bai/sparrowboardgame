package sparrow.constants;

public class MultiLanguage {
    public interface ERROR {
        final public static String CLIENT_NUMBER_OVERFLOW = "Client number exceed.";
        final public static String PLAYER_MOVE_ERROR = "Player move error.";
    }

    public interface BUTTON {
        final public static String TAKE_OVER = "TakeOver";
        final public static String PAUSE = "||";
        final public static String RESUME = "|>";
        final public static String NEXT_STEP = ">";
    }

    public interface GAME_STATUS {
        final public static String GAME_PRE = "Game ";
        final public static String READY = "Ready";
        final public static String STARTED = "Started";
        final public static String CLIENT_ERROR = "Client Error!";
        final public static String GAME_OVER = "Over";
        final public static String WIN = " Win!";
        final public static String LOSE = " Lose.";
    }

    public interface PLAYER_STATUS {
        final public static String PLAYER_PRE = "Player #";
        final public static String WAITING = " is waiting...";
        final public static String CONNECTED = " connected.";
        final public static String DISCONNECTED = " disconnected.";
        final public static String TAKE_OVER = " is taken over.";
    }

    public interface FIVE {
        final public static String FOUL_LONG = "长连禁手";
        final public static String FOUL_THREE_THREE = "三三禁手";
        final public static String FOUL_FOUR_FOUR = "四四禁手";
        final public static String WIN_FIVE = "五连胜";
        final public static String WIN_FOUR = "四连胜";
    }

}
