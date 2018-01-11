package sparrow.code;

import sparrow.platform.PlayerInterface;

public class PlayerCode implements PlayerInterface {

    @Override
    public int[] move(int sn, int color, int[][] board, int[] prev) {
        int[] m = new int[2];
        if (sn == 1) {
            m[0] = m[1] = 7;
            return m;
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (0 == board[i][j]) {
                    m[0] = i;
                    m[1] = j;
                    return m;
                }
            }
        }

        return m;
    }

    @Override
    public String getPlayerName() {
        return "Player1";
    }

}
