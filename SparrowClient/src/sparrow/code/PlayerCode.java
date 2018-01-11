package sparrow.code;

import sparrow.platform.PlayerInterface;
import sparrow.util.Log;

public class PlayerCode implements PlayerInterface {

    @Override
    public int[] move(int sn, int color, int[][] board, int[] prev) {
        int[] m = new int[2];
        if (sn == 1) {
            m[0] = m[1] = 7;
            return m;
        }

        Log.d("haha");
        return m;
    }

    @Override
    public String getPlayerName() {
        return "Player1";
    }

}
