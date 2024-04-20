package Views;

import src.service.AuthService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Base {

    private JFrame mainFrame;
    private JPanel sideNavBarPanel;
    private JPanel topBarPanel;
    AuthService serviceauthentif;

    public Base() {
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

        ImageIcon iconWithText = new ImageIcon("Files/dentist.png");
        Image iconedntst = iconWithText.getImage().getScaledInstance(70,70,Image.SCALE_SMOOTH);
        ImageIcon finaliconedntst = new ImageIcon(iconedntst);
        JLabel iconLabel = new JLabel(finaliconedntst);
        JLabel textLabel = new JLabel("Dentiste");
        topBarPanel.add(iconLabel);
        topBarPanel.add(textLabel);
    }

    private void createSideNavBar() {
        sideNavBarPanel = new JPanel();
        sideNavBarPanel.setBackground(Color.DARK_GRAY);
        sideNavBarPanel.setLayout(new BoxLayout(sideNavBarPanel, BoxLayout.Y_AXIS));
        sideNavBarPanel.setPreferredSize(new Dimension(130, 80));
        sideNavBarPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        String[] navItems = {"Accueil", "Mes Patients", "Caisse"};
        String[] iconPaths = {"Files/accueil.png", "Files/patient.png", "Files/caisse.png"};
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
                        new AccueilDentisteView();
                    }
                    if ("Mes Patients".equals(item)){
                        mainFrame.dispose();
                        new MesPatientsView();
                    }
                    if ("Caisse".equals(item)){
                        mainFrame.dispose();
                        new CaisseView();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Base());
    }
}
