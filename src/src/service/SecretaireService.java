package src.service;

import src.Dao.fileBase.*;
import src.entity.*;
import src.entity.enums.StatutPaiement;
import src.entity.enums.TypePaiement;
import src.exception.ConsultationNonTrouveeException;
import src.exception.FormulaireException;
import src.exception.SecretaireNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class SecretaireService implements SecretaireInterface {
    private SecretaireDao secretaireDao;
    private PatientDao patientDao;
    private DentisteDao dentisteDao;
    private SituationFinanciereDao situationFinanciereDao;
    private FactureDao factureDao;
    private CaisseDao caisseDao;
    private DossierMedicaleDao dossierMedicalDao;
    private ConsultationDao consultationDao;

    public SecretaireService(
            SecretaireDao secretaireDao,
            PatientDao patientDao,
            DentisteDao dentisteDao,
            SituationFinanciereDao situationFinanciereDao,
            FactureDao factureDao,
            CaisseDao caisseDao,
            DossierMedicaleDao dossierMedicalDao,
            ConsultationDao consultationDao) {
        this.secretaireDao = secretaireDao;
        this.patientDao = patientDao;
        this.dentisteDao = dentisteDao;
        this.situationFinanciereDao = situationFinanciereDao;
        this.factureDao = factureDao;
        this.caisseDao = caisseDao;
        this.dossierMedicalDao = dossierMedicalDao;
        this.consultationDao = consultationDao;
    }

    public SecretaireDao getSecretaireDao() {
        return secretaireDao;
    }

    public void setSecretaireDao(SecretaireDao secretaireDao) {
        this.secretaireDao = secretaireDao;
    }

    public PatientDao getPatientDao() {
        return patientDao;
    }

    public void setPatientDao(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    public SecretaireService(SecretaireDao secretaireDao) {
        this.secretaireDao = secretaireDao;
    }

    public List<Secretaire> getAllSecretaires() {
        return secretaireDao.findAll();
    }
    public Secretaire getSecretaireById(Long id) throws SecretaireNotFoundException {
        Secretaire secretaire = secretaireDao.findById(id);
        if (secretaire == null) {
            throw new SecretaireNotFoundException("Secretaire inexistante avec l'identifiant " + id);
        }
        return secretaire;
    }



    @Override
    public void updatePatient(Patient patient) throws FormulaireException {
        try {
            System.out.println("");
            patientDao.updatePatientAttribute(patientDao, new Scanner(System.in));
        } catch (Exception e) {
            throw new FormulaireException("Error updating patient", e);
        }
    }

    @Override
    public void deletePatient(long patientId) throws FormulaireException {
        System.out.println("Suppression d'un patient :");
        Patient patientToDelete = patientDao.findById(patientId);
        if (patientToDelete != null) {
            if (patientDao.delete(patientToDelete)) {
                System.out.println("Le patient a été supprimé avec succès !");
            } else {
                throw new FormulaireException("La suppression du patient a échoué.");
            }
        } else {
            throw new FormulaireException("Aucun patient trouvé avec cet ID.");
        }
    }
    public void CreationPatient() {
        patientDao.createPatientFromInput();
    }
    @Override
    public void createPatient(Patient patient) throws FormulaireException {
        try {
            if (patientDao.findById(patient.getId()) == null) {
                patientDao.save(patient);
            } else {
                throw new FormulaireException("Un patient avec le même identifiant existe déjà");
            }
        } catch (Exception e) {
            throw new FormulaireException("Erreur lors de la création du patient", e);
        }
    }


    @Override
    public void createDossierMedical(DossierMedicale dossierMedical, long patientId) throws FormulaireException {
        Patient patient = patientDao.findById(patientId);

        if (patient != null) {
            if (dossierMedicalDao.findByIdPatient(patientId) == null) {
                dossierMedical.setPatient(patient);
                dossierMedicalDao.save(dossierMedical);
            } else {
                throw new FormulaireException("Un dossier médical pour ce patient existe déjà.");
            }
        } else {
            throw new FormulaireException("Patient non trouvé avec l'ID fourni.");
        }
    }
    public void createDossierMedical() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez le numéro du dossier : ");
        long numeroDossier = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Entrez l'ID du patient : ");
        long patientId = scanner.nextLong();
        scanner.nextLine();

        Patient patient = patientDao.findById(patientId);

        if (patient != null) {
                System.out.print("Entrez l'ID du dentiste traitant : ");
                long dentisteId = scanner.nextLong();
                scanner.nextLine();
                List<Consultation> consultations= new ArrayList<>();
                Dentiste dentiste = dentisteDao.findById(dentisteId);

                if (dentiste != null) {
                    DossierMedicale dossierMedical = new DossierMedicale(numeroDossier,patient,dentiste,consultations,LocalDate.now(),StatutPaiement.IMPAYE);
                    try {
                        createDossierMedical(dossierMedical, patientId);
                        System.out.println("Le dossier médical a été créé avec succès !");
                    } catch (FormulaireException e) {
                        System.out.println("Erreur lors de la création du dossier médical : " + e.getMessage());
                    }
                } else {
                    System.out.println("Dentiste non trouvé avec l'ID fourni.");
                }
            } else {
                System.out.println("Patient non trouvé avec l'ID fourni.");
            }
        }


    @Override
    public void updateDossierMedical(DossierMedicale dossierMedical) throws FormulaireException {
        try {
            boolean updated = dossierMedicalDao.update(dossierMedical);
            if (!updated) {
                throw new FormulaireException("Le dossier médical n'a pas été mis à jour.");
            }
        } catch (Exception e) {
            throw new FormulaireException("Erreur lors de la mise à jour du dossier médical", e);
        }
    }
    public void updateDossierMedical() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("Mise à jour des informations d'un dossier Medical :");
            System.out.print("Entrez l'ID du dossier à mettre à jour (ou entrez '0' pour quitter) : ");
            long idToUpdate = scanner.nextLong();
            scanner.nextLine();
            if (idToUpdate == 0) {
                exit = true;
                break;
            }
            DossierMedicale existing = dossierMedicalDao.findById(idToUpdate);
            if (existing != null) {
                System.out.println("Dossier trouvé : ");
                System.out.print("Choisissez une option : ");
                System.out.println("Quel attribut souhaitez-vous mettre à jour ?");
                System.out.println("1. Date de création");
                System.out.println("2. Statut de paiement");
                System.out.println("0. Quitter la mise à jour");
                System.out.print("Choisissez le numéro de l'attribut à mettre à jour : ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Entrez la nouvelle date de création (AAAA-MM-JJ) : ");
                        String newDateCreationString = scanner.nextLine();
                        LocalDate newDateCreation = LocalDate.parse(newDateCreationString);
                        existing.setDateCreation(newDateCreation);
                        break;
                    case 2:
                        System.out.println("Entrez le nouveau statut de paiement : ");
                        System.out.println("Options disponibles : ");

                        for (StatutPaiement sp : StatutPaiement.values()) {
                            System.out.println(sp.name());
                        }
                        System.out.print("Entrez le statut de paiement : ");
                        String statutPaiementString = scanner.nextLine();
                        StatutPaiement statutPaiement = StatutPaiement.valueOf(statutPaiementString);
                        existing.setStatutPaiement(statutPaiement);
                        break;
                    case 0:
                        exit = true;
                        break;
                    default:
                        System.out.println("Choix invalide !");
                        break;
                }
            } else {
                System.out.println("Aucun patient trouvé avec cet ID.");
            }
    }}
    @Override
    public void deleteDossierMedical(long dossierMedicalId) throws FormulaireException {
        try {
            if (!dossierMedicalDao.deleteById(dossierMedicalId)) {
                throw new FormulaireException("Le dossier médical n'a pas été supprimé.");
            }
        } catch (Exception e) {
            throw new FormulaireException("Erreur lors de la suppression du dossier médical", e);
        }

    }


    public void deleteDossierMedical() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez l'identifiant du dossier médical à supprimer : ");
        long dossierMedicalId = scanner.nextLong();
        scanner.nextLine();

        try {
            deleteDossierMedical(dossierMedicalId);
            System.out.println("Le dossier médical avec l'identifiant " + dossierMedicalId + " a été supprimé avec succès.");
        } catch (FormulaireException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    @Override
    public SituationFinanciere createSituationFinanciere() throws FormulaireException {
        try {
            Scanner scanner=new Scanner(System.in);
            System.out.println("Veuillez saisir le numéro de dossier médical : ");
            Long numeroDossier = scanner.nextLong();
            scanner.nextLine();
            DossierMedicale dossierMedicale = dossierMedicalDao.findById(numeroDossier);
            if (dossierMedicale != null) {
                SituationFinanciere nouvelleSituationFinanciere = new SituationFinanciere();
                nouvelleSituationFinanciere.setDossierMedicale(dossierMedicale);
                nouvelleSituationFinanciere.setDateCreation(LocalDate.now());
                nouvelleSituationFinanciere.setFactures(new ArrayList<>());

                SituationFinanciere savedSituationFinanciere = situationFinanciereDao.save(nouvelleSituationFinanciere);

                if (savedSituationFinanciere != null) {
                    return savedSituationFinanciere;
                } else {
                    throw new FormulaireException("La sauvegarde de la situation financière a échoué");
                }
            } else {
                throw new FormulaireException("Aucun dossier médical trouvé avec le numéro fourni");
            }
        } catch (InputMismatchException e) {
            throw new FormulaireException("Erreur lors de la saisie du numéro de dossier médical", e);
        } catch (Exception e) {
            throw new FormulaireException("Erreur lors de la création de la situation financière", e);
        }
    }

    @Override
    public Caisse createCaisse(Long situationFinanciereId) throws FormulaireException {
        try {
            SituationFinanciere situationFinanciere = situationFinanciereDao.findById(situationFinanciereId);
            if (situationFinanciere != null) {
                Caisse nouvelleCaisse = new Caisse();
                List<SituationFinanciere> listeSituationFinanciere = new ArrayList<>();
                listeSituationFinanciere.add(situationFinanciere);
                nouvelleCaisse.setSituationFinanciere(listeSituationFinanciere);
                nouvelleCaisse.setRecetteDuJour(0.0);
                nouvelleCaisse.setRecetteDumois(0.0);
                nouvelleCaisse.setRecetteDeLAnnee(0.0);

                Caisse savedCaisse = caisseDao.save(nouvelleCaisse);

                if (savedCaisse != null) {
                    return savedCaisse;
                } else {
                    throw new FormulaireException("La sauvegarde de la caisse a échoué");
                }
            } else {
                throw new FormulaireException("La situation financière avec l'identifiant fourni n'existe pas");
            }
        } catch (Exception e) {
            throw new FormulaireException("Erreur lors de la création de la caisse", e);
        }
    }
    public Facture createFacture(Long situationFinanciereId, Long consultationId) throws FormulaireException {
        try {
            SituationFinanciere situationFinanciere = situationFinanciereDao.findById(situationFinanciereId);
            if (situationFinanciere != null) {
                Consultation consultation = consultationDao.findById(consultationId);
                if (consultation != null) {
                    Facture nouvelleFacture = new Facture();
                    nouvelleFacture.setIdsituationFinanciere(situationFinanciereId);
                    nouvelleFacture.setIdConsultation(consultationId);
                    nouvelleFacture.setDateFacturation(LocalDate.now());
                    nouvelleFacture.setTypePaiement(TypePaiement.AUTRE);

                    Facture savedFacture = factureDao.save(nouvelleFacture);

                    if (savedFacture != null) {
                        return savedFacture;
                    } else {
                        throw new FormulaireException("La sauvegarde de la facture a échoué");
                    }
                } else {
                    throw new FormulaireException("La consultation avec l'identifiant fourni n'existe pas");
                }
            } else {
                throw new FormulaireException("La situation financière avec l'identifiant fourni n'existe pas");
            }
        } catch (Exception e) {
            throw new FormulaireException("Erreur lors de la création de la facture", e);
        }
    }

    @Override
    public void ajouterFactureASituationFinanciere(Long idSituationFinanciere, Facture facture) throws FormulaireException {
        SituationFinanciere situationFinanciere = situationFinanciereDao.findById(idSituationFinanciere);
        if (situationFinanciere == null) {
            throw new FormulaireException("Situation financière non trouvée pour l'ID spécifié");
        }
        situationFinanciere.getFactures().add(facture);
        situationFinanciereDao.update(situationFinanciere);
    }
    public Facture creerNouvelleFacture() throws FormulaireException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez l'identifiant de la situation financière pour la nouvelle facture : ");
        Long idSituationFinanciere = scanner.nextLong();
        SituationFinanciere situationFinanciere = situationFinanciereDao.findById(idSituationFinanciere);
        if (situationFinanciere == null) {
            throw new FormulaireException("Situation financière non trouvée pour l'ID spécifié");
        }
        System.out.println("Entrez le montant total de la nouvelle facture : ");
        Double montantTotal = scanner.nextDouble();

        System.out.println("Entrez l'ID de la consultation pour la nouvelle facture : ");
        Long idConsultation = scanner.nextLong();
        Facture nouvelleFacture = new Facture();
        nouvelleFacture.setMontantTotal(montantTotal);
        nouvelleFacture.setIdConsultation(idConsultation);
        nouvelleFacture.setIdsituationFinanciere(idSituationFinanciere);
        factureDao.save(nouvelleFacture);
        return nouvelleFacture;

    }

    public void ajouterFactureASituationFinanciere() throws FormulaireException,ConsultationNonTrouveeException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez l'identifiant de la facture : ");
        Long idSituationFinanciere = scanner.nextLong();
        SituationFinanciere situationFinanciere = situationFinanciereDao.findById(idSituationFinanciere);
        if (situationFinanciere == null) {
            throw new FormulaireException("Situation financière non trouvée pour l'ID spécifié");
        }

        Facture facture = creerNouvelleFacture();

        System.out.println("Entrez le montant de la facture : ");
        Double montant = scanner.nextDouble();
        System.out.println("Entrez l'ID de la consultation : ");
        Long idConsultation = scanner.nextLong();

        Consultation consultation = consultationDao.findById(idConsultation);
        if (consultation == null) {
            throw new ConsultationNonTrouveeException("Consultation non trouvée pour l'ID spécifié, impossible d'ajouter la facture");
        }
        facture.setMontantTotal(montant);
        facture.setIdConsultation(idConsultation);
        situationFinanciere.getFactures().add(facture);
        situationFinanciereDao.update(situationFinanciere);
    }

    @Override
    public void ajouterSituationFinanciereACaisse(Long idCaisse, SituationFinanciere situationFinanciere) throws FormulaireException {
        Caisse caisse = caisseDao.findById(idCaisse);
        if (caisse == null) {
            throw new FormulaireException("Caisse non trouvée pour l'ID spécifié");
        }
        caisse.getSituationFinanciere().add(situationFinanciere);
        caisseDao.update(caisse);
    }
    public void ajouterSituationFinanciereACaisse() throws FormulaireException {
        SituationFinanciere S= createSituationFinanciere();
        Scanner s= new Scanner(System.in);
        System.out.println("Veuillez entrer l'id de la caisse: ");
        Long id = s.nextLong();
        ajouterSituationFinanciereACaisse(id,S);
    }

    @Override
    public void modifierMontantPayeFacture(Long idFacture, Double nouveauMontant) throws FormulaireException {
        Facture facture = factureDao.findById(idFacture);
        if (facture == null) {
            throw new FormulaireException("Facture non trouvée pour l'ID spécifié");
        }
        facture.setMontantPaye(nouveauMontant);
        factureDao.update(facture);
    }


    @Override
    public void enregistrerPaiement(Facture facture, double montantPaye) throws FormulaireException {
        if (facture == null) {
            throw new FormulaireException("Facture non spécifiée pour l'enregistrement du paiement");}

        double nouveauMontantPaye = facture.getMontantPaye() + montantPaye;
        double nouveauMontantRestant = facture.getMontantRestant() - montantPaye;

        if (nouveauMontantRestant < 0) {
            throw new FormulaireException("Le montant payé dépasse le montant restant de la facture");
        }
        facture.setMontantPaye(nouveauMontantPaye);
        facture.setMontantRestant(nouveauMontantRestant);

        factureDao.update(facture);
    }
    public double consulterSoldeCaisse() throws FormulaireException {
        try {
            System.out.println("Veuillez saisir l'identifiant");
            Scanner scanner = new Scanner(System.in);
            Long id = scanner.nextLong();
            Caisse caisse = caisseDao.getCaisse(id);

            if (caisse == null) {
                throw new FormulaireException("La caisse n'a pas été trouvée");
            }
            double recetteDuJour = caisse.recetteDuJour(LocalDate.now());
            double recetteDumois = caisse.recetteDumois(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
            double recetteDeLAnnee = caisse.recetteDeLAnnee(LocalDate.now().getYear());

            double soldeCaisse = recetteDuJour + recetteDumois + recetteDeLAnnee;

            return soldeCaisse;
        } catch (Exception e) {
            throw new FormulaireException("Erreur lors de la consultation du solde de la caisse", e);
        }
    }

    @Override
    public void ajouterAuSoldeCaisse(double montant) throws FormulaireException {
        System.out.println("Veuillez saisir l'identifiant");
        Scanner scanner = new Scanner(System.in);
        Long id = scanner.nextLong();
        Caisse caisse = caisseDao.getCaisse(id);
        if (caisse != null) {
            caisse.setRecetteDuJour(caisse.getRecetteDuJour() + montant);
            caisseDao.update(caisse);
        } else {
            throw new FormulaireException("Caisse introuvable");
        }
    }

    @Override
    public void retirerDuSoldeCaisse(double montant) throws FormulaireException {
        System.out.println("Veuillez saisir l'identifiant");
        Scanner scanner = new Scanner(System.in);
        Long id = scanner.nextLong();
        Caisse caisse = caisseDao.getCaisse(id);
        if (caisse != null) {
            if (montant <= caisse.getRecetteDuJour()) {
                caisse.setRecetteDuJour(caisse.getRecetteDuJour() - montant);
                caisseDao.update(caisse);
            } else {
                throw new FormulaireException("Solde insuffisant");
            }
        } else {
            throw new FormulaireException("Caisse introuvable");
        }
    }


    public Caisse createCaisse() throws FormulaireException {
        try {
            Caisse nouvelleCaisse = new Caisse();
            caisseDao.save(nouvelleCaisse);

            return nouvelleCaisse;
        } catch (Exception e) {
            throw new FormulaireException("Erreur lors de la création de la caisse", e);
        }
    }


}
