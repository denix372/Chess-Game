package tests1;

import game.Board;
import game.Player;
import model.ChessPair;
import model.Colors;
import model.Position;
import pieces.Piece;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TestPlayer {
    public static void main(String[] args) {
        //Testare clasa Player
        System.out.println("============ Testare clasa Player =================");
        Player player1 = new Player("Denis", Colors.WHITE);
        Player player2 = new Player("Computer", Colors.BLACK);
        Board board1 = new Board();

        player1.setOwnedPieces(board1.getWhitePieces());
        player2.setOwnedPieces(board1.getBlackPieces());

        System.out.println(board1);
        player1.makeMove(new Position('B', 2), new Position('B', 3), board1);
        System.out.println(board1);
        player2.makeMove(new Position('D', 7), new Position('D', 6), board1);
        System.out.println(board1);
        player1.makeMove(new Position('F', 2), new Position('F', 3), board1);
        System.out.println(board1);
        player2.makeMove(new Position('C', 8), new Position('E', 6), board1);
        System.out.println(board1);
        player1.makeMove(new Position('G', 2), new Position('G', 3), board1);
        System.out.println(board1);

        player2.makeMove(new Position('E', 6), new Position('B', 3), board1);
        System.out.println(board1);

        player1.setOwnedPieces(board1.getWhitePieces());
        player2.setOwnedPieces(board1.getBlackPieces());

        System.out.println("Points player1: " + player1.getPoints());
        System.out.println("Captured Pieces player1:" + player1.getCapturedPieces());
        System.out.print("Captured Owned player1:");
        for (ChessPair<Position, Piece> owned : player1.getOwnedPieces()) {
            System.out.print(owned.getValue() + " ");
        }

        System.out.println("\nPoints player2: " + player2.getPoints());
        System.out.println("Captured Pieces player2:" + player2.getCapturedPieces());
        System.out.print("Captured Owned player2:");
        for (ChessPair<Position, Piece> owned : player2.getOwnedPieces()) {
            System.out.print(owned.getValue() + " ");
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("\nIntrodu o mutare (ex: B2-B3): ");
        String move = sc.nextLine();
        String[] parts = move.split("-");
        String from = parts[0]; // B2
        String to = parts[1]; //B3
        Position fromPos = new Position(from.charAt(0), Character.getNumericValue(from.charAt(1)));
        Position toPos = new Position(to.charAt(0), Character.getNumericValue(to.charAt(1)));

        player1.makeMove(fromPos, toPos, board1);
        System.out.println(board1);

        player2.setOwnedPieces(board1.getBlackPieces());

        //Testare randomizare
        ArrayList<ChessPair<Position, Piece>> posible = new ArrayList<>();
        for(ChessPair<Position, Piece> pair: player2.getOwnedPieces()) {
            for(Position pos : pair.getValue().getPossibleMoves(board1)) {
                if (board1.isValidMove(pair.getValue().getPosition(), pos)) {
                    posible.add(new ChessPair<>(pos, pair.getValue()));
                }
            }
        }

        // avem o lista cu toate mutarile posibile pe care poate sa le faca player2 (perechi Pozitie Piese)
        // Player2 selecteaza random una (consideram ca nu este in sah)
        Random rand = new Random();
        ChessPair<Position, Piece> chosen = posible.get(rand.nextInt(posible.size()));
        Position fromPosInitial = chosen.getValue().getPosition();
        Position toPosInitial = chosen.getKey();

        player2.makeMove( chosen.getValue().getPosition() ,chosen.getKey(), board1);
        System.out.println("Mutare random pentru player2: "
                + chosen.getValue().type() + " din "
                + fromPosInitial + " la "
                + toPosInitial);

        System.out.println(board1);

        player1.setOwnedPieces(board1.getWhitePieces());

        // Finalizare joc pe baza deciziilor random ale computerului si inputuri
        sc = new Scanner(System.in);
        System.out.println("Introdu o mutare (ex: B2-B3): ");
        move = sc.nextLine();
        parts = move.split("-");
        from = parts[0]; // B2
        to = parts[1]; //B3
        fromPos = new Position(from.charAt(0), Character.getNumericValue(from.charAt(1)));
        toPos = new Position(to.charAt(0), Character.getNumericValue(to.charAt(1)));

        player1.makeMove(fromPos, toPos, board1);
        System.out.println(board1);

        player2.setOwnedPieces(board1.getBlackPieces());

        posible = new ArrayList<>();
        for(ChessPair<Position, Piece> pair: player2.getOwnedPieces())
            for(Position pos : pair.getValue().getPossibleMoves(board1))
                if(board1.isValidMove(pair.getValue().getPosition(), pos))
                    posible.add(new ChessPair<>( pos, pair.getValue()));

        rand = new Random();
        chosen = posible.get(rand.nextInt(posible.size()));
        fromPosInitial = chosen.getValue().getPosition();
        toPosInitial = chosen.getKey();

        player2.makeMove( chosen.getValue().getPosition() ,chosen.getKey(), board1);
        System.out.println("Mutare random pentru player2: "
                + chosen.getValue().type() + " din "
                + fromPosInitial + " la "
                + toPosInitial);

        System.out.println(board1);

        player1.setOwnedPieces(board1.getWhitePieces());

        sc = new Scanner(System.in);
        System.out.println("Introdu o mutare (ex: B2-B3): ");
        move = sc.nextLine();
        parts = move.split("-");
        from = parts[0]; // B2
        to = parts[1]; //B3
        fromPos = new Position(from.charAt(0), Character.getNumericValue(from.charAt(1)));
        toPos = new Position(to.charAt(0), Character.getNumericValue(to.charAt(1)));

        player1.makeMove(fromPos, toPos, board1);
        System.out.println(board1);

        player1.setOwnedPieces(board1.getWhitePieces());
        player2.setOwnedPieces(board1.getBlackPieces());

        System.out.println("Points player1: " + player1.getPoints());
        System.out.println("Captured Pieces player1:" + player1.getCapturedPieces());
        System.out.print("Captured Owned player1:");
        for (ChessPair<Position, Piece> owned : player1.getOwnedPieces()) {
            System.out.print(owned.getValue() + " ");
        }

        System.out.println("\nPoints player2: " + player2.getPoints());
        System.out.println("Captured Pieces player2:" + player2.getCapturedPieces());
        System.out.print("Captured Owned player2:");
        for (ChessPair<Position, Piece> owned : player2.getOwnedPieces()) {
            System.out.print(owned.getValue() + " ");
        }

    }
}
