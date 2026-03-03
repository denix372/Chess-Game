package gui;

import model.User;
import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JFrame {
    private User user;
    private JButton newGameButton;
    private JButton continueGameButton;
    private JButton logoutButton;

    public MenuFrame(User user) {

        this.user = user;
        setTitle("Chess Master - cli.Main Menu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainContent = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("resources/backgrounds/Menu-01.png");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        // bara de sus user profile
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setOpaque(false);

        // cream un icon cu initiala userului
        JLabel profileIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // in forma de cerc
                g2.setColor(new Color(70, 130, 180));
                g2.fillOval(0, 0, 50, 50);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 24));
                String initial = user.getEmail().substring(0, 1).toUpperCase();
                FontMetrics fm = g2.getFontMetrics();
                int x = (50 - fm.stringWidth(initial)) / 2;
                int y = ((50 - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(initial, x, y);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(55, 55);
            }
        };

        topBar.add(profileIcon);
        mainContent.add(topBar, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        statsPanel.setOpaque(false);

        JLabel pointsLabel = createStyledLabel("Points: " + user.getPoints());
        JLabel gamesLabel = createStyledLabel("Active Games: " + user.getActiveGames().size());

        statsPanel.add(pointsLabel);
        statsPanel.add(gamesLabel);

        newGameButton = createMenuButton("NEW GAME");
        continueGameButton = createMenuButton("CONTINUE GAME");
        logoutButton = createMenuButton("LOGOUT");

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(statsPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        centerPanel.add(newGameButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(continueGameButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(logoutButton);
        centerPanel.add(Box.createVerticalGlue());

        mainContent.add(centerPanel, BorderLayout.CENTER);
        add(mainContent);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 25));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 150));
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return label;
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Arial", Font.BOLD, 25));
        btn.setMinimumSize(new Dimension(400, 75));
        btn.setMaximumSize(new Dimension(400, 75));
        btn.setPreferredSize(new Dimension(400, 75));
        btn.setBackground(new Color(253, 199, 91));
        btn.setForeground(new Color(12, 15,66));
        return btn;
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }
    public JButton getContinueGameButton() {
        return continueGameButton;
    }
    public JButton getLogoutButton() {
        return logoutButton;
    }
}