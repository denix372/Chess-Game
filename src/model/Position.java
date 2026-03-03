package model;

import exceptions.InvalidPositionException;

public class Position implements Comparable<Position> {
    // Pozitiile de sah x = A - H, y = 1 - 8
    private char x;
    private int y;

    public Position() {
       this('A', 1);
    }

    public Position(char x, int y) {
        if(!(x >= 'A' && x <= 'H' && y >= 1 && y <= 8))
            throw new InvalidPositionException("Pozitie invalida!");
        this.x = x;
        this.y = y;
    }

    public Position(String positionString) {
        if (positionString == null || positionString.length() < 2)
            throw new IllegalArgumentException("Position string must be at least two characters (e.g., 'A1').");

        this.x = Character.toUpperCase(positionString.charAt(0));

        try {
            String rankString = positionString.substring(1);
            this.y = Integer.parseInt(rankString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Position string rank must be an integer: " + positionString, e);
        }

        if (!this.isOnBoard())
            throw new IllegalArgumentException("Position is off-board: " + positionString);
    }

    public char getX() {
        return x;
    }

    public void setX(char x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Character getXNext() {
        if(x == 'A')
            return 'B';
        else if(x == 'B')
            return 'C';
        else if(x == 'C')
            return 'D';
        else if(x == 'D')
            return 'E';
        else if(x == 'E')
            return 'F';
        else if(x == 'F')
            return 'G';
        else if(x == 'G')
            return 'H';
        return null;
    }

    public Character getXPrev() {
        if(x == 'B')
            return 'A';
        else if(x == 'C')
            return 'B';
        else if(x == 'D')
            return 'C';
        else if(x == 'E')
            return 'D';
        else if(x == 'F')
            return 'E';
        else if(x == 'G')
            return 'F';
        else if(x == 'H')
            return 'G';
        return null;
    }


    public boolean equals(Object o) {
        //https://stackoverflow.com/questions/16497471/casting-in-equals-method
        if( !(o instanceof Position))
            return false;
        Position pos = (Position)o;
        return Character.compare(getX(), pos.getX()) == 0
                && Integer.compare(getY(), pos.getY()) == 0;
    }

    public boolean isOnBoard() {
        return getX() >= 'A' && getX() <= 'H' && getY() >= 1 && getY() <= 8;
    }


    public boolean isOnBoard(Position p) {
        return p.getX() >= 'A' && p.getX() <= 'H' && p.getY() >= 1 && p.getY() <= 8;
    }

    @Override
    public String toString() {
        return getX() + Integer.toString(getY());
    }

    @Override
    public int compareTo(Position p) {
        if( Integer.compare(getY(), p.getY()) != 0)
            return Integer.compare(getY(), p.getY());
        return Character.compare(getX(), p.getX());
    }
}
