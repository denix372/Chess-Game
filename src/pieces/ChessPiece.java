package pieces;
import model.*;
import game.*;

import java.util.ArrayList;

public interface ChessPiece {
    // 3. metoda e acm implementata de strategy
    ArrayList<Position> getPossibleMoves(Board board);
    boolean checkForCheck(Board board, Position kingPosition);
    char type();
}
