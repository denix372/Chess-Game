package cli;

import game.*;
import io.*;
import model.*;
import scoresStrategies.RegularScoreStrategy;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static Main obj = null;

    private String USERS_FILE = "resources/accounts.json";
    private String GAMES_FILE = "resources/games.json";
    private List<User> users;
    private Map<Integer, Game> games;
    private User currentUser;
    private Scanner scanner;

    // 1. Folosim pattern ul Singleton avand constructorul private
    private Main() {
        this.users = new ArrayList<>();
        this.games = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }
    // Folosim Lazy Instantiation pentru Singleton
    public static Main getInstance() {
        if (obj == null)
            obj = new Main();
        return obj;
    }

    public void read() {
        games = JsonReaderUtil.readGames(Paths.get(GAMES_FILE));
        users = JsonReaderUtil.readUsers(Paths.get(USERS_FILE), games);
    }

    public void write() {
        JsonWriterUtil.writeUsers(users, USERS_FILE);
        JsonWriterUtil.writeGames(games, GAMES_FILE);
    }

    public User login(String email, String password) {
        for (User u : users)
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                currentUser = u;
                return u;
            }
        return null;
    }

    public User newAccount(String email, String password) {
        // verificam daca exista contul inainte sa il cream
        for (User u : users)
            if (u.getEmail().equalsIgnoreCase(email)) {
                System.out.println("Cont existent.");
                return null;
            }
        User newUser = new User(email, password);
        currentUser = newUser;
        users.add(newUser);
        return newUser;
    }

    public void run() {
        // bluca principala pentru aplicatie
        // daca avem user logat vom afisa meniul propriu alegerea jocului
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                printMainMenu();
                String cmd = scanner.nextLine().trim();

                if (cmd.equals("1"))
                    handleLoginLoop();
                else if (cmd.equals("2"))
                    handleRegister();
                else if (cmd.equals("3"))
                    running = false;
                else
                    System.out.println("Optiune invalida.");

            } else {
                printUserMernu();
                String cmd = scanner.nextLine().trim();

                if (cmd.equals("1")) {
                    startNewGame();
                } else if (cmd.equals("2")) {
                    continueGame();
                } else if (cmd.equals("3")) {
                    currentUser = null;
                    System.out.println("Delogat.");
                } else if (cmd.equals("4")) {
                    running = false;
                } else {
                    System.out.println("Optiune invalida.");
                }
            }
        }
        // salvam datele la iesirea din aplicatie
        write();
    }

    public void printUserMernu() {
        System.out.println("\nSalut " + currentUser.getEmail());
        System.out.println("1. Joc nou");
        System.out.println("2. Continua joc");
        System.out.println("3. Logout");
        System.out.println("4. Exit");
        System.out.print("Optiune: ");
    }

    public void printMainMenu() {
        System.out.println("\n=== MENIU PRINCIPAL ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Optiune: ");
    }

    private void handleLoginLoop() {
        boolean authenticated = false;

        // Userul ramane in acest loop pana reuseste login-ul sau alege sa iasa, asa cum cere in cerinta
        while (!authenticated) {
            System.out.println("\n--- LOGIN  ---");
            System.out.print("Email: ");
            String e = scanner.nextLine().trim();

            System.out.print("Pass: ");
            String p = scanner.nextLine().trim();

            if(login(e, p) != null) {
                System.out.println("Success! Bine ai venit, " + e);
                authenticated = true;
            } else {
                System.out.println("Fail. Email sau parola gresite.");
                System.out.println("1. Revino la meniu");
                System.out.println("2. Incearca din nou");
                System.out.print("Optiune: ");

                String choice = scanner.nextLine().trim();

                if (choice.equals("1"))
                    return;
            }
        }
    }

    private void handleRegister() {
        System.out.print("Email nou: ");
        String e = scanner.nextLine().trim();
        System.out.print("Parola noua: ");
        String p = scanner.nextLine().trim();
        if (newAccount(e, p) != null) {
            System.out.println("Cont creat si autentificat automat!");
        }
    }

    private void startNewGame() {
        System.out.println("\n=== ALEGE CULOAREA ===");
        System.out.println("1. WHITE");
        System.out.println("2. BLACK");
        System.out.print("Optiune: ");

        String colorChoice = scanner.nextLine().trim();
        Colors userColor;

        if (colorChoice.equals("2")) {
            userColor = Colors.BLACK;
            System.out.println("Ai ales NEGRU.");
        } else {
            userColor = Colors.WHITE;
            System.out.println("Ai ales ALB.");
        }

        Player human = new Player(currentUser.getEmail(), userColor);
        Game game = new Game(human, new RegularScoreStrategy());
        // 4. Cream observatorul
        game.addObserver(new GameLogger());

        // generam un id unic pentru joc bazat pe maximul curent
        int nextId = 1;
        if (!games.isEmpty())
            nextId = Collections.max(games.keySet()) + 1;
        game.setId(nextId);

        games.put(nextId, game);
        currentUser.addGame(game);
        // trimitem scanner-ul pentru a citi mutarile din clasa Game
        game.setSc(scanner);
        game.start();

        // verificam starea jocului dupa ce metoda start() se incheie
        handleGameEnd(game);
    }

    private void continueGame() {
        ArrayList<Game> activeGames = currentUser.getActiveGames();
        if(activeGames.isEmpty()) {
            System.out.println("Nu ai jocuri active.");
            return;
        }

        System.out.println("Jocuri active:");
        for(Game g : activeGames)
            System.out.println("ID: " + g.getId() + " vs Computer");


        System.out.print("Introdu ID joc: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Game g = games.get(id);


            // jocul trebuie sa exista si sa apartina userului curent logat
            if (g != null && currentUser.getGames().contains(g)) {
                g.setActive(true);
                g.setSc(this.scanner);
                g.resume();
                handleGameEnd(g);
            } else {
                System.out.println("Joc invalid sau inexistent.");
            }
        } catch (Exception e) {
            System.out.println("Input invalid.");
        }
    }

    public void handleGameEnd(Game game) {
        // daca jocul nu mai e activ (sah mat, remiza etc.) , il stergem
        if (!game.getActive()) {
            System.out.println("\n--- JOC FINALIZAT ---");

            int pointsEarned = game.getPlayer().getPoints();
            int currentTotal = currentUser.getPoints();
            int newTotal = currentTotal + pointsEarned;

            if (newTotal < 0)
                newTotal = 0;

            System.out.println("Puncte anterioare: " + currentTotal);
            System.out.println("Puncte din acest joc: " + pointsEarned);
            System.out.println("Total nou: " + newTotal);
            game.printGameMoves();


            currentUser.setPoints(newTotal);
            // stergem jocul din map ul global si din lista
            currentUser.removeGame(game);
            games.remove((int)game.getId());

            System.out.println("Jocul a fost sters din memorie.");
        } else {
            System.out.println("Jocul a fost salvat (Pauza).");
        }
    }

    public static void main(String[] args) {
        Main app = Main.getInstance();
        app.read();
        app.run();
    }

}