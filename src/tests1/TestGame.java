package tests1;

import game.Game;
import game.Player;
import model.Colors;
import scoresStrategies.RegularScoreStrategy;

import java.util.Scanner;

public class TestGame {
    public static void main(String[] args) {

        Player player1 = new Player("Denis", Colors.WHITE);
        // TESTARE CLASA GAME
        Game g = new Game(player1, new RegularScoreStrategy());
        g.setSc(new Scanner(System.in));
        g.setId(1);
        g.start();

        // verificam logica resume
        g.setActive(true);
        g.resume();

    }
}
