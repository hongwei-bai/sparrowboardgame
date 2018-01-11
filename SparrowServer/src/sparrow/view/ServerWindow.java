package sparrow.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sparrow.constants.Constants;
import sparrow.five.FiveBoard;
import sparrow.five.FiveModel;
import sparrow.model.Dispatcher;
import sparrow.protocol.FiveProtocol;
import sparrow.server.TCPServer;
import sparrow.view.ControlPanel.PlayerButtonObserver;

public class ServerWindow extends JFrame {
    private static final long serialVersionUID = 8093757172462558425L;
    private int mWidth = 0;
    private int mHeight = 0;
    private final static float BOARD_SIZE_SCREEN_SIZE_RATIO = 0.5F;
    private final static float INFO_PANEL_SCREEN_SIZE_RATIO = 0.25F;

    private TCPServer mTcpServer;
    private FiveBoard mFiveBoard;
    private ControlPanel mControlPanel;
    private int mPlayerNumber = 0;
    private int mBoardSideSize;
    private int mInfoPanelWidth;
    private FiveModel mFiveModel;

    public ServerWindow() {
        initModel();
        initLayout();
        initListener();
        mTcpServer.startInNewThread(new FiveProtocol());
    }

    public void initLayout() {
        JPanel content = new JPanel();
        setContentPane(content);
        content.setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(Constants.APP.TITLE);
        pack();
        dynamicCalculateBoardSize();
        ServerWindowMethods.setCenterDisplay(this, mWidth, mHeight);
        setPreferredSize(new Dimension(mWidth, mHeight));

        mFiveBoard = new FiveBoard(mBoardSideSize);
        mFiveBoard.setBounds(0, 0, mBoardSideSize, mBoardSideSize);
        content.add(mFiveBoard);
        mFiveModel.setView(mFiveBoard);
        mFiveBoard.setModel(mFiveModel);

        mControlPanel = ControlPanel.getInstance().init(mInfoPanelWidth, mBoardSideSize,
                mTcpServer);
        mControlPanel.setBounds(mBoardSideSize, 0, mInfoPanelWidth, mBoardSideSize);
        content.add(mControlPanel);
        pack();
        setVisible(true);
    }

    public void initModel() {
        mPlayerNumber = FiveBoard.getPlayerNumber();
        mTcpServer = new TCPServer(mPlayerNumber);
        mFiveModel = new FiveModel();
        Dispatcher.getInstance().setPlayerNumber(mPlayerNumber);
        Dispatcher.getInstance().setModel(mFiveModel);
        Dispatcher.getInstance().setTCPServer(mTcpServer);
    }

    public void initListener() {
        mControlPanel.setPlayerButtonObserver(new PlayerButtonObserver() {

            @Override
            public void onTakeOver(int player) {
                Dispatcher.getInstance().setTakeOver(player);
            }

            @Override
            public void onResume(int player) {
            }

            @Override
            public void onPause(int player) {
            }

            @Override
            public void onNextStep(int player) {
            }
        });
        ServerWindowMethods.registerListenerForReleaseTCPResource(this, mTcpServer);
    }

    private void dynamicCalculateBoardSize() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int titlebarHeight = getInsets().top;

        mBoardSideSize = (int) (screenSize.height * BOARD_SIZE_SCREEN_SIZE_RATIO) - titlebarHeight;
        mInfoPanelWidth = (int) (screenSize.height * INFO_PANEL_SCREEN_SIZE_RATIO);
        mHeight = mBoardSideSize + getInsets().top + getInsets().bottom;
        mWidth = mBoardSideSize + mInfoPanelWidth + getInsets().left + getInsets().right;
    }

    public static void main(String[] args) {
        new ServerWindow();
    }
}
