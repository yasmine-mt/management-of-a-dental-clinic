package Views;

import src.Dao.fileBase.DossierMedicaleDao;
import src.Dao.fileBase.FactureDao;
import src.entity.Facture;
import src.service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CaisseView
{
    private JFrame mainFrame;
    private JPanel sideNavBarPanel;
    private JPanel topBarPanel;
    AuthService serviceauthentif;
    FactureDao factureDao= new FactureDao();
    DossierMedicaleDao dossierMedicaleDao = new DossierMedicaleDao();

    public CaisseView() {
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
        addInvoiceListToCenter();
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


    private Object[][] getFactureData() {
        List<Facture> factures = new ArrayList<>();

        factures = factureDao.findAll();
        Object[][] data = new Object[factures.size()][5];
        for (int i = 0; i < factures.size(); i++) {
            Facture facture = factures.get(i);
            data[i] = new Object[]{
                    facture.getIdFacture(),
                    facture.getDateFacturation(),
                    facture.getMontantTotal(),
                    facture.getMontantPaye(),
                    facture.getMontantRestant()
            };
        }
        return data;

    }

    private void addInvoiceListToCenter() {
        String[] columns = {"ID Facture", "Date Facturation", "Montant Total", "Montant Payé", "Montant Restant"};
        Object[][] data = getFactureData();
        JTable factureTable = new JTable(data, columns);
        factureTable.getTableHeader().setBackground(new Color(126, 98, 182));
        factureTable.getTableHeader().setForeground(Color.WHITE);
        factureTable.setRowHeight(30);
        factureTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        factureTable.getTableHeader().setPreferredSize(new Dimension(100, 40));
        factureTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Arial", Font.PLAIN, 14));
                if (row % 2 == 0) {
                    c.setBackground(new Color(195, 177, 231));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(factureTable);
        mainFrame.add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CaisseView());
    }
}