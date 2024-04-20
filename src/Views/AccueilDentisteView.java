package Views;

import src.Dao.fileBase.*;
import src.entity.Caisse;
import src.service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class AccueilDentisteView extends JFrame {
    private JFrame mainFrame;
    private JPanel sideNavBarPanel;
    private JPanel topBarPanel;
    private JPanel mainContentPanel;
    ConsultationDao consultationDao = new ConsultationDao();
    DossierMedicaleDao dossierMedicaleDao =new DossierMedicaleDao();
    AuthService serviceauthentif;
    private Caisse caisse;
    SituationFinanciereDao situationFinanciereDao=new SituationFinanciereDao(new DossierMedicaleDao(),new FactureDao());
    CaisseDao caisseDao =new CaisseDao(situationFinanciereDao);
    public AccueilDentisteView() {
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
        mainContentPanel = new JPanel(new GridBagLayout());
        mainContentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        addCircleToGrid(mainContentPanel, new Color(167, 147, 227), gbc, 0, 0, "Recette du jour : \n" + (caisseDao.SommerecetteDuJour(LocalDate.now())));
        addCircleToGrid(mainContentPanel, new Color(167, 147, 227), gbc, 2, 0, "Recette du mois : \n" +(caisseDao.SommerecetteDuMois(LocalDate.now())));
        addCircleToGrid(mainContentPanel, new Color(167, 147, 227), gbc, 1, 1, "Nombre des patients:\n"+( consultationDao.getNombreConsultationsPourMois(LocalDate.now().getMonth())));
        addCircleToGrid(mainContentPanel, new Color(167, 147, 227), gbc, 0, 2, "Recette de l'année : \n" + (caisseDao.SommerecetteDeLAnnee(LocalDate.now())));
        addCircleToGrid(mainContentPanel, new Color(167, 147, 227), gbc, 2, 2, "Nombre de consultations du mois : \n"+( consultationDao.getNombreConsultationsPourMois(LocalDate.now().getMonth())));;
        mainFrame.add(mainContentPanel, BorderLayout.CENTER);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void addCircleToGrid(JPanel panel, Color color, GridBagConstraints gbc, int row, int col, String text) {
        CirclePanel circlePanel = new CirclePanel(color, text);
        gbc.gridx = col;
        gbc.gridy = row;
        mainContentPanel.add(circlePanel, gbc);
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
        Image iconedntst = iconWithText.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
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

        String[] navItems = {"Accueil", "Mes Patients", "Caisse", "Dossier Médical"};
        String[] iconPaths = {"Files/accueil.png", "Files/patient.png", "Files/caisse.png", "Files/img.png"};

        for (int i = 0; i < navItems.length; i++) {
            String item = navItems[i];
            String iconPath = iconPaths[i];

            JPanel navPanel = new JPanel();
            navPanel.setLayout(new BorderLayout());
            navPanel.setBackground(new Color(52, 73, 94));
            navPanel.setPreferredSize(new Dimension(40, 60));

            JButton navButton = new JButton(item);
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            navButton.setIcon(new ImageIcon(scaledIcon));
            navButton.setForeground(Color.WHITE);
            navButton.setBackground(new Color(52, 73, 94));
            navButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            navButton.setHorizontalAlignment(SwingConstants.LEFT);

            navButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (item.equals("Accueil")) {
                        mainFrame.dispose();
                        new AccueilDentisteView();
                    } else if (item.equals("Mes Patients")) {
                        mainFrame.dispose();
                        new MesPatientsView();
                    } else if (item.equals("Caisse")) {
                        mainFrame.dispose();
                        new CaisseView();
                    } else if (item.equals("Dossier Médical")) {
                        mainFrame.dispose();
                        new DossierMedicalView(dossierMedicaleDao.findAll());
                    }
                }
            });

            navPanel.add(navButton, BorderLayout.WEST);
            sideNavBarPanel.add(navPanel);
        }

        // Bouton Logout
        JButton logoutButton = new JButton();
        ImageIcon logoutIcon = new ImageIcon("Files/user.png");
        Image scaledIcon = logoutIcon.getImage().getScaledInstance(30, 25, Image.SCALE_SMOOTH);
        ImageIcon finalLogoutIcon = new ImageIcon(scaledIcon);
        logoutButton.setIcon(finalLogoutIcon);
        logoutButton.setText("Logout");
        logoutButton.setForeground(Color.GRAY);
        logoutButton.setBackground(Color.DARK_GRAY);
        logoutButton.setFocusPainted(false);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        logoutButton.setPreferredSize(new Dimension(150, 60));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new AuthentificationView(serviceauthentif);
            }
        });
        sideNavBarPanel.add(Box.createVerticalStrut(20));
        sideNavBarPanel.add(logoutButton);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccueilDentisteView());
    }}
