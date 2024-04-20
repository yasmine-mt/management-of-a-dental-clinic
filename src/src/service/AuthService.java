package src.service;

import src.Dao.fileBase.UtilisateurDao;
import src.entity.Classe_G.Utilisateur;
import src.entity.enums.Role;
import src.exception.*;

public class AuthService implements AuthInterface{
    private UtilisateurDao utilisateurDao;
    private Utilisateur loggedInUser;

    public UtilisateurDao getUtilisateurDao() {
        return utilisateurDao;
    }

    public void setUtilisateurDao(UtilisateurDao utilisateurDao) {
        this.utilisateurDao = utilisateurDao;
    }

    public Utilisateur getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Utilisateur loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public AuthService(UtilisateurDao utilisateurDao) {
        this.utilisateurDao = utilisateurDao;
    }

    @Override
    public boolean login(String username, String password) throws AuthenticationException {
        try {
            loggedInUser = utilisateurDao.getByUsername(username);
            if (loggedInUser != null) {
                if (loggedInUser.getPassword().equals(password)) {
                    System.out.println("Connexion réussie pour l'utilisateur : " + username);
                    return true;
                } else {
                    throw new AuthenticationException("Mot de passe incorrect pour l'utilisateur : " + username);
                }
            } else {
                throw new AuthenticationException("Nom d'utilisateur inconnu : " + username);
            }
        } catch (DaoException e) {
            throw new AuthenticationException("Erreur lors de l'authentification de l'utilisateur : " + username);
        }
    }

    @Override
    public void logout(String username) {
        System.out.println("Déconnexion de l'utilisateur : " + username);
        loggedInUser = null;
    }


    @Override
    public void Register(Utilisateur utilisateur) throws UserRegistrationException {
        try {
            if (utilisateurDao.findById(utilisateur.getId())!= null) {
                throw new UserRegistrationException("L'utilisateur avec le nom d'utilisateur '" + utilisateur.getUsername() + "' existe déjà.");
            }
            if (utilisateur.getPassword().length() <= 5) {
                throw new InvalidPasswordException("Le mot de passe doit contenir plus de 5 caractères.");
            }

            if (utilisateur.getUsername().length() <= 8 || Character.isDigit(utilisateur.getUsername().charAt(0))) {
                throw new InvalidUsernameException("Le nom d'utilisateur doit dépasser 8 caractères et ne doit pas commencer par un chiffre.");
            }

            utilisateurDao.save(utilisateur);
        } catch (InvalidPasswordException | InvalidUsernameException e) {
            throw new UserRegistrationException("Erreur lors de l'ajout de l'utilisateur : " + utilisateur.getUsername());
        }
    }

    public String getRole(String username) throws AuthenticationException, DaoException {
        Utilisateur user = utilisateurDao.getByUsername(username);
        if (user != null) {
            return user.getRoles().toString();
        }
        throw new AuthenticationException("User not found");
    }


    @Override
    public void deleteUser(String username) throws UserNotFoundException {
        try {
            Utilisateur utilisateur = utilisateurDao.getByUsername(username);
            if (utilisateur == null) {
                throw new UserNotFoundException("Utilisateur avec le nom d'utilisateur '" + username + "' introuvable.");
            }
            utilisateurDao.deleteById(utilisateur.getId());
        } catch (DaoException e) {
            throw new UserNotFoundException("Une erreur s'est produite lors de la suppression de l'utilisateur : " + username);
        }
    }
    public Role getRole() throws AuthenticationException {
        if (loggedInUser != null) {
            return loggedInUser.getRoles();
        }
        throw new AuthenticationException("User not logged in");
    }






}
