package src.service;

import src.entity.Classe_G.Utilisateur;
import src.exception.*;

public interface AuthInterface {
    boolean login(String username, String password) throws AuthenticationException;
    void logout(String username);
    void Register(Utilisateur utilisateur) throws UserRegistrationException, InvalidPasswordException, InvalidUsernameException;
    void deleteUser(String username) throws UserNotFoundException;


}
