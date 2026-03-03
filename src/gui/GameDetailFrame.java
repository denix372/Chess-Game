package gui;

import game.Game;
import game.Move;
import model.Colors;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameDetailFrame extends JFrame {

    public GameDetailFrame(final Game g, final ActionListener onContinueListener) {
        setTitle("Game Details #" + g.getId());
        setSize(500, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("GAME #" + g.getId(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String pColor = g.getPlayer().getColor().toString();
        String cColor = (g.getPlayer().getColor() == Colors.WHITE) ? "BLACK" : "WHITE";

        JLabel playersLabel = new JLabel("<html><b>Players:</b><br>" +
                "User (" + pColor + "): " + g.getPlayer().getPoints() + " pts<br>" +
                "Computer (" + cColor + "): 0 pts</html>");
        playersLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        playersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel historyTitle = new JLabel("Moves History (" + g.getMoves().size() + " moves)");
        historyTitle.setFont(new Font("Arial", Font.BOLD, 14));
        historyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (Move m : g.getMoves())
            listModel.addElement(m.toString());

        JList<String> movesList = new JList<String>(listModel);
        JScrollPane scrollPane = new JScrollPane(movesList);
        scrollPane.setPreferredSize(new Dimension(400, 250));

        centerPanel.add(playersLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(historyTitle);
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        JButton continueBtn = new JButton("CONTINUE TO BOARD");
        continueBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                // Apelam metoda standard din ActionListener
                onContinueListener.actionPerformed(e);
            }
        });
        add(continueBtn, BorderLayout.SOUTH);

        setVisible(true);
    }
}