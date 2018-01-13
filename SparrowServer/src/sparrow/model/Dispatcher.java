package sparrow.model;

import sparrow.constants.MultiLanguage;
import sparrow.model.ModelBase.MoveData;
import sparrow.model.ModelBase.ResultDeliverObserver;
import sparrow.server.TCPServer;
import sparrow.server.TCPServer.ClientObserver;
import sparrow.view.ControlPanel;

public class Dispatcher {
    private static Dispatcher mInstance = new Dispatcher();
    private ModelBase mModel = null;
    protected int mPlayerNumber = 0;
    private Thread mDispatchThread = null;
    private TCPServer mTcpServer = null;
    private int[] mPlayerStatus = null;
    final private Object MOUSE_EVENT_LOCK = new Object();
    private int mConnectedPlayerCount = 0;
    private boolean mGameRunning = true;

    public interface PLAYER_STATE {
        public int AI = 0;
        public int AI_PAUSE = 1;
        public int TAKE_OVER = 2;
    }

    private Dispatcher() {

    }

    private void startGameWhenMinimumPlayerNumberFulfilled() {
        mConnectedPlayerCount++;
        if (mConnectedPlayerCount >= mModel.getMinimumPlayerNumber()) {
            mTcpServer.setGameStart();
            Dispatcher.getInstance().startDispatchInNewThread();
            ControlPanel.setGameInfo(
                    MultiLanguage.GAME_STATUS.GAME_PRE + MultiLanguage.GAME_STATUS.STARTED);
        }
    }

    public static Dispatcher getInstance() {
        return mInstance;
    }

    public void setModel(ModelBase m) {
        mModel = m;
        mModel.setResultDeliverObserver(new ResultDeliverObserver() {

            @Override
            public void onWin(int color, String info) {
                String colorString = mModel.getColorDescription(color);
                stopGame();
                String playerName = mTcpServer.getClientName(mModel.getPlayerId(color));
                String playerNameWrapper = playerName != null ? "(" + playerName + ")" : "";
                String gameInfo = MultiLanguage.GAME_STATUS.GAME_PRE
                        + MultiLanguage.GAME_STATUS.GAME_OVER + ", " + colorString
                        + playerNameWrapper + MultiLanguage.GAME_STATUS.WIN;
                ControlPanel.setGameInfo(gameInfo);
                ControlPanel.postMessage(info);
                ControlPanel.postMessage(gameInfo);
            }

            @Override
            public void onFoul(int color, String info) {
                String colorString = mModel.getColorDescription(color);
                stopGame();
                String playerName = mTcpServer.getClientName(mModel.getPlayerId(color));
                String playerNameWrapper = playerName != null ? "(" + playerName + ")" : "";
                String gameInfo = MultiLanguage.GAME_STATUS.GAME_PRE
                        + MultiLanguage.GAME_STATUS.GAME_OVER + ", " + colorString
                        + playerNameWrapper + MultiLanguage.GAME_STATUS.LOSE;
                ControlPanel.setGameInfo(gameInfo);
                ControlPanel.postMessage(info);
                ControlPanel.postMessage(gameInfo);
            }
        });
    }

    public ModelBase getModel() {
        return mModel;
    }

    public void setTCPServer(TCPServer s) {
        mTcpServer = s;
        mTcpServer.setClientObserver(new ClientObserver() {

            @Override
            public void onClientDisconnected(int clientId) {
                ControlPanel.getInstance().setClientDisconnected(clientId);
            }

            @Override
            public void onClientConnected(int clientId, String clientName) {
                ControlPanel.getInstance().setClientConnected(clientId, clientName);
                startGameWhenMinimumPlayerNumberFulfilled();
            }
        });
    }

    public void setPlayerNumber(int playerNumber) {
        mPlayerNumber = playerNumber;
        mPlayerStatus = new int[playerNumber];
    }

    public void startDispatchInNewThread() {
        mDispatchThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (mGameRunning) {
                    int nextPlayerId = mModel.getNextMovePlayer();
                    boolean result = false;
                    if (PLAYER_STATE.AI == getPlayerStatus(nextPlayerId)) {
                        MoveData md = mTcpServer.getMoveDataFromClientBlocked(nextPlayerId,
                                mModel.getMoveSN(), mModel.getColor(nextPlayerId),
                                mModel.getBoard(), mModel.getPreviousMove());
                        result = mModel.move(mModel.getColor(nextPlayerId), md.to);
                    } else if (PLAYER_STATE.TAKE_OVER == getPlayerStatus(nextPlayerId)) {
                        lockMouseEvent();
                        continue;
                    }

                    if (!result) {
                        mModel.notifyFoul(mModel.getColor(nextPlayerId),
                                MultiLanguage.ERROR.PLAYER_MOVE_ERROR);
                        stopGame();
                        break;
                    }
                    mModel.incSN();
                }
            }
        });
        mDispatchThread.start();
    }

    private void lockMouseEvent() {
        try {
            synchronized (MOUSE_EVENT_LOCK) {
                MOUSE_EVENT_LOCK.wait();
                mModel.incSN();
            }
        } catch (InterruptedException e) {
        }
    }

    public void notifyMouseEvent() {
        synchronized (MOUSE_EVENT_LOCK) {
            MOUSE_EVENT_LOCK.notify();
        }
    }

    public void setTakeOver(int playerId) {
        mPlayerStatus[playerId] = PLAYER_STATE.TAKE_OVER;
        mTcpServer.setTakeOver(playerId);
        startGameWhenMinimumPlayerNumberFulfilled();
    }

    public int getPlayerStatus(int playerId) {
        return mPlayerStatus[playerId];
    }

    private void stopGame() {
        mGameRunning = false;
    }

    public boolean isGameRunning() {
        return mGameRunning;
    }
}
