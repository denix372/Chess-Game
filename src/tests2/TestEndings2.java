package tests2;

import game.*;
import java.util.ArrayList;
import java.util.Scanner;

import gui.GameFrame;
import model.ChessPair;
import model.Colors;
import model.Position;
import pieces.*;
import scoresStrategies.RegularScoreStrategy;

public class TestEndings2 {
    public static void main(String[] args) {
        System.out.println(
                "==================== Testare Sah Mat ====================");
        Player humanPlayer = new Player("Human", Colors.WHITE);
        Game game = new Game(humanPlayer, new RegularScoreStrategy());
        Board custom = new Board();
        custom.movePiece(new Position('F', 7), new Position('F', 5));
        custom.movePiece(new Position('E', 2), new Position('E', 3));
        custom.movePiece(new Position('G', 7), new Position('G', 5));
        // daca se face miscarea D1-H5 jocul se incheie prin SAH MAT

        game.setBoard(custom);
        GameFrame.runGUI(game);

        System.out.println("Mai e jocul activ? " + game.getActive());
        System.out.println(
                "S-a terminat jocul prin sah? " + game.checkForCheckMate());

    }
}