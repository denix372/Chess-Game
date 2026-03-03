package pieces;
import game.Board;
import model.*;
import movesStrategies.MoveStrategy;

import java.util.ArrayList;

public abstract class Piece implements ChessPiece {
    private Colors color;
    private Position position;
    // 3. Parametru
    protected MoveStrategy moveStrategy;

    public Piece(Colors color, Position position) {
        this.color = color;
        this.position = position;
    }

    // 3. Folosim metoda getPossibleMoves la nivel de Piesa pentru Strategy
    @Override public ArrayList<Position> getPossibleMoves(Board board) {
        return moveStrategy.getPossibleMoves(board, this);
    }

    public Colors getColor(){
        return color;
    }
    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }
}
