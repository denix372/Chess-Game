package movesStrategies;

import game.Board;
import model.Position;
import pieces.Piece;

import java.util.ArrayList;

public class BishopMoveStrategy implements MoveStrategy{
    @Override
    public ArrayList<Position> getPossibleMoves(Board board, Piece piece) {
        {
            ArrayList<Position> moves = new ArrayList<>();

            int i = piece.getPosition().getY() + 1;
            char ch = (char)(piece.getPosition().getX() + 1);
            for(; ch <= 'H' && i <=8; ch = (char)(ch + 1), i++) {
                Position pos = new Position(ch, i);
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

            i = piece.getPosition().getY() + 1;
            ch = (char)(piece.getPosition().getX() - 1);
            for(; ch >= 'A' && i <= 8; ch = (char)(ch - 1), i++) {
                Position pos = new Position(ch, i);
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

            i = piece.getPosition().getY() - 1;
            ch = (char)(piece.getPosition().getX() + 1);
            for(; ch <= 'H' && i >= 1; ch = (char)(ch + 1), i--) {
                Position pos = new Position(ch, i);
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

            i = piece.getPosition().getY() - 1;
            ch = (char)(piece.getPosition().getX() - 1);
            for(; ch >= 'A' && i >= 1; ch = (char)(ch - 1), i--) {
                Position pos = new Position(ch, i);
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
}
