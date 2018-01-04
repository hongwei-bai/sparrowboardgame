package sparrow.constants;

import java.awt.Color;

public class Constants {
    public interface TCP {
        public final static int SERVER_LISTEN_PORT = 6044;
        public final static String TCP_RESET_ERROR_STRING = "Connection reset";
    }

    public interface APP {
        public final static String TITLE = "Sparrow Board Game Server";
        public final static Color COLOR_SYSTEM_GRAY = Color.getHSBColor(0.0F, 0.0F, 0.93333334F);
    }
}
