package tests1;

import game.*;
import model.*;
import pieces.*;
import java.util.Scanner;

public class TestSpecialMoves {

    public static void main(String[] args) {
        System.out.println("============ Testare Pawn Promotion ============");
        Game game = new Game();
        Board custom = new Board();
        custom.getChessBoard().clear();

        custom.getChessBoard().add(new ChessPair<>(new Position("A7"), new King(Colors.BLACK, new Position("A7"))));
        Pawn p1 = new Pawn(Colors.BLACK, new Position("A3"));
        p1.setFirstmove(1);
        custom.getChessBoard().add(new ChessPair<>(new Position("A3"),p1));

        custom.getChessBoard().add(new ChessPair<>(new Position("C8"), new Rook(Colors.WHITE, new Position("C8"))));
        custom.getChessBoard().add(new ChessPair<>(new Position("B5"), new King(Colors.WHITE, new Position("B5"))));
        custom.getChessBoard().add(new ChessPair<>(new Position("C5"), new Knight(Colors.WHITE, new Position("C5"))));

        custom.getChessBoard().add(new ChessPair<>(new Position("H7"), new Pawn(Colors.WHITE, new Position("H7"))));
        // voi muta pionul de la h7 la h8 , v-a deveni regina
        // de asemenea si pionul computerului v-a deveni un pion ales random
        game.setBoard(custom);
        game.setActive(true);
        game.setSc(new Scanner(System.in));
        game.resume();


    }
}