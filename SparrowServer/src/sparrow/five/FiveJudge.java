package sparrow.five;

import java.util.ArrayList;

import sparrow.constants.MultiLanguage;
import sparrow.five.FiveJudgeMethods.Pattern;
import sparrow.util.Log;

public class FiveJudge {
    public static void judgeWin(FiveModel model, int[][] board, int color, int x, int y) {
        judgeWinFive(model, board, color, x, y);
        if (FiveModel.STONE_BLACK == color) {
            judgeFouls(model, board, color, x, y);
        }
    }

    private static void judgeWinFive(FiveModel model, int[][] board, int color, int x, int y) {
        if (FiveJudgeMethods.countStoneInRow(board, color, x, y) == 5
                || FiveJudgeMethods.countStoneInColumn(board, color, x, y) == 5
                || FiveJudgeMethods.countStoneInClock2(board, color, x, y) == 5
                || FiveJudgeMethods.countStoneInClock4(board, color, x, y) == 5) {
            model.notifyWin(color, MultiLanguage.FIVE.WIN_FIVE);
        }
        if (FiveJudgeMethods.countStoneInRow(board, color, x, y) > 5
                || FiveJudgeMethods.countStoneInColumn(board, color, x, y) > 5
                || FiveJudgeMethods.countStoneInClock2(board, color, x, y) > 5
                || FiveJudgeMethods.countStoneInClock4(board, color, x, y) > 5) {
            if (FiveModel.STONE_BLACK == color) {
                model.notifyFoul(color, MultiLanguage.FIVE.FOUL_LONG);
            } else {
                model.notifyWin(color, MultiLanguage.FIVE.WIN_FIVE);
            }
        }
    }

    private static void judgeFouls(FiveModel model, int[][] board, int color, int x, int y) {
        ArrayList<Pattern> list = FiveJudgeMethods.findStonesInRow(board, color, x, y);
        list.addAll(FiveJudgeMethods.findStonesInColumn(board, color, x, y));
        list.addAll(FiveJudgeMethods.findStonesInClock2(board, color, x, y));
        list.addAll(FiveJudgeMethods.findStonesInClock4(board, color, x, y));

        list = FiveJudgeMethods.findJumpInList(board, color, x, y, list);

        ArrayList<Pattern> list4 = FiveJudgeMethods.filterLiveFour(board, color, list);
        ArrayList<Pattern> list3 = FiveJudgeMethods.filterLiveThree(board, color, list);

        // debugPrintList(list3);

        if (FiveJudgeMethods.hasCommonPoint(list4)) {
            model.notifyFoul(color, MultiLanguage.FIVE.FOUL_FOUR_FOUR);
        }

        if (FiveJudgeMethods.hasCommonPoint(list3)) {
            model.notifyFoul(color, MultiLanguage.FIVE.FOUL_THREE_THREE);
        }
    }

    public static void debugPrintList(ArrayList<Pattern> list) {
        Log.d("--------");
        for (Pattern pattern : list) {
            Log.d(pattern.toString());
        }
    }
}
