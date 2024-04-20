package Views;

import src.Dao.fileBase.DossierMedicaleDao;
import src.Dao.fileBase.UtilisateurDao;
import src.entity.Classe_G.Utilisateur;
import src.entity.enums.Role;
import src.exception.AuthenticationException;
import src.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthentificationView extends JFrame {

    private AuthService authService;
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    DossierMedicaleDao dossierMedicaleDao =new DossierMedicaleDao();

    public AuthentificationView(AuthService authService) {
        this.authService = authService;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Authentication Service");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.getContentPane().setBackground(new Color(240, 240, 240));

        JPanel logoPanel = new JPanel();
        ImageIcon logoIcon = new ImageIcon("Files/dentiste.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        ImageIcon scaledLogoIcon = new ImageIcon(scaledLogo);
        JLabel logoLabel = new JLabel(scaledLogoIcon);
        logoPanel.setBackground(new Color(240, 240, 240));
        logoPanel.add(logoLabel);
        frame.add(logoPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        inputPanel.setBackground(new Color(240, 240, 240));

        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(30);
        usernameField.setPreferredSize(new Dimension(150, 40));
        ImageIcon userIcon = new ImageIcon("Files/user.png");
        Image scaledUserIcon = userIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledUserIconLabel = new ImageIcon(scaledUserIcon);
        JLabel userIconLabel = new JLabel(scaledUserIconLabel);
        usernamePanel.add(userIconLabel);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        usernamePanel.setBackground(new Color(240, 240, 240));
        inputPanel.add(usernamePanel);

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(30);
        passwordField.setPreferredSize(new Dimension(150, 40));
        ImageIcon passIcon = new ImageIcon("Files/Password.png");
        Image scaledPassIcon = passIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledPassIconLabel = new ImageIcon(scaledPassIcon);
        JLabel passIconLabel = new JLabel(scaledPassIconLabel);
        passwordPanel.add(passIconLabel);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        passwordPanel.setBackground(new Color(240, 240, 240));
        inputPanel.add(passwordPanel);

        frame.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 50));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 40));
        loginButton.setBackground(new Color(60, 179, 113));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    if (authService.login(username, password)) {
                        Utilisateur loggedInUser = authService.getLoggedInUser();

                        if (loggedInUser.getRoles() == Role.Administateur) {
                            frame.dispose();
                            new AccueilDentisteView();
                        } else {
                            frame.dispose();
                            new AccueilSecretaireView();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid username or password!");
                    }
                } catch (AuthenticationException ex) {
                    JOptionPane.showMessageDialog(frame, "Authentication error: " + ex.getMessage());
                }
            }
        });
        buttonPanel.add(loginButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(100, 40));
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        buttonPanel.add(exitButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        UtilisateurDao utilisateurDao = new UtilisateurDao();
        AuthService authService = new AuthService(utilisateurDao);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AuthentificationView(authService);
            }
        });
    }
}