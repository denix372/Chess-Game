package tests2;

import game.*;
import game.Player;
import gui.GameFrame;
import model.Colors;
import scoresStrategies.RegularScoreStrategy;

public class TestGame2 {
    public static void main(String[] args) {
        Player humanPlayer = new Player("Human", Colors.WHITE);
        Game game = new Game(humanPlayer, new RegularScoreStrategy());

        game.getPlayer().setOwnedPieces(game.getBoard().getChessBoard());
        game.getComputer().setOwnedPieces(game.getBoard().getChessBoard());

        GameFrame.runGUI(game);
    }
}

