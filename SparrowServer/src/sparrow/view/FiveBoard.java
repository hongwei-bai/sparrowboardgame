package sparrow.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class FiveBoard extends JPanel {
    private static final long serialVersionUID = -224472017215121849L;
    private int mBoardSideSize = 0;
    private int mMarginTop = 0;
    private int mMarginBottom = 0;
    private int mGridSize = 0;
    private final static int NUMBER_GRID = 15;
    private final static float STONE_SIZE_COEFFICIENT = 0.8F;
    private final static float BOARD_DOT_DIAMETER_COEFFICIENT = 0.2F;
    private int mStoneDiameter = 0;
    private int mBoardDotDiameter = 0;
    private int mGridTextSize = 0;
    private int mTextSize = 0;
    private final static String[] CHAR_ARRAY = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O" };

    public FiveBoard(int height) {
        dynamicCalculateBoardSize(height);
    }

    public static int getPlayerNumber() {
        return 2;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintBoard(g);
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
            g.setFont(new Font("Calibri", Font.PLAIN, mGridTextSize));
            g.drawString((15 - i) + "", pTextLeft, y);
        }

        final int pTextBottom = p2 + p1 / 2;
        for (int i = 0; i < NUMBER_GRID; i++) {
            final int x = (1 + i) * mGridSize - textOffset;
            g.setFont(new Font("Calibri", Font.PLAIN, mGridTextSize));
            g.drawString(CHAR_ARRAY[i], x, pTextBottom);
        }
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
        mTextSize = mGridTextSize;
    }
}
