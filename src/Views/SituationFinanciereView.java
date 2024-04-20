package Views;

import src.Dao.fileBase.DossierMedicaleDao;
import src.entity.DossierMedicale;
import src.entity.Patient;
import src.service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SituationFinanciereView {
    private JFrame mainFrame;
    private JPanel sideNavBarPanel;
    private JPanel topBarPanel;
    DossierMedicaleDao dossierMedicaleDao = new DossierMedicaleDao();
    private JPanel containerPanel = new JPanel();

    AuthService serviceauthentif;
    public SituationFinanciereView(Patient patient) {
        mainFrame = new JFrame("Situation Financière de " + patient.getNom() + " " + patient.getPrenom());
        mainFrame.setSize(1200, 800);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        createTopBar();
        mainFrame.add(topBarPanel, BorderLayout.NORTH);

        createSideNavBar();
        mainFrame.add(sideNavBarPanel, BorderLayout.WEST);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.add(createDetailsPanel(patient), BorderLayout.CENTER);
    }

    private JPanel createDetailsPanel(Patient patient) {
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE);

        List<DossierMedicale> liste = dossierMedicaleDao.findAll();
        DossierMedicale dossier = null;
        for (DossierMedicale dossierMedicale : liste) {
            if (dossierMedicale.getPatient().equals(patient)) {
                dossier = dossierMedicale;
                break;
            }
        }
        String[] columnNames = {"Détail", "Valeur"};
        Object[][] data = {
                {"ID", patient.getId()},
                {"Nom", patient.getNom()},
                {"Dossier Médical", (dossier != null) ? dossier.toString() : "Aucun dossier médical trouvé"}
        };

        JTable table = new JTable(data, columnNames);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.BLUE);
        table.getTableHeader().setForeground(Color.WHITE);

        final int DOSSIER_MEDICAL_ROW_INDEX = 1;
        final int DOSSIER_MEDICAL_COLUMN_INDEX = 2;
        TableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row == DOSSIER_MEDICAL_ROW_INDEX && column == DOSSIER_MEDICAL_COLUMN_INDEX) {
                    if (value != null) {
                        setToolTipText(value.toString());
                    } else {
                        setToolTipText("Aucun dossier médical trouvé");
                    }
                }
                return c;
            }
        };

        table.setDefaultRenderer(Object.class, cellRenderer);

        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        detailsPanel.add(scrollPane, BorderLayout.CENTER);
        return detailsPanel;
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
        logoLabel.setBorder(new EmptyBorder(0, 20, 0, 20));
        topBarPanel.add(logoLabel);
        topBarPanel.add(Box.createHorizontalStrut(950));

        ImageIcon iconWithText = new ImageIcon("Files/dentist.png");
        Image iconedntst = iconWithText.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        ImageIcon finaliconedntst = new ImageIcon(iconedntst);
        JLabel iconLabel = new JLabel(finaliconedntst);
        JLabel textLabel = new JLabel("Dentiste");
        textLabel.setFont(new Font("Arial", Font.BOLD, 18));
        textLabel.setForeground(Color.BLUE);

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

    private JButton createNavButton(String itemName, String iconPath) {
        JButton navButton = new JButton(itemName);
        ImageIcon icon = new ImageIcon(iconPath);
        Image scaledIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        navButton.setIcon(new ImageIcon(scaledIcon));
        navButton.setForeground(Color.WHITE);
        navButton.setBackground(new Color(52, 73, 94));
        navButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        navButton.setHorizontalAlignment(SwingConstants.LEFT);
        return navButton;
    }

}
