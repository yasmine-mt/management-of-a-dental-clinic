package src.Dao.fileBase;

import src.Dao.IDao;
import src.entity.enums.Role;
import src.entity.Dentiste;
import src.entity.enums.Specialite;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DentisteDao implements IDao<Dentiste,Long> {

    private static File file = new File("Files/Dentistes.txt");
    private static final String DELIMITER = ";";
    private static final String ROLES_DELIMITER = ",";

    @Override
    public List<Dentiste> findAll() {
        List<Dentiste> dentistes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(DELIMITER);
                Dentiste dentiste = createDentisteFromData(data);
                dentistes.add(dentiste);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dentistes;
    }

    @Override
    public Dentiste findById(Long identifiant) {

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(DELIMITER);
                    Long currentId = Long.parseLong(data[0]);
                    if (currentId.equals(identifiant)) {
                        return createDentisteFromData(data);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        return null;
    }

    @Override
    public Dentiste save(Dentiste newElement) {
        Long newId = generateNewId();
        newElement.setId(newId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String data = createDataFromDentiste(newElement);
            writer.write(data);
            writer.newLine();
            return newElement;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Long generateNewId() {
        List<Dentiste> dentistes = findAll();
        if (!dentistes.isEmpty()) {
            return dentistes.get(dentistes.size() - 1).getId() + 1;
        } else {
            return 1L;
        }
    }


    @Override
    public List<Dentiste> saveAll(Dentiste... elements) {
        List<Dentiste> savedDentistes = new ArrayList<>();
        for (Dentiste dentiste : elements) {
            Dentiste savedDentiste = save(dentiste);
            if (savedDentiste != null) {
                savedDentistes.add(savedDentiste);
            }
        }
        return savedDentistes;
    }



    @Override
    public boolean update(Dentiste newValueElement) {
        List<Dentiste> allDentistes = findAll();
        for (int i = 0; i < allDentistes.size(); i++) {
            if (allDentistes.get(i).getId().equals(newValueElement.getId())) {
                allDentistes.set(i, newValueElement);
                overwriteFile(allDentistes);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteById(Long identifiant) {
        List<Dentiste> allDentistes = findAll();
        for (int i = 0; i < allDentistes.size(); i++) {
            if (allDentistes.get(i).getId().equals(identifiant)) {
                allDentistes.remove(i);
                overwriteFile(allDentistes);
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean delete(Dentiste element) {
        List<Dentiste> allDentistes = findAll();
        if (allDentistes.contains(element)) {
            allDentistes.remove(element);
            overwriteFile(allDentistes);
            return true;
        }
        return false;
    }

    private void overwriteFile(List<Dentiste> dentistes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (Dentiste dentiste : dentistes) {
                String data = createDataFromDentiste(dentiste);
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Dentiste createDentisteFromData(String[] data) {
        Dentiste dentiste = new Dentiste();
        dentiste.setId(Long.parseLong(data[0]));
        dentiste.setNom(data[1]);
        dentiste.setPrenom(data[2]);
        dentiste.setSalaireDeBase(Double.parseDouble(data[3]));
        dentiste.setSpecialite(stringToSpecialite(data[4]));
        dentiste.setUsername(data[5]);
        dentiste.setPassword(data[6]);
        return dentiste;
    }

    private String createDataFromDentiste(Dentiste dentiste) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dentiste.getId()).append(DELIMITER);
        stringBuilder.append(dentiste.getNom()).append(DELIMITER);
        stringBuilder.append(dentiste.getPrenom()).append(DELIMITER);
        stringBuilder.append(dentiste.getSalaireDeBase()).append(DELIMITER);
        stringBuilder.append(specialiteToString(dentiste.getSpecialite())).append(DELIMITER);
        stringBuilder.append(dentiste.getUsername()).append(DELIMITER);
        stringBuilder.append(dentiste.getPassword()).append(DELIMITER);
        return stringBuilder.toString();
    }





    private String specialiteToString(Specialite specialite) {
        return specialite != null ? specialite.name() : "";
    }

    private Specialite stringToSpecialite(String specialiteString) {
        try {
            return Specialite.valueOf(specialiteString);
        } catch (IllegalArgumentException e) {
            return Specialite.AUTRE;
        }
    }


            public static void main(String[] args) {
                DentisteDao dentisteDao = new DentisteDao();

                // Creating a new Dentiste
                Dentiste newDentiste = new Dentiste();
                newDentiste.setNom("Doe");
                newDentiste.setPrenom("John");
                newDentiste.setSalaireDeBase(5000.0);
                newDentiste.setSpecialite(Specialite.ORTHODONTIE);
                newDentiste.setUsername("johndoe");
                newDentiste.setPassword("password");
                dentisteDao.save(newDentiste);
                Dentiste retrievedDentiste = dentisteDao.findById(1L);
                if (retrievedDentiste != null) {
                    System.out.println("Retrieved Dentiste:");
                    System.out.println(retrievedDentiste);
                }

                if (retrievedDentiste != null) {
                    retrievedDentiste.setSalaireDeBase(6000.0);
                    dentisteDao.update(retrievedDentiste);
                    System.out.println("Dentiste Updated");
                }

                dentisteDao.deleteById(1L);
                System.out.println("Dentiste Deleted");

                List<Dentiste> allDentistes = dentisteDao.findAll();
                if (!allDentistes.isEmpty()) {
                    System.out.println("All Dentistes:");
                    for (Dentiste dentiste : allDentistes) {
                        System.out.println(dentiste);
                    }
                }
            }
        }

