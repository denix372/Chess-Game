package tests1;

import game.*;
import java.util.ArrayList;
import java.util.Scanner;
import model.ChessPair;
import model.Colors;
import model.Position;
import pieces.*;

public class TestEndings {
    public static void main(String[] args) {
        System.out.println(
                "==================== Testare Sah Mat ====================");
        Game game = new Game();
        Board custom = new Board();
        custom.movePiece(new Position('F', 7), new Position('F', 5));
        custom.movePiece(new Position('E', 2), new Position('E', 3));
        custom.movePiece(new Position('G', 7), new Position('G', 5));
        // daca se face miscarea D1-H5 jocul se incheie prin SAH MAT

        game.setBoard(custom);
        game.setActive(true);
        game.setSc(new Scanner(System.in));
        game.resume();

        System.out.println("Mai e jocul activ? " + game.getActive());
        System.out.println(
                "S-a terminat jocul prin sah? " + game.checkForCheckMate());

        System.out.println("==================== Testare Sah Mat Comuper "
                + "====================");
        game = new Game();
        custom = new Board();
        custom.getChessBoard().clear();

        custom.getChessBoard().add(new ChessPair<>(
                new Position("G1"), new King(Colors.WHITE, new Position("G1"))));
        custom.getChessBoard().add(new ChessPair<>(
                new Position("G2"), new Pawn(Colors.WHITE, new Position("G2"))));
        custom.getChessBoard().add(new ChessPair<>(
                new Position("H2"), new Pawn(Colors.WHITE, new Position("H2"))));
        custom.getChessBoard().add(new ChessPair<>(
                new Position("H8"), new King(Colors.BLACK, new Position("H8"))));
        custom.getChessBoard().add(new ChessPair<>(
                new Position("E1"), new Rook(Colors.BLACK, new Position("E1"))));
        custom.getChessBoard().add(new ChessPair<>(
                new Position("G3"), new Queen(Colors.BLACK, new Position("G3"))));
        // caz in care sunt in sah mat

        game.setBoard(custom);
        game.setActive(true);
        game.setSc(new Scanner(System.in));
        game.resume();
        System.out.println("Mai e jocul activ? " + game.getActive());
        System.out.println(
                "S-a terminat jocul prin sah? " + game.checkForCheckMate());

        System.out.println(
                "==================== Testare Remiza ====================");
        game = new Game();
        custom = new Board();
        custom.getChessBoard().clear();

        custom.getChessBoard().add(new ChessPair<>(
                new Position("H8"), new King(Colors.BLACK, new Position("H8"))));
        custom.getChessBoard().add(new ChessPair<>(
                new Position("F7"), new King(Colors.WHITE, new Position("F7"))));
        custom.getChessBoard().add(new ChessPair<>(
                new Position("G5"), new Queen(Colors.WHITE, new Position("G5"))));
        // voi muta regina de la g5 la g6 si jocul se va termina in remiza

        game.setBoard(custom);
        game.setActive(true);

        game.setSc(new Scanner(System.in));
        game.resume();

        System.out.println("Mai e jocul activ? " + game.getActive());
        System.out.println(
                "S-a terminat jocul prin sah? " + game.checkForCheckMate());

        System.out.println(
                "==================== Testare ThreeFold ====================");
        game = new Game();
        custom = new Board();
        ArrayList<Move> moves = new ArrayList<>();

        moves.add(new Move(
                Colors.WHITE, new Position("C2"), new Position("C3"), null));
        moves.add(new Move(
                Colors.BLACK, new Position("E7"), new Position("E5"), null));

        moves.add(new Move(
                Colors.WHITE, new Position("H2"), new Position("H3"), null));
        moves.add(new Move(
                Colors.BLACK, new Position("G8"), new Position("F6"), null));
        moves.add(new Move(
                Colors.WHITE, new Position("F3"), new Position("G1"), null));
        moves.add(new Move(
                Colors.BLACK, new Position("F6"), new Position("G8"), null));

        moves.add(new Move(
                Colors.WHITE, new Position("G1"), new Position("F3"), null));
        moves.add(new Move(
                Colors.BLACK, new Position("G8"), new Position("F6"), null));

        custom.movePiece(new Position("C2"), new Position("C3"));
        custom.movePiece(new Position("E7"), new Position("E5"));
        custom.movePiece(new Position("G1"), new Position("F3"));
        custom.movePiece(new Position("G8"), new Position("F6"));

        // daca introduc mutarea f3-g1  jocul se termina prin threefold
        game.setMoves(moves);
        game.setBoard(custom);
        game.setBoard(custom);
        game.setActive(true);
        game.setSc(new Scanner(System.in));
        game.resume();
        game.printGameMoves();
        System.out.println("Mai e jocul activ? " + game.getActive());
        System.out.println(
                "S-a terminat jocul prin sah? " + game.checkForCheckMate());
    }
}