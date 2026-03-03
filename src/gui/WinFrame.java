package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WinFrame extends JFrame {
    public WinFrame(boolean playerWon, int pointsEarned, ActionListener onPlayAgain, ActionListener onExit) {
        setTitle(playerWon ? "Victory!" : "Defeat!");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        String bgPath = playerWon ? "resources/backgrounds/WinPlayer-01.png" : "resources/backgrounds/WinComputer-01.png";

        JPanel content = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon(bgPath);
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        JPanel uiPanel = new JPanel();
        uiPanel.setOpaque(false);
        uiPanel.setLayout(new BoxLayout(uiPanel, BoxLayout.Y_AXIS));

        JLabel pointsLabel = new JLabel("Points Earned: " + pointsEarned);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 30));
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pointsLabel.setOpaque(true);
        pointsLabel.setBackground(new Color(0,0,0,150));

        Dimension fixedSize = new Dimension(250,75);
        JButton playAgainBtn = new JButton("Play Again");
        JButton exitBtn = new JButton("Exit to Menu");

        playAgainBtn.setBackground(new Color(252, 23, 137));
        playAgainBtn.setForeground(new Color(255,255,255));
        playAgainBtn.setPreferredSize(fixedSize);
        playAgainBtn.setMinimumSize(fixedSize);
        playAgainBtn.setMaximumSize(fixedSize);
        playAgainBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainBtn.setFocusPainted(false);

        exitBtn.setBackground(new Color(25, 16, 99));
        exitBtn.setForeground(new Color(255,255,255));
        exitBtn.setPreferredSize(fixedSize);
        exitBtn.setMinimumSize(fixedSize);
        exitBtn.setMaximumSize(fixedSize);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setFocusPainted(false);
        Font buttonFont = new Font("Arial", Font.BOLD, 22);
        exitBtn.setFont(buttonFont);
        playAgainBtn.setFont(buttonFont);

        JFrame frameRef = this;
        playAgainBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameRef.dispose();
                onPlayAgain.actionPerformed(e);
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameRef.dispose();
                onExit.actionPerformed(e);
            }
        });

        uiPanel.add(Box.createVerticalGlue());
        uiPanel.add(pointsLabel);
        uiPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        uiPanel.add(playAgainBtn);
        uiPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        uiPanel.add(exitBtn);
        uiPanel.add(Box.createVerticalGlue());

        content.add(uiPanel, BorderLayout.CENTER);
        add(content);
        setVisible(true);
    }
}