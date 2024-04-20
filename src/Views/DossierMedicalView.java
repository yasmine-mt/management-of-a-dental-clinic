package Views;
import src.Dao.fileBase.DossierMedicaleDao;
import src.entity.Dentiste;
import src.entity.DossierMedicale;
import src.entity.Patient;
import src.entity.enums.StatutPaiement;
import src.service.AuthService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DossierMedicalView extends JFrame{
    private JTable dossierTable;
    private JFrame mainFrame;
    private JPanel sideNavBarPanel;
    private JPanel topBarPanel;
    AuthService serviceauthentif;
    DossierMedicaleDao dossierMedicaleDao =new DossierMedicaleDao();

    public DossierMedicalView(List<DossierMedicale> dossiers) {
        mainFrame = new JFrame("IBTISSAMATY");
        mainFrame.setSize(1200, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        createTopBar();
        mainFrame.add(topBarPanel, BorderLayout.NORTH);

        createSideNavBar();
        mainFrame.add(sideNavBarPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Liste des Dossiers Médicaux", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Numéro de Dossier", "Nom du Patient", "Prénom du Patient", "Date de Création", "Statut de Paiement", "Consultations"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == columns.length - 1 ? JButton.class : Object.class;

            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (DossierMedicale dossier : dossiers) {
            String nomPatient = "N/A"; // Valeur par défaut si le patient est null
            String prenomPatient = "N/A"; // Valeur par défaut si le patient est null

            Patient patient = dossier.getPatient();
            if (patient != null) {
                nomPatient = patient.getNom();
                prenomPatient = patient.getPrenom();
            }

            Object[] rowData = { dossier.getNumeroDossier(), nomPatient, prenomPatient, dossier.getDateCreation(), dossier.getStatutPaiement(), "Details" };
            model.addRow(rowData);
        }


        JTable medicalRecordsTable = new JTable(model);
        customizeTableHeader(medicalRecordsTable);
        medicalRecordsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = medicalRecordsTable.getColumnModel().getColumnIndex("Consultations");
                int row = medicalRecordsTable.rowAtPoint(e.getPoint());

                if (column == -1 || row == -1 || column != medicalRecordsTable.getColumnCount() - 1) {
                    return; // Clicked outside the "Details" button column or not on the last column
                }

                Long idDossier = (Long) medicalRecordsTable.getValueAt(row, 0); // Assuming ID is in the first column
                mainFrame.dispose();
                new ConsultationsView(idDossier);
            }
        });
        JScrollPane scrollPane = new JScrollPane(medicalRecordsTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        mainFrame.add(contentPanel, BorderLayout.CENTER);

        mainFrame.setVisible(true);


    }
    private void customizeTableHeader(JTable table) {
        JTableHeader header = table.getTableHeader();

        // Set custom font for the header
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(126, 98, 182));

        header.setPreferredSize(new Dimension(header.getWidth(), 40)); // Adjust 40 to your desired height
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(126, 98, 182));
                c.setForeground(Color.WHITE);
                return c;
            }
        });
    }

    public class CustomRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (row % 2 == 0) {
                c.setBackground(Color.WHITE);
            } else {
                c.setBackground(new Color(195, 177, 231));
            }

            return c;
        }
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

        // Create another icon with text on the far right
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


    public static void main(String[] args) {
        DossierMedicaleDao dossierMedicaleDao = new DossierMedicaleDao();
        List<DossierMedicale>
         dossiers = dossierMedicaleDao.findAll();
        SwingUtilities.invokeLater(() -> new DossierMedicalView(dossiers));
    }

}
