package sparrow.code;

import sparrow.platform.PlayerInterface;
import sparrow.util.Log;

public class PlayerCode implements PlayerInterface {

    @Override
    public String move(String input) {
        Log.d("haha");
        return null;
    }

    @Override
    public String getPlayerName() {
        return "Player1";
    }

}
