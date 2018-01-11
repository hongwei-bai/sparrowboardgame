package sparrow.constants;

public class MultiLanguage {
    public interface ERROR {
        public static final String CLIENT_NUMBER_OVERFLOW = "Client number exceed.";
    }

    public interface BUTTON {
        public static final String TAKE_OVER = "TakeOver";
        public static final String PAUSE = "||";
        public static final String RESUME = "|>";
        public static final String NEXT_STEP = ">";
    }

    public interface GAME_STATUS {
        public static final String GAME_PRE = "Game ";
        public static final String READY = "Ready";
        public static final String STARTED = "Started";
        public static final String CLIENT_ERROR = "Client Error!";
        public static final String GAME_OVER = "Over";
        public static final String WIN = " Win!";
        public static final String LOSE = " Lose.";
    }

    public interface PLAYER_STATUS {
        public static final String PLAYER_PRE = "Player #";
        public static final String WAITING = " is waiting...";
        public static final String CONNECTED = " connected.";
        public static final String DISCONNECTED = " disconnected.";
        public static final String TAKE_OVER = " is taken over.";
    }

    public interface FIVE {
        public static final String FOUL_LONG = "长连禁手";
        public static final String FOUL_THREE_THREE = "三三禁手";
        public static final String FOUL_FOUR_FOUR = "四四禁手";
        public static final String WIN_FIVE = "五连胜";
        public static final String WIN_FOUR = "四连胜";
    }

}
