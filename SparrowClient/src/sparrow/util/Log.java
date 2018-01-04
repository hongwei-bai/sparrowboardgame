package sparrow.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

public class Log {
    private final static String DEFAULT_LOG_PATH = "trace.log";
    private final static String LEVEL_ERROR = "[ERROR]";

    private static String mLogPathString = DEFAULT_LOG_PATH;
    private static BufferedWriter mOut = null;

    public static void setLogPath(String path) {
        if (path != null) {
            mLogPathString = path;
        } else {
            Log.e("setLogPath failed, path = " + path);
        }
    }

    public static void dd(String msg) {
        System.out.print(msg);
    }

    public static void d(String msg) {
        System.out.println(msg);
    }

    public static void i(String msg) {
        String m = buildFormatMsg(null, msg);
        System.out.println(buildFormatMsg(null, m));
        write(m);
    }

    public static void e(String msg) {
        String m = buildFormatMsg(LEVEL_ERROR, msg);
        System.out.println(buildFormatMsg(LEVEL_ERROR, m));
        write(m);
    }

    private static final String buildFormatMsg(String level, String msg) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String string = time.format(new java.util.Date());
        if (level != null) {
            string += LEVEL_ERROR;
        }
        string += msg;
        return string;
    }

    public static void printStackTrace() {
        printStackTrace(100);
    }

    public static void printStackTrace(int deep) {
        StackTraceElement[] stackElements = new Throwable().getStackTrace();
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length && i < deep; i++) {
                System.out.println("" + stackElements[i]);
            }
        }
    }

    private synchronized static void open() {
        try {
            mOut = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(mLogPathString, true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private synchronized static void write(String msg) {
        if (null == mOut) {
            open();
        }
        try {
            mOut.write(msg + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        mOut.close();
        super.finalize();
    }

}