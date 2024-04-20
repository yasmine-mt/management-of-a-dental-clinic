package src.Dao.fileBase;
import src.Dao.IDao;
import src.entity.Patient;
import src.entity.enums.CategorieAntecedentsMedicaux;
import src.entity.enums.GroupeSanguin;
import src.entity.enums.Mutuelle;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;


public class PatientDao implements IDao<Patient,Long> {

    static File fileBase = new File("Files/patients.txt");
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

    private static List<Patient> readPatientsFromFile(File fileBase) {
        List<Patient> patients = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileBase))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Patient patient = convertToPatient(line);
                    if (patient != null) {
                        patients.add(patient);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return patients;
    }


    private static Patient convertToPatient(String fileLine) {
        Patient patient = null;
        String[] parts = fileLine.split("\\|");

        if (parts.length == 12) {
            try {
                long id = Long.parseLong(parts[0]);
                String nom = parts[1];
                String prenom = parts[2];
                String adresse = parts[3];
                String telephone = parts[4];
                String email = parts[5];
                String CIN = parts[6];
                LocalDate dateNaissance = LocalDate.parse(parts[7]);
                Mutuelle mutuelle = Mutuelle.valueOf(parts[8]);
                GroupeSanguin groupeSanguin = GroupeSanguin.valueOf(parts[9]);
                List<CategorieAntecedentsMedicaux> antecedentsMedicaux = convertAntecedentsMedicaux(parts[10]);
                String profession = parts[11];

                patient = new Patient(nom, prenom, adresse, id, telephone, email, CIN, dateNaissance, mutuelle, groupeSanguin, antecedentsMedicaux, profession);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                System.err.println("Erreur de conversion ou format de date invalide : " + e.getMessage());
            }
        } else {
            System.err.println("Format de ligne invalide : " + fileLine);
        }

        return patient;
    }


    private static void displayAllPatients(PatientDao dao) {
        List<Patient> patients = dao.findAll();
        if (patients.isEmpty()) {
            System.out.println("Aucun patient trouvé !");
        } else {
            System.out.println("Liste des patients :");
            patients.forEach(System.out::println);
        }
    }

    public Long findIdByPatient(Patient patient) {
        List<Patient> patients = readPatientsFromFile(fileBase);
        for (Patient p : patients) {
            if (p.equals(patient)) {
                return p.getId();
            }
        }
        return null;
    }

    private static void searchPatientById(PatientDao dao, Scanner scanner) {
        System.out.print("Entrez l'ID du patient : ");
        long id = scanner.nextLong();

        Patient patient = dao.findById(id);
        if (patient != null) {
            System.out.println("Patient trouvé :");
            System.out.println(patient);
        } else {
            System.out.println("Aucun patient trouvé avec cet ID !");
        }
    }

    private static List<CategorieAntecedentsMedicaux> convertAntecedentsMedicaux(String antecedentsMedicauxString) {
        String[] antecedentsArray = antecedentsMedicauxString.split(",");
        List<CategorieAntecedentsMedicaux> antecedentsList = new ArrayList<>();

        for (String antecedent : antecedentsArray) {
            try {
                CategorieAntecedentsMedicaux categorie = CategorieAntecedentsMedicaux.valueOf(antecedent.trim());
                antecedentsList.add(categorie);
            } catch (IllegalArgumentException e) {
                antecedentsList.add(CategorieAntecedentsMedicaux.AUTRE);
            }
        }

        return antecedentsList;
    }


    private static void writePatientToFile(Patient patient) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Files/patients.txt", true))) {
            String patientData = convertPatientToLine(patient);
            writer.write(patientData);
            writer.newLine();
            System.out.println("Le patient a été ajouté au fichier avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'écriture du patient dans le fichier !");
        }
    }

    private static String convertPatientToLine(Patient patient) {
        return patient.getId() + "|" + patient.getNom() + "|" + patient.getPrenom() + "|" + patient.getAdresse() + "|"
                + patient.getTelephone() + "|" + patient.getEmail() + "|" + patient.getCIN() + "|"
                + patient.getDateNaissance() + "|" + patient.getMutuelle() + "|" + patient.getGroupeSanguin() + "|"
                + convertAntecedentsMedicauxToString(patient.getAntecedentsMedicaux()) + "|" + patient.getProfession();
    }

    private static String convertAntecedentsMedicauxToString(List<CategorieAntecedentsMedicaux> antecedents) {
        if (antecedents == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (CategorieAntecedentsMedicaux antecedent : antecedents) {
            sb.append(antecedent).append(",");
        }
        return sb.toString().replaceAll(",$", ""); // Supprimer la virgule finale
    }


    public void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PatientDao dao = new PatientDao();

        System.out.println("===== Gestion des patients =====");

        boolean exit = false;
        while (!exit) {
            System.out.println("\nMenu :");
            System.out.println("1. Afficher tous les patients");
            System.out.println("2. Rechercher un patient par ID");
            System.out.println("3. Ajouter un nouveau patient");
            System.out.println("4. Mettre à jour un patient");
            System.out.println("5. Supprimer un patient");
            System.out.println("6. Quitter");
            System.out.print("Choix : ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayAllPatients(dao);
                    break;
                case 2:
                    searchPatientById(dao, scanner);
                    break;
                case 3:
                    createPatientFromInput();
                    break;
                case 4:
                    updatePatientAttribute(dao, scanner);
                    break;
                case 5:
                    deletePatient(dao, scanner);
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }

        scanner.close();
    }

    @Override
    public List<Patient> findAll() {
        return readPatientsFromFile(fileBase);
    }


    @Override
    public Patient findById(Long identifiant) {
        List<Patient> patients = readPatientsFromFile(fileBase);
        for (Patient patient : patients) {
            if (patient.getId() == identifiant) {
                return patient;
            }
        }
        return null;
    }


    public  static Patient save2(Patient newElement) {
        long newId = generateNewId();
        newElement.setId(newId);
        writePatientToFile(newElement);
        return newElement;
    }
    @Override
    public  Patient save(Patient newElement) {
        long newId = generateNewId();
        newElement.setId(newId);
        writePatientToFile(newElement);
        return newElement;
    }
    @Override
    public List<Patient> saveAll(Patient... elements) {
        List<Patient> savedPatients = new ArrayList<>();
        for (Patient patient : elements) {
            Patient savedPatient = save(patient);
            savedPatients.add(savedPatient);
        }
        return savedPatients;
    }

    @Override
    public boolean update(Patient newValueElement) {
        List<Patient> patients = readPatientsFromFile(fileBase);
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getId() == newValueElement.getId()) {
                patients.set(i, newValueElement);
                updatePatientsFile(patients);
                return true;
            }
        }
        return false;
    }



    @Override
    public boolean deleteById(Long identifiant) {
        List<Patient> patients = readPatientsFromFile(fileBase);
        Iterator<Patient> iterator = patients.iterator();
        while (iterator.hasNext()) {
            Patient patient = iterator.next();
            if (patient.getId() == identifiant) {
                iterator.remove();
                updatePatientsFile(patients);
                return true;
            }
        }
        return false;
    }


    private void updatePatientsFile(List<Patient> patients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileBase))) {
            for (Patient patient : patients) {
                writer.write(convertPatientToLine(patient));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la mise à jour du fichier des patients !");
        }
    }

    @Override
    public boolean delete(Patient element) {
        List<Patient> patients = readPatientsFromFile(fileBase);
        boolean removed = patients.removeIf(patient -> patient.getId() == element.getId());
        if (removed) {
            updatePatientsFile(patients);
        }
        return removed;
    }




    public static void updatePatientAttribute(PatientDao dao, Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("Mise à jour des informations d'un patient :");
            System.out.print("Entrez l'ID du patient à mettre à jour (ou entrez '0' pour quitter) : ");
            long idToUpdate = scanner.nextLong();
            scanner.nextLine();

            if (idToUpdate == 0) {
                exit = true;
                break;
            }

        Patient existingPatient = dao.findById(idToUpdate);
        if (existingPatient != null) {
            System.out.println("Patient trouvé : ");
            System.out.println(existingPatient);
            System.out.println("Quel attribut souhaitez-vous mettre à jour ?");
            System.out.println("1. Date de naissance");
            System.out.println("2. Mutuelle");
            System.out.println("3. Groupe sanguin");
            System.out.println("4. Antécédents médicaux");
            System.out.println("5. Profession");
            System.out.print("Choisissez le numéro de l'attribut à mettre à jour : ");
            System.out.println("0. Quitter la mise à jour");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Entrez la nouvelle date de naissance (AAAA-MM-JJ) : ");
                    String newDateNaissanceString = scanner.nextLine();
                    LocalDate newDateNaissance = LocalDate.parse(newDateNaissanceString);
                    existingPatient.setDateNaissance(newDateNaissance);
                    break;
                case 2:
                    System.out.print("Entrez la mutuelle du patient : ");
                    String mutuelleString = scanner.nextLine();
                    Mutuelle mutuelle = Mutuelle.valueOf(mutuelleString);
                    existingPatient.setMutuelle(mutuelle);
                    break;
                case 3:
                    System.out.println("Veuillez choisir le groupe sanguin du patient : ");
                    System.out.println("Options disponibles : ");


                    for (GroupeSanguin gs : GroupeSanguin.values()) {
                        System.out.println(gs.name());
                    }
                    System.out.print("Entrez le groupe sanguin du patient : ");
                    String groupeSanguinString = scanner.nextLine();
                    GroupeSanguin groupeSanguin = GroupeSanguin.valueOf(groupeSanguinString);
                    existingPatient.setGroupeSanguin(groupeSanguin);

                    break;
                case 4:
                    System.out.println("Veuillez choisir les antécédents médicaux du patient : ");
                    System.out.println("Options disponibles : ");


                    for (CategorieAntecedentsMedicaux a : CategorieAntecedentsMedicaux.values()) {
                        System.out.println(a.name());
                    }
                    System.out.print("Entrez les antécédents médicaux du patient (séparés par des virgules) : ");
                    String antecedentsMedicauxString = scanner.nextLine();
                    List<CategorieAntecedentsMedicaux> antecedentsMedicaux = convertAntecedentsMedicaux(antecedentsMedicauxString);
                    existingPatient.setAntecedentsMedicaux(antecedentsMedicaux);
                    break;
                case 5:
                    System.out.print("Entrez la nouvelle profession : ");
                    String newProfession = scanner.nextLine();
                    existingPatient.setProfession(newProfession);
                    break;


            case 0:
                exit = true;
                break;
            default:
                System.out.println("Choix invalide !");
                break;
        }
        } else {
            System.out.println("Aucun patient trouvé avec cet ID.");
        }
    }
    }


    public static void deletePatient(PatientDao dao, Scanner scanner) {
        System.out.println("Suppression d'un patient :");
        System.out.print("Entrez l'ID du patient à supprimer : ");
        long idToDelete = scanner.nextLong();
        scanner.nextLine();

        Patient patientToDelete = dao.findById(idToDelete);
        if (patientToDelete != null) {
            if (dao.delete(patientToDelete)) {
                System.out.println("Le patient a été supprimé avec succès !");
            } else {
                System.out.println("La suppression du patient a échoué.");
            }
        } else {
            System.out.println("Aucun patient trouvé avec cet ID.");
        }
    }
    public static Patient createPatientFromInput() {
        System.out.println("Ajout d'un nouveau patient :");
        Scanner scanner= new Scanner(System.in);
        System.out.print("Entrez le nom du patient : ");
        String nom = scanner.nextLine();

        System.out.print("Entrez le prénom du patient : ");
        String prenom = scanner.nextLine();

        System.out.print("Entrez l'adresse du patient : ");
        String adresse = scanner.nextLine();

        System.out.print("Entrez le numéro de téléphone du patient : ");
        String telephone = scanner.nextLine();

        System.out.print("Entrez l'email du patient : ");
        String email = scanner.nextLine();

        System.out.print("Entrez le CIN du patient : ");
        String CIN = scanner.nextLine();

        System.out.print("Entrez la date de naissance du patient (AAAA-MM-JJ) : ");
        String dateNaissanceString = scanner.nextLine();
        LocalDate dateNaissance = LocalDate.parse(dateNaissanceString);

        System.out.print("Entrez la mutuelle du patient : ");
        String mutuelleString = scanner.nextLine();
        Mutuelle mutuelle = Mutuelle.valueOf(mutuelleString);

        System.out.println("Veuillez choisir le groupe sanguin du patient : ");
        System.out.println("Options disponibles : ");


        for (GroupeSanguin gs : GroupeSanguin.values()) {
            System.out.println(gs.name());
        }

        System.out.print("Entrez le groupe sanguin du patient : ");
        String groupeSanguinString = scanner.nextLine();
        GroupeSanguin groupeSanguin = GroupeSanguin.valueOf(groupeSanguinString);
        System.out.println("Veuillez choisir les antécédents médicaux du patient : ");
        System.out.println("Options disponibles : ");


        for (CategorieAntecedentsMedicaux a : CategorieAntecedentsMedicaux.values()) {
            System.out.println(a.name());
        }
        System.out.print("Entrez les antécédents médicaux du patient (séparés par des virgules) : ");
        String antecedentsMedicauxString = scanner.nextLine();
        List<CategorieAntecedentsMedicaux> antecedentsMedicaux = convertAntecedentsMedicaux(antecedentsMedicauxString);
        System.out.print("Entrez la profession du patient : ");
        String profession = scanner.nextLine();
        Patient newPatient=new Patient(nom, prenom, adresse, generateNewId(), telephone, email, CIN, dateNaissance, mutuelle,
                groupeSanguin, antecedentsMedicaux, profession);
        save2(newPatient);
        return newPatient ;

    }


}

