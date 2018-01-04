package sparrow.view;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class MessageBox {
    private volatile static MessageBox mInstance = null;
    private JTextArea mJTextArea = null;

    private MessageBox() {

    }

    public static MessageBox getInstance() {
        if (null == mInstance) {
            synchronized (MessageBox.class) {
                if (null == mInstance) {
                    mInstance = new MessageBox();
                }
            }
        }
        return mInstance;
    }

    public void setMessageBox(JTextArea mb) {
        mJTextArea = mb;
    }

    private void appendMessage(String message) {
        if (mJTextArea != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    mJTextArea.setText(mJTextArea.getText() + message + "\n");
                }
            });
        }
    }

    public static void postMessage(String message) {
        getInstance().appendMessage(message);
    }

}
