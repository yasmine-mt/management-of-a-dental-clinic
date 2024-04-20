package src.entity.Classe_G;

/**
 * La classe  Personne représente une personne générique.
 */
public class Personne {
    private String nom;
    private String prenom;
    private String adresse;
    private Long id;
    private String telephone;
    private String email;
    private String CIN;
    public Personne(){}

    public Personne(String nom, String prenom, String adresse, Long id, String telephone, String email, String CIN) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.id = id;
        this.telephone = telephone;
        this.email = email;
        this.CIN = CIN;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCIN() {
        return CIN;
    }

    public void setCIN(String CIN) {
        this.CIN = CIN;
    }
}
