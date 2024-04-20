package src.Dao.fileBase;

import src.Dao.IDao;
import src.entity.Consultation;
import src.entity.InterventionMedecin;
import src.entity.enums.CategorieActe;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InterventionMedecinDao implements IDao<InterventionMedecin, Long> {
    static File fileBase = new File("Files/Interventions.txt");

    private static long generateNewId() {
        long lastId = getLastIdFromFile();
        return lastId + 1;
    }

    private static long getLastIdFromFile() {
        long lastId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileBase))) {
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

    private static void writeInterventionToFile(InterventionMedecin intervention) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileBase, true))) {
            String interventionData = convertInterventionToLine(intervention);
            writer.write(interventionData);
            writer.newLine();
            System.out.println("L'intervention a été ajoutée au fichier avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'écriture de l'intervention dans le fichier !");
        }
    }

    private static String convertInterventionToLine(InterventionMedecin intervention) {
        return intervention.getIdIntervention() + "|" +
                intervention.getActe().name() + "|" +
                intervention.getActe().getPrixDeBase() + "|" +
                intervention.getPrixPatient() + "|" +
                intervention.getDent() + "|" +
                intervention.getNoteMedecin() + "|" +
                intervention.getIdConsultation();
    }


    @Override
    public List<InterventionMedecin> findAll() {
        List<InterventionMedecin> interventions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileBase))) {
            String line;
            while ((line = reader.readLine()) != null) {
                InterventionMedecin intervention = convertToIntervention(line);
                if (intervention != null) {
                    interventions.add(intervention);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return interventions;
    }

    @Override
    public InterventionMedecin findById(Long idIntervention) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileBase))) {
            String line;
            while ((line = reader.readLine()) != null) {
                InterventionMedecin intervention = convertToIntervention(line);
                if (intervention != null && intervention.getIdIntervention().equals(idIntervention)) {
                    return intervention;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InterventionMedecin save(InterventionMedecin newElement) {
        newElement.setIdIntervention(generateNewId());
        writeInterventionToFile(newElement);
        return newElement;
    }

    @Override
    public List<InterventionMedecin> saveAll(InterventionMedecin... elements) {
        List<InterventionMedecin> savedInterventions = new ArrayList<>();
        for (InterventionMedecin element : elements) {
            writeInterventionToFile(element);
        }
        return savedInterventions;
    }

    @Override
    public boolean update(InterventionMedecin newValueElement) {
        List<InterventionMedecin> interventions = findAll();
        for (int i = 0; i < interventions.size(); i++) {
            if (interventions.get(i).getIdIntervention().equals(newValueElement.getIdIntervention())) {
                interventions.set(i, newValueElement);
                updateInterventionsFile(interventions);
                return true;
            }
        }
        return false;
    }

    private static void updateInterventionsFile(List<InterventionMedecin> interventions) {
        try (PrintWriter writer = new PrintWriter(fileBase)) {
            for (InterventionMedecin intervention : interventions) {
                writer.println(convertInterventionToLine(intervention));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(InterventionMedecin element) {
        return deleteById(element.getIdIntervention());
    }

    @Override
     public boolean deleteById(Long identifiant) {
        List<InterventionMedecin> interventions = findAll();
        for (int i = 0; i < interventions.size(); i++) {
            if (interventions.get(i).getIdIntervention().equals(identifiant)) {
                interventions.remove(i);
                updateInterventionsFile(interventions);
                return true;
            }
        }
        return false;
    }

    static InterventionMedecin convertToIntervention(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 7) {
            try {
                long id = Long.parseLong(parts[0]);
                String acte = parts[1];
                double prixPatient = Double.parseDouble(parts[3]);
                Long dent = Long.parseLong(parts[4]);
                String noteMedecin = parts[5];
                Long idConsultation = Long.parseLong(parts[6]);

                CategorieActe c = null;
                for (CategorieActe categorie : CategorieActe.values()) {
                    if (categorie.name().equals(acte)) {
                        c = categorie;
                        break;
                    }
                }

                return new InterventionMedecin(noteMedecin, prixPatient, dent, id, c, idConsultation);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private static void displayAllInterventions(InterventionMedecinDao dao) {
        List<InterventionMedecin> interventions = dao.findAll();
        if (interventions.isEmpty()) {
            System.out.println("Aucune intervention trouvée.");
        } else {
            for (InterventionMedecin intervention : interventions) {
                System.out.println(intervention);
            }
        }
    }

    private static void searchInterventionById(InterventionMedecinDao dao, Scanner scanner) {
        System.out.print("Entrez l'ID de l'intervention à rechercher : ");
        long id = scanner.nextLong();
        InterventionMedecin intervention = dao.findById(id);
        if (intervention != null) {
            System.out.println("Intervention trouvée : " + intervention);
        } else {
            System.out.println("Aucune intervention trouvée avec cet ID.");
        }
    }
    private static void addNewIntervention(InterventionMedecinDao dao, Scanner scanner) {
        System.out.println("Ajout d'une nouvelle intervention :");
        InterventionMedecin newIntervention = createInterventionFromInput(scanner);
        InterventionMedecin savedIntervention = dao.save(newIntervention);
        if (savedIntervention != null) {
            System.out.println("Nouvelle intervention ajoutée avec succès : " + savedIntervention);
        } else {
            System.out.println("Échec de l'ajout de la nouvelle intervention.");
        }
    }
    private static InterventionMedecin createInterventionFromInput(Scanner scanner) {
        InterventionMedecin newIntervention = new InterventionMedecin();

        System.out.println("Veuillez saisir la note du médecin : ");
        String noteMedecin = scanner.nextLine();
        newIntervention.setNoteMedecin(noteMedecin);

        System.out.println("Veuillez saisir le prix payé par le patient : ");
        double prixPatient = scanner.nextDouble();
        newIntervention.setPrixPatient(prixPatient);

        System.out.println("Veuillez saisir le numéro de la dent : ");
        long dent = scanner.nextLong();
        newIntervention.setDent(dent);
        System.out.println("Veuillez saisir le type d'intervention (CategorieActe) : ");
        scanner.nextLine();
        String categorieActeString = scanner.nextLine();
        CategorieActe categorieActe = CategorieActe.valueOf(categorieActeString);

        newIntervention.setActe(categorieActe);

        System.out.println("Veuillez saisir l'ID de consultation : ");
        long idConsultation = scanner.nextLong();
        newIntervention.setIdConsultation(idConsultation);

        scanner.nextLine();
        ajouterInterventionAConsultation(idConsultation,newIntervention);
        return newIntervention;
    }
    public static void ajouterInterventionAConsultation(Long idConsultation, InterventionMedecin nouvelleIntervention) {
        ConsultationDao consultationDao = new ConsultationDao();
        Consultation consultation = consultationDao.findById(idConsultation);
        if (consultation != null) {
            List<InterventionMedecin> interventions = consultation.getInterventions();
            if (interventions == null) {
                interventions = new ArrayList<>();
            }
            interventions.add(nouvelleIntervention);
            consultation.setInterventions(interventions);

            if (consultationDao.update(consultation)) {
                System.out.println("Nouvelle intervention ajoutée à la consultation avec succès !");

            } else {
                System.out.println("Échec de l'ajout de la nouvelle intervention à la consultation.");
            }
        } else {
            System.out.println("Aucune consultation trouvée avec cet ID.");
        }
    }

    private static void updateIntervention(InterventionMedecinDao interventionDao, Scanner scanner) {
        System.out.print("Veuillez saisir l'ID de l'intervention à mettre à jour : ");
        long idToUpdate = scanner.nextLong();
        scanner.nextLine();

        InterventionMedecin existingIntervention = interventionDao.findById(idToUpdate);
        if (existingIntervention != null) {
            InterventionMedecin updatedIntervention = createInterventionFromInput(scanner);
            updatedIntervention.setIdIntervention(idToUpdate);
            if (interventionDao.update(updatedIntervention)) {
                System.out.println("L'intervention a été mise à jour avec succès !");
            } else {
                System.out.println("La mise à jour de l'intervention a échoué.");
            }
        } else {
            System.out.println("Aucune intervention trouvée avec cet ID.");
        }
    }

    private static void deleteIntervention(InterventionMedecinDao interventionDao, Scanner scanner) {
        System.out.print("Veuillez saisir l'ID de l'intervention à supprimer : ");
        long idToDelete = scanner.nextLong();
        scanner.nextLine();

        boolean deleted = interventionDao.deleteById(idToDelete);
        if (deleted) {
            System.out.println("L'intervention a été supprimée avec succès !");
        } else {
            System.out.println("Aucune intervention trouvée avec cet ID.");
        }
    }



}
