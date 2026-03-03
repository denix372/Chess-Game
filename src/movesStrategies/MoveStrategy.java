package movesStrategies;

import game.Board;
import model.Position;
import pieces.Piece;

import java.util.ArrayList;

public interface MoveStrategy {
    ArrayList<Position> getPossibleMoves(Board board, Piece piece);
}
