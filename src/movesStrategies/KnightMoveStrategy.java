package movesStrategies;

import game.Board;
import model.Position;
import pieces.Piece;

import java.util.ArrayList;

public class KnightMoveStrategy implements MoveStrategy{
    @Override
    public ArrayList<Position> getPossibleMoves(Board board, Piece piece) {
        ArrayList<Position> moves = new ArrayList<>();

        if((char)(piece.getPosition().getX() + 1) >= 'A' &&  (char)(piece.getPosition().getX() + 1) <= 'H') {
            if(piece.getPosition().getY() + 2 <= 8) {
                Position pos1 = new Position((char)(piece.getPosition().getX() + 1), piece.getPosition().getY() + 2);
                Piece p = board.getPieceAt(pos1);
                if (p == null || p.getColor() != piece.getColor())
                    moves.add(pos1);
            }
            if(piece.getPosition().getY() - 2 >= 1) {
                Position pos2 = new Position((char)(piece.getPosition().getX() + 1), piece.getPosition().getY() - 2);
                Piece p = board.getPieceAt(pos2);
                if (p == null || p.getColor() != piece.getColor())
                    moves.add(pos2);
            }
        }
        if((char)(piece.getPosition().getX() - 1) >= 'A' &&  (char)(piece.getPosition().getX() - 1) <= 'H') {
            if(piece.getPosition().getY() + 2 <= 8) {
                Position pos3 = new Position((char)(piece.getPosition().getX() - 1), piece.getPosition().getY() + 2);
                Piece p = board.getPieceAt(pos3);
                if (p == null || p.getColor() != piece.getColor())
                    moves.add(pos3);
            }
            if(piece.getPosition().getY() - 2 >= 1) {
                Position pos4 = new Position((char)(piece.getPosition().getX() - 1), piece.getPosition().getY() - 2);
                Piece p = board.getPieceAt(pos4);
                if (p == null || p.getColor() != piece.getColor())
                    moves.add(pos4);
            }
        }
        if((char)(piece.getPosition().getX() + 2) >= 'A' &&  (char)(piece.getPosition().getX() + 2) <= 'H') {
            if(piece.getPosition().getY() + 1 <= 8) {
                Position pos1 = new Position((char)(piece.getPosition().getX() + 2), piece.getPosition().getY() + 1);
                Piece p = board.getPieceAt(pos1);
                if (p == null || p.getColor() != piece.getColor())
                    moves.add(pos1);
            }
            if(piece.getPosition().getY() - 1 >= 1) {
                Position pos2 = new Position((char)(piece.getPosition().getX() + 2), piece.getPosition().getY() - 1);
                Piece p = board.getPieceAt(pos2);
                if (p == null || p.getColor() != piece.getColor())
                    moves.add(pos2);
            }
        }
        if((char)(piece.getPosition().getX() - 2) >= 'A' &&  (char)(piece.getPosition().getX() - 2) <= 'H') {
            if(piece.getPosition().getY() + 1 <= 8) {
                Position pos1 = new Position((char)(piece.getPosition().getX() - 2), piece.getPosition().getY() + 1);
                Piece p = board.getPieceAt(pos1);
                if (p == null || p.getColor() != piece.getColor())
                    moves.add(pos1);
            }
            if(piece.getPosition().getY() - 1 >= 1) {
                Position pos2 = new Position((char)(piece.getPosition().getX() - 2), piece.getPosition().getY() - 1);
                Piece p = board.getPieceAt(pos2);
                if (p == null || p.getColor() != piece.getColor())
                    moves.add(pos2);
            }
        }

        return moves;
    }
}
