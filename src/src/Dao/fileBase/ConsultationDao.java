package src.Dao.fileBase;
import src.Dao.IDao;
import src.entity.Consultation;
import src.entity.Facture;
import src.entity.InterventionMedecin;
import src.entity.enums.CategorieActe;
import src.entity.enums.GroupeSanguin;
import src.entity.enums.TypeConsultation;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static src.Dao.fileBase.InterventionMedecinDao.convertToIntervention;

public class ConsultationDao implements IDao<Consultation, Long> {

     static File file = new File("Files/consultations.txt");

    public long generateNewId() {
        long lastId = getLastIdFromFile();
        return lastId + 1;
    }

    public long getLastIdFromFile() {
        long lastId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
    public List<Consultation> findAll() {
        List<Consultation> consultations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Consultation consultation = convertToConsultation(line);
                if (consultation != null) {
                    consultations.add(consultation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return consultations;
    }
    @Override
    public Consultation findById(Long idConsultation) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Consultation consultation = convertToConsultation(line);
                if (consultation != null && consultation.getIdConsultation().equals(idConsultation)) {
                    return consultation;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  Consultation convertToConsultation(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 6) {
            try {
                long idConsultation = Long.parseLong(parts[0]);
                long iddossierMedicale = Long.parseLong(parts[1]);
                LocalDate dateConsultation = LocalDate.parse(parts[2]);
                List <Facture> factures =new ArrayList<>();
                FactureDao factureDao = new FactureDao();
                List<Facture> allfactures=factureDao.findAll();

                for (Facture facture : allfactures) {
                    if (facture.getIdConsultation().equals(idConsultation)) {
                        factures.add(facture);
                    }
                }
                String TypeConsultationString = parts[4];
                TypeConsultation typeConsultation = TypeConsultation.valueOf(TypeConsultationString);

                List<InterventionMedecin> interventions = new ArrayList<>();
                InterventionMedecinDao interventionMedecinDao = new InterventionMedecinDao();
                List<InterventionMedecin> allInterventions = interventionMedecinDao.findAll();

                for (InterventionMedecin intervention : allInterventions) {
                    if (intervention.getIdConsultation().equals(idConsultation)) {
                        interventions.add(intervention);
                    }
                }

                return new Consultation(idConsultation, interventions, iddossierMedicale, dateConsultation, typeConsultation, factures);
            } catch (NumberFormatException | DateTimeParseException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private static String convertConsultationToLine(Consultation consultation) {
        StringBuilder builder = new StringBuilder();
        builder.append("ID Consultation: ").append(consultation.getIdConsultation()).append("\n");
        builder.append("ID Dossier Médical: ").append(consultation.getIddossierMedicale()).append("\n");
        builder.append("Date Consultation: ").append(consultation.getDateConsultation()).append("\n");
        builder.append("Type Consultation: ").append(consultation.getTypeConsultation()).append("\n");
        List<InterventionMedecin> interventions = consultation.getInterventions();
        if (interventions != null && !interventions.isEmpty()) {
            builder.append("Interventions:\n");
            for (InterventionMedecin intervention : interventions) {
                builder.append("\t- ").append(intervention).append("\n");
            }
        } else {
            builder.append("Aucune intervention associée à cette consultation.\n");
        }

        return builder.toString();
    }



    @Override
    public Consultation save(Consultation consultation) {
        consultation.setIdConsultation(generateNewId());
        writeConsultationToFile(consultation);
        return consultation;
    }


    public  void updateConsultationsFile(List<Consultation> consultations) {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (Consultation consultation : consultations) {
                writer.println(convertConsultationToLine(consultation));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean update(Consultation consultation) {
        List<Consultation> consultations = findAll();
        for (int i = 0; i < consultations.size(); i++) {
            if (consultations.get(i).getIdConsultation().equals(consultation.getIdConsultation())) {
                consultations.set(i, consultation);
                convertConsultationToLine(consultation);
                return true;
            }
        }return false;
    }

    @Override
    public boolean delete(Consultation consultation) {
            return deleteById(consultation.getIdConsultation());
        }

        @Override
    public List<Consultation> saveAll(Consultation... elements) {
        List<Consultation> savedInterventions = new ArrayList<>();
        for (Consultation element : elements) {
            writeConsultationToFile(element);
        }
        return savedInterventions;

    }

    public void writeConsultationToFile(Consultation consultation) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String ConsultationData = convertConsultationToLine(consultation);
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
        List<Consultation> consultations = findAll();
        for (int i = 0; i < consultations.size(); i++) {
            if (consultations.get(i).getIdConsultation().equals(identifiant)) {
                consultations.remove(i);
                updateConsultationsFile(consultations);
                return true;
            }
        }
        return false;
    }
    public int getNombreConsultationsPourMois(Month mois) {
        List<Consultation> consultations = findAll();
        int count = 0;

        for (Consultation consultation : consultations) {
            LocalDate dateConsultation = consultation.getDateConsultation();
            if (dateConsultation != null && dateConsultation.getMonth() == mois) {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {
         File file = new File("Files/consultations.txt");
        ConsultationDao consultationDao = new ConsultationDao();
        List<Consultation> allConsultations = consultationDao.findAll();
        System.out.println("Liste de toutes les consultations :");
        for (Consultation consultation : allConsultations) {
            System.out.println(consultation);
        }
        Long idToFind = 1L;
        Consultation foundConsultation = consultationDao.findById(idToFind);
        if (foundConsultation != null) {
            System.out.println("Consultation trouvée pour l'ID " + idToFind + " :");
            System.out.println(foundConsultation);
        } else {
            System.out.println("Aucune consultation trouvée pour l'ID " + idToFind);
        }
            }
        }



