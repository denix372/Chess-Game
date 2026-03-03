package game;
import gui.Main2;
import pieces.Piece;

public class GameLogger implements GameObserver {
    @Override
    public void onMoveMade(Move move) {
        System.out.println("[OBSERVER] Move: " + move);
    }

    @Override
    public void onPieceCaptured(Piece piece) {
        System.out.println("[OBSERVER] Atentie!" + piece.type() + " a fost scoasa de pe tabla");
    }

    @Override
    public void onPlayerSwitch(Player currentPlayer) {
        System.out.println("[OBSERVER] It is now " + currentPlayer.getName() + "'s turn.");
    }

    @Override
    public void onGameFinished(Game game) {
        System.out.println("[OBSERVER] Jocul #" + game.getId() + " s-a terminat");
        if (Main2.getInstance() != null)
            Main2.getInstance().handleGameEnd(game);
    }

    @Override
    public void onMessageReceived(String message) {
        //nimic

    }
}