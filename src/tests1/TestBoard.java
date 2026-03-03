package tests1;

import model.*;
import pieces.*;
import game.*;

public class TestBoard {
    public static void main(String[] args) {
        System.out.println(" ====================== Testare clasa Position " +
                "====================== ");
        Position pos1 = new Position('A', 2);
        Position pos2 = new Position('H', 7);
        Position pos3 = new Position('A', 2);
        Position pos4 = new Position();
        pos4.setX('G'); pos4.setY(8);
        System.out.println("Pozitia pos1: " + pos1);
        System.out.println("Pentru pos1: " + "x = " + pos1.getX() + ", y = " + pos1.getY());
        System.out.println("Pozitia pos2: " + pos2);
        System.out.println("Pozitia pos3: " + pos3);
        System.out.println("Pozitia pos4: " + pos4);
        System.out.println("Sunt " + pos1 + " si " + pos2 + " egale? - " + pos1.equals(pos2));
        System.out.println("Sunt " + pos1 + " si " + pos3 + " egale? - " + pos1.equals(pos3));

        System.out.println(" ======================== Testare clasa Pawn " +
                "======================== ");

        Pawn pawn1 = new Pawn(Colors.WHITE, pos1);
        Pawn pawn2 = new Pawn(Colors.BLACK, new Position('H', 7));
        System.out.println("Pawn1: " + pawn1);
        System.out.println("Pawn2: " + pawn2);
        System.out.println("Piece type: " + pawn1.type());

        Knight knight1 = new Knight(Colors.BLACK, pos4);
        Knight knight2 = new Knight(Colors.WHITE, new Position('B', 1));
        System.out.println(knight1);
        System.out.println(knight1.type());
        System.out.println(knight2);

        System.out.println(" ======================== Testare ChessPair " +
                "======================== ");
        ChessPair<Position, Piece> pair1 = new ChessPair<>(pos1, pawn1);
        ChessPair<Position, Piece> pair2 = new ChessPair<>(pos2, pawn2);
        ChessPair<Position, Piece> pair3 = new ChessPair<>(pos1, knight1);
        System.out.println("Pair1: " + pair1.print());
        System.out.println("Pair2: " + pair2.print());
        System.out.println("Pair3: " + pair3.print());
        System.out.println("Rezultat comparare pair1 si pair2: " + pair1.compareTo(pair2));
        System.out.println("Rezultat comparare pair1 si pair3: " + pair1.compareTo(pair3));

        System.out.println(" ======================== Testare Board " +
                "======================== ");
        Board board = new Board();
        System.out.println(board);

        // Testare pozitie Pawm - Knight - Rook - Bishop
        System.out.println("Pozitii pentru pionul alb de pe A2:" + pawn1.getPossibleMoves(board));
        System.out.println("Pozitii pentru pionul negru de pe H7:" + pawn2.getPossibleMoves(board));

        System.out.println("Pozitii pentru cal alb de pe G8:" + knight1.getPossibleMoves(board));
        System.out.println("Pozitii pentru cal negru de pe B1:" + knight2.getPossibleMoves(board));

        Rook rook1 = new Rook(Colors.WHITE, new Position('D', 5));
        Rook rook2 = new Rook(Colors.BLACK, new Position('E', 4));
        System.out.println("Pozitii pentru tura alba de pe D5:" + rook1.getPossibleMoves(board));
        System.out.println("Pozitii pentru tura neagra de pe E4:" + rook2.getPossibleMoves(board));

        Bishop bishop1 = new Bishop(Colors.WHITE, new Position('D', 5));
        Bishop bishop2 = new Bishop(Colors.BLACK, new Position('E', 4));
        System.out.println("Pozitii pentru nebunul alb de pe D5:" + bishop1.getPossibleMoves(board));
        System.out.println("Pozitii pentru nubunul negru de pe E4:" + bishop2.getPossibleMoves(board));

        Queen queen1 = new Queen(Colors.WHITE, new Position('D', 5));
        Queen queen2 = new Queen(Colors.BLACK, new Position('E', 4));
        System.out.println("Pozitii pentru regina alba de pe D5:" + queen1.getPossibleMoves(board));
        System.out.println("Pozitii pentru regina neagra de pe E4:" + queen2.getPossibleMoves(board));

        King king1 = new King(Colors.WHITE, new Position('D', 5));
        King king2 = new King(Colors.BLACK, new Position('E', 4));
        System.out.println("Pozitii pentru regele alb de pe D5:" + king1.getPossibleMoves(board));
        System.out.println("Pozitii pentru regele negru de pe E4:" + king2.getPossibleMoves(board));

        // Testare check mate
        System.out.println("Poate fi regele negru aflat pe E4 luat de regina alba aflata pe D5?: "
                + queen1.checkForCheck(board, new Position('E', 4)));

        // Vom lua regii adevarati si vom vedea daca piesele adverse il vad in sah
        king1.setPosition(new Position('E', 1));
        king2.setPosition(new Position('E', 8));
        System.out.println("Este regele in sah? " + pawn1.checkForCheck(board, new Position('E', 8)));
        System.out.println("Este regele in sah? " + knight1.checkForCheck(board, new Position('E', 1)));

        //Testare minimala pentru a muta o piese
        board.movePiece(new Position('B', 2), new Position('B', 3));
        System.out.println(board);

        board.movePiece(new Position('D', 7), new Position('D', 6));
        System.out.println(board);

        board.movePiece(new Position('C', 8), new Position('E', 6));
        System.out.println(board);

        board.movePiece(new Position('F', 2), new Position('F', 3));
        System.out.println(board);

        // Testare capturare piese (nebunul ia un pion)
        board.movePiece(new Position('E', 6), new Position('B', 3));
        System.out.println(board);
    }
}