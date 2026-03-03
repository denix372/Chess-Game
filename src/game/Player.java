package game;

import exceptions.InvalidMoveException;
import model.*;
import pieces.*;
import java.util.ArrayList;
import java.util.TreeSet;

public class Player {
    private String name;
    private Colors color;
    private ArrayList<Piece> capturedPieces = new ArrayList<>();
    private TreeSet<ChessPair<Position, Piece>> ownedPieces = new TreeSet<>();
    private int points;

    public Player(String name, Colors color){
        this.name = name;
        this.color = color;
        setPoints(0);
    }

    public void setOwnedPieces(TreeSet<ChessPair<Position, Piece>> pieces) {
        this.ownedPieces.clear();
        for (ChessPair<Position, Piece> pair : pieces) {
            Piece p = pair.getValue();
            if (p != null && p.getColor() == this.color)
                this.ownedPieces.add(pair);
        }
    }

    public void makeMove(Position from, Position to, Board board) {
        Piece want = board.getPieceAt(from);

        if (want == null )
            throw new InvalidMoveException("Eroare: Nu exista piesa la pozitia respectiva");
        if (want.getColor() != getColor())
            throw new InvalidMoveException("Piesa nu e de culoarea ta");

        ownedPieces.remove(new ChessPair<>(from, want));
        Piece captured = board.getPieceAt(to);
        board.movePiece(from, to);
        ownedPieces.add( new ChessPair<>(to, want));

        if(want instanceof Pawn) {
            ((Pawn)want).setFirstmove(1);
        }

        if(captured != null)
            capturedPieces.add(captured);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public ArrayList<Piece> getCapturedPieces() {
        return capturedPieces;
    }

    public TreeSet<ChessPair<Position, Piece>> getOwnedPieces() {
        return ownedPieces;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
