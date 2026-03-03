package movesStrategies;

import game.Board;
import model.Colors;
import model.Position;
import pieces.Pawn;
import pieces.Piece;

import java.util.ArrayList;

public class PawnMoveStrategy implements MoveStrategy{
    @Override
    public ArrayList<Position> getPossibleMoves(Board board, Piece piece) {
        ArrayList<Position> moves = new ArrayList<>();
        if (piece.getColor() == Colors.WHITE) {
            if (piece.getPosition().getY() + 1 <= 8) {

                Position pos1 = new Position(piece.getPosition().getX(), piece.getPosition().getY() + 1);
                Piece p = board.getPieceAt(pos1);
                if (p == null)
                    moves.add(pos1);

                if (piece.getPosition().getXPrev() != null) {
                    Position posleft = new Position(piece.getPosition().getXPrev(), piece.getPosition().getY() + 1);
                    p = board.getPieceAt(posleft);
                    if (p != null && p.getColor() != piece.getColor())
                        moves.add(posleft);
                }

                if (piece.getPosition().getXNext() != null) {
                    Position posright = new Position(piece.getPosition().getXNext(), piece.getPosition().getY() + 1);
                    p = board.getPieceAt(posright);
                    if (p != null && p.getColor() != piece.getColor())
                        moves.add(posright);
                }

                if (((Pawn)piece).getFirstmove() == 0 && moves.contains(pos1)) {
                    if (piece.getPosition().getY() + 2 <= 8) {
                        Position pos2 = new Position(piece.getPosition().getX(), piece.getPosition().getY() + 2);
                        p = board.getPieceAt(pos2);
                        if (p == null)
                            moves.add(pos2);
                    }
                }
            }
        } else  {
            if( piece.getPosition().getY() -1 >= 1) {
                Position pos1 = new Position(piece.getPosition().getX(), piece.getPosition().getY() - 1);
                Piece p = board.getPieceAt(pos1);
                if (p == null)
                    moves.add(pos1);

                if (piece.getPosition().getXPrev() != null) {
                    Position posleft = new Position(piece.getPosition().getXPrev(), piece.getPosition().getY() - 1);
                    p = board.getPieceAt(posleft);
                    if (p != null && p.getColor() != piece.getColor())
                        moves.add(posleft);
                }

                if (piece.getPosition().getXNext() != null) {
                    Position posright = new Position(piece.getPosition().getXNext(), piece.getPosition().getY() - 1);
                    p = board.getPieceAt(posright);
                    if (p != null && p.getColor() != piece.getColor())
                        moves.add(posright);
                }

                if (((Pawn)piece).getFirstmove() == 0 && moves.contains(pos1)) {
                    if (piece.getPosition().getY() - 2 >= 1) {
                        Position pos2 = new Position(piece.getPosition().getX(), piece.getPosition().getY() - 2);
                        p = board.getPieceAt(pos2);
                        if (p == null)
                            moves.add(pos2);
                    }
                }
            }
        }
        return moves;
    }
}
