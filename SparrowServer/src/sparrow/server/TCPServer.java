package sparrow.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import sparrow.constants.Constants;
import sparrow.constants.MultiLanguage;
import sparrow.server.ClientThread.ClientThreadObserver;
import sparrow.view.MessageBox;

public class TCPServer {
    private ClientThread[] mClientThreads;
    private int mCurrentClientThreadIndex = -1;
    private final static int DEFAULT_CLIENT_NUMBER = 2;
    private final static int ERROR = -1;
    private ClientObserver mClientObserver = null;
    private int mClientNumber;

    public TCPServer() {
        mClientThreads = new ClientThread[DEFAULT_CLIENT_NUMBER];
    }

    public TCPServer(int clientNumber) {
        mClientNumber = clientNumber;
        mClientThreads = new ClientThread[clientNumber];
    }

    public interface ClientObserver {
        public void onClientConnected(int clientId);

        public void onClientDisconnected(int clientId);
    }

    public void setClientObserver(ClientObserver o) {
        mClientObserver = o;
    }

    public void startInNewThread() {
        new Thread(new Runnable() {
            public void run() {
                runServer();
            }
        }).start();
    }

    private void runServer() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Constants.TCP.SERVER_LISTEN_PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                int index = allocateClientThread();
                MessageBox.postMessage("allocateClientThread = " + index);
                if (index != ERROR) {
                    mClientThreads[index] = new ClientThread(socket);
                    mClientThreads[index].start();
                    if (mClientObserver != null) {
                        mClientObserver.onClientConnected(index);
                    }
                    mClientThreads[index].setClientThreadObserver(index,
                            new ClientThreadObserver() {

                                @Override
                                public void onClientDisconnected(int clientId) {
                                    if (mClientObserver != null) {
                                        mClientObserver.onClientDisconnected(index);
                                    }
                                }
                            });
                } else {
                    MessageBox.postMessage(MultiLanguage.ERROR.CLIENT_NUMBER_OVERFLOW);
                }
            }
        } catch (IOException e) {

        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int allocateClientThread() {
        int index = mCurrentClientThreadIndex + 1;
        while (index != mCurrentClientThreadIndex) {
            if (null == mClientThreads[index]) {
                mCurrentClientThreadIndex = index;
                return index;
            }
            index++;
        }
        return ERROR;
    }

    public int getClientNumber() {
        return mClientNumber;
    }
}