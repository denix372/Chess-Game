package scoresStrategies;

import pieces.Piece;

public interface ScoreStrategy {
    int calculateCaptureScore(Piece capturedPiece);
    int calculateEndGameScore(boolean isCheckmate, boolean won, boolean isDraw);
}
