package sparrow.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import sparrow.constants.Constants;
import sparrow.constants.MultiLanguage;
import sparrow.model.ModelBase.MoveData;
import sparrow.protocol.Protocol;
import sparrow.protocol.Protocol.MoveRecord;
import sparrow.protocol.ProtocolParser;
import sparrow.view.ControlPanel;

public class TCPServer {
    // private ClientThread[] mClientThreads;
    private Socket[] mClientSockets;
    private int mCurrentClientIndex = -1;
    private final static int DEFAULT_CLIENT_NUMBER = 2;
    private final static int ERROR = -1;
    private ClientObserver mClientObserver = null;
    private int mClientNumber;
    private boolean mGameStart = false;
    private BufferedReader[] mBufferedReaders = null;
    private BufferedWriter[] mBufferedWriters = null;
    private String[] mClientNames = null;
    private ProtocolParser mProtocolParser = null;
    private boolean[] mClientTakeOver = null;

    public TCPServer() {
        initClients(DEFAULT_CLIENT_NUMBER);
    }

    public TCPServer(int clientNumber) {
        initClients(clientNumber);
    }

    private void initClients(int clientNumber) {
        mClientNumber = clientNumber;
        mClientSockets = new Socket[clientNumber];
        mBufferedReaders = new BufferedReader[clientNumber];
        mBufferedWriters = new BufferedWriter[clientNumber];
        mClientNames = new String[clientNumber];
        mClientTakeOver = new boolean[clientNumber];
    }

    public interface ClientObserver {
        public void onClientConnected(int clientId, String clientName);

        public void onClientDisconnected(int clientId);
    }

    public void setClientObserver(ClientObserver o) {
        mClientObserver = o;
    }

    public void startInNewThread(ProtocolParser p) {
        mProtocolParser = p;
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
            while (!mGameStart) {
                Socket socket = serverSocket.accept();
                int index = allocateClientThread();
                if (index != ERROR) {
                    mClientSockets[index] = socket;
                    processConnectRespond(index);
                    ControlPanel.postMessage("Client " + index + " connected.");
                } else {
                    ControlPanel.postMessage(MultiLanguage.ERROR.CLIENT_NUMBER_OVERFLOW);
                }
            }
        } catch (IOException e) {
            releaseAllClientsResource();
        } finally {

        }
    }

    public void setGameStart() {
        mGameStart = true;
    }

    private void processConnectRespond(int clientId) {
        while (true) {
            try {
                mBufferedReaders[clientId] = new BufferedReader(
                        new InputStreamReader(mClientSockets[clientId].getInputStream()));
                mBufferedWriters[clientId] = new BufferedWriter(
                        new OutputStreamWriter(mClientSockets[clientId].getOutputStream()));
                String message = mBufferedReaders[clientId].readLine();
                if (mProtocolParser.getMessageId(message).equals(Protocol.MSGID.CONNECTION)) {
                    String playerName = mProtocolParser.getValue(message, Protocol.KEY.PLAYER_NAME);
                    mClientNames[clientId] = playerName;
                    if (mClientObserver != null) {
                        mClientObserver.onClientConnected(clientId, playerName);
                    }
                    return;
                }
            } catch (IOException e) {
                if (e.getMessage().equals(Constants.TCP.TCP_RESET_ERROR_STRING)) {
                    if (mClientObserver != null) {
                        mClientObserver.onClientDisconnected(clientId);
                    }
                    releaseClientResource(clientId);
                    return;
                }
                e.printStackTrace();
            }
        }
    }

    public MoveData getMoveDataFromClientBlocked(int clientId, int sn, int color, int[][] board,
            MoveRecord mr) {
        try {
            String str = Protocol.buildMessage().addSN(sn).addColor(color).addBoard(board)
                    .addPreviousMove(mr).getMessage();
            mBufferedWriters[clientId].write(str);
            mBufferedWriters[clientId].newLine();
            mBufferedWriters[clientId].flush();

            String message = mBufferedReaders[clientId].readLine();
            ControlPanel.postMessage(
                    "rcv msg from [" + clientId + "]" + mClientNames[clientId] + ": " + message);
            if (mProtocolParser.getMessageId(message).equals(Protocol.MSGID.MOVE)) {
                int[] move = mProtocolParser
                        .parseMove(mProtocolParser.getValue(message, Protocol.KEY.MOVE));
                MoveData moveData = new MoveData();
                moveData.id = clientId;
                moveData.to = move;
                return moveData;
            }
        } catch (IOException e) {
            if (e.getMessage().equals(Constants.TCP.TCP_RESET_ERROR_STRING)) {
                if (mClientObserver != null) {
                    mClientObserver.onClientDisconnected(clientId);
                }
                releaseClientResource(clientId);
                return null;
            }
            e.printStackTrace();
        }
        return null;
    }

    public void setTakeOver(int clientId) {
        mClientTakeOver[clientId] = true;
    }

    private int allocateClientThread() {
        int index = mCurrentClientIndex + 1;
        while (index != mCurrentClientIndex) {
            if (null == mClientSockets[index] && !mClientTakeOver[index]) {
                mCurrentClientIndex = index;
                return index;
            }
            index++;
        }
        return ERROR;
    }

    public int getClientNumber() {
        return mClientNumber;
    }

    public void releaseAllClientsResource() {
        for (int i = 0; i < mClientNumber; i++) {
            releaseClientResource(i);
        }
    }

    public void releaseClientResource(int clientId) {
        if (mBufferedReaders[clientId] != null) {
            try {
                mBufferedReaders[clientId].close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mBufferedWriters[clientId] != null) {
            try {
                mBufferedWriters[clientId].close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mClientSockets[clientId] != null) {
            try {
                mClientSockets[clientId].close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mClientSockets[clientId] = null;
        }
    }
}