package model;
import game.Game;

import java.util.ArrayList;

public class User {
    private String email;
    private String password;
    private ArrayList<Game> games;
    private int points;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        games = new ArrayList<>();
        points = 0;
    }

    public User() {
        this(null, null);
    }

    public void addGame(Game game) {
        if (game != null && !games.contains(game)) {
            games.add(game);
        }
    }

    public void removeGame(Game game) {
        games.remove(game);
    }

    public ArrayList<Game> getActiveGames() {
        ArrayList<Game> actives = new ArrayList<>();
        for(Game g : games) {
            if (g.getActive())
                actives.add(g);
        }
        return actives;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return email + " (" + points + " pct)";
    }
}
