package pieces;
import game.Board;
import model.*;
import movesStrategies.KnightMoveStrategy;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(Colors color, Position position ) {
        super(color, position);
        this.moveStrategy = new KnightMoveStrategy();
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        ArrayList<Position> moves = getPossibleMoves(board);
        return moves.contains(kingPosition);
    }

    @Override
    public String toString() {
        if( super.getColor() == Colors.BLACK)
            return "N-B";
        else
            return "N-W";
    }

    @Override
    public char type() {
        return 'N';
    }
}
