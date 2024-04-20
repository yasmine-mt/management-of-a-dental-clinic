package src.Dao.fileBase;

import src.Dao.IDao;
import src.entity.Caisse;
import src.entity.SituationFinanciere;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CaisseDao implements IDao<Caisse, Long> {
    private static final File FILE = new File("Files/Caisse.txt");
   private  final SituationFinanciereDao  situationFinanciereDao;

    public CaisseDao(SituationFinanciereDao situationFinanciereDao) {
        this.situationFinanciereDao = situationFinanciereDao;
    }

    @Override
        public List<Caisse> findAll() {
            List<Caisse> caisses = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
                caisses = reader.lines()
                        .map(this::mapToCaisse)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return caisses;
        }

        @Override
        public Caisse findById(Long id) {
            return findAll().stream()
                    .filter(caisse -> id.equals(caisse.getIdCaisse()))
                    .findFirst()
                    .orElse(null);
        }
        @Override
        public Caisse save(Caisse newElement) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
                Long nextId = generateNextId();
                newElement.setIdCaisse(nextId);
                String line = mapToLine(newElement);
                writer.write(line);
                return newElement;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public List<Caisse> saveAll(Caisse... elements) {
            List<Caisse> savedCaisseList = new ArrayList<>();
            for (Caisse caisse : elements) {
                Caisse savedCaisse = save(caisse);
                if (savedCaisse != null) {
                    savedCaisseList.add(savedCaisse);
                }
            }
            return savedCaisseList;
        }

        @Override
        public boolean update(Caisse newValueElement) {
            List<Caisse> caisses = findAll();
            for (int i = 0; i < caisses.size(); i++) {
                Caisse current = caisses.get(i);
                if (current.getIdCaisse().equals(newValueElement.getIdCaisse())) {
                    caisses.set(i, newValueElement);
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
                        for (Caisse caisse : caisses) {
                            String line = mapToLine(caisse);
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

    @Override
    public boolean deleteById(Long id) {
        List<Caisse> caisses = findAll();
        caisses.removeIf(caisse -> caisse.getIdCaisse().equals(id));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (Caisse caisse : caisses) {
                String line = mapToLine(caisse);
                writer.write(line);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Caisse element) {
        if (element != null) {
            return deleteById(element.getIdCaisse());
        }
        return false;
    }
    public Caisse getCaisse(Long IdCaisse) {
        return findAll().stream()
                .filter(caisse -> IdCaisse.equals(caisse.getIdCaisse()))
                .findFirst()
                .orElse(null);
    }

    private Long generateNextId() {
            List<Caisse> caisses = findAll();
            return caisses.isEmpty() ? 1L : caisses.get(caisses.size() - 1).getIdCaisse() + 1;
        }
    private String mapToLine(Caisse caisse) {
        StringBuilder sb = new StringBuilder();
        sb.append(caisse.getIdCaisse()).append("|")
                .append(caisse.recetteDuJour(LocalDate.now())).append("|")
                .append(caisse.recetteDumois(LocalDate.now().getYear(), LocalDate.now().getMonthValue())).append("|")
                .append(caisse.recetteDeLAnnee(LocalDate.now().getYear())).append("|");

        List<SituationFinanciere> situations = caisse.getSituationFinanciere();
        if (situations != null && !situations.isEmpty()) {
            String situationIds = situations.stream()
                    .map(situation -> String.valueOf(situation.getIdSituationFinanciere()))
                    .collect(Collectors.joining(","));
            sb.append(situationIds);
        }
        sb.append("\n");
        return sb.toString();
    }

    private Caisse mapToCaisse(String fileLine) {
        String[] values = fileLine.split("\\|");
        if (values.length >= 4) {
            try {
                Long id = Long.parseLong(values[0]);
                Double recetteDuJour = Double.parseDouble(values[1]);
                Double recetteDuMois = Double.parseDouble(values[2]);
                Double recetteDeLAnnee = Double.parseDouble(values[3]);

                List<SituationFinanciere> situationFinancieres = new ArrayList<>();
                if (values.length >= 5 && !values[4].isEmpty()) {
                    String[] situationIds = values[4].split(",");
                    for (String situationId : situationIds) {
                        Long situationIdLong = Long.parseLong(situationId);
                        SituationFinanciere situationFinanciere = situationFinanciereDao.findById(situationIdLong);
                        if (situationFinanciere != null) {
                            situationFinancieres.add(situationFinanciere);
                        }
                    }
                }
                return new Caisse(situationFinancieres, recetteDuJour, recetteDuMois, recetteDeLAnnee, id);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public Double SommerecetteDuJour(LocalDate date){
        List<Caisse> Caisses = findAll();
        Double Somme = 0D;
        for (Caisse caisse :Caisses) {
            Somme+= caisse.recetteDuJour(date);

        }
        return Somme;
    }
    public Double SommerecetteDuMois(LocalDate date){
        List<Caisse> Caisses = findAll();
        Double Somme = 0D;
        for (Caisse caisse :Caisses) {
            Somme+= caisse.recetteDumois(date.getYear(),date.getMonthValue());

        }
        return Somme;
    }
    public Double SommerecetteDeLAnnee(LocalDate date){
        List<Caisse> Caisses = findAll();
        Double Somme = 0D;
        for (Caisse caisse :Caisses) {
            Somme+= caisse.recetteDeLAnnee(date.getYear());

        }
        return Somme;
    }
    public static void main(String[] args) {
        SituationFinanciereDao situationFinanciereDao = new SituationFinanciereDao(new DossierMedicaleDao(),new FactureDao());
        CaisseDao caisseDao = new CaisseDao(situationFinanciereDao);

        // Test findAll
        List<Caisse> allCaisses = caisseDao.findAll();
        System.out.println("All Caisses:");
        for (Caisse caisse : allCaisses) {
            System.out.println(caisse);
        }

//        // Test findById (assuming you know an existing ID)
//        Long idToFind = 1L; // Change this ID according to your entries
//        Caisse foundCaisse = caisseDao.findById(idToFind);
//        if (foundCaisse != null) {
//            System.out.println("Caisse found with ID " + idToFind + ": " + foundCaisse);
//        } else {
//            System.out.println("Caisse with ID " + idToFind + " not found.");
//        }
//
//        // Test save (assuming you have a Caisse instance to save)
//        Caisse caisseToSave = new Caisse(/* initialize your Caisse object */);
//        Caisse savedCaisse = caisseDao.save(caisseToSave);
//        System.out.println("Saved Caisse: " + savedCaisse);
//
//        // Test update (assuming you have a Caisse object with updated values)
//        Caisse caisseToUpdate = new Caisse(/* initialize your updated Caisse object */);
//        boolean isUpdated = caisseDao.update(caisseToUpdate);
//        System.out.println("Is Caisse updated? " + isUpdated);
//
//        // Test delete (assuming you have an ID to delete)
//        Long idToDelete = 2L; // Change this ID according to your entries
//        boolean isDeleted = caisseDao.deleteById(idToDelete);
//        System.out.println("Is Caisse with ID " + idToDelete + " deleted? " + isDeleted);
    }
    }
