package scoresStrategies;

import pieces.Piece;

public class RegularScoreStrategy implements ScoreStrategy{
    @Override
    public int calculateCaptureScore(Piece captured) {
        if (captured.type() == 'Q')
            return 90;
        else if (captured.type() == 'R')
            return 50;
        else if (captured.type() == 'B' || captured.type() == 'N')
            return 30;
        else if (captured.type() == 'P')
            return 10;
        return 0;
    }

    @Override
    public int calculateEndGameScore(boolean isCheckmate, boolean won, boolean isDraw) {
        if ( isDraw)
            return 150;

        if (isCheckmate && won)
            return 300;
        else if (isCheckmate && !won)
            return -300;

        return 0;
    }
}
