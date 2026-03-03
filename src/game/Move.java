package game;

import model.*;
import pieces.*;

public class Move {
    private Colors color;
    private Position from, to;
    private Piece captured;

    public Piece getCaptured() {
        return captured;
    }

    public void setCaptured(Piece captured) {
        this.captured = captured;
    }

    public Move(Colors color, Position from, Position to, Piece captured) {
        this.color = color;
        this.from = from;
        this.to = to;
        this.captured = captured;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    @Override
    public String toString() {
        return color + ": " + from + " -> " + to + " => " + captured;
    }
}
