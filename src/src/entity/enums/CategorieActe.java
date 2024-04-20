package src.entity.enums;

public enum CategorieActe {
    PREVENTION(50.0, "Prévention"),
    CHIRURGIE(200.0, "Chirurgie"),
    ORTHODONTIE(150.0, "Orthodontie"),
    IMPLANTOLOGIE(300.0, "Implantologie"),
    SOINS_DES_CARIES(80.0, "Soins des caries"),
    PROTHESES_DENTAIRES(250.0, "Prothèses dentaires"),
    DIAGNOSTIC(100.0, "Diagnostic"),
    SOINS_DES_TISSUS_MOUS(120.0, "Soins des tissus mous");

    private final Double prixDeBase;
    private final String libelle;

    CategorieActe(Double prixDeBase, String libelle) {
        this.prixDeBase = prixDeBase;
        this.libelle = libelle;
    }

    public Double getPrixDeBase() {
        return prixDeBase;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return name() + " : (Prix de base = " + prixDeBase + ", Libellé = \"" + libelle + "\")";
    }
    }
