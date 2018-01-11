package sparrow.platform;

public interface PlayerInterface {
    public int[] move(int sn, int color, int[][] board, int[] prev);

    public String getPlayerName();
}
