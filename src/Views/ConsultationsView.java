package Views;

import javax.swing.*;

import src.Dao.fileBase.ConsultationDao;
import src.Dao.fileBase.DossierMedicaleDao;
import src.entity.Consultation;
import src.entity.enums.TypeConsultation;
import src.service.AuthService;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;


public class ConsultationsView extends JFrame {

    private JFrame mainFrame;
    private JPanel sideNavBarPanel;
    private JTable consultationsTable;
    private JPanel topBarPanel;
    AuthService serviceauthentif;
    private JTextField searchField;
    private JButton searchButton;
    DossierMedicaleDao dossierMedicaleDao =new DossierMedicaleDao();

    public ConsultationsView(Long idDossier){
        mainFrame = new JFrame("IBTISSAMATY");
        mainFrame.setSize(1200, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        // Create the top bar
        createTopBar();
        mainFrame.add(topBarPanel, BorderLayout.NORTH); // Add top bar to the north position

        // Create side nav bar
        createSideNavBar();
        mainFrame.add(sideNavBarPanel, BorderLayout.WEST); // Add side nav bar to the west position
        JPanel consultationsPanel = new JPanel(new BorderLayout());
        consultationsPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add padding
        mainFrame.add(consultationsPanel, BorderLayout.CENTER);
        ArrayList<Consultation> patientConsultations = fetchConsultationsFordossier(idDossier);
        consultationsTable = new JTable(consultationTableModel(patientConsultations));
        consultationsTable.setRowHeight(30);
        JTableHeader header = consultationsTable.getTableHeader();
        header.setBackground(Color.DARK_GRAY);
        header.setForeground(Color.WHITE);
        consultationsTable.setDefaultRenderer(Object.class, customTableCellRenderer());
        consultationsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(consultationsTable);
        consultationsPanel.add(new JScrollPane(consultationsTable), BorderLayout.CENTER);
        addSearchBar(consultationsPanel, consultationsTable);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        styleTableHeaders(consultationsTable);

    }

    private void addSearchBar(JPanel consultationsPanel, JTable consultationsTable) {
        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchBarPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding for the search bar panel

        // Create search field
        searchField = new JTextField(30); // Adjust the size as needed
        searchBarPanel.add(searchField);

        // Create search button
        searchButton = new JButton("Search");
        searchBarPanel.add(searchButton);

        // Add search bar panel to consultations panel at the top
        consultationsPanel.add(searchBarPanel, BorderLayout.NORTH);

        // Add action listener to the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText().trim();
                filterTableByPatientName(consultationsTable, searchTerm);
            }
        });
    }

    private void filterTableByPatientName(JTable table, String searchTerm) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Set the filter based on the search term for the "Patient Name" column (adjust column index accordingly)
        if (searchTerm.length() == 0) {
            sorter.setRowFilter(null); // Remove any existing filters
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTerm, /* Column index for Patient Name */ 1));
        }
    }
    private static DefaultTableCellRenderer customTableCellRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Add background and foreground colors
                if (row % 2 == 0) { // Alternate row color for better readability
                    c.setBackground(Color.WHITE);
                } else {
                    c.setBackground(new Color(195, 177, 231));
                }
                c.setForeground(Color.BLACK); // Reset to default text color
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                return c;
            }
        };}

    private void styleTableHeaders(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.DARK_GRAY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private DefaultTableModel consultationTableModel(ArrayList<Consultation> consultations) {
        String[] columns = {"Consultation ID", "Date", "Type"}; // You can modify these column headers as per your requirement

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Consultation consultation : consultations) {
            // Assuming Consultation has getId(), getDate(), and getDetails() methods. Modify accordingly.
            Object[] rowData = {consultation.getIdConsultation(), consultation.getDateConsultation(), consultation.getTypeConsultation()};
            model.addRow(rowData);
        }

        return model;
    }


    private ArrayList<Consultation> fetchConsultationsFordossier(Long id) {
        ConsultationDao consultationDao = new ConsultationDao();
        List<Consultation> allConsultations = consultationDao.findAll();
        ArrayList<Consultation> consultationsForPatient = new ArrayList<>();

        for (Consultation consultation : allConsultations) {
            if (consultation.getIddossierMedicale().equals(id)) {
                consultationsForPatient.add(consultation);
            }
        }
        return consultationsForPatient;
    }


    private void createTopBar() {
        topBarPanel = new JPanel();
        topBarPanel.setBackground(Color.LIGHT_GRAY);
        topBarPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 70));
        topBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        ImageIcon originalLogoIcon = new ImageIcon("Files/cbdnt.png"); // Replace with your logo path
        Image originalImage = originalLogoIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(70, 60, Image.SCALE_SMOOTH);

        ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(scaledLogoIcon);
        topBarPanel.add(logoLabel);
        logoLabel.setBorder(new EmptyBorder(0, 20, 0, 0));

        topBarPanel.add(Box.createHorizontalStrut(950));

        // Create another icon with text on the far right
        ImageIcon iconWithText = new ImageIcon("Files/dentist.png"); // Replace with your icon path
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

        Long dossierId = 1L;
        SwingUtilities.invokeLater(() -> {
            ConsultationsView consultationsView = new ConsultationsView(dossierId);
            consultationsView.setVisible(true);
        });
    }



}
