package sparrow.five;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import sparrow.constants.Constants;
import sparrow.model.Dispatcher;
import sparrow.protocol.Protocol.MoveRecord;

public class FiveBoard extends JPanel implements FiveConstants {
    private static final long serialVersionUID = -224472017215121849L;
    private int mBoardSideSize = 0;
    private int mMarginTop = 0;
    private int mMarginBottom = 0;
    private int mGridSize = 0;
    private final static float STONE_SIZE_COEFFICIENT = 0.8F;
    private final static float BOARD_DOT_DIAMETER_COEFFICIENT = 0.2F;
    private int mStoneDiameter = 0;
    private int mBoardDotDiameter = 0;
    private int mGridTextSize = 0;
    private FiveModel mFiveModel;

    public FiveBoard(int height) {
        dynamicCalculateBoardSize(height);
        initListener();
    }

    public void setModel(FiveModel m) {
        mFiveModel = m;
    }

    public static int getPlayerNumber() {
        return 2;
    }

    private void initListener() {
        addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent arg0) {

            }

            @Override
            public void mousePressed(MouseEvent arg0) {

            }

            @Override
            public void mouseExited(MouseEvent arg0) {

            }

            @Override
            public void mouseEntered(MouseEvent arg0) {

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!Dispatcher.getInstance().isGameRunning()) {
                    return;
                }

                int color;
                boolean takeOverBlack = Dispatcher.PLAYER_STATE.TAKE_OVER == Dispatcher
                        .getInstance().getPlayerStatus(mFiveModel.getPlayerId(STONE_BLACK));
                boolean takeOverWhite = Dispatcher.PLAYER_STATE.TAKE_OVER == Dispatcher
                        .getInstance().getPlayerStatus(mFiveModel.getPlayerId(STONE_WHITE));

                if (takeOverBlack && mFiveModel.getNextMoveColor() == STONE_BLACK) {
                    color = STONE_BLACK;
                } else if (takeOverWhite && mFiveModel.getNextMoveColor() == STONE_WHITE) {
                    color = STONE_WHITE;
                } else {
                    return;
                }

                int[] point = judgePosition(e.getX(), e.getY());
                if (point != null) {
                    boolean result = mFiveModel.move(color, point);
                    if (result) {
                        Dispatcher.getInstance().notifyMouseEvent(point);
                    }
                }
            }
        });
    }

    private int[] judgePosition(int x, int y) {
        int xLeft = mGridSize >>> 1;
        int xRight = (mGridSize >>> 1) + mGridSize * NUMBER_GRID;

        int yTop = (mGridSize >>> 1) + mMarginTop;
        int yBottom = (mGridSize >>> 1) + mMarginTop + mGridSize * NUMBER_GRID;

        if (x > xLeft && x < xRight && y > yTop && y < yBottom) {
            int gridX = (x - xLeft) / mGridSize;
            int gridY = NUMBER_GRID - (y - yTop) / mGridSize - 1;
            int r[] = { gridX, gridY };
            return r;
        } else {
            return null;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintBoard(g);
        paintStones(g);
    }

    private void paintBoard(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(0, 0, mBoardSideSize, mBoardSideSize + mMarginTop + mMarginBottom);

        g.setColor(Color.BLACK);
        int p1 = mGridSize;
        int p2 = mGridSize * NUMBER_GRID;
        for (int i = 0; i < NUMBER_GRID; i++) {
            final int y = (1 + i) * mGridSize;
            g.drawLine(p1, y, p2, y);
        }
        for (int i = 0; i < NUMBER_GRID; i++) {
            final int x = (1 + i) * mGridSize;
            g.drawLine(x, p1, x, p2);
        }

        final int pMid = mGridSize * ((NUMBER_GRID >>> 1) + 1);
        g.fillOval(pMid - mBoardDotDiameter / 2, pMid - mBoardDotDiameter / 2, mBoardDotDiameter,
                mBoardDotDiameter);

        final int pTextLeft = p1 / 3;
        final int textOffset = mGridSize / 6;
        for (int i = 0; i < NUMBER_GRID; i++) {
            final int y = (1 + i) * mGridSize + textOffset;
            g.setFont(new Font(Constants.VIEW.BOARD_TEXT_FONT, Font.PLAIN, mGridTextSize));
            g.drawString((15 - i) + "", pTextLeft, y);
        }

        final int pTextBottom = p2 + p1 / 2;
        for (int i = 0; i < NUMBER_GRID; i++) {
            final int x = (1 + i) * mGridSize - textOffset;
            g.setFont(new Font(Constants.VIEW.BOARD_TEXT_FONT, Font.PLAIN, mGridTextSize));
            g.drawString(CHAR_ARRAY[i], x, pTextBottom);
        }
    }

    private void paintStones(Graphics g) {
        final int radius = mStoneDiameter >>> 1;
        final int[][] data = mFiveModel.get();
        for (int row = 0; row < NUMBER_GRID; row++) {
            for (int col = 0; col < NUMBER_GRID; col++) {
                int color = data[col][row];
                if (color > STONE_NONE) {
                    g.setColor(color == STONE_BLACK ? Color.BLACK : Color.WHITE);
                    int x = getXPoint(col);
                    int y = getYPoint(row);
                    g.fillOval(x - radius, y - radius, mStoneDiameter, mStoneDiameter);
                }
            }
        }

        MoveRecord mr = mFiveModel.getPreviousMove();
        if (mr != null) {
            int x = getXPoint(mr.to[0]);
            int y = getYPoint(mr.to[1]);
            g.setColor(Color.RED);
            g.drawOval(x - radius, y - radius, mStoneDiameter, mStoneDiameter);
        }
    }

    private int getXPoint(int x) {
        int pointX = x * mGridSize + mGridSize;
        return pointX;
    }

    private int getYPoint(int y) {
        int column = NUMBER_GRID - y - 1;
        int pointY = column * mGridSize + mGridSize + mMarginTop;
        return pointY;
    }

    private void dynamicCalculateBoardSize(int height) {
        mGridSize = height / (NUMBER_GRID + 1);
        mBoardSideSize = mGridSize * (NUMBER_GRID + 1);
        int Margin2 = height - mBoardSideSize;
        if (Margin2 % 2 == 1) {
            mMarginTop = Margin2 / 2;
            mMarginBottom = mMarginTop + 1;
        } else {
            mMarginTop = mMarginBottom = Margin2 / 2;
        }
        mStoneDiameter = (int) (mGridSize * STONE_SIZE_COEFFICIENT);
        mBoardDotDiameter = (int) (mGridSize * BOARD_DOT_DIAMETER_COEFFICIENT);
        mGridTextSize = mGridSize / 2;
    }
}
