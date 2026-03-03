package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JButton createAccountButton;
    private JButton exitButton;

    public LoginFrame() {
        setTitle("Chess Game - Account Access");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel rightPanel = new JPanel();
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // panoul din dreapta
        ImageIcon icon = new ImageIcon("resources/backgrounds/Account-01.png");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int targetWidth = (int)(screenSize.width * 0.525);
        int targetHeight = screenSize.height;
        Image img = icon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        leftPanel.add(imageLabel, BorderLayout.CENTER);

        // panoul din stanga
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 48));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTextLabel = new JLabel("Sign in to continue your game");
        subTextLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        subTextLabel.setForeground(Color.GRAY);
        subTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension fixedSize = new Dimension(400, 75);

        emailField = new JTextField();
        emailField.setPreferredSize(fixedSize);
        emailField.setMaximumSize(fixedSize);
        emailField.setMinimumSize(fixedSize);
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(fixedSize);
        passwordField.setMaximumSize(fixedSize);
        passwordField.setMinimumSize(fixedSize);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 20));
        emailLabel.setPreferredSize(new Dimension(400, 24));
        emailLabel.setMaximumSize(new Dimension(400, 24));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setHorizontalAlignment(SwingConstants.LEFT);

        emailField.setFont(new Font("Arial", Font.PLAIN, 24));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 20));
        passLabel.setPreferredSize(new Dimension(400, 24));
        passLabel.setMaximumSize(new Dimension(400, 24));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel formFields = new JPanel();
        formFields.setLayout(new BoxLayout(formFields, BoxLayout.Y_AXIS));
        formFields.setBackground(Color.WHITE);
        formFields.setMaximumSize(new Dimension(400, 75));
        formFields.setAlignmentX(Component.CENTER_ALIGNMENT);

        formFields.add(emailLabel);
        formFields.add(emailField);
        formFields.add(Box.createRigidArea(new Dimension(0, 15)));
        formFields.add(passLabel);
        formFields.add(passwordField);

        signInButton = new JButton("Sign In");
        signInButton.setBackground(new Color(148, 11, 195));
        signInButton.setForeground(new Color(255,255,255));
        signInButton.setPreferredSize(fixedSize);
        signInButton.setMaximumSize(fixedSize);
        signInButton.setMinimumSize(fixedSize);
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInButton.setFocusPainted(false);

        createAccountButton = new JButton("Create New Account");
        createAccountButton.setBackground(new Color(255,255,255));
        createAccountButton.setForeground(new Color(148, 11, 195));
        createAccountButton.setPreferredSize(fixedSize);
        createAccountButton.setMaximumSize(fixedSize);
        createAccountButton.setMinimumSize(fixedSize);
        createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccountButton.setFocusPainted(false);
        createAccountButton.setBorder(BorderFactory.createLineBorder(new Color(148, 11, 195), 3));

        exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(25, 16, 99));
        exitButton.setForeground(new Color(255,255,255));
        exitButton.setPreferredSize(fixedSize);
        exitButton.setMaximumSize(fixedSize);
        exitButton.setMinimumSize(fixedSize);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setFocusPainted(false);

        Font buttonFont = new Font("Arial", Font.BOLD, 22);

        signInButton.setFont(buttonFont);
        createAccountButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main2.getInstance().exitApplication();
            }
        });

        // adaugam componentele in panou
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(welcomeLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(subTextLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        rightPanel.add(formFields);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        rightPanel.add(signInButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(createAccountButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(exitButton);

        rightPanel.add(Box.createVerticalGlue());

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel);
    }

    public String getEmail() {
        return emailField.getText();
    }
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    public JButton getSignInButton() {
        return signInButton;
    }
    public JButton getCreateAccountButton() {
        return createAccountButton;
    }
    public JButton getExitButton() {
        return exitButton;
    }
}