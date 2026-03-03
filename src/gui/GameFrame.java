package gui;

import game.*;
import model.*;
import pieces.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameFrame extends JFrame implements GameObserver{
    private final JButton[][] squares = new JButton[8][8];
    private final Game game;
    private Position selectedPosition = null;
    private final boolean isPlayerWhite;

    private JTextArea moveHistoryArea;
    private JPanel capturedWhitePanel;
    private JPanel capturedBlackPanel;
    private JLabel status;

    private final Color lightSquare = new Color(255, 255, 255, 200);
    private final Color darkSquare = new Color(0, 0, 0, 200);
    private final Color highlightColor = new Color(252, 23, 137, 200);

    public GameFrame(Game game) {
        this.game = game;
        this.isPlayerWhite = game.getPlayer().getColor() == Colors.WHITE;

        setTitle("Chess - " + (isPlayerWhite ? "White" : "Black"));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        BackgroundPanel mainPanel = new BackgroundPanel("resources/backgrounds/Game2.png");
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // in west panel avem move history
        setupWestPanel(mainPanel);

        // in jos avem un label care afiseaza mesaje sugestigve
        status = new JLabel("Welcome to Chess!", SwingConstants.CENTER);
        status.setFont(new Font("Arial", Font.BOLD, 18));
        status.setForeground(new Color(25, 16, 99));
        status.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(status, BorderLayout.CENTER);

        // in centru avem tabla de sah
        JPanel boardContainer = new JPanel(new GridBagLayout());
        boardContainer.setOpaque(false);
        initializeGrid(boardContainer);
        mainPanel.add(boardContainer, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        // in east avem piesele capturate
        setupEastPanel(mainPanel);

        setVisible(true);
        refreshBoard();
        updateUIComponents();

        if (game.getIndex() == 1)
            checkAndProcessTurn();
    }

    // 3.5 Observer pentru updatarea statusului
    @Override
    public void onMessageReceived(String message) {
        setStatusMessage(message);
    }

    @Override
    public void onMoveMade(Move move) {
        refreshBoard();
    }

    @Override
    public void onPieceCaptured(Piece piece) {
        setStatusMessage("Captured: " + piece.type());
    }

    @Override
    public void onPlayerSwitch(Player currentPlayer) {
        if (game.getBoard().isKingInCheck(currentPlayer.getColor()))
            setStatusMessage("ALERT: " + currentPlayer.getName() + " is in CHECK!");
        else if (currentPlayer.getName().equalsIgnoreCase("Computer"))
            setStatusMessage("Next move: Computer");
        else
            setStatusMessage("Your turn!");

    }

    @Override
    public void onGameFinished(Game game) {
        // GameLogger are deja implementata metoda
    }

    private void setupWestPanel(JPanel mainContainer) {
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setPreferredSize(new Dimension(250, 0));
        westPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Move History", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        westPanel.add(titleLabel, BorderLayout.NORTH);

        moveHistoryArea = new TransparentTextArea();
        moveHistoryArea.setEditable(false);
        moveHistoryArea.setForeground(Color.WHITE);
        moveHistoryArea.setFont(new Font("Arial", Font.BOLD, 16));
        moveHistoryArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(moveHistoryArea);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        westPanel.add(scroll, BorderLayout.CENTER);
        mainContainer.add(westPanel, BorderLayout.WEST);
    }

    private void setupEastPanel(JPanel mainContainer) {
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.setPreferredSize(new Dimension(250, 0));
        eastPanel.setOpaque(false);

        JPanel topEast = new JPanel();
        topEast.setLayout(new BoxLayout(topEast, BoxLayout.Y_AXIS));
        topEast.setOpaque(false);

        JLabel blackTitle = new JLabel("Black Captured", SwingConstants.CENTER);
        blackTitle.setForeground(Color.WHITE);
        blackTitle.setFont(new Font("Arial", Font.BOLD, 18));
        blackTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        blackTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        capturedBlackPanel = new TransparentPanel(new FlowLayout(FlowLayout.LEFT));
        capturedBlackPanel.setPreferredSize(new Dimension(230, 400));
        capturedBlackPanel.setMaximumSize(new Dimension(230, 400));

        JLabel whiteTitle = new JLabel("White Captured", SwingConstants.CENTER);
        whiteTitle.setForeground(Color.WHITE);
        whiteTitle.setFont(new Font("Arial", Font.BOLD, 18));
        whiteTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        whiteTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));

        capturedWhitePanel = new TransparentPanel(new FlowLayout(FlowLayout.LEFT));
        capturedWhitePanel.setPreferredSize(new Dimension(230, 400));
        capturedWhitePanel.setMaximumSize(new Dimension(230, 400));

        // Adaugam elementele in ordine, exact ca in West Panel
        topEast.add(blackTitle);
        topEast.add(capturedBlackPanel);
        topEast.add(whiteTitle);
        topEast.add(capturedWhitePanel);

        JPanel bottomEast = new JPanel(new GridLayout(3, 1, 5, 5));
        bottomEast.setOpaque(false);
        bottomEast.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        Dimension fixedSize = new Dimension(300, 50);
        Font buttonFont = new Font("Arial", Font.BOLD, 17);

        JButton resignBtn = new JButton("Resign");
        resignBtn.setBackground(new Color(148, 11, 195));
        resignBtn.setForeground(new Color(255,255,255));
        resignBtn.setPreferredSize(fixedSize);
        resignBtn.setMaximumSize(fixedSize);
        resignBtn.setMinimumSize(fixedSize);
        resignBtn.setFont(buttonFont);

        JButton saveBtn = new JButton("Save & Exit");
        saveBtn.setBackground(new Color(253, 199, 91));
        saveBtn.setForeground(new Color(25, 16, 99));
        saveBtn.setPreferredSize(fixedSize);
        saveBtn.setMaximumSize(fixedSize);
        saveBtn.setMinimumSize(fixedSize);
        saveBtn.setFont(buttonFont);

        JButton menuBtn = new JButton("Back to Menu");
        menuBtn.setBackground(new Color(252, 23, 137));
        menuBtn.setForeground(new Color(255,255,255));
        menuBtn.setPreferredSize(fixedSize);
        menuBtn.setMaximumSize(fixedSize);
        menuBtn.setMinimumSize(fixedSize);
        menuBtn.setFont(buttonFont);

        JFrame frameReference = this;

        resignBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(frameReference, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    game.getPlayer().setPoints(- 150);
                    game.setActive(false);

                    Main2.getInstance().handleGameEnd(game);
                    frameReference.dispose();
                }
            }
        });

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.setPaused(true);
                game.setActive(true);
                Main2.getInstance().saveActiveGame(game);
                JOptionPane.showMessageDialog(frameReference, "Jocul #" + game.getId() + " a fost salvat!");
                frameReference.dispose();
                Main2.getInstance().showMenu();
            }
        });

        menuBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameReference.dispose();
                Main2.getInstance().showMenu();
            }
        });

        bottomEast.add(resignBtn);
        bottomEast.add(saveBtn);
        bottomEast.add(menuBtn);

        eastPanel.add(topEast, BorderLayout.CENTER);
        eastPanel.add(bottomEast, BorderLayout.SOUTH);

        mainContainer.add(eastPanel, BorderLayout.EAST);
    }

    private void updateUIComponents() {
        moveHistoryArea.setText("");
        ArrayList<Move> moves = game.getMoves();

        for (int i = 0; i < moves.size(); i++) {
            Move m = moves.get(i);
            String symbol = (m.getColor() == Colors.WHITE) ? "●" : "○";
            moveHistoryArea.append(String.format("%d. %s %s → %s\n",
                    i + 1, symbol, m.getFrom(), m.getTo()));
        }

        capturedWhitePanel.removeAll();
        capturedBlackPanel.removeAll();

        for (Move m : game.getMoves()) {
            if (m.getCaptured() != null) {
                JLabel pieceLabel = new JLabel(getUTFSymbol(m.getCaptured()));
                pieceLabel.setFont(new Font("Serif", Font.PLAIN, 24));
                pieceLabel.setForeground(Color.WHITE);

                // daca o piese alba e capturata o afisam pe panoul pieselor negre
                // si vice verse
                if (m.getCaptured().getColor() == Colors.BLACK)
                    capturedWhitePanel.add(pieceLabel);
                else
                    capturedBlackPanel.add(pieceLabel);

            }
        }
        capturedWhitePanel.revalidate();
        capturedBlackPanel.revalidate();
        repaint();
    }

    private String getUTFSymbol(Piece p) {
        if (p instanceof King)
            return p.getColor() == Colors.WHITE ? "♔" : "♚";
        if (p instanceof Queen)
            return p.getColor() == Colors.WHITE ? "♕" : "♛";
        if (p instanceof Rook)
            return p.getColor() == Colors.WHITE ? "♖" : "♜";
        if (p instanceof Bishop)
            return p.getColor() == Colors.WHITE ? "♗" : "♝";
        if (p instanceof Knight)
            return p.getColor() == Colors.WHITE ? "♘" : "♞";
        return p.getColor() == Colors.WHITE ? "♙" : "♟";
    }

    class BackgroundPanel extends JPanel {
        private Image img;
        public BackgroundPanel(String path) {
            this.img = new ImageIcon(path).getImage();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void handleGameCompletion(boolean playerWon) {
        int points = game.getPlayer().getPoints();

        Main2.getInstance().handleGameEnd(game);

        new WinFrame(playerWon, points,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Main2.getInstance().startNewGameGUI();
                    }
                },
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Main2.getInstance().showMenu();
                    }
                }
        );
        this.dispose();
    }

    private void initializeGrid(JPanel container) {
        JPanel boardPanel = new JPanel(new GridLayout(8, 8, 5,5));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        boardPanel.setPreferredSize(new Dimension(830, 830));
        boardPanel.setMinimumSize(new Dimension(830, 830));
        boardPanel.setMaximumSize(new Dimension(830, 830));
        boardPanel.setOpaque(false);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton btn = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        g.setColor(getBackground());
                        g.fillRect(0, 0, getWidth(), getHeight());
                        super.paintComponent(g);
                    }
                };

                btn.setMargin(new Insets(0, 0, 0, 0));
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setContentAreaFilled(false);
                btn.setOpaque(false);

                if ((row + col) % 2 == 0) btn.setBackground(lightSquare);
                else btn.setBackground(darkSquare);

                final int r = row;
                final int c = col;
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleSquareClick(r, c);
                    }
                });

                squares[row][col] = btn;
                boardPanel.add(btn);
            }
        }
        container.add(boardPanel);
    }

    private Position getPositionFromGrid(int row, int col) {
        char file;
        int rank;
        // inversam coloanele daca jucatorul este cu Negru
        if (isPlayerWhite) {
            file = (char)('A' + col);
            rank = 8 - row;
        } else {
            file = (char)('H' - col);
            rank = 1 + row;
        }
        return new Position(file, rank);
    }

    public void refreshBoard() {
        Board board = game.getBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position logicalPos = getPositionFromGrid(row, col);
                Piece p = board.getPieceAt(logicalPos);

                if (p != null) {
                    String colorPrefix = (p.getColor() == Colors.WHITE) ? "White" : "Black";
                    String typeName = p.getClass().getSimpleName();
                    String imagePath = "resources/pieces2/" + colorPrefix + typeName + "-01.png";

                    ImageIcon icon = new ImageIcon(imagePath);

                    Image img = icon.getImage();
                    Image fixedImage = img.getScaledInstance(100, 100, Image.SCALE_DEFAULT);

                    squares[row][col].setIcon(new ImageIcon(fixedImage));
                    squares[row][col].setHorizontalAlignment(SwingConstants.CENTER);
                    squares[row][col].setVerticalAlignment(SwingConstants.CENTER);
                } else {
                    squares[row][col].setIcon(null);
                }
            }
        }
        updateUIComponents();
        getContentPane().repaint();
    }
    private void handleSquareClick(int row, int col) {
        // blocam input-ul daca este randul calculatorului
        if (game.getIndex() == 1) {
            setStatusMessage("Wait for computer to finish!");
            return;
        }

        Position clickedPos = getPositionFromGrid(row, col);
        Player currentPlayer = game.getPlayer();

        // daca o piesa nu a fost capturata
        if (selectedPosition == null) {
            Piece p = game.getBoard().getPieceAt(clickedPos);
            if (p != null && p.getColor() == currentPlayer.getColor()) {
                selectedPosition = clickedPos;

                // o facem highlight-uim
                squares[row][col].setBackground(highlightColor);
                showPossibleMoves(p);
            }
            return;
        }

        // daca piesa a fost selectata incercam sa facem mutarea
        try {
            if (game.getBoard().isValidMove(selectedPosition, clickedPos)) {
                game.addMove(currentPlayer, selectedPosition, clickedPos);
                currentPlayer.makeMove(selectedPosition, clickedPos, game.getBoard());

                // implementare pawn promotion
                Piece movedPiece = game.getBoard().getPieceAt(clickedPos);
                if (movedPiece instanceof Pawn) {
                    Pawn pawn = (Pawn) movedPiece;
                    if ((pawn.getColor() == Colors.WHITE && clickedPos.getY() == 8) ||
                            (pawn.getColor() == Colors.BLACK && clickedPos.getY() == 1)) {
                        handleGuiPawnPromotion(pawn);
                    }
                }

                refreshBoard();
                selectedPosition = null;
                resetHighlights();
                game.switchPlayer();
                checkAndProcessTurn();
                setStatusMessage("Next move: Computer");
            } else {
                //deselectam patratele invalide
                setStatusMessage("Invalid move!");
                selectedPosition = null;
                resetHighlights();
            }
        } catch (Exception ex) {
            setStatusMessage("Invalid command: " + ex.getMessage());
            selectedPosition = null;
            resetHighlights();
        }
    }

    private void showPossibleMoves(Piece p) {
        ArrayList<Position> moves = p.getPossibleMoves(game.getBoard());

        for (Position target : moves) {
            try {
                // folosim isValidMove sa aflam daca regele e in sah
                if (game.getBoard().isValidMove(p.getPosition(), target)) {
                    int[] gridCoords = getGridFromPosition(target);
                    squares[gridCoords[0]][gridCoords[1]].setBackground(highlightColor);
                }
            } catch (Exception e) {
                // ignoram daca miscarea e invalida
            }
        }
    }

    public int[] getGridFromPosition(Position pos) {
        int row, col;
        if (isPlayerWhite) {
            row = 8 - pos.getY();
            col = pos.getX() - 'A';
        } else {
            row = pos.getY() - 1;
            col = 'H' - pos.getX();
        }
        return new int[]{row, col};
    }

    private void resetHighlights() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if ((r + c) % 2 == 0) squares[r][c].setBackground(lightSquare);
                else squares[r][c].setBackground(darkSquare);
            }
        }
    }

    private void handleGuiPawnPromotion(Pawn pawn) {
        String colorPrefix = (pawn.getColor() == Colors.WHITE) ? "White" : "Black";
        String[] pieceTypes = {"Queen", "Rook", "Bishop", "Knight"};
        ImageIcon[] icons = new ImageIcon[4];

        for (int i = 0; i < pieceTypes.length; i++) {
            String path = "resources/PawnPromotions/" + colorPrefix + pieceTypes[i] + ".png";
            try {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                icons[i] = new ImageIcon(img);
            } catch (Exception e) {
                System.err.println("Could not find promotion image at: " + path);
                icons[i] = null;
            }
        }

        // afisam Dialog Custom cu imaginile pieselor pentru alegere
        int choice = JOptionPane.showOptionDialog( this, "", "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, icons, icons[0]
        );

        Piece newPiece;
        if (choice == 1)
            newPiece = new Rook(pawn.getColor(), pawn.getPosition());
        else if (choice == 2)
            newPiece = new Bishop(pawn.getColor(), pawn.getPosition());
        else if (choice == 3)
            newPiece = new Knight(pawn.getColor(), pawn.getPosition());
        else
            newPiece = new Queen(pawn.getColor(), pawn.getPosition());

        game.getBoard().pawnPromotion(pawn, newPiece);
    }

    private void checkAndProcessTurn() {
        Player nextPlayer = (game.getIndex() == 0) ? game.getPlayer() : game.getComputer();

        // verificare daca s-a terminat jocul sah mat sau remiza sau threefold
        if (game.checkGame(nextPlayer)) {
            boolean playerWon = (game.getIndex() == 1);
            int finalPoints = game.getLastMatchScore();

            Main2.getInstance().handleGameEnd(game);

            new WinFrame(playerWon, finalPoints,
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e2) {
                            Main2.getInstance().startNewGameGUI();
                        }
                    },
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e2) {
                            Main2.getInstance().showMenu();
                        }
                    }
            );

            this.dispose();
            return;
        }

        // daca e randul calculatorului, declansam mutarea dupa un scurt delay
        // sa aiba timp observatorul sa afiseze "computer turn"
        if (game.getIndex() == 1) {
            Timer timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    game.computerTurn();
                    refreshBoard();
                    checkAndProcessTurn();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    // metoda pentru afisarea mesajelor
    public void setStatusMessage(String message) {
        status.setText(message);
        if (message.contains("Invalid")) {
            Timer timer = new Timer(3000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    status.setText(" ");
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    // am creat o clase interna pentru zone trasparente
    class TransparentPanel extends JPanel {
        public TransparentPanel(LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 150)); // Fundal negru semi-transparent
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class TransparentTextArea extends JTextArea {
        public TransparentTextArea() {
            setOpaque(false);
        }


        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void runGUI(Game game) {
        new GameFrame(game);
    }
}