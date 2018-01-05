package sparrow.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import sparrow.constants.Constants;
import sparrow.model.FiveModel;

public class FiveBoard extends JPanel {
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
    private final static String[] CHAR_ARRAY = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O" };
    private boolean[] mTakeOverFlag;

    public FiveBoard(int height) {
        dynamicCalculateBoardSize(height);
        mFiveModel = new FiveModel();
        mTakeOverFlag = new boolean[3];
        initListener();
    }

    public static int getPlayerNumber() {
        return 2;
    }

    public void setTakeOver(final int player) {
        mTakeOverFlag[player + 1] = true;
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
                int color;
                if (mTakeOverFlag[FiveModel.STONE_BLACK] && mFiveModel.getMoveSideBlack()) {
                    color = FiveModel.STONE_BLACK;
                } else if (mTakeOverFlag[FiveModel.STONE_WHITE] && !mFiveModel.getMoveSideBlack()) {
                    color = FiveModel.STONE_WHITE;
                } else {
                    return;
                }

                int[] point = judgePosition(e.getX(), e.getY());
                if (point != null) {
                    move(color, point[0], point[1]);
                }
            }
        });
    }

    private int[] judgePosition(int x, int y) {
        int xLeft = mGridSize >>> 1;
        int xRight = (mGridSize >>> 1) + mGridSize * FiveModel.NUMBER_GRID;

        int yTop = (mGridSize >>> 1) + mMarginTop;
        int yBottom = (mGridSize >>> 1) + mMarginTop + mGridSize * FiveModel.NUMBER_GRID;

        if (x > xLeft && x < xRight && y > yTop && y < yBottom) {
            int gridX = (x - xLeft) / mGridSize;
            int gridY = FiveModel.NUMBER_GRID - (y - yTop) / mGridSize - 1;
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
        int p2 = mGridSize * FiveModel.NUMBER_GRID;
        for (int i = 0; i < FiveModel.NUMBER_GRID; i++) {
            final int y = (1 + i) * mGridSize;
            g.drawLine(p1, y, p2, y);
        }
        for (int i = 0; i < FiveModel.NUMBER_GRID; i++) {
            final int x = (1 + i) * mGridSize;
            g.drawLine(x, p1, x, p2);
        }

        final int pMid = mGridSize * ((FiveModel.NUMBER_GRID >>> 1) + 1);
        g.fillOval(pMid - mBoardDotDiameter / 2, pMid - mBoardDotDiameter / 2, mBoardDotDiameter,
                mBoardDotDiameter);

        final int pTextLeft = p1 / 3;
        final int textOffset = mGridSize / 6;
        for (int i = 0; i < FiveModel.NUMBER_GRID; i++) {
            final int y = (1 + i) * mGridSize + textOffset;
            g.setFont(new Font(Constants.VIEW.BOARD_TEXT_FONT, Font.PLAIN, mGridTextSize));
            g.drawString((15 - i) + "", pTextLeft, y);
        }

        final int pTextBottom = p2 + p1 / 2;
        for (int i = 0; i < FiveModel.NUMBER_GRID; i++) {
            final int x = (1 + i) * mGridSize - textOffset;
            g.setFont(new Font(Constants.VIEW.BOARD_TEXT_FONT, Font.PLAIN, mGridTextSize));
            g.drawString(CHAR_ARRAY[i], x, pTextBottom);
        }
    }

    private void paintStones(Graphics g) {
        final int radius = mStoneDiameter >>> 1;
        final int[][] data = mFiveModel.get();
        for (int i = 0; i < FiveModel.NUMBER_GRID; i++) {
            for (int j = 0; j < FiveModel.NUMBER_GRID; j++) {
                int color = data[i][j];
                if (color > FiveModel.STONE_NONE) {
                    g.setColor(color == FiveModel.STONE_BLACK ? Color.BLACK : Color.WHITE);
                    int x = getXPoint(i);
                    int y = getYPoint(j);
                    g.fillOval(x - radius, y - radius, mStoneDiameter, mStoneDiameter);
                }
            }
        }
    }

    public boolean move(int color, int x, int y) {
        boolean success = mFiveModel.put(color, x, y);
        if (success) {
            repaint();
        }
        return success;
    }

    private int getXPoint(int x) {
        int pointX = x * mGridSize + mGridSize;
        return pointX;
    }

    private int getYPoint(int y) {
        int column = FiveModel.NUMBER_GRID - y - 1;
        int pointY = column * mGridSize + mGridSize + mMarginTop;
        return pointY;
    }

    private void dynamicCalculateBoardSize(int height) {
        mGridSize = height / (FiveModel.NUMBER_GRID + 1);
        mBoardSideSize = mGridSize * (FiveModel.NUMBER_GRID + 1);
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
