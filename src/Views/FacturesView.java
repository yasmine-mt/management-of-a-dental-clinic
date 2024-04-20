package Views;
import src.Dao.fileBase.*;
import src.entity.Facture;
import src.entity.enums.TypePaiement;
import src.service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FacturesView extends JFrame {
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
    FactureDao factureDao= new FactureDao();

    FacturesView(){
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
        JButton addInvoiceButton = new JButton("Ajouter Facture");
        addInvoiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajtFactureform(); // Appeler la méthode pour afficher le formulaire d'ajout de facture
            }
        });
        JPanel tableAndButtonPanel = new JPanel(new BorderLayout());
        tableAndButtonPanel.add(new JScrollPane(factureTable), BorderLayout.CENTER); // Ajoutez la table à un JScrollPane au centre
        tableAndButtonPanel.add(addInvoiceButton, BorderLayout.SOUTH);
        mainFrame.add(tableAndButtonPanel, BorderLayout.CENTER);



    }

    private void ajtFactureform() {
        JDialog dialog = new JDialog(mainFrame, "Ajouter une facture", true);
        dialog.setLayout(new GridLayout(6, 2, 10, 10)); // Added some spacing between components

        // Styling components
        Font boldFont = new Font("Arial", Font.BOLD, 14);

        JTextField idField = new JTextField();
        idField.setBorder(BorderFactory.createLineBorder(new Color(123, 69, 232))); // Setting border
        JTextField dateField = new JTextField();
        dateField.setBorder(BorderFactory.createLineBorder(new Color(123, 69, 232))); // Setting border
        JTextField totalField = new JTextField();
        totalField.setBorder(BorderFactory.createLineBorder(new Color(123, 69, 232))); // Setting border
        JTextField payeField = new JTextField();
        payeField.setBorder(BorderFactory.createLineBorder(new Color(123, 69, 232))); // Setting border

        JLabel restantField = new JLabel();
        restantField.setBorder(BorderFactory.createLineBorder(new Color(123, 69, 232))); // Setting border

        dialog.add(new JLabel("ID Facture:")).setFont(boldFont);
        dialog.add(idField);
        dialog.add(new JLabel("Date Facturation (YYYY-MM-DD):")).setFont(boldFont);
        dialog.add(dateField);
        dialog.add(new JLabel("Montant Total:")).setFont(boldFont);
        dialog.add(totalField);
        dialog.add(new JLabel("Montant Payé:")).setFont(boldFont);
        dialog.add(payeField);

        JComboBox<TypePaiement> typePaiementComboBox = new JComboBox<>(TypePaiement.values());
        typePaiementComboBox.setBorder(BorderFactory.createLineBorder(new Color(123, 69, 232))); // Setting border
        dialog.add(new JLabel("Type de Paiement:")).setFont(boldFont); // Étiquette pour le JComboBox
        dialog.add(typePaiementComboBox);

        JButton submitButton = new JButton("Valider");
        submitButton.setBackground(Color.BLUE); // Setting background color
        submitButton.setForeground(Color.WHITE); // Setting text color
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Long idsituationFinanciere = 0L;
                    Long idConsultation = Long.parseLong(idField.getText());
                    Double montantPaye = Double.parseDouble(payeField.getText());
                    Long idFacture = Long.parseLong(idField.getText());
                    LocalDate dateFacturation = LocalDate.parse(dateField.getText()); // Assurez-vous que le format correspond à "YYYY-MM-DD"
                    Double montantTotal = Double.parseDouble(totalField.getText());
                    TypePaiement typePaiement = (TypePaiement) typePaiementComboBox.getSelectedItem();
                    Double montantRestant = montantTotal - montantPaye;
                    restantField.setText(String.valueOf(montantRestant));

                    // Créer une nouvelle facture
                    Facture newFacture = new Facture(idsituationFinanciere, idConsultation, montantPaye, idFacture, dateFacturation, montantTotal, typePaiement);

                    // Enregistrer la facture dans un fichier
                    saveFactureToFile(newFacture); // Méthode pour sauvegarder la facture dans un fichier

                    // Fermer la JDialog
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Erreur lors de la création de la facture : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    System.out.println(ex);
                }
            }
        });

        dialog.getContentPane().setBackground(Color.WHITE); // Setting background color for the dialog
        dialog.setSize(450, 350); // Adjusted size for better spacing
        dialog.setLocationRelativeTo(mainFrame); // Centering the JDialog
        dialog.setVisible(true);
    }

    private void saveFactureToFile(Facture facture) throws IOException {
        try (FileWriter fw = new FileWriter("Files/factures.txt", true); // "path_to_your_file.txt" est le chemin vers votre fichier
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            // Écrire la facture dans le fichier avec le format approprié
            out.println(facture.getIdsituationFinanciere() + "|" +
                    facture.getIdConsultation() + "|" +
                    facture.getDateFacturation() + "|" +
                    facture.getIdFacture() + "|" +
                    facture.getTypePaiement().toString() + "|" +
                    facture.getMontantTotal() + "|" +
                    facture.getMontantPaye());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->new FacturesView());
    }
}
