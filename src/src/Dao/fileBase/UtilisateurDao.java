package src.Dao.fileBase;


import src.Dao.IDao;
import src.entity.Classe_G.Utilisateur;
import src.entity.enums.Role;
import src.exception.DaoException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class UtilisateurDao implements IDao<Utilisateur, Long> {
    File userTable = new File("Files/users.txt");


    private Utilisateur mapToUser(String fileLine) {
        Utilisateur user = null;
        try {
            String[] values = fileLine.split("\\|");

            if (values.length >= 10) {
                long id = Long.parseLong(values[0]);
                String username = values[1];
                String password = values[2];
                String firstName = values[3].equals("null") ? null : values[3];
                String lastName = values[4].equals("null") ? null : values[4];
                String address = values[5].equals("null") ? null : values[5];
                String email = values[6].equals("null") ? null : values[6];
                String telephone = values[7].equals("null") ? null : values[7];
                String CIN = values[8].equals("null") ? null : values[8];
                Role role = Role.Secretaire;

                if (values.length > 9 && !values[9].equals("null")) {
                    role = values[9].equals("A") ? Role.Administateur : Role.Secretaire;
                }

                user = new Utilisateur(lastName, firstName, address, id, telephone, email, CIN, username, password, role);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return user;
    }

    private String mapToLine(Utilisateur user){
        return    (user.getId())    + "|"
                + (user.getUsername() + "|"
                + (user.getPassword())  + "|"
                + (user.getPrenom() == null ? "null" : user.getPrenom()) + "|"
                + (user.getNom()  == null ? "null" : user.getNom())  + "|"
                + (user.getCIN()       == null ? "null" : user.getCIN())       + "|"
                + (user.getRoles()      == null ? "null" : user.getRoles().toString().charAt(0)));
    }
    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> users = new ArrayList<>();

        try {
            var lines = Files.readAllLines(userTable.toPath());
            lines.remove(0);
            users = lines.stream()
                    .map(this::mapToUser)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }


    @Override
    public Utilisateur findById(Long identifiant) {
        return findAll()
                .stream()
                .filter(user-> user.getId() == identifiant)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Utilisateur save(Utilisateur newElement) {
        try {
            var maxId =  findAll()
                    .stream()
                    .mapToLong(Utilisateur::getId)
                    .max().orElse(0);
            newElement.setId(++maxId);
            Files.writeString( userTable.toPath(),
                    mapToLine(newElement),
                    StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return newElement;
    }

    @Override
    public List<Utilisateur> saveAll(Utilisateur... elements) {
        return new ArrayList<>(Arrays.asList(elements)).stream()
                .map(user->save(user)).collect(Collectors.toList());
    }
    @Override
    public boolean update(Utilisateur newValuesElement) {
        try {
            Path filePath = userTable.toPath();
            List<String> lines = Files.readAllLines(filePath);

            var id = newValuesElement.getId();
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                var lineTab = line.split("\\|");
                if (lineTab.length > 0 && lineTab[0].trim().equals(id + "")) {
                    lines.set(i, mapToLine(newValuesElement)); // Update the line
                    break;
                }
            }

            Files.write(filePath, lines);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean delete(Utilisateur element) {
        return deleteById(element.getId());
    }

    @Override
    public boolean deleteById(Long identifiant) {
        boolean removed = false;
        try {
            var lines = Files.readAllLines(userTable.toPath());
            removed = lines.removeIf(line -> {
                var lineTab = line.split("\\|");
                if(lineTab[0].trim().equals(identifiant+""))
                    return true;
                else return false;
            });
            userTable.delete();
            var newContent = String.join(System.lineSeparator(), lines);
            newContent += "\n";
            Files.writeString( userTable.toPath(),
                    newContent,
                    StandardOpenOption.CREATE_NEW);
            removed = true;
        } catch (IOException e){
            removed =false; }
        return removed;
    }
    public Utilisateur getByUsername(String username) throws DaoException {
        try (BufferedReader reader = new BufferedReader(new FileReader(userTable))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userAttributes = line.split("\\|");
                if (userAttributes.length >= 3 && userAttributes[1].equals(username)) {
                    String password = userAttributes[2];
                    Role role;
                    if ("A".equals(userAttributes[9])) {
                        role = Role.Administateur;
                    } else if ("S".equals(userAttributes[9])) {
                        role = Role.Secretaire;
                    } else {
                        throw new DaoException("Invalid role value for username: " + username);
                    }

                    return new Utilisateur(username, password, role);
                }
            }
        } catch (IOException e) {
            throw new DaoException("Erreur lors de la récupération de l'utilisateur par son nom d'utilisateur : " + username, e);
        }
        return null;
    }

    public static void main(String[] args) {
        UtilisateurDao utilisateurDao = new UtilisateurDao();


        // Retrieve and display all users
        List<Utilisateur> allUsers = utilisateurDao.findAll();
        System.out.println("All Users:");
        for (Utilisateur user : allUsers) {
            System.out.println(user);
        }
        Utilisateur foundUser = utilisateurDao.findById(1L);
        if (foundUser != null) {
            System.out.println("\nUser with ID 1 found: " + foundUser);
        } else {
            System.out.println("\nUser with ID 1 not found.");
        }
        if (foundUser != null) {
            foundUser.setPrenom("UpdatedFirstName");
            foundUser.setNom("UpdatedLastName");
            utilisateurDao.update(foundUser);
            System.out.println("\nUser with ID 1 updated: " + foundUser);
        }
    }
    }

