package src.entity;

import src.entity.Classe_G.Personne;
import src.entity.enums.CategorieAntecedentsMedicaux;
import src.entity.enums.GroupeSanguin;
import src.entity.enums.Mutuelle;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * La classe Patient représente une personne recevant des soins médicaux dans un cabinet médical ou dentaire.
 */
public class Patient extends Personne {

    private LocalDate dateNaissance;
    private Mutuelle mutuelle;
    private GroupeSanguin groupeSanguin;
    private List<CategorieAntecedentsMedicaux> AntecedentsMedicaux;

    private String profession ;

    public Patient() {}

    public Patient(String nom, String prenom, String adresse, Long id, String telephone, String email, String CIN, LocalDate dateNaissance, Mutuelle mutuelle, GroupeSanguin groupeSanguin, List<CategorieAntecedentsMedicaux> antecedentsMedicaux, String profession) {
        super(nom, prenom, adresse, id, telephone, email, CIN);
        this.dateNaissance = dateNaissance;
        this.mutuelle = mutuelle;
        this.groupeSanguin = groupeSanguin;
        AntecedentsMedicaux = antecedentsMedicaux;
        this.profession = profession;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Mutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(Mutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public GroupeSanguin getGroupeSanguin() {
        return groupeSanguin;
    }

    public List<CategorieAntecedentsMedicaux> getAntecedentsMedicaux() {
        return AntecedentsMedicaux;
    }

    public void setAntecedentsMedicaux(List<CategorieAntecedentsMedicaux> antecedentsMedicaux) {
        AntecedentsMedicaux = antecedentsMedicaux;
    }

    public void setGroupeSanguin(GroupeSanguin groupeSanguin) {
        this.groupeSanguin = groupeSanguin;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(dateNaissance, patient.dateNaissance) && mutuelle == patient.mutuelle && groupeSanguin == patient.groupeSanguin && Objects.equals(AntecedentsMedicaux, patient.AntecedentsMedicaux) && Objects.equals(profession, patient.profession);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateNaissance, mutuelle, groupeSanguin, AntecedentsMedicaux, profession);
    }

    @Override
    public String toString() {
        return super.getId() + "|" + super.getNom() + "|" + super.getPrenom() + "|" +super.getAdresse() + "|" + super.getTelephone() + "|" + super.getEmail()+ "|" +super.getCIN() + "|" +dateNaissance + "|" + mutuelle + "|" + groupeSanguin + "|"+ "AUTRE" +"|"+ profession;

    }


}
