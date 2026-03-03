package pieces;
import model.Colors;
import model.Position;

public class PieceFactory {
    // 2. metoda factory o folosim in Board.initialize() pentru a crea piesele
    // + si in parserul json cand cream o tabla noua
    public static Piece factory(char type, Colors color, Position pos) {
        if (type == 'K')
            return new King(color, pos);
        else if (type == 'Q')
            return new Queen(color, pos);
        else if (type == 'R')
            return new Rook(color, pos);
        else if (type == 'B')
            return new Bishop(color, pos);
        else if (type == 'N')
            return new Knight(color, pos);
        return new Pawn(color, pos);
    }
}
