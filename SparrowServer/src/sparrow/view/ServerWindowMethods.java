package sparrow.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import sparrow.server.TCPServer;

public class ServerWindowMethods {
    public static void setCenterDisplay(JFrame f, int w, int h) {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (h > screenSize.height) {
            h = screenSize.height;
        }
        if (w > screenSize.width) {
            w = screenSize.width;
        }
        f.setLocation((screenSize.width - w) / 2, (screenSize.height - h) / 2);
    }

    public static void registerListenerForReleaseTCPResource(JFrame f, TCPServer s) {
        f.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                s.releaseAllClientsResource();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }
        });
    }
}
