package sparrow.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import sparrow.constants.Constants;
import sparrow.constants.Protocol;
import sparrow.view.MessageBox;

public class ClientThread extends Thread {
    private Socket mSocket = null;
    private int mClientId;
    private ClientThreadObserver mClientThreadObserver = null;

    public ClientThread(Socket socket) {
        mSocket = socket;
    }

    public interface ClientThreadObserver {
        public void onClientDisconnected(int clientId);
    }

    public void setClientThreadObserver(int clientId, ClientThreadObserver o) {
        mClientId = clientId;
        mClientThreadObserver = o;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
            while (true) {
                String message = br.readLine();
                if (message.startsWith(Protocol.MSGID.CONNECTION)) {
                    String playerName = Protocol.getValue(message, Protocol.KEY.PLAYER_NAME);
                    MessageBox.postMessage("Player " + playerName + " connected.");
                }

                // to do, send message to client
                String str = "服务器>>" + message + "\n";
                bw.write(str);
                bw.flush();
            }
        } catch (IOException e) {
            if (e.getMessage().equals(Constants.TCP.TCP_RESET_ERROR_STRING)) {
                if (mClientThreadObserver != null) {
                    mClientThreadObserver.onClientDisconnected(mClientId);
                }
                return;
            }
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void interrupt() {
        closeSocket();
        super.interrupt();
    }

    @Override
    protected void finalize() throws Throwable {
        closeSocket();
        super.finalize();
    }

    private void closeSocket() {
        MessageBox.postMessage("closeSocket called.");
        if (mSocket != null && !mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
