package src.Dao.fileBase;
import src.Dao.IDao;
import src.entity.DossierMedicale;
import src.entity.SituationFinanciere;
import src.entity.Facture;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SituationFinanciereDao implements IDao<SituationFinanciere, Long> {
    private static final File FILE = new File("Files/situationsFinanciere.txt");
    private final DossierMedicaleDao dossierMedicaleDao;
    private final FactureDao factureDao;

    public SituationFinanciereDao(DossierMedicaleDao dossierMedicaleDao, FactureDao factureDao) {
        this.dossierMedicaleDao = dossierMedicaleDao;
        this.factureDao = factureDao;
    }


    private String mapToLine(SituationFinanciere situationFinanciere) {
        String dossierMedicaleId = situationFinanciere.getDossierMedicale() != null ?
                String.valueOf(situationFinanciere.getDossierMedicale().getNumeroDossier()) : "null";
        String factureIds = situationFinanciere.getFactures().stream()
                .map(facture -> String.valueOf(facture.getIdFacture()))
                .collect(Collectors.joining(","));
        return situationFinanciere.getIdSituationFinanciere() + "|" +
                situationFinanciere.getDateCreation() + "|" +
                dossierMedicaleId + "|" +
                factureIds + "\n";
    }

    private SituationFinanciere mapToSituationFinanciere(String fileLine) {
        String[] values = fileLine.split("\\|");
        if (values.length >= 4) {
            try {
                Long id = Long.parseLong(values[0]);
                LocalDate dateCreation = LocalDate.parse(values[1]);
                DossierMedicale dossierMedicale = null;
                if (!values[2].isEmpty() && !values[2].equals("null")) {
                    Long dossierMedicaleId = Long.parseLong(values[2]);
                    dossierMedicale = dossierMedicaleDao.findById(dossierMedicaleId);
                }
                List<Facture> factures = new ArrayList<>();
                if (!values[3].isEmpty() && !values[3].equals("null")) {
                    String[] factureIds = values[3].split(",");
                    for (String factureId : factureIds) {
                        Long factureIdLong = Long.parseLong(factureId);
                        Facture facture = factureDao.findById(factureIdLong);
                        if (facture != null) {
                            factures.add(facture);
                        }
                    }
                }
                return new SituationFinanciere(dossierMedicale, dateCreation, id, factures);
            } catch (NumberFormatException | DateTimeParseException e) {
                e.printStackTrace();

            }
        }
        return null;
    }

    @Override
    public List<SituationFinanciere> findAll() {
        List<SituationFinanciere> situationsFinancieres = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            situationsFinancieres = reader.lines()
                    .map(this::mapToSituationFinanciere)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return situationsFinancieres;
    }

    @Override
    public SituationFinanciere findById(Long id) {
        return findAll().stream()
                .filter(sf -> sf.getIdSituationFinanciere().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public SituationFinanciere save(SituationFinanciere situationFinanciere) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
            Long nextId = generateNextId();
            situationFinanciere.setIdSituationFinanciere(nextId);
            String line = mapToLine(situationFinanciere);
            writer.write(line);
            return situationFinanciere;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        SituationFinanciere toDelete = findById(id);
        if (toDelete != null) {
            return delete(toDelete);
        }
        return false;
    }

    @Override
    public boolean delete(SituationFinanciere element) {
        List<SituationFinanciere> situationsFinancieres = findAll();
        situationsFinancieres.removeIf(situation -> {
            if (situation != null && element != null) {
                return situation.getIdSituationFinanciere().equals(element.getIdSituationFinanciere());
            }
            return false;
        });

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (SituationFinanciere situation : situationsFinancieres) {
                String line = mapToLine(situation);
                writer.write(line);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }




    @Override
    public List<SituationFinanciere> saveAll(SituationFinanciere... elements) {
        List<SituationFinanciere> existingSituations = findAll();
        List<SituationFinanciere> savedSituations = new ArrayList<>();

        long maxId = existingSituations.stream()
                .mapToLong(SituationFinanciere::getIdSituationFinanciere)
                .max()
                .orElse(0);

        for (SituationFinanciere situation : elements) {
            situation.setIdSituationFinanciere(++maxId);
            existingSituations.add(situation);
            savedSituations.add(situation);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (SituationFinanciere situation : existingSituations) {
                String line = mapToLine(situation);
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            savedSituations.clear();
        }

        return savedSituations;
    }



    @Override
    public boolean update(SituationFinanciere newValueElement) {
        List<SituationFinanciere> situations = findAll();
        for (int i = 0; i < situations.size(); i++) {
            SituationFinanciere current = situations.get(i);
            if (current.getIdSituationFinanciere().equals(newValueElement.getIdSituationFinanciere())) {
                situations.set(i, newValueElement);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
                    for (SituationFinanciere situation : situations) {
                        String line = mapToLine(situation);
                        writer.write(line);
                    }
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }
    public SituationFinanciere findByPatientId(Long patientId) {
        List<SituationFinanciere> allSituations = findAll();
        return allSituations.stream()
                .filter(situation -> situation.getDossierMedicale().getPatient().getId().equals(patientId))
                .findFirst()
                .orElse(null);
    }

    private Long generateNextId() {
        List<SituationFinanciere> situationsFinancieres = findAll();
        return situationsFinancieres.isEmpty() ? 1L : situationsFinancieres.get(situationsFinancieres.size() - 1).getIdSituationFinanciere() + 1;
    }

        public static void main(String[] args) {
            // Create instances of DossierMedicaleDao and FactureDao
            DossierMedicaleDao dossierMedicaleDao = new DossierMedicaleDao();
            FactureDao factureDao = new FactureDao();

            // Create an instance of SituationFinanciereDao
            SituationFinanciereDao situationFinanciereDao = new SituationFinanciereDao(dossierMedicaleDao, factureDao);

//            // Example: Retrieve all SituationFinanciere objects
//            List<SituationFinanciere> allSituations = situationFinanciereDao.findAll();
//            for (SituationFinanciere situation : allSituations) {
//                System.out.println(situation); // Assuming toString method is overridden in SituationFinanciere class
//            }

            // Example: Find a specific SituationFinanciere by ID
//            Long idToFind = 1L;
//            SituationFinanciere foundSituation = situationFinanciereDao.findById(idToFind);
//            if (foundSituation != null) {
//                System.out.println("Found Situation: " + foundSituation);
//            } else {
//                System.out.println("Situation with ID " + idToFind + " not found.");
//            }



//            // Example: Delete a SituationFinanciere by ID
//            Long idToDelete = 2L;
//            boolean isDeleted = situationFinanciereDao.deleteById(idToDelete);
//            if (isDeleted) {
//                System.out.println("Situation with ID " + idToDelete + " deleted successfully.");
//            } else {
//                System.out.println("Failed to delete Situation with ID " + idToDelete);
//            }
        }





}
