package Views;

import src.Dao.fileBase.*;
import src.entity.Patient;
import src.entity.enums.GroupeSanguin;
import src.entity.enums.Mutuelle;
import src.exception.FormulaireException;
import src.service.AuthService;
import src.service.SecretaireService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

public class PatientsView extends JFrame {
    private JFrame mainFrame;
    private JPanel sideNavBarPanel;
    private JPanel topBarPanel;
    AuthService serviceauthentif;
    private JTable patientTable;
    JButton dossierButton;
    SecretaireDao s = new SecretaireDao(new UtilisateurDao());
    PatientDao p = new PatientDao();
    DentisteDao d = new DentisteDao();
    FactureDao f =new FactureDao();
    DossierMedicaleDao dm =new DossierMedicaleDao();
    ConsultationDao c =new ConsultationDao();
    SituationFinanciereDao SF = new SituationFinanciereDao(dm,f);
    CaisseDao caisse =new CaisseDao(SF);


    PatientsView(){
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
        createPatientTable();
    }
    private void createPatientTable() {
        try {
            ArrayList<Patient> patients = new ArrayList<>(PFileReader.readPatientsFromFile("Files/Patients.txt"));
            String[] columns = {"ID", "Nom", "Prénom", "Date de Naissance", "Mutuelle", "Groupe Sanguin", "Profession"};
            Object[][] data = new Object[patients.size()][7];

            for (int i = 0; i < patients.size(); i++) {
                Patient patient = patients.get(i);
                data[i] = new Object[]{
                        patient.getId(),
                        patient.getNom(),
                        patient.getPrenom(),
                        patient.getDateNaissance(),
                        patient.getMutuelle(),
                        patient.getGroupeSanguin(),
                        patient.getProfession()
                };
            }

            JLabel tableLabel = new JLabel("Liste des Patients");
            tableLabel.setFont(new Font("Arial", Font.BOLD, 22));
            tableLabel.setForeground(new Color(199, 115, 241));
            patientTable = new JTable(data, columns);
            patientTable.setRowHeight(30);
            JTableHeader header = patientTable.getTableHeader();
            header.setPreferredSize(new Dimension(100,40));
            header.setBackground(new Color(126, 98, 182));
            header.setForeground(Color.WHITE);
            patientTable.setGridColor(Color.GRAY);
            patientTable.setSelectionBackground(new Color(0, 128, 255));
            patientTable.setSelectionForeground(Color.WHITE);
            Font headerFont = new Font("Arial", Font.BOLD, 14);
            header.setFont(headerFont);
            Font tableFont = new Font("Arial", Font.PLAIN, 16);
            patientTable.setFont(tableFont);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(tableLabel, BorderLayout.NORTH);
            panel.add(new JScrollPane(patientTable), BorderLayout.CENTER);
            JPanel formPanel = new JPanel(new GridLayout(7, 2));

            formPanel.setBorder(BorderFactory.createTitledBorder("Ajouter un nouveau patient"));

            JTextField idField = new JTextField(10);
            JTextField nomField = new JTextField(20);
            JTextField prenomField = new JTextField(20);
            JTextField dateNaissanceField = new JTextField(10);
            JComboBox<Mutuelle> mutuelleComboBox = new JComboBox<>(Mutuelle.values());
            JComboBox<GroupeSanguin> groupeSanguinComboBox = new JComboBox<>(GroupeSanguin.values());
            JTextField professionField = new JTextField(20);
            JButton addButton = new JButton("Ajouter");
            addButton.addActionListener(e -> {
                long newPatientId = Long.parseLong(idField.getText());
                boolean idExists = patients.stream().anyMatch(patient -> patient.getId() == newPatientId);

                if (idExists) {
                    JOptionPane.showMessageDialog(mainFrame, "Un patient avec cet ID existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } else {
                    Patient newPatient = new Patient(
                            nomField.getText(),
                            prenomField.getText(),
                            "",
                            newPatientId,
                            "",
                            "",
                            "",
                            LocalDate.parse(dateNaissanceField.getText()),
                            (Mutuelle) mutuelleComboBox.getSelectedItem(),
                            (GroupeSanguin) groupeSanguinComboBox.getSelectedItem(),
                            null,
                            professionField.getText()
                    );
                    patients.add(newPatient);
                    try (PrintWriter writer = new PrintWriter(new FileWriter("Files/Patients.txt", true))) {
                        writer.println(newPatient.toString());
                        writer.println();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, "Erreur lors de l'écriture du fichier Patients.txt : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            formPanel = new JPanel(new GridBagLayout());

            formPanel.setBorder(BorderFactory.createTitledBorder("Ajouter un nouveau patient"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanel.add(new JLabel("ID: "), gbc);
            gbc.gridx = 1;
            formPanel.add(idField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            formPanel.add(new JLabel("Nom: "), gbc);
            gbc.gridx = 1;
            formPanel.add(nomField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            formPanel.add(new JLabel("Prénom: "), gbc);
            gbc.gridx = 1;
            formPanel.add(prenomField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            formPanel.add(new JLabel("Date de naissance (YYYY-MM-DD): "), gbc);
            gbc.gridx = 1;
            formPanel.add(dateNaissanceField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 4;
            formPanel.add(new JLabel("Mutuelle: "), gbc);
            gbc.gridx = 1;
            formPanel.add(mutuelleComboBox, gbc);
            gbc.gridx = 0;
            gbc.gridy = 5;
            formPanel.add(new JLabel("Groupe Sanguin: "), gbc);
            gbc.gridx = 1;
            formPanel.add(groupeSanguinComboBox, gbc);
            gbc.gridx = 0;
            gbc.gridy = 6;
            formPanel.add(new JLabel("Profession: "), gbc);
            gbc.gridx = 1;
            formPanel.add(professionField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 7;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            formPanel.add(addButton, gbc);
            JPanel containerPanel = new JPanel();
            containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
            containerPanel.add(panel);
            containerPanel.add(Box.createVerticalStrut(20));
            containerPanel.add(Box.createVerticalStrut(20));
            containerPanel.add(formPanel);
            JButton deleteButton = new JButton("Supprimer");
            deleteButton.addActionListener(e -> {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow != -1) {
                    int option = JOptionPane.showConfirmDialog(
                            mainFrame,
                            "Voulez-vous vraiment supprimer ce patient ?",
                            "Confirmation de suppression",
                            JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        long patientId = (long) patientTable.getValueAt(selectedRow, 0);

                        try {
                            SecretaireService service = new SecretaireService(s, p, d, SF, f, caisse, dm, c);
                            service.deletePatient(patientId);

                            ((DefaultTableModel) patientTable.getModel()).removeRow(selectedRow);
                        } catch (FormulaireException ex) {
                            JOptionPane.showMessageDialog(mainFrame,
                                    "Erreur lors de la suppression du patient : " + ex.getMessage(),
                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Veuillez sélectionner un patient à supprimer.",
                            "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                }
            });

            mainFrame.add(containerPanel, BorderLayout.CENTER);
            containerPanel.add(deleteButton);
            gbc.gridx = 0;
            gbc.gridy = 8;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            formPanel.add(deleteButton, gbc);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Erreur lors de la lecture du fichier Patients.txt : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->new PatientsView());
    }
}
