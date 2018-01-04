package sparrow.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import sparrow.code.PlayerCode;
import sparrow.constants.Protocol;
import sparrow.platform.PlayerInterface;
import sparrow.view.MessageBox;

public class TCPClient {
    private String mPlayerName = null;
    private Socket mSocket = null;
    private PlayerInterface mPlayerImpl = null;

    public TCPClient(String playerName) {
        mPlayerName = playerName;
        mPlayerImpl = new PlayerCode();
    }

    public void connect(String host, int port) {
        BufferedWriter bw = null;
        try {
            mSocket = new Socket(host, port);
            bw = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
            bw.write(Protocol.MSGID.CONNECTION
                    + Protocol.buildPairString(Protocol.KEY.PLAYER_NAME, mPlayerName));
            bw.newLine();
            bw.flush();
            MessageBox.postMessage("connected");
            loop();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSocket();
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loop() {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
            while (true) {
                String message = br.readLine();
                MessageBox.postMessage("received message: " + message);
                String reply = mPlayerImpl.move(message);
                bw.write(Protocol.MSGID.PLAYER_MOVE + reply);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
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

    private void closeSocket() {
        if (mSocket != null && !mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        closeSocket();
        super.finalize();
    }
}
