package game;

import pieces.Piece;

public interface GameObserver {
    void onMoveMade(Move move);
    void onPieceCaptured(Piece piece);
    void onPlayerSwitch(Player currentPlayer);
    void onGameFinished(Game game);
    void onMessageReceived(String message);
}
