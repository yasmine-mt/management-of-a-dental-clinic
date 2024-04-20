package src.Dao.fileBase;

import src.Dao.IDao;
import src.entity.Consultation;
import src.entity.Facture;
import src.entity.enums.TypePaiement;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FactureDao implements IDao<Facture, Long> {
    private static final File FILE = new File("Files/factures.txt");

    public long generateNewId() {
        long lastId = getLastIdFromFile();
        return lastId + 1;
    }

    public long getLastIdFromFile() {
        long lastId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length > 0 && !parts[0].isEmpty()) {
                        long id = Long.parseLong(parts[0]);
                        if (id > lastId) {
                            lastId = id;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lastId;
    }

    @Override
    public List<Facture> findAll() {
        List<Facture> factures = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Facture facture = convertToFacture(line);
                if (facture != null) {
                    factures.add(facture);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return factures;
    }

    @Override
    public Facture findById(Long idFacture) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Facture facture = convertToFacture(line);
                if (facture != null && facture.getIdFacture().equals(idFacture)) {
                    return facture;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Facture convertToFacture(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 7) {
            try {
                long idFacture = Long.parseLong(parts[0]);
                long idSituationFinanciere = Long.parseLong(parts[1]);
                LocalDate dateFacturation = LocalDate.parse(parts[2]);
                long idConsultation = Long.parseLong(parts[3]);
                String typePaiementString = parts[4];
                TypePaiement typePaiement = TypePaiement.valueOf(typePaiementString);
                Double montantTotal = Double.parseDouble(parts[5]);
                Double montantPaye = Double.parseDouble(parts[6]);

                return new Facture(idSituationFinanciere, idConsultation, montantPaye, idFacture, dateFacturation, montantTotal, typePaiement);
            } catch (NumberFormatException | DateTimeParseException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private static String convertFactureToLine(Facture facture) {
        StringBuilder builder = new StringBuilder();
        builder.append("ID Facture: ").append(facture.getIdFacture()).append("\n");
        builder.append("ID Situation Financiere: ").append(facture.getIdsituationFinanciere()).append("\n");
        builder.append("Date Facturation: ").append(facture.getDateFacturation()).append("\n");
        builder.append("Type Paiement: ").append(facture.getTypePaiement()).append("\n");
        builder.append("Date Facturation: ").append(facture.getDateFacturation()).append("\n");
        builder.append("Montant total: ").append(facture.getMontantTotal()).append("\n");
        builder.append("Montant Paye: ").append(facture.getMontantPaye()).append("\n");
        builder.append("Montant Restant: ").append(facture.getMontantRestant()).append("\n");

        return builder.toString();
    }

    @Override
    public Facture save(Facture facture) {
        facture.setIdFacture(generateNewId());
        writeFactureToFile(facture);
        return facture;
    }

    public void updateFacturesFile(List<Facture> factures) {
        try (PrintWriter writer = new PrintWriter(FILE)) {
            for (Facture facture : factures) {
                writer.println(convertFactureToLine(facture));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Override
    public boolean update(Facture facture) {
        List<Facture> factures = findAll();
        for (int i = 0; i < factures.size(); i++) {
            if (factures.get(i).getIdFacture().equals(facture.getIdFacture())) {
                factures.set(i, facture);
                convertFactureToLine(facture);
                return true;
            }
        }return false;
    }

    @Override
    public boolean delete(Facture facture) {
        return deleteById(facture.getIdFacture());
    }
    @Override
    public List<Facture> saveAll(Facture... elements) {
        List<Facture> savedFacture= new ArrayList<>();
        for (Facture element : elements) {
            writeFactureToFile(element);
        }
        return savedFacture;

    }
    public void writeFactureToFile(Facture facture) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
            String ConsultationData = convertFactureToLine(facture);
            writer.write(ConsultationData);
            writer.newLine();
            System.out.println("La consultation a été ajoutée au fichier avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'écriture de la consultation dans le fichier !");
        }
    }
    @Override
    public boolean deleteById(Long identifiant) {
        List<Facture> factures = findAll();
        for (int i = 0; i < factures.size(); i++) {
            if (factures.get(i).getIdFacture().equals(identifiant)) {
                factures.remove(i);
                updateFacturesFile(factures);
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // Creating a FactureDao instance
        FactureDao factureDao = new FactureDao();

        // Retrieving all factures
//        List<Facture> allFactures = factureDao.findAll();
//        System.out.println("All Factures:");
//        for (Facture facture : allFactures) {
//            System.out.println(facture.toString()); // Assuming you have overridden toString() in Facture class
//        }

        // Finding a specific facture by ID
        Long factureIdToFind = 1L;
        Facture foundFacture = factureDao.findById(factureIdToFind);
        if (foundFacture != null) {
            System.out.println("Found Facture by ID " + factureIdToFind + ":");
            System.out.println(foundFacture.toString());
        } else {
            System.out.println("Facture with ID " + factureIdToFind + " not found.");
        }
//
//        // Adding a new Facture
//        Facture newFacture = new Facture();
//        // Set attributes for the new Facture
//        newFacture.setIdFacture(factureDao.generateNewId());
//        newFacture.setDateFacturation(LocalDate.now());
//        newFacture.setMontantTotal(2500.0);
//        newFacture.setMontantPaye(0.0);
//        // Set other attributes...
//        // For example: newFacture.setIdsituationFinanciere(123);
//        //              newFacture.setConsultation(new Consultation());
//        //              newFacture.setTypePaiement(TypePaiement.CASH);
//        factureDao.save(newFacture);
//        System.out.println("New Facture added successfully.");
//
//        // Updating an existing Facture
//        Facture factureToUpdate = factureDao.findById(2L); // Fetching a Facture by ID
//        if (factureToUpdate != null) {
//            // Modify attributes of the factureToUpdate
//            factureToUpdate.setMontantTotal(3000.0);
//            factureDao.update(factureToUpdate);
//            System.out.println("Facture updated successfully.");
//        } else {
//            System.out.println("Facture to update not found.");
//        }
//
//        // Deleting a Facture by ID
//        Long factureIdToDelete = 3L;
//        boolean isDeleted = factureDao.deleteById(factureIdToDelete);
//        if (isDeleted) {
//            System.out.println("Facture with ID " + factureIdToDelete + " deleted successfully.");
//        } else {
//            System.out.println("Facture with ID " + factureIdToDelete + " not found or couldn't be deleted.");
//        }
//    }


    }
}
