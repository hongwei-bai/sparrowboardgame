package sparrow.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import sparrow.code.PlayerCode;
import sparrow.platform.ParseSession;
import sparrow.platform.PlayerInterface;
import sparrow.protocol.Protocol;
import sparrow.protocol.Protocol.MSGID;
import sparrow.protocol.ProtocolParser;
import sparrow.util.Log;
import sparrow.view.MessageBox;

public class TCPClient {
    private PlayerInterface mPlayerImpl = null;
    private ProtocolParser mParser;

    public TCPClient(ProtocolParser p) {
        mParser = p;
        mPlayerImpl = new PlayerCode();
    }

    public void connect(String host, int port) {
        Socket socket = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            socket = new Socket(host, port);
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String messageOut = Protocol.buildMessage().setMessageId(MSGID.CONNECTION)
                    .addPlayerName(mPlayerImpl.getPlayerName()).getMessage();
            bw.write(messageOut);
            bw.newLine();
            bw.flush();
            MessageBox.postMessage("connected");

            while (true) {
                String message = br.readLine();
                Log.d(message);
                if (message != null) {
                    MessageBox.postMessage("received message: " + message);
                    Log.d("received message: " + message);

                    ParseSession session = new ParseSession();
                    session.readMessage(mParser, message);
                    int move[] = mPlayerImpl.move(session.getSN(), session.getColor(),
                            session.getBoard(), session.getPreviousMove());
                    String reply = Protocol.buildMessage().setMessageId(MSGID.MOVE).addMove(move)
                            .getMessage();
                    MessageBox.postMessage(reply);
                    bw.write(reply);
                    bw.newLine();
                    bw.flush();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
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
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
