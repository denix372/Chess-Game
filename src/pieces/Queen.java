package pieces;
import game.Board;
import model.*;
import movesStrategies.QueenMoveStrategy;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(Colors color, Position position ) {
        super(color, position);
        this.moveStrategy = new QueenMoveStrategy();
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        ArrayList<Position> moves = getPossibleMoves(board);
        return moves.contains(kingPosition);
    }

    @Override
    public char type() {
        return 'Q';
    }

    @Override
    public String toString() {
        if( super.getColor() == Colors.BLACK)
            return "Q-B";
        else
            return "Q-W";
    }


}
