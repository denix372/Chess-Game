package gui;

import game.*;
import io.*;
import model.*;
import scoresStrategies.RegularScoreStrategy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;
import java.util.*;

public class Main2 {
    private static Main2 obj = null;

    private String USERS_FILE = "resources/accounts.json";
    private String GAMES_FILE = "resources/games.json";
    private List<User> users;
    private Map<Integer, Game> games;
    private User currentUser;
    private Scanner scanner;

    private LoginFrame loginFrame;
    private MenuFrame menuFrame;
    private GameFrame gameFrame;


    // 1. Folosim pattern ul Singleton avand constructorul private
    private Main2() {
        this.users = new ArrayList<>();
        this.games = new HashMap<>();
        this.scanner = new Scanner(System.in);
        read();
        showLogin();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // 1. Folosim Lazy Instantiation pentru Singleton
    public static Main2 getInstance() {
        if (obj == null)
            obj = new Main2();
        return obj;
    }

    public void read() {
        this.games.clear();
        this.users.clear();
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

    public User authenticate(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) return u;
        }
        return null;
    }

    // adaugam un user nou in lista si salvam in fisierul json
    public User register(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                JOptionPane.showMessageDialog(loginFrame, "Account already exists!");
                return null;
            }
        }
        User newUser = new User(email, password);
        users.add(newUser);
        write();
        return newUser;
    }

    public void exitApplication() {
        write();
        System.exit(0);
    }

    // functie care face trecerea de la meniul principal la login
    public void showLogin() {
        if (menuFrame != null) menuFrame.dispose();

        loginFrame = new LoginFrame();

        loginFrame.getSignInButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = loginFrame.getEmail();
                String pass = loginFrame.getPassword();
                User u = authenticate(email, pass);
                if (u != null) {
                    currentUser = u;
                    showMenu();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid Email or Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginFrame.getCreateAccountButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = loginFrame.getEmail();
                String pass = loginFrame.getPassword();
                if (email.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(loginFrame, "Please fill all fields");
                    return;
                }
                User u = register(email, pass);
                if (u != null) {
                    currentUser = u;
                    showMenu();
                }
            }
        });

        loginFrame.setVisible(true);
    }

    public User newAccount(String email, String password) {
        // verificam daca exista contul inainte sa il cream
        for (User u : users)
            if (u.getEmail().equalsIgnoreCase(email)) {
                System.out.println("Account already exists");
                return null;
            }
        User newUser = new User(email, password);
        currentUser = newUser;
        users.add(newUser);
        return newUser;
    }

    // configuram functionaitatea meniului principal dupa logare
    public void showMenu() {
        if (loginFrame != null)
            loginFrame.dispose();
        if (menuFrame != null)
            menuFrame.dispose();

        menuFrame = new MenuFrame(currentUser);

        // cream jocul in functie de culoare
        // 3. instantiem strategiile
        // 4. adaugam observatorii
        menuFrame.getNewGameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"WHITE", "BLACK"};
                int choice = JOptionPane.showOptionDialog(menuFrame, "Choose Color:", "New Game",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (choice == -1)
                    return;

                Colors userColor = (choice == 1) ? Colors.BLACK : Colors.WHITE;
                Player human = new Player(currentUser.getEmail(), userColor);
                Game game = new Game(human, new RegularScoreStrategy());
                game.addObserver(new GameLogger());

                // generam id ul unic
                int nextId;
                if (games.isEmpty())
                    nextId = 1;
                else
                    nextId = Collections.max(games.keySet()) + 1;
                game.setId(nextId);

                games.put(nextId, game);
                currentUser.addGame(game);

                menuFrame.setVisible(false);
                GameFrame gf = new GameFrame(game);
                game.addObserver(gf);
            }
        });

        // continuam jocul utilizatorului si deschidem gereastra de detallii pentru joc
        menuFrame.getContinueGameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Game> activeGames = currentUser.getActiveGames();
                if (activeGames.isEmpty()) {
                    JOptionPane.showMessageDialog(menuFrame, "Nu ai jocuri active.");
                    return;
                }

                String[] gameIds = new String[activeGames.size()];
                for (int i = 0; i < activeGames.size(); i++)
                    gameIds[i] = "ID: " + activeGames.get(i).getId();

                String selected = (String) JOptionPane.showInputDialog(menuFrame, "Selectează jocul:",
                        "Continuă Joc", JOptionPane.QUESTION_MESSAGE, null, gameIds, gameIds[0]);

                if (selected != null) {
                    int id = Integer.parseInt(selected.replace("ID: ", ""));
                    Game g = games.get(id);

                    // aratam panoul de jocuri, implementand interfata noastra
                    new GameDetailFrame(g, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            g.setActive(true);
                            menuFrame.setVisible(false);
                            GameFrame gf = new GameFrame(g);
                            g.addObserver(gf);
                        }
                    });
                }
            }
        });

        menuFrame.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                write();
                currentUser = null;
                showLogin();
            }
        });

        menuFrame.setVisible(true);
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

    public void startNewGameGUI() {
        menuFrame.getNewGameButton().doClick();
    }

    // pentru butonul save & exit salvam jocul in json
    public void saveActiveGame(Game game) {
        this.games.put((int) game.getId(), game);

        if (currentUser != null) {
            if (!currentUser.getGames().contains(game)) {
                currentUser.addGame(game);
            }
        }
        game.setActive(true);
        write();
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
        finalizeGameData(game);
        if (menuFrame != null) {
            menuFrame.dispose();
            showMenu();
        }
    }

    // transferam punctele din joc in profilul utilizatorului si sterge jocul finalizat
    // din lista
    public void finalizeGameData(Game game) {
        int gameId = (int) game.getId();

        if (currentUser != null) {
            int sessionTotal = game.getPlayer().getPoints();
            currentUser.setPoints(currentUser.getPoints() + sessionTotal);
            game.getPlayer().setPoints(0);

            List<Game> userGames = currentUser.getGames();
            for (int i = 0; i < userGames.size(); i++) {
                if (userGames.get(i).getId() == gameId) {
                    userGames.remove(i);
                    break;
                }
            }
        }

        this.games.remove(gameId);
        write();
    }

    public static void main(String[] args) {
        // metoda de preventie pentru elementele UI pe ecrane High-DPI
        System.setProperty("sun.java2d.uiScale", "1.0");
        Main2 app = Main2.getInstance();
        app.read();
        app.run();
    }

}