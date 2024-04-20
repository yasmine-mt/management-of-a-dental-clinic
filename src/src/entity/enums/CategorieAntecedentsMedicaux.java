package src.entity.enums;

import java.util.ArrayList;
import java.util.List;

public enum CategorieAntecedentsMedicaux {
    MALADIE_CHRONIQUE,CONTRE_INDICATION,AUTRE,MALADIE_HEREDITAIRE,ALLERGIE;
    private String description;
    private Risque risqueAssocie;

    public String getDescription() {
        return description;
    }

    public void setDescrption(String description) {
        this.description = description;
    }

    public Risque getRisqueAssocie() {
        return risqueAssocie;
    }

    public void setRisqueAssocie(Risque risqueAssocie) {
        this.risqueAssocie = risqueAssocie;
    }

    public static List<CategorieAntecedentsMedicaux> convertAntecedentsMedicaux(String antecedentsMedicauxString) {
        String[] antecedentsArray = antecedentsMedicauxString.split(",");
        List<CategorieAntecedentsMedicaux> antecedentsList = new ArrayList<>();

        for (String antecedent : antecedentsArray) {
            try {
                CategorieAntecedentsMedicaux categorie = CategorieAntecedentsMedicaux.valueOf(antecedent.trim());
                antecedentsList.add(categorie);
            } catch (IllegalArgumentException e) {
                CategorieAntecedentsMedicaux newCategorie = addNewCategorie(antecedent.trim());
                antecedentsList.add(newCategorie);
            }
        }

        return antecedentsList;
    }

    public static CategorieAntecedentsMedicaux addNewCategorie(String newCategorie) {
        for (CategorieAntecedentsMedicaux categorie : CategorieAntecedentsMedicaux.values()) {
            if (categorie.name().equalsIgnoreCase(newCategorie)) {
                return categorie;
            }
        }
        try {
            CategorieAntecedentsMedicaux.valueOf(newCategorie);
        } catch (IllegalArgumentException e) {
            CategorieAntecedentsMedicaux.valueOf(newCategorie);
        }

        return CategorieAntecedentsMedicaux.valueOf(newCategorie);
    }

}
