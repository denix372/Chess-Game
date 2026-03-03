package movesStrategies;

import game.Board;
import model.Position;
import pieces.Piece;

import java.util.ArrayList;

public class RookMoveStrategy implements MoveStrategy{
    @Override
    public ArrayList<Position> getPossibleMoves(Board board, Piece piece) {
        ArrayList<Position> moves = new ArrayList<>();

        for(char ch = (char)(piece.getPosition().getX() + 1); ch <= 'H'; ch = (char)(ch + 1)) {
            Position pos = new Position(ch, piece.getPosition().getY());
            Piece p = board.getPieceAt(pos);
            if (p == null)
                moves.add(pos);
            else if (p.getColor() == piece.getColor())
                break;
            else if (p.getColor() != piece.getColor()) {
                moves.add(pos);
                break;
            }
        }
        for(char ch = (char)(piece.getPosition().getX() - 1); ch >= 'A'; ch = (char)(ch - 1)) {
            Position pos = new Position(ch, piece.getPosition().getY());
            Piece p = board.getPieceAt(pos);
            if (p == null)
                moves.add(pos);
            else if (p.getColor() == piece.getColor())
                break;
            else if (p.getColor() != piece.getColor()) {
                moves.add(pos);
                break;
            }

        }
        for(int i = piece.getPosition().getY() + 1; i <= 8; i++) {
            Position pos = new Position(piece.getPosition().getX(), i);
            Piece p = board.getPieceAt(pos);
            if (p == null)
                moves.add(pos);
            else if (p.getColor() == piece.getColor())
                break;
            else if (p.getColor() != piece.getColor()) {
                moves.add(pos);
                break;
            }
        }
        for(int i = piece.getPosition().getY() - 1; i >= 1; i--) {
            Position pos = new Position(piece.getPosition().getX(), i);
            Piece p = board.getPieceAt(pos);
            if (p == null)
                moves.add(pos);
            else if (p.getColor() == piece.getColor())
                break;
            else if (p.getColor() != piece.getColor()) {
                moves.add(pos);
                break;
            }
        }
        return moves;

    }
}
