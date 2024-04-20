package src.Dao.fileBase;

import src.Dao.IDao;
import src.entity.Consultation;
import src.entity.Dentiste;
import src.entity.DossierMedicale;
import src.entity.Patient;
import src.entity.enums.StatutPaiement;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DossierMedicaleDao implements IDao<DossierMedicale, Long> {

    static File fileBase = new File("Files/DossiersMedicaux.txt");

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
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return lastId;
    }

    public static List<DossierMedicale> readDossiersMedicauxFromFile(File fileBase) {
        List<DossierMedicale> dossiersMedicaux = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileBase))) {
            String line;
            while ((line = reader.readLine()) != null) {
                DossierMedicale dossier = convertToDossierMedicale(line);
                if (dossier != null) {
                    dossiersMedicaux.add(dossier);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dossiersMedicaux;
    }

    private static DossierMedicale convertToDossierMedicale(String fileLine) {
        DossierMedicale dossierMedicale = null;
        String[] parts = fileLine.split(";");

        try {
            List<Consultation> consultations = new ArrayList<>();
            ConsultationDao consultationDao = new ConsultationDao();
            var id = Long.parseLong(parts[0]);

            var patientId = Long.parseLong(parts[1]);
            var medecinId = Long.parseLong(parts[2]);
            if (!parts[3].isEmpty()) {
                var consultationsIds = parts.length > 3 ? parts[3].split(",") : new String[0];

                if (consultationsIds.length > 0) {
                    for (String consultationId : consultationsIds) {
                        Consultation consultation = consultationDao.findById(Long.parseLong(consultationId));
                        consultations.add(consultation);
                    }
                }

            } else {
                var consultationsIds = 0;

            }
            var dateCreationString = parts[4];
            String statutPaiementString = parts[5];

            StatutPaiement statutPaiement = null;
            for (StatutPaiement categorie : StatutPaiement.values()) {
                if (categorie.name().equals(statutPaiementString)) {
                    statutPaiement = categorie;
                    break;
                }
            }

            PatientDao patientDao = new PatientDao();
            DentisteDao dentisteDao = new DentisteDao();
            Patient patient = patientDao.findById(patientId);
            Dentiste medecin = dentisteDao.findById(medecinId);


            LocalDate dateCreation = LocalDate.parse(dateCreationString);

            dossierMedicale = new DossierMedicale();
            dossierMedicale.setNumeroDossier(id);
            dossierMedicale.setPatient(patient);
            dossierMedicale.setMedecin(medecin);
            dossierMedicale.setConsultations(consultations);
            dossierMedicale.setDateCreation(dateCreation);
            dossierMedicale.setStatutPaiement(statutPaiement);

        } catch (NumberFormatException | DateTimeParseException e) {
            e.printStackTrace();
            System.err.println("Conversion impossible: invalid data format!");
        }

        return dossierMedicale;

    }

    public static void writeDossierMedicaleToFile(DossierMedicale dossier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileBase, true))) {
            String dossierData = convertDossierMedicaleToLine(dossier);
            writer.write(dossierData);
            writer.newLine();
            System.out.println("Le dossier médical a été ajouté au fichier avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'écriture du dossier médical dans le fichier !");
        }
    }

    private static String convertDossierMedicaleToLine(DossierMedicale dossier) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dossier.getNumeroDossier()).append(";");
        stringBuilder.append(dossier.getPatient().getId()).append(";");
        stringBuilder.append(dossier.getMedecin().getId()).append(";");

        List<Consultation> consultations = dossier.getConsultations();
        if (consultations != null && !consultations.isEmpty()) {
            for (Consultation consultation : consultations) {
                stringBuilder.append(consultation.getIdConsultation()).append(",");
            }
        }
        stringBuilder.append(";");

        stringBuilder.append(dossier.getDateCreation()).append(";");
        stringBuilder.append(dossier.getStatutPaiement().toString());

        return stringBuilder.toString();
    }



    @Override
    public List<DossierMedicale> findAll() {
        return readDossiersMedicauxFromFile(fileBase);
    }

    @Override
    public DossierMedicale findById(Long identifiant) {
        List<DossierMedicale> dossiersMedicaux = readDossiersMedicauxFromFile(fileBase);
        for (DossierMedicale dossier : dossiersMedicaux) {
            if (dossier.getNumeroDossier().equals(identifiant)) {
                return dossier;
            }
        }
        return null;
    }

    @Override
    public DossierMedicale save(DossierMedicale newElement) {
        long newId = generateNewId();
        newElement.setNumeroDossier(newId);
        writeDossierMedicaleToFile(newElement);
        return newElement;
    }

    @Override
    public List<DossierMedicale> saveAll(DossierMedicale... elements) {
        List<DossierMedicale> savedDossiersMedicaux = new ArrayList<>();
        for (DossierMedicale dossier : elements) {
            DossierMedicale savedDossier = save(dossier);
            savedDossiersMedicaux.add(savedDossier);
        }
        return savedDossiersMedicaux;
    }

    @Override
    public boolean update(DossierMedicale updatedElement) {
        List<DossierMedicale> dossiersMedicaux = readDossiersMedicauxFromFile(fileBase);
        for (int i = 0; i < dossiersMedicaux.size(); i++) {
            if (dossiersMedicaux.get(i).getNumeroDossier().equals(updatedElement.getNumeroDossier())) {
                dossiersMedicaux.set(i, updatedElement);
                updateDossiersMedicauxFile(dossiersMedicaux);
                return true;
            }
        }
        return false;
    }

    private void updateDossiersMedicauxFile(List<DossierMedicale> dossiersMedicaux) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileBase))) {
            for (DossierMedicale dossier : dossiersMedicaux) {
                writer.write(convertDossierMedicaleToLine(dossier));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la mise à jour du fichier des dossiers médicaux !");
        }
    }

    @Override
    public boolean deleteById(Long identifiant) {
        List<DossierMedicale> dossiersMedicaux = readDossiersMedicauxFromFile(fileBase);
        Iterator<DossierMedicale> iterator = dossiersMedicaux.iterator();
        while (iterator.hasNext()) {
            DossierMedicale dossier = iterator.next();
            if (dossier.getNumeroDossier().equals(identifiant)) {
                iterator.remove();
                updateDossiersMedicauxFile(dossiersMedicaux);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(DossierMedicale element) {
        List<DossierMedicale> dossiersMedicaux = readDossiersMedicauxFromFile(fileBase);
        boolean removed = dossiersMedicaux.removeIf(dossier -> dossier.getNumeroDossier().equals(element.getNumeroDossier()));
        if (removed) {
            updateDossiersMedicauxFile(dossiersMedicaux);
        }
        return removed;
    }

    public List<DossierMedicale> findByIdPatient(long patientId) {
        List<DossierMedicale> dossierMedicaleList = new ArrayList<>();
        List<DossierMedicale> allDossiers = readDossiersMedicauxFromFile(fileBase);

        for (DossierMedicale dossier : allDossiers) {
            if (dossier.getPatient().getId() == patientId) {
                dossierMedicaleList.add(dossier);
            }
        }

        return dossierMedicaleList;
    }
    public boolean deleteDossierMedicale(long patientId) {
        List<DossierMedicale> dossiersMedicaux = readDossiersMedicauxFromFile(fileBase);
        Iterator<DossierMedicale> iterator = dossiersMedicaux.iterator();
        while (iterator.hasNext()) {
            DossierMedicale dossier = iterator.next();
            if (dossier.getPatient().getId() == patientId) {
                iterator.remove();
                updateDossiersMedicauxFile(dossiersMedicaux);
                return true;
            }
        }
        return false;
    }
    public DossierMedicale findByPatientId(Long patientId) {
        // Lisez tous les dossiers médicaux depuis votre source de données (par exemple, fichier)
        List<DossierMedicale> allDossiers = findAll();

        // Parcourez la liste des dossiers médicaux et trouvez celui associé au patientId
        for (DossierMedicale dossier : allDossiers) {
            if (dossier.getPatient().getId().equals(patientId)) {
                return dossier;
            }
        }

        // Si aucun dossier médical correspondant n'est trouvé, retournez null
        return null;
    }



    public static void main(String[] args) {
        DossierMedicaleDao dossierMedicaleDao = new DossierMedicaleDao();

        System.out.println("All Dossiers Medicaux:");
     dossierMedicaleDao.findAll().forEach(System.out::println);

//


//        long dossierId = 1; // Replace this with an existing dossier ID
//        DossierMedicale retrievedDossier = dossierMedicaleDao.findById(dossierId);
//        if (retrievedDossier != null) {
//            System.out.println("Retrieved Dossier Medical:");
//            System.out.println(retrievedDossier);
//        } else {
//            System.out.println("Dossier Medical with ID " + dossierId + " not found!");
//        }

        //dossierMedicaleDao.delete(dossierMedicaleDao.findById(4L));
    }
    }


