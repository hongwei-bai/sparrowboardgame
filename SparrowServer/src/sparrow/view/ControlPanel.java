package sparrow.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import sparrow.constants.Constants;
import sparrow.constants.MultiLanguage;
import sparrow.server.TCPServer;

public class ControlPanel extends JPanel {
    private static final long serialVersionUID = -54346519923680640L;

    private final static int TEXT_MARGIN = 10;
    private final static int GRID_GAP = 8;

    private TCPServer mTcpServer;

    private int mPlayerNumber = 0;
    private JTextArea[] mPlayerConnInfo;
    private JButton[] mPlayerTakeOverButton;
    private JButton[] mPlayerPauseResumeButton;
    private JButton[] mPlayerNextStepButton;
    private JTextArea mMessageBox = null;
    private JTextArea mGameInfo;
    private int mWidth;
    @SuppressWarnings("unused")
    private int mHeight;
    private PlayerButtonObserver mPlayerButtonObserver;
    private static ControlPanel mInstance = new ControlPanel();

    private ControlPanel() {

    }

    public ControlPanel init(int width, int height, TCPServer s) {
        mWidth = width;
        mHeight = height;
        mTcpServer = s;
        mPlayerNumber = mTcpServer.getClientNumber();
        dynamicCalculateBoardSize();
        initLayout();
        initListener();
        return this;
    }

    public static ControlPanel getInstance() {
        return mInstance;
    }

    public void initLayout() {
        setLayout(new FlowLayout());

        Box boxBase = Box.createVerticalBox();
        mPlayerConnInfo = new JTextArea[mPlayerNumber];
        mPlayerTakeOverButton = new JButton[mPlayerNumber];
        mPlayerPauseResumeButton = new JButton[mPlayerNumber];
        mPlayerNextStepButton = new JButton[mPlayerNumber];
        for (int i = 0; i < mPlayerNumber; i++) {
            Box boxPlayer = Box.createHorizontalBox();

            mPlayerConnInfo[i] = new JTextArea();
            mPlayerConnInfo[i].setEditable(false);
            mPlayerConnInfo[i].setBackground(Constants.VIEW.COLOR_SYSTEM_GRAY);
            boxPlayer.add(mPlayerConnInfo[i]);

            boxPlayer.add(Box.createHorizontalStrut(GRID_GAP));

            mPlayerTakeOverButton[i] = new JButton(MultiLanguage.BUTTON.TAKE_OVER);
            boxPlayer.add(mPlayerTakeOverButton[i]);

            mPlayerPauseResumeButton[i] = new JButton(MultiLanguage.BUTTON.PAUSE);
            mPlayerPauseResumeButton[i].setVisible(false);
            boxPlayer.add(mPlayerPauseResumeButton[i]);

            boxPlayer.add(Box.createHorizontalStrut(GRID_GAP));

            mPlayerNextStepButton[i] = new JButton(MultiLanguage.BUTTON.NEXT_STEP);
            mPlayerNextStepButton[i].setVisible(false);
            boxPlayer.add(mPlayerNextStepButton[i]);

            boxBase.add(boxPlayer);
            boxBase.add(Box.createVerticalStrut(GRID_GAP));
        }
        add(boxBase);

        mGameInfo = new JTextArea(
                MultiLanguage.GAME_STATUS.GAME_PRE + MultiLanguage.GAME_STATUS.READY);
        mGameInfo.setBackground(Constants.VIEW.COLOR_SYSTEM_GRAY);
        boxBase.add(mGameInfo);
        boxBase.add(Box.createVerticalStrut(GRID_GAP));

        mMessageBox = new JTextArea();
        mMessageBox.setBackground(Constants.VIEW.COLOR_SYSTEM_GRAY);
        mMessageBox.setLineWrap(true);
        JScrollPane jScrollPane = new JScrollPane(mMessageBox);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBorder(null);
        jScrollPane.setPreferredSize(new Dimension(mWidth - TEXT_MARGIN - TEXT_MARGIN, mWidth));
        boxBase.add(jScrollPane);
    }

    public void initListener() {
        for (int i = 0; i < mPlayerNumber; i++) {
            mPlayerConnInfo[i].setText(MultiLanguage.PLAYER_STATUS.PLAYER_PRE + i
                    + MultiLanguage.PLAYER_STATUS.WAITING);
        }

        for (int i = 0; i < mPlayerNumber; i++) {
            final int playerId = i;
            mPlayerTakeOverButton[i].addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (mPlayerButtonObserver != null) {
                        mPlayerButtonObserver.onTakeOver(playerId);
                        mPlayerConnInfo[playerId].setText(MultiLanguage.PLAYER_STATUS.PLAYER_PRE
                                + playerId + MultiLanguage.PLAYER_STATUS.TAKE_OVER);
                        mPlayerTakeOverButton[playerId].setEnabled(false);
                    }
                }
            });
        }
    }

    private void dynamicCalculateBoardSize() {

    }

    public void setPlayerButtonObserver(PlayerButtonObserver o) {
        mPlayerButtonObserver = o;
    }

    public interface PlayerButtonObserver {
        public void onTakeOver(int player);

        public void onPause(int player);

        public void onResume(int player);

        public void onNextStep(int player);
    }

    private void appendMessage(String message) {
        if (mMessageBox != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    mMessageBox.setText(mMessageBox.getText() + message + "\n");
                }
            });
        }
    }

    private void setGameInfoImpl(String info) {
        if (mGameInfo != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    mGameInfo.setText(info + "\n");
                }
            });
        }
    }

    public static void postMessage(String message) {
        getInstance().appendMessage(message);
    }

    public static void setGameInfo(String info) {
        getInstance().setGameInfoImpl(info);
    }

    public void setClientConnected(int clientId, String clientName) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mPlayerConnInfo[clientId].setText(
                        MultiLanguage.PLAYER_STATUS.PLAYER_PRE + clientId + "(" + clientName + ")");
                mPlayerTakeOverButton[clientId].setVisible(false);
                mPlayerPauseResumeButton[clientId].setVisible(true);
                mPlayerNextStepButton[clientId].setVisible(true);
            }
        });
    }

    public void setClientDisconnected(int clientId) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mPlayerConnInfo[clientId].setText(MultiLanguage.PLAYER_STATUS.PLAYER_PRE + clientId
                        + MultiLanguage.PLAYER_STATUS.DISCONNECTED);
                mPlayerTakeOverButton[clientId].setVisible(true);
                mPlayerPauseResumeButton[clientId].setVisible(false);
                mPlayerNextStepButton[clientId].setVisible(false);
            }
        });
    }
}
