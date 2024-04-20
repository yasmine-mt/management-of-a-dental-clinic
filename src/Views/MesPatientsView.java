package Views;

import src.Dao.fileBase.*;
import src.entity.Dentiste;
import src.entity.DossierMedicale;
import src.entity.Patient;
import src.entity.enums.CategorieAntecedentsMedicaux;
import src.entity.enums.GroupeSanguin;
import src.entity.enums.Mutuelle;
import src.entity.enums.StatutPaiement;
import src.exception.FormulaireException;
import src.service.AuthService;
import src.service.SecretaireService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class MesPatientsView {
    private JFrame mainFrame;
    private JPanel sideNavBarPanel;
    private JPanel topBarPanel;
    AuthService serviceauthentif;
    private JTable patientTable;
    private JPanel containerPanel = new JPanel();

    SecretaireDao s = new SecretaireDao(new UtilisateurDao());
    PatientDao p = new PatientDao();
    DentisteDao d = new DentisteDao();
    FactureDao f =new FactureDao();
    DossierMedicaleDao dm =new DossierMedicaleDao();
    ConsultationDao c =new ConsultationDao();
    SituationFinanciereDao SF = new SituationFinanciereDao(dm,f);
    CaisseDao caisse =new CaisseDao(SF);
    private JRadioButton antecedentsMedicauxRadioButton;
    private JPanel antecedentsMedicauxPanel;
    private JComboBox<CategorieAntecedentsMedicaux> antecedentsMedicauxField;



    public MesPatientsView() {
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
        ArrayList<Patient> patients = new ArrayList<>(p.findAll());
        CustomTableModel tableModel = new CustomTableModel(patients);
        JComboBox<CategorieAntecedentsMedicaux> antecedentsMedicauxField = new JComboBox<>(CategorieAntecedentsMedicaux.values());
        patientTable = new JTable(tableModel);
        JLabel tableLabel = new JLabel("Liste des Patients");
        tableLabel.setFont(new Font("Arial", Font.BOLD, 22));
        tableLabel.setForeground(new Color(199, 115, 241));
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
        JTextField adresseField = new JTextField(20);
        JTextField telephoneField = new JTextField(15);
        JTextField emailField = new JTextField(30);
        JTextField cinField = new JTextField(15);
        JTextField dateNaissanceField = new JTextField(10);
        JComboBox<Mutuelle> mutuelleComboBox = new JComboBox<>(Mutuelle.values());
        JComboBox<GroupeSanguin> groupeSanguinComboBox = new JComboBox<>(GroupeSanguin.values());
        JTextField professionField = new JTextField(20);
        JComboBox <CategorieAntecedentsMedicaux> antecendantsfield =  new JComboBox<>(CategorieAntecedentsMedicaux.values());
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
                          adresseField.getText(),
                          newPatientId,
                          telephoneField.getText(),
                          emailField.getText(),
                          cinField.getText(),
                          LocalDate.parse(dateNaissanceField.getText()),
                          (Mutuelle) mutuelleComboBox.getSelectedItem(),
                          (GroupeSanguin) groupeSanguinComboBox.getSelectedItem(),
                          Arrays.asList((CategorieAntecedentsMedicaux) antecedentsMedicauxField.getSelectedItem()),  // Ajout des antécédents médicaux
                          professionField.getText()
                  );
                patients.add(newPatient);
                p.save(newPatient);
                DentisteDao d =new DentisteDao();
                Dentiste dentiste = d.findById(1L);
                DossierMedicale dossierMedicale = new DossierMedicale(p.findIdByPatient(newPatient),newPatient,dentiste,null,LocalDate.now(), StatutPaiement.IMPAYE);
                dm.writeDossierMedicaleToFile(dossierMedicale);

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
        gbc.gridy = 8;
        formPanel.add(new JLabel("Adresse: "), gbc);
        gbc.gridx = 1;
        formPanel.add(adresseField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(new JLabel("Téléphone: "), gbc);
        gbc.gridx = 1;
        formPanel.add(telephoneField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 10;
        formPanel.add(new JLabel("Email: "), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 11;
        formPanel.add(new JLabel("CIN: "), gbc);
        gbc.gridx = 1;
        formPanel.add(cinField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 7;
        antecedentsMedicauxRadioButton = new JRadioButton("Avez-vous des antécédents médicaux?");
        antecedentsMedicauxRadioButton.addActionListener(e -> toggleAntecedentsMedicauxFields());

        antecedentsMedicauxPanel = new JPanel(new GridBagLayout());
        antecedentsMedicauxPanel.setVisible(false);
        gbc.gridx = 0;
        gbc.gridy = 12;
        formPanel.add(new JLabel("Antécédents médicaux: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 13;
        formPanel.add(antecedentsMedicauxRadioButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 14;
        formPanel.add(antecendantsfield, gbc);

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
                        p.deleteById(patientId);
                        service.deletePatient(patientId);
                        p.deleteById(patientId);

                        DossierMedicaleDao dossierMedicaleDao = new DossierMedicaleDao();
                        DossierMedicale dossierMedicale = dossierMedicaleDao.findByPatientId(patientId);

                        if (dossierMedicale != null) {
                            dossierMedicaleDao.delete(dossierMedicale);
                        }


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


        patientTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = patientTable.getColumnModel().getColumnIndexAtX(evt.getX());
                int row = evt.getY() / patientTable.getRowHeight();
                int columnCount = patientTable.getColumnCount();

                if (row < patientTable.getRowCount() && column < columnCount && column == 7) {

                    Patient selectedPatient = tableModel.getPatientAt(row);
                    mainFrame.dispose();
                    new SituationFinanciereView(selectedPatient);
                }
            }
        });

         gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 15;  // Ajoutez une nouvelle ligne pour le bouton "Ajouter"
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(addButton, gbc);

        gbc.gridy = 16;  // Ajoutez une nouvelle ligne pour le bouton "Supprimer"
        formPanel.add(deleteButton, gbc);

        gbc.gridy = 17;  // Ajoutez une nouvelle ligne pour le panneau "Antécédents médicaux"
        formPanel.add(antecedentsMedicauxPanel, gbc);

        mainFrame.add(panel, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);

    }
    private void toggleAntecedentsMedicauxFields() {
        boolean showFields = antecedentsMedicauxRadioButton.isSelected();
        antecedentsMedicauxPanel.setVisible(showFields);
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return (Component) value;
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
                        new DossierMedicalView(dm.findAll());
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
    public class CustomTableModel extends DefaultTableModel {
        private final String[] columnsWithSituationFinanciere = {"ID", "Nom", "Prénom", "Date de Naissance", "Mutuelle", "Groupe Sanguin", "Profession", "Situation Financière"};
        private final ArrayList<Patient> patients;

        public CustomTableModel(ArrayList<Patient> patients) {
            this.patients = patients;
            setDataVector(createData(), columnsWithSituationFinanciere);
        }

        private Object[][] createData() {
            Object[][] data = new Object[patients.size()][8];
            for (int i = 0; i < patients.size(); i++) {
                Patient patient = patients.get(i);
                data[i] = new Object[]{
                        patient.getId(),
                        patient.getNom(),
                        patient.getPrenom(),
                        patient.getDateNaissance(),
                        patient.getMutuelle(),
                        patient.getGroupeSanguin(),
                        patient.getProfession(),
                        "Détails"
                };
            }
            return data;
        }
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public Patient getPatientAt(int row) {
            return patients.get(row);
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MesPatientsView());
    }
}
