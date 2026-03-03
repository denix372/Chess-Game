package pieces;
import game.Board;
import model.*;
import movesStrategies.PawnMoveStrategy;

import java.util.ArrayList;

public class Pawn extends Piece {
    int firstmove;

    public Pawn(Colors color, Position position ) {
        super(color, position);
        firstmove = 0;
        this.moveStrategy = new PawnMoveStrategy();
    }

    public int getFirstmove() {
        return firstmove;
    }

    public void setFirstmove(int firstmove) {
        this.firstmove = firstmove;
    }


    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        ArrayList<Position> moves = getPossibleMoves(board);
        return moves.contains(kingPosition);
    }

    @Override
    public char type() {
        return 'P';
    }

    @Override
    public String toString() {
        if( super.getColor() == Colors.BLACK)
            return "P-B";
        else
            return "P-W";
    }
}
