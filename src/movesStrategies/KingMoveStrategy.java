package movesStrategies;

import game.Board;
import model.Position;
import pieces.Piece;

import java.util.ArrayList;

public class KingMoveStrategy implements MoveStrategy{
    @Override
    public ArrayList<Position> getPossibleMoves(Board board, Piece piece) {
        ArrayList<Position> moves = new ArrayList<>();

        for(char ch = (char)(piece.getPosition().getX() - 1); ch <= (char)(piece.getPosition().getX() + 1); ch = (char)(ch + 1))
            for(int i =piece.getPosition().getY() - 1 ; i <= piece.getPosition().getY() + 1; i++ )
                if( ch != piece.getPosition().getX() || i != piece.getPosition().getY()) {
                    if(ch >= 'A' && ch <= 'H' && i >= 1 && i <= 8) {
                        Position pos = new Position(ch, i);
                        Piece p = board.getPieceAt(pos);
                        if(p == null || p.getColor() != piece.getColor())
                            moves.add(pos);
                    }
                }
        return moves;
    }
}
