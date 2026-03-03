package pieces;
import game.Board;
import model.*;
import movesStrategies.BishopMoveStrategy;

import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(Colors color, Position position ) {
        super(color, position);
        this.moveStrategy = new BishopMoveStrategy();
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        ArrayList<Position> moves = getPossibleMoves(board);
        return moves.contains(kingPosition);
    }

    @Override
    public char type() {
        return 'B';
    }

    @Override
    public String toString() {
        if( super.getColor() == Colors.BLACK)
            return "B-B";
        else
            return "B-W";
    }


}
