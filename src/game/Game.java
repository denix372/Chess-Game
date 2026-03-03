package game;
import exceptions.InvalidCommandException;
import exceptions.InvalidMoveException;
import gui.Main2;
import model.*;
import pieces.*;
import scoresStrategies.RegularScoreStrategy;
import scoresStrategies.ScoreStrategy;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private boolean active;
    private boolean paused;
    private boolean checkMate;
    private int index; // daca 0 zero e randul playerului si 1 in contrar
    private long id;
    private Scanner sc;
    private Board board;
    private Player player, computer;
    private ArrayList<Move> moves = new ArrayList<>();
    private int lastMatchScore = 0;

    // 3. Folosim Strategy Pattern pentru calcularea puncatajului
    private ScoreStrategy scoreStrategy;

    // 4. Folosim o lista de observatori pentru a implementa Observer Pattern
    private ArrayList<GameObserver> observers = new ArrayList<>();

    public Game(Player player, ScoreStrategy strategy) {
        this.board = new Board();
        this.player = player;
        // 3. Atribuim fiecarui joc o modalitate de calculare
        this.scoreStrategy = strategy;

        if (player.getColor() == Colors.WHITE) {
            computer = new Player("Computer", Colors.BLACK);
            index = 0;
        } else {
            computer = new Player("Computer", Colors.WHITE);
            index = 1;
        }

        this.active = false;
        this.paused = false;

        // setam piesele jucatorilor
        player.setOwnedPieces(board.getChessBoard());
        computer.setOwnedPieces(board.getChessBoard());
    }

    public Game() {
        // default player1 e cu alb, el incepe primul
        this.board = new Board();
        this.player = new Player("Player", Colors.WHITE);
        this.computer = new Player("Computer", Colors.BLACK);
        this.scoreStrategy = new RegularScoreStrategy();
        index = 0;
        player.setOwnedPieces(board.getChessBoard());
        computer.setOwnedPieces(board.getChessBoard());
    }


    public void start() {
        board = new Board();
        moves.clear();
        setActive(true);
        setPaused(false);
        setCheckMate(false);

        player.setOwnedPieces(board.getChessBoard());
        computer.setOwnedPieces(board.getChessBoard());

        if (getSc() == null)
            throw new IllegalStateException("Scanner not set in Game");
        resume();
    }

    public void resume() {
        setPaused(false);

        if(!getActive()) {
            System.out.println("Acest joc s-a terminat");
            return;
        }

        if (this.sc == null) {
            System.out.println("Scanner nu a fost setat");
            return;
        }

        Player current;
        System.out.println(board.toString(player.getColor()));

        // jocul in sine, ruleaza pana cand avem sah mat, remiza sau pauza
        while (getActive() && !getPaused()) {
            if (index == 0)
                current = player;
            else
                current = computer;
            if(checkGame(current))
                return; // jocul terminat

            if(current == player)
                playerTurn(getSc());
            else
                computerTurn();
        }

        if (paused) {
            System.out.println("Joc pus pe pauza. Id-ul lui este: " + getId());
        }
    }

    public boolean checkGame(Player currentPlayer) {
        Colors color = currentPlayer.getColor();
        boolean inCheck = board.isKingInCheck(color);
        boolean hasMoves = board.hasAnyValidMoves(color);

        if (inCheck && !hasMoves) { // s-a terminat prin sah mat
            System.out.println("Sah Mat " + currentPlayer.getName() + " a piedut");
            setActive(false);
            setCheckMate(true);

            boolean playerWon = (currentPlayer == computer);
            int bonus = scoreStrategy.calculateEndGameScore(true, playerWon, false);
            player.setPoints(player.getPoints() + bonus);
            // punctele obtinute din capturi + jocul
            this.lastMatchScore = player.getPoints();
            // 4. notificam observatorii
            notifyGameFinished();
            return true;
        }

        if (!inCheck && !hasMoves || isThreefold()) {
            setActive(false);
            int draw = scoreStrategy.calculateEndGameScore(false, false, true);
            player.setPoints(player.getPoints() + draw);
            this.lastMatchScore = player.getPoints();

            System.out.println("Remiza! Puncte acordate: " + lastMatchScore);

            // 4. notificam observatorii
            notifyGameFinished();
            return true;
        }

        if (inCheck)
            if (currentPlayer == player)
                System.out.println("\nESTI IN SAH!");
            else
                System.out.println("\nADVERSARUL E IN SAH!");

        setCheckMate(false);
        return false;
    }

    public void playerTurn(Scanner sc) {
        printMenu();
        System.out.print("Optiune: ");
        String choice = sc.nextLine().trim();
        if (choice.isEmpty())
            return;

        try {
            if(choice.equals("1")) {
                System.out.println("Introdu pozitia piese (ex: B2): ");
                String input = sc.nextLine().trim().toUpperCase();

                if (input.length() < 2)
                    throw new InvalidCommandException("Format gresit pentru piesa! Foloseste formatul B2");

                try {
                    Position pos = new Position(input);
                    Piece piece = board.getPieceAt(pos);

                    if (piece != null && piece.getColor() == player.getColor())
                        System.out.println("Mutari posibile:" + piece.getPossibleMoves(board));
                    else
                        System.out.println("nu exista piesa de culoarea ta la aceasta pozitie");
                } catch (Exception e) {
                   throw new InvalidCommandException("Pozitie invalida");
                }
            } else if(choice.equals("2")) {
                System.out.println("Introdu o mutare (ex: B2-B3): ");

                String[] parts = sc.nextLine().trim().toUpperCase().split("-");
                Position from = new Position(parts[0].trim());
                Position to = new Position(parts[1].trim());

                if (parts.length != 2)
                    throw new InvalidCommandException("Mutare invalida! Foloseste formatul A1-B2");

                Piece p = board.getPieceAt(from);

                if(p == null || p.getColor() != player.getColor())
                    throw new InvalidCommandException("Nu exista piesa de culoarea ta pe acea pozitie");

                if (board.isValidMove(from, to)) { // daca nu e valida arunca exceptie
                    addMove(player, from, to);
                    player.makeMove(from, to, board);

                    // pawn promotion, pe ultimele randuri ale tablei 1 sau 8
                    Piece moved = board.getPieceAt(to);
                    if (moved instanceof Pawn) {
                        Pawn pawn = (Pawn) moved;
                        if (moved.getPosition().getY() == 8 && pawn.getColor() == Colors.WHITE ||
                            moved.getPosition().getY() == 1 && pawn.getColor() == Colors.BLACK)
                            playerPawnPromotion(pawn, sc);
                    }

                    // actualizam piesele pentru ca si-au schimbat pozitiile
                    player.setOwnedPieces(board.getChessBoard());
                    computer.setOwnedPieces(board.getChessBoard());

                    System.out.println(board.toString(player.getColor()));
                    switchPlayer();
                }

            } else if (choice.equals("3")) {
                System.out.println("Ai renuntat.");
                player.setPoints(player.getPoints() - 150);
                setActive(false);
            } else if (choice.equals("4")) {
                System.out.println("Pauza.");
                setPaused(true);
            } else {
                throw new InvalidCommandException("Optiune de meniu invalida");
            }
        } catch (InvalidCommandException e ) {
            System.out.println("EROARE COMANDA: " + e.getMessage());
        } catch (InvalidMoveException e) {
            System.out.println("MUTARE INVALIDA: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("EROARE: Pozitia invalida");
        } catch (Exception e) {
            System.out.println("EROARE: " + e.getMessage());
        }
    }

    public void computerTurn() {
        // actualizam piesele inainte de a calcula
        player.setOwnedPieces(board.getChessBoard());
        computer.setOwnedPieces(board.getChessBoard());

        // cautam mutarile legale posibile si alegem una random
        ArrayList<Piece> copied = new ArrayList<>();
        for (ChessPair<Position, Piece> pair : board.getChessBoard()) {
            if (pair.getValue().getColor() == computer.getColor()) {
                copied.add(pair.getValue());
            }
        }

        Random rand = new Random();
        boolean moveFound = false;

        while (!copied.isEmpty() && !moveFound) {
            // alegem o piesa random din lista ramasa
            int randomIndex = rand.nextInt(copied.size());
            Piece candidatePiece = copied.get(randomIndex);

            // calculam mutarile valide doar pentru aceasta piesa
            ArrayList<Position> validMoves = new ArrayList<>();
            for (Position target : candidatePiece.getPossibleMoves(board)) {
                try {
                    if (board.isValidMove(candidatePiece.getPosition(), target))
                        validMoves.add(target);
                } catch (InvalidMoveException e) {
                    // ignoram mutarile invalide
                }
            }

            if (!validMoves.isEmpty()) {
                // alegem o mutare random din cele valide
                Position to = validMoves.get(rand.nextInt(validMoves.size()));
                Position from = candidatePiece.getPosition();
                System.out.println("Computerul muta: " + candidatePiece.type() + " " + from + "-" + to);

                addMove(computer, from, to);
                computer.makeMove(from, to, board);

                // verificare promovare pion
                Piece moved = board.getPieceAt(to);
                if (moved instanceof Pawn) {
                    Pawn pawn = (Pawn) moved;
                    if (moved.getPosition().getY() == 8 && pawn.getColor() == Colors.WHITE ||
                            moved.getPosition().getY() == 1 && pawn.getColor() == Colors.BLACK)
                        computerPawnPromotion(pawn);
                }
                moveFound = true;

            } else {
                // daca piesa nu are mutari valide, o eliminam sa alegem alta
                copied.remove(randomIndex);
            }
        }

        player.setOwnedPieces(board.getChessBoard());
        computer.setOwnedPieces(board.getChessBoard());

        System.out.println(board.toString(player.getColor()));
        switchPlayer();
    }

    public void playerPawnPromotion(Pawn pawn, Scanner sc) {
        if(board.getPieceAt(pawn.getPosition()) != pawn)
            return;

        System.out.println("\n === PROMOVARE PION! ===");
        System.out.println("Alege piesa cu care vrei sa o inlocuiesti:");
        System.out.println("1. Regina");
        System.out.println("2. Nebun");
        System.out.println("3. Tura");
        System.out.println("4. Cal");

        Piece piece = null;
        boolean valid = false;

        while (!valid) {
            System.out.println("Optiune:");
            String choice =sc.nextLine().trim();

            // 2. Utilizam Factory pattern
            if (choice.equals("1")) {
                piece = PieceFactory.factory('Q', pawn.getColor(), pawn.getPosition());
                valid = true;
            } else if (choice.equals("2")) {
                piece = PieceFactory.factory('B', pawn.getColor(), pawn.getPosition());
                valid = true;
            } else if (choice.equals("3")) {
                piece = PieceFactory.factory('R', pawn.getColor(), pawn.getPosition());
                valid = true;
            } else if (choice.equals("4")) {
                piece = PieceFactory.factory('N', pawn.getColor(), pawn.getPosition());
                valid = true;
            } else {
                System.out.println("Optiune invalida");
            }
        }

        board.pawnPromotion(pawn, piece);
        player.setOwnedPieces(board.getChessBoard());
        computer.setOwnedPieces(board.getChessBoard());
    }

    public void computerPawnPromotion(Pawn pawn) {
        if(board.getPieceAt(pawn.getPosition()) != pawn)
            return;

        Random rand = new Random();
        int choice = rand.nextInt(3);
        Piece piece = null;
        if (choice == 0)
            piece = new Queen(pawn.getColor(),pawn.getPosition());
        else if (choice == 1)
            piece = new Bishop(pawn.getColor(),pawn.getPosition());
        else if (choice == 2)
            piece = new Rook(pawn.getColor(),pawn.getPosition());
        else if (choice == 3)
            piece = new Knight(pawn.getColor(),pawn.getPosition());

        if (piece != null) {
            board.pawnPromotion(pawn, piece);
            player.setOwnedPieces(board.getChessBoard());
            computer.setOwnedPieces(board.getChessBoard());
        }
    }

    public boolean checkForCheckMate() {
        return getCheckMate();
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public void setCheckMate(boolean checkMate) {
        this.checkMate = checkMate;
    }

    public boolean isThreefold() {
        if(moves.size() <= 8)
            return false;

        Move last1 = moves.getLast();
        Move last2 = moves.get(moves.size() - 2);
        Move last5 = moves.get(moves.size() - 5);
        Move last6 = moves.get(moves.size() - 6);

        return last1.getFrom().equals(last5.getFrom()) &&
                last1.getTo().equals(last5.getTo()) &&
                last2.getFrom().equals(last6.getFrom()) &&
                last2.getTo().equals(last6.getTo());
    }

    public void switchPlayer() {
        if(getIndex() == 0) setIndex(1);
        else setIndex(0);

        // 4. Notificam folsind design Pattern ul
        if(getIndex() == 0) notifySwitch(player);
        else notifySwitch(computer);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getComputer() {
        return computer;
    }

    public void setComputer(Player computer) {
        this.computer = computer;
    }

    public void setSc(Scanner sc) {
        this.sc = sc;
    }

    public Scanner getSc() {
        return sc;
    }

    public int getLastMatchScore() {
        return this.lastMatchScore;
    }

    public void addMove(Player p, Position from, Position to) {
        Piece captured = board.getPieceAt(to);
        // 3. Folosim Strategy pattern ul creat
        // 4. Si notificam observatorii

        if (captured != null) {
            // 4. Observer
            notifyCapture(captured);
            if (p == player) {
                // 3. Strategy
                int capturePoints = scoreStrategy.calculateCaptureScore(captured);
                player.setPoints(player.getPoints() + capturePoints);
            }
        }
        Move move = new Move(p.getColor(), from, to, captured);
        moves.add(move);

        // 4. Trigger pentru Move Observer
        notifyMove(move);
    }

    public void printMenu() {
        System.out.println("Optiuni joc:");
        System.out.println("1. afiseaza miscari posibile pentru o piesa selectata");
        System.out.println("2. fa o mutare");
        System.out.println("3. renunta la joc (-150 puncte)");
        System.out.println("4. pune pauza la joc, ai grija sa retii id-ul jocului");
    }

    // 4. Implementam metodlee specifice Observer Pattern
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    // 4. Metode de notify
    private void notifyMove(Move move) {
        for (GameObserver o : observers)
            o.onMoveMade(move);
    }

    private void notifyCapture(Piece piece) {
        for (GameObserver o : observers)
            o.onPieceCaptured(piece);
    }

    private void notifySwitch(Player current) {
        for (GameObserver o : observers)
            o.onPlayerSwitch(current);
    }

    private void notifyGameFinished() {
        for (GameObserver o : observers) {
            o.onGameFinished(this);
        }
    }

    public void printGameMoves() {
        for(Move move : moves)
            System.out.println(move);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Game game = (Game) o;
        return id == game.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
