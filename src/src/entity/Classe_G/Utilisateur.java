package src.entity.Classe_G;


import src.entity.enums.Role;


/**
 * La classe User représente un utilisateur, étendant la classe Personne.
 */
public class Utilisateur extends Personne {
    private String username;
    private String password;
    private Role roles;

    public Utilisateur(){}
    public Utilisateur(String username, String password, Role roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Utilisateur(String nom, String prenom, String adresse, Long id, String telephone, String email, String CIN, String username, String password, Role roles) {
        super(nom, prenom, adresse, id, telephone, email, CIN);
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRoles() {
        return roles;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Utilisateur: ");
        sb.append("username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", roles=").append(roles);
        sb.append('\n');
        return sb.toString();
    }
}