package sparrow.view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import sparrow.constants.Constants;
import sparrow.server.TCPServer;
import sparrow.server.TCPServer.ClientObserver;

public class ServerWindow extends JFrame {
    private static final long serialVersionUID = 8093757172462558425L;
    private int mWidth = 0;
    private int mHeight = 0;
    private final static float BOARD_SIZE_SCREEN_SIZE_RATIO = 0.5F;
    private final static float INFO_PANEL_SCREEN_SIZE_RATIO = 0.25F;
    private final static int TEXT_MARGIN = 10;
    private TCPServer mTcpServer;
    private FiveBoard mFiveBoard;
    private int mPlayerNumber = 0;
    private JTextArea[] mPlayerConnInfo;
    private JTextArea mGameInfo;
    private int mBoardSideSize;
    private int mInfoPanelWidth;
    private int mTextHeight;

    public ServerWindow() {
        initModel();
        initLayout();
        initListener();
    }

    public void initLayout() {
        JPanel content = new JPanel();
        setContentPane(content);
        content.setLayout(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(Constants.APP.TITLE);
        pack();

        dynamicCalculateBoardSize();
        setCenterDisplay();
        setPreferredSize(new Dimension(mWidth, mHeight));

        mFiveBoard = new FiveBoard(mBoardSideSize);
        mFiveBoard.setBounds(0, 0, mBoardSideSize, mBoardSideSize);
        content.add(mFiveBoard);

        mPlayerConnInfo = new JTextArea[mPlayerNumber];
        for (int i = 0; i < mPlayerNumber; i++) {
            mPlayerConnInfo[i] = new JTextArea();
            int y = TEXT_MARGIN + i * mTextHeight;
            mPlayerConnInfo[i].setBounds(mBoardSideSize + TEXT_MARGIN, y,
                    mInfoPanelWidth - TEXT_MARGIN - TEXT_MARGIN, mTextHeight);
            mPlayerConnInfo[i].setEditable(false);
            mPlayerConnInfo[i].setBackground(Constants.APP.COLOR_SYSTEM_GRAY);
            content.add(mPlayerConnInfo[i]);
        }
        mGameInfo = new JTextArea();
        mGameInfo.setBounds(mBoardSideSize + TEXT_MARGIN, TEXT_MARGIN + mPlayerNumber * mTextHeight,
                mInfoPanelWidth - TEXT_MARGIN - TEXT_MARGIN, mTextHeight);
        mGameInfo.setBackground(Constants.APP.COLOR_SYSTEM_GRAY);
        content.add(mGameInfo);

        JTextArea messageBox = new JTextArea();
        messageBox.setBackground(Constants.APP.COLOR_SYSTEM_GRAY);
        JScrollPane jScrollPane = new JScrollPane(messageBox);
        content.add(jScrollPane);
        int messageBoxY = TEXT_MARGIN + mPlayerNumber * mTextHeight + mTextHeight;
        jScrollPane.setBounds(mBoardSideSize + TEXT_MARGIN, messageBoxY,
                mInfoPanelWidth - TEXT_MARGIN - TEXT_MARGIN,
                mHeight - messageBoxY - TEXT_MARGIN - TEXT_MARGIN);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBorder(null);
        messageBox.setLineWrap(true);
        MessageBox.getInstance().setMessageBox(messageBox);

        pack();
        setVisible(true);
    }

    public void initModel() {
        mPlayerNumber = FiveBoard.getPlayerNumber();
        mTcpServer = new TCPServer(mPlayerNumber);
        mTcpServer.startInNewThread();
    }

    public void initListener() {
        for (int i = 0; i < mPlayerNumber; i++) {
            mPlayerConnInfo[i].setText(">>Player " + i + " is awaiting...");
        }
        mTcpServer.setClientObserver(new ClientObserver() {

            @Override
            public void onClientDisconnected(int clientId) {
                mPlayerConnInfo[clientId].setText(">>Player " + clientId + " disconnected.");
            }

            @Override
            public void onClientConnected(int clientId) {
                mPlayerConnInfo[clientId].setText("Player " + clientId + " connected.");
            }
        });
    }

    private void dynamicCalculateBoardSize() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int titlebarHeight = getInsets().top;
        mTextHeight = titlebarHeight;

        mBoardSideSize = (int) (screenSize.height * BOARD_SIZE_SCREEN_SIZE_RATIO) - titlebarHeight;
        mInfoPanelWidth = (int) (screenSize.height * INFO_PANEL_SCREEN_SIZE_RATIO);
        mHeight = mBoardSideSize + getInsets().top + getInsets().bottom;
        mWidth = mBoardSideSize + mInfoPanelWidth + getInsets().left + getInsets().right;
    }

    private void setCenterDisplay() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (mHeight > screenSize.height) {
            mHeight = screenSize.height;
        }
        if (mWidth > screenSize.width) {
            mWidth = screenSize.width;
        }
        this.setLocation((screenSize.width - mWidth) / 2, (screenSize.height - mHeight) / 2);
    }

    public static void main(String[] args) {
        new ServerWindow();
    }
}
