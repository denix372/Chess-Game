package pieces;
import game.Board;
import model.*;
import movesStrategies.RookMoveStrategy;

import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(Colors color, Position position ) {
        super(color, position);
        this.moveStrategy = new RookMoveStrategy();
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        ArrayList<Position> moves = getPossibleMoves(board);
        return moves.contains(kingPosition);
    }

    @Override
    public char type() {
        return 'R';
    }

    @Override
    public String toString() {
        if( super.getColor() == Colors.BLACK)
            return "R-B";
        else
            return "R-W";
    }


}
