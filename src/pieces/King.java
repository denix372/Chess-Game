package pieces;
import game.Board;
import model.*;
import movesStrategies.KingMoveStrategy;

import java.util.ArrayList;

public class King extends Piece {
    public King(Colors color, Position position ) {
        super(color, position);
        this.moveStrategy = new KingMoveStrategy();
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        ArrayList<Position> moves = getPossibleMoves(board);
        return moves.contains(kingPosition);
    }

    @Override
    public char type() {
        return 'K';
    }

    @Override
    public String toString() {
        if( super.getColor() == Colors.BLACK)
            return "K-B";
        else
            return "K-W";
    }
}
