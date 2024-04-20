package Views;

import src.service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class AccueilSecretaireView extends JFrame {
    private JFrame mainFrame;
    private JPanel sideNavBarPanel;
    private JPanel topBarPanel;
    AuthService serviceauthentif;

    AccueilSecretaireView(){
        mainFrame = new JFrame("IBTISSAMATY");
        mainFrame.setSize(1200, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        createTopBar();
        mainFrame.add(topBarPanel, BorderLayout.NORTH);

        createSideNavBar();
        mainFrame.add(sideNavBarPanel, BorderLayout.WEST);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.add(addGuessingGame(),BorderLayout.CENTER);
    }

    private JPanel addGuessingGame() {
        JPanel gamePanel = new JPanel(new GridBagLayout());
        gamePanel.setBackground(Color.WHITE);  // Set background color to white

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);  // Increased padding for more space around components

        // Styling for the instruction label
        JLabel instructionLabel = new JLabel("Guess a number between 1 and 100:");
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 24));  // Larger font size
        instructionLabel.setForeground(new Color(126, 98, 182));  // Set font color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gamePanel.add(instructionLabel, gbc);

        // Styling for the text field where user inputs the guess
        JTextField guessField = new JTextField(15);
        guessField.setFont(new Font("Arial", Font.PLAIN, 20));  // Larger font size
        guessField.setBackground(Color.LIGHT_GRAY);  // Set background color
        guessField.setForeground(new Color(123, 69, 232));  // Set font color
        guessField.setHorizontalAlignment(JTextField.CENTER);  // Center-align text
        gbc.gridy = 1;
        gamePanel.add(guessField, gbc);

        // Styling for the submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 22));  // Larger font size
        submitButton.setBackground(new Color(255, 255, 255));  // Set button background color
        submitButton.setForeground(new Color(195, 177, 231));  // Set button text color
        gbc.gridy = 2;
        gamePanel.add(submitButton, gbc);

        // Generate a random number for the user to guess
        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1;

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int userGuess = Integer.parseInt(guessField.getText());

                    if (userGuess == randomNumber) {
                        JOptionPane.showMessageDialog(mainFrame, "Correct! You guessed the number.");
                    } else if (userGuess < randomNumber) {
                        JOptionPane.showMessageDialog(mainFrame, "Too low! Try again.");
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Too high! Try again.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Please enter a valid number.");
                }
            }
        });

        return gamePanel;  // Return the JPanel containing the game components
    }



    private void createTopBar() {
        topBarPanel = new JPanel();
        topBarPanel.setBackground(Color.LIGHT_GRAY);
        topBarPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 70));
        topBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        ImageIcon originalLogoIcon = new ImageIcon("Files/cbdnt.png");
        Image originalImage = originalLogoIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(70, 60, Image.SCALE_SMOOTH);
        ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(scaledLogoIcon);
        topBarPanel.add(logoLabel);
        logoLabel.setBorder(new EmptyBorder(0, 20, 0, 0));

        topBarPanel.add(Box.createHorizontalStrut(950));

        ImageIcon iconWithText = new ImageIcon("Files/secretaire.png");
        Image iconedntst = iconWithText.getImage().getScaledInstance(70,70,Image.SCALE_SMOOTH);
        ImageIcon finaliconedntst = new ImageIcon(iconedntst);
        JLabel iconLabel = new JLabel(finaliconedntst);
        JLabel textLabel = new JLabel("Secrétaire");
        topBarPanel.add(iconLabel);
        topBarPanel.add(textLabel);
    }

    private void createSideNavBar() {
        sideNavBarPanel = new JPanel();
        sideNavBarPanel.setBackground(Color.DARK_GRAY);
        sideNavBarPanel.setLayout(new BoxLayout(sideNavBarPanel, BoxLayout.Y_AXIS));
        sideNavBarPanel.setPreferredSize(new Dimension(130, 80));
        sideNavBarPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        String[] navItems = {"Accueil", "Patients", "Factures"};
        String[] iconPaths = {"Files/accueil.png", "Files/patient.png", "Files/facture.png"};
        for (int i = 0; i < navItems.length; i++) {
            String item = navItems[i];
            String iconPath = iconPaths[i];

            JButton navButton = new JButton(item);
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            navButton.setIcon(new ImageIcon(scaledIcon));
            navButton.setPreferredSize(new Dimension(100, 60));
            navButton.setForeground(Color.WHITE);
            navButton.setBackground(new Color(52, 73, 94));
            navButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            navButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            navButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if ("Accueil".equals(item)){
                        mainFrame.dispose();
                        new AccueilSecretaireView();
                    }
                    if ("Patients".equals(item)){
                        mainFrame.dispose();
                        new PatientsView();
                    }
                    if ("Factures".equals(item)){
                        mainFrame.dispose();
                        new FacturesView();
                    }
                }
            });

            sideNavBarPanel.add(navButton);
            sideNavBarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        sideNavBarPanel.add(Box.createVerticalGlue());

        JButton logoutButton = new JButton();
        ImageIcon logoutIcon = new ImageIcon("Files/user.png");
        Image scaledIcon = logoutIcon.getImage().getScaledInstance(28, 25, Image.SCALE_SMOOTH);
        ImageIcon finalLogoutIcon = new ImageIcon(scaledIcon);
        logoutButton.setIcon(finalLogoutIcon);
        logoutButton.setText("Logout");
        logoutButton.setForeground(Color.GRAY);
        logoutButton.setBackground(Color.DARK_GRAY);
        logoutButton.setFocusPainted(false);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        logoutButton.setPreferredSize(new Dimension(100, 60));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new AuthentificationView(serviceauthentif);
            }
        });
        sideNavBarPanel.add(logoutButton);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new AccueilSecretaireView());
    }


}
