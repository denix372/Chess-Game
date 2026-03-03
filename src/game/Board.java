package game;
import exceptions.InvalidMoveException;
import model.*;
import pieces.*;

import java.util.ArrayList;
import java.util.TreeSet;

public class Board {
    TreeSet<ChessPair<Position, Piece>> chessBoard =
            new TreeSet<>(new ChessPairComparator<>());

    public Board() {
        initialize();
    }

    public void initialize() {
        // 2. Folosim pattern-ul factory pentru a crea Piesele
        chessBoard.add(new ChessPair<>(new Position('A', 1), PieceFactory.factory('R', Colors.WHITE, new Position('A', 1))));
        chessBoard.add(new ChessPair<>(new Position('B', 1), PieceFactory.factory('N', Colors.WHITE, new Position('B', 1))));
        chessBoard.add(new ChessPair<>(new Position('C', 1), PieceFactory.factory('B', Colors.WHITE, new Position('C', 1))));
        chessBoard.add(new ChessPair<>(new Position('D', 1), PieceFactory.factory('Q', Colors.WHITE, new Position('D', 1))));
        chessBoard.add(new ChessPair<>(new Position('E', 1), PieceFactory.factory('K', Colors.WHITE, new Position('E', 1))));
        chessBoard.add(new ChessPair<>(new Position('F', 1), PieceFactory.factory('B', Colors.WHITE, new Position('F', 1))));
        chessBoard.add(new ChessPair<>(new Position('G', 1), PieceFactory.factory('N', Colors.WHITE, new Position('G', 1))));
        chessBoard.add(new ChessPair<>(new Position('H', 1), PieceFactory.factory('R', Colors.WHITE, new Position('H', 1))));

        chessBoard.add(new ChessPair<>(new Position('A', 8), PieceFactory.factory('R', Colors.BLACK, new Position('A', 8))));
        chessBoard.add(new ChessPair<>(new Position('B', 8), PieceFactory.factory('N', Colors.BLACK, new Position('B', 8))));
        chessBoard.add(new ChessPair<>(new Position('C', 8), PieceFactory.factory('B', Colors.BLACK, new Position('C', 8))));
        chessBoard.add(new ChessPair<>(new Position('D', 8), PieceFactory.factory('Q', Colors.BLACK, new Position('D', 8))));
        chessBoard.add(new ChessPair<>(new Position('E', 8), PieceFactory.factory('K', Colors.BLACK, new Position('E', 8))));
        chessBoard.add(new ChessPair<>(new Position('F', 8), PieceFactory.factory('B', Colors.BLACK, new Position('F', 8))));
        chessBoard.add(new ChessPair<>(new Position('G', 8), PieceFactory.factory('N', Colors.BLACK, new Position('G', 8))));
        chessBoard.add(new ChessPair<>(new Position('H', 8), PieceFactory.factory('R', Colors.BLACK, new Position('H', 8))));

        for (char ch = 'A'; ch <= 'H'; ch++) {
            chessBoard.add(new ChessPair<>(new Position(ch, 2), PieceFactory.factory('P', Colors.WHITE, new Position(ch, 2))));
            chessBoard.add(new ChessPair<>(new Position(ch, 7), PieceFactory.factory('P', Colors.BLACK, new Position(ch, 7))));
        }
    }

    public TreeSet<ChessPair<Position, Piece>> getWhitePieces() {
        TreeSet<ChessPair<Position, Piece>> whitePieces = new TreeSet<>(new ChessPairComparator<>());
        for (ChessPair<Position, Piece> pair : chessBoard)
            if( pair.getValue().getColor() == Colors.WHITE)
                whitePieces.add(pair);

        return whitePieces;
    }

    public TreeSet<ChessPair<Position, Piece>> getBlackPieces() {
        TreeSet<ChessPair<Position, Piece>> blackPieces = new TreeSet<>(new ChessPairComparator<>());
        for (ChessPair<Position, Piece> pair : chessBoard)
            if( pair.getValue().getColor() == Colors.BLACK)
                blackPieces.add(pair);

        return blackPieces;
    }

    public void movePiece(Position from, Position to) {
            Piece p1 = getPieceAt(from);
            Piece p2 = getPieceAt(to);

            chessBoard.remove(new ChessPair<>(from, p1));
            if(p2 != null)
                chessBoard.remove(new ChessPair<>(to, p2));

            chessBoard.add(new ChessPair<>(to, p1));
            p1.setPosition(to);
            if(p1.type() == 'P')
                ((Pawn)p1).setFirstmove(1);
    }

    public boolean pawnPromotion(Pawn pawn, Piece piece) {
        if (pawn.getColor() == Colors.WHITE && pawn.getPosition().getY() == 8
            || pawn.getColor() == Colors.BLACK && pawn.getPosition().getY() == 1) {
            Position pos = pawn.getPosition();
            chessBoard.remove(new ChessPair<>(pos, pawn));
            chessBoard.add(new ChessPair<>(pos, piece));
            piece.setPosition(pos);
            System.out.println("Promotion! Pionul a devenit! " + piece.type());
            return true;
        }
        return false;
    }

    public boolean isValidMove(Position from, Position to) {
        Piece p1 = getPieceAt(from);
        Piece p2 = getPieceAt(to);
        if(p1 == null)
            throw new InvalidMoveException("Nu exista nicio piesa la pozitia sursa.");

        ArrayList<Position> moves = p1.getPossibleMoves(this);
        if(!moves.contains(to))
            throw new InvalidMoveException("Mutare neconforma regulilor de sah");

        //scoatem piesa sursa
        chessBoard.remove(new ChessPair<>(from, p1));
        // daca exista o piesa la destinatie(captura) o scoatem temporar
        if (p2 != null)
            chessBoard.remove(new ChessPair<>(to, p2));

        // adaugam piesa la destinatie si ii actualizam pozitia
        chessBoard.add(new ChessPair<>(to, p1));
        p1.setPosition(to);
        boolean safe = !isKingInCheck(p1.getColor());

        chessBoard.remove(new ChessPair<>(to, p1));
        p1.setPosition(from);
        chessBoard.add(new ChessPair<>(from, p1));

        if (p2 != null)
            chessBoard.add(new ChessPair<>(to, p2));

        if(!safe)
            throw  new InvalidMoveException("Mutarea lasa regele sa ramana in sah!");

        return true;
    }

    public boolean hasAnyValidMoves(Colors color) {
        ArrayList<ChessPair<Position, Piece>> snapshot = new ArrayList<>(chessBoard);
        for (ChessPair<Position, Piece> pair : snapshot) {
            Piece piece = pair.getValue();

            if (piece.getColor() == color)
                for (Position pos : piece.getPossibleMoves(this)) {
                    try {
                        // testam daca mutarea e valida
                        if (isValidMove(piece.getPosition(), pos))
                            return true;
                    } catch (InvalidMoveException e) {
                        // daca e invalida (regele ramane in sah), o ignoram
                    }
                }
        }
        return false;
    }

    // metoda prin care verifica ca regele printr-o anumita culoare este atacat
    public boolean isKingInCheck(Colors color) {
        Position kingPos = null;
        for(ChessPair<Position, Piece> pair : chessBoard) {
            Piece p = pair.getValue();
            if (p instanceof King && p.getColor() == color) {
                kingPos = p.getPosition();
                break;
            }
        }

        if (kingPos == null)
            return false;

        for(ChessPair<Position, Piece> pair : chessBoard) {
            Piece enemy = pair.getValue();
            if (enemy.getColor() != color) {
                ArrayList<Position> enemyMoves = enemy.getPossibleMoves(this);
                if (enemyMoves.contains(kingPos))
                    return true;
            }
        }
        return false;
    }


    public Piece getPieceAt(Position position) {
        for( ChessPair<Position, Piece> cp : chessBoard)
            if( cp.getKey().equals(position))
                return cp.getValue();
        return null;
    }

    public TreeSet<ChessPair<Position, Piece>> getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(TreeSet<ChessPair<Position, Piece>> chessBoard) {
        this.chessBoard = chessBoard;
    }

    @Override
    public String toString() {
        return toString(Colors.WHITE);
    }

    public String toString(Colors color) {
        final String RESET = "\u001B[0m";
        final String WHITE_PIECE = "\u001B[97m";
        final String BLACK_PIECE = "\u001B[91m";

        StringBuilder sb = new StringBuilder();
        sb.append("   --------------------------------\n");
        if (color == Colors.WHITE) {

            for (int i = 8; i >= 1; i--) {
                sb.append(i).append(" | ");
                for (char ch = 'A'; ch <= 'H'; ch = (char) (ch + 1)) {
                    Piece p = getPieceAt(new Position(ch, i));

                    if (p != null)
                        if (p.getColor() == Colors.WHITE)
                            sb.append(WHITE_PIECE).append(p).append(RESET).append(" ");
                        else
                            sb.append(BLACK_PIECE).append(p).append(RESET).append(" ");
                    else
                        sb.append("... ");
                }
                sb.append("\n");
            }
            sb.append("   --------------------------------\n");
            sb.append("     ");
            for (char ch = 'A'; ch <= 'H'; ch = (char) (ch + 1))
                sb.append(ch).append("   ");

        } else {

            for (int i = 1; i <= 8; i++) {
                sb.append(i).append(" | ");
                for (char ch = 'H'; ch >= 'A'; ch = (char) (ch - 1)) {
                    Piece p = getPieceAt(new Position(ch, i));

                    if (p != null)
                        if (p.getColor() == Colors.WHITE)
                            sb.append(WHITE_PIECE).append(p).append(RESET).append(" ");
                        else
                            sb.append(BLACK_PIECE).append(p).append(RESET).append(" ");
                    else
                        sb.append("... ");
                }
                sb.append("\n");
            }
            sb.append("   --------------------------------\n");
            sb.append("     ");
            for (char ch = 'H'; ch >= 'A'; ch = (char) (ch - 1))
                sb.append(ch).append("   ");
        }
        sb.append("\n");
        return sb.toString();
    }
}
