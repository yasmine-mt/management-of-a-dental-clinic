package src.Dao.fileBase;

import src.Dao.IDao;
import src.entity.Classe_G.Utilisateur;
import src.entity.Secretaire;
import src.entity.enums.Role;
import src.exception.DaoException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SecretaireDao implements IDao<Secretaire, Long> {
    private static final File FILE = new File("Files/secretaires.txt");
    private UtilisateurDao utilisateurDao;
    public SecretaireDao(UtilisateurDao utilisateurDao) {
        this.utilisateurDao = utilisateurDao;
    }

    @Override
    public List<Secretaire> findAll() {
        List<Secretaire> secretaires = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            secretaires = reader.lines()
                    .map(this::mapToSecretaire)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return secretaires;
    }

    @Override
    public Secretaire findById(Long id) {
        return findAll().stream()
                .filter(secretary -> secretary.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Secretaire save(Secretaire secretaire) {
            Utilisateur utilisateur = new Utilisateur(
                    secretaire.getUsername(), secretaire.getPassword(), secretaire.getRoles());
            utilisateur = utilisateurDao.save(utilisateur);

            if (utilisateur != null) {
                secretaire.setId(utilisateur.getId());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
            String line = mapToLine(secretaire);
            writer.write(line);
            return secretaire;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return secretaire;
    } else return null;}


    @Override
    public boolean deleteById(Long id) {
        Secretaire toDelete = findById(id);
        if (toDelete != null) {
            Utilisateur utilisateurToDelete = new Utilisateur();
            utilisateurToDelete.setId(toDelete.getId());
            boolean utilisateurDeleted = utilisateurDao.delete(utilisateurToDelete);

            if (utilisateurDeleted) {
                List<Secretaire> secretaires = findAll();
                secretaires.removeIf(secretary -> secretary.getId().equals(id));
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
                    for (Secretaire secretary : secretaires) {
                        String line = mapToLine(secretary);
                        writer.write(line);
                    }
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean delete(Secretaire element) {
        return deleteById(element.getId());
    }

    private String mapToLine(Secretaire secretaire) {
        return secretaire.getId() + "|" +
                secretaire.getUsername() + "|" +
                secretaire.getPassword() + "|" +
                secretaire.getSalaireDeBase() + "|" +
                secretaire.getPrime() + "\n";
    }

    private Secretaire mapToSecretaire(String fileLine) {
        String[] values = fileLine.split("\\|");
        if (values.length >= 5) {
            try {
                Long id = Long.parseLong(values[0]);
                String username = values[1];
                String password = values[2];
                Double salaireDeBase = Double.parseDouble(values[3]);
                Double prime = Double.parseDouble(values[4]);
                Secretaire secretaire = new Secretaire();
                secretaire.setId(id);
                secretaire.setUsername(username);
                secretaire.setPassword(password);
                secretaire.setRoles(Role.Secretaire);
                secretaire.setSalaireDeBase(salaireDeBase);
                secretaire.setPrime(prime);

                return secretaire;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static long generateNextId() {
        long lastId = getLastIdFromFile();
        return lastId + 1;
    }
    private static long getLastIdFromFile() {
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
    public List<Secretaire> saveAll(Secretaire... elements) {
        List<Secretaire> savedSecretaires = new ArrayList<>();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
            for (Secretaire secretaire : elements) {
                Long nextId = generateNextId();
                secretaire.setId(nextId);
                String line = mapToLine(secretaire);
                writer.write(line);
                savedSecretaires.add(secretaire);
            }
        } catch (IOException e) {
            e.printStackTrace();
            savedSecretaires.clear();
        }
        return savedSecretaires;
    }

    @Override
    public boolean update(Secretaire newValueElement) {
        List<Secretaire> secretaires = findAll();
        for (int i = 0; i < secretaires.size(); i++) {
            Secretaire current = secretaires.get(i);
            if (current.getId().equals(newValueElement.getId())) {
                secretaires.set(i, newValueElement);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
                    for (Secretaire secretaire : secretaires) {
                        String line = mapToLine(secretaire);
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
    public static void main(String[] args) {
        // Création d'un DAO d'utilisateur
        UtilisateurDao utilisateurDao = new UtilisateurDao();

        // Création d'un DAO de secrétaire en lui passant le DAO d'utilisateur
        SecretaireDao secretaireDao = new SecretaireDao(utilisateurDao);

        // Création d'un nouvel objet Secretaire
        Secretaire secretaire = new Secretaire();
        secretaire.setUsername("johndoe");
        secretaire.setPassword("password");
        secretaire.setRoles(Role.Secretaire);
        secretaire.setSalaireDeBase(5000.0);
        secretaire.setPrime(1000.0);

        // Enregistrement du secrétaire
        secretaireDao.save(secretaire);

        // Obtention de la liste de tous les secrétaires
        List<Secretaire> secretaires = secretaireDao.findAll();
        System.out.println("Liste de tous les secrétaires : ");
        for (Secretaire sec : secretaires) {
            System.out.println(sec);
        }

//        long idToFind = 1;
//        Secretaire foundSecretaire = secretaireDao.findById(idToFind);
//        if (foundSecretaire != null) {
//            System.out.println("\nSecrétaire trouvé avec l'ID " + idToFind + " : " + foundSecretaire);
//        } else {
//            System.out.println("\nAucun secrétaire trouvé avec l'ID " + idToFind);
//        }
//
//        long idToDelete = 2;
//        boolean deleted = secretaireDao.deleteById(idToDelete);
//        if (deleted) {
//            System.out.println("\nSecrétaire avec l'ID " + idToDelete + " supprimé avec succès !");
//        } else {
//            System.out.println("\nImpossible de supprimer le secrétaire avec l'ID " + idToDelete);
//        }
    }


}
