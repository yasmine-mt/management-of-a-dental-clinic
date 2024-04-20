package src.service;

import src.Dao.fileBase.*;
import src.entity.Consultation;
import src.entity.DossierMedicale;
import src.entity.InterventionMedecin;
import src.entity.Patient;
import src.entity.enums.CategorieActe;
import src.entity.enums.TypeConsultation;
import src.exception.FormulaireException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DentisteService extends SecretaireService implements DentisteInterface {

   private ConsultationDao consultationDao;
   private InterventionMedecinDao interventionMedecinDao;
    private DossierMedicaleDao dossierMedicalDao;
    SecretaireService s ;

    public DentisteService(SecretaireDao secretaireDao, PatientDao patientDao, DentisteDao dentisteDao, SituationFinanciereDao situationFinanciereDao, FactureDao factureDao, CaisseDao caisseDao, DossierMedicaleDao dossierMedicalDao, ConsultationDao consultationDao) {
        super(secretaireDao, patientDao, dentisteDao, situationFinanciereDao, factureDao, caisseDao, dossierMedicalDao, consultationDao);
    }

    public DentisteService(SecretaireDao secretaireDao) {
        super(secretaireDao);
    }

    @Override
    public  void creerInterventionMedecin() throws FormulaireException {
        System.out.println("Veuillez saisir identifiant de Consultation: ");
        Scanner scanner =new Scanner(System.in);
        long idConsultation = scanner.nextLong();
        if (consultationDao.findById(idConsultation)!= null) {
            InterventionMedecin nouvelleIntervention = new InterventionMedecin();
            System.out.println("Veuillez saisir l'identifiant de l'intervention : ");
            nouvelleIntervention.setIdIntervention(scanner.nextLong());
            System.out.println("Veuillez saisir la note du médecin : ");
            nouvelleIntervention.setNoteMedecin(scanner.nextLine());
            System.out.println("Veuillez saisir le prix patient : ");
            nouvelleIntervention.setPrixPatient(scanner.nextDouble());
            System.out.println("Veuillez saisir la dent: ");
            nouvelleIntervention.setDent(scanner.nextLong());
            System.out.println("Veuillez saisir la note du médecin : ");
            nouvelleIntervention.setNoteMedecin(scanner.nextLine());
            scanner.nextLine();
            System.out.println("Voici les catégories d'acte disponibles :");
            CategorieActe[] categories = CategorieActe.values();

            for (int i = 0; i < categories.length; i++) {
                System.out.println((i + 1) + ". " + categories[i].name());
            }

            System.out.println("Veuillez choisir le numéro de la catégorie d'acte : ");
            int choixUtilisateur = scanner.nextInt();

            if (choixUtilisateur >= 1 && choixUtilisateur <= categories.length) {
                CategorieActe categorieChoisie = categories[choixUtilisateur - 1];
                System.out.println("Catégorie d'acte choisie : " + categorieChoisie);

                nouvelleIntervention.setActe(categorieChoisie);
                interventionMedecinDao.save(nouvelleIntervention);
                Consultation c =consultationDao.findById(idConsultation);
                List<InterventionMedecin>  L =c.getInterventions();
                L.add(nouvelleIntervention);
                c.setInterventions(L);

            } else {
                System.out.println("Choix invalide. Veuillez choisir un numéro de catégorie valide.");

            }
        } else {
            throw new FormulaireException("ID de consultation inexistant");
        }

    }

    @Override
    public void mettreAJourInterventionMedecin(InterventionMedecin intervention) throws FormulaireException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Que souhaitez-vous mettre à jour dans l'intervention médicale ?");
        System.out.println("1. Note du médecin");
        System.out.println("2. Prix pour le patient");
        System.out.println("3. Dent");
        System.out.println("4. Catégorie d'acte");
        System.out.println("5. Quitter");

        System.out.println("Entrez le numéro de votre choix : ");
        int choixUtilisateur = scanner.nextInt();
        scanner.nextLine();

        switch (choixUtilisateur) {
            case 1:
                System.out.println("Entrez la nouvelle note du médecin : ");
                String nouvelleNote = scanner.nextLine();
                intervention.setNoteMedecin(nouvelleNote);
                break;
            case 2:
                System.out.println("Entrez le nouveau prix pour le patient : ");
                Double nouveauPrix = scanner.nextDouble();
                intervention.setPrixPatient(nouveauPrix);
                break;
            case 3:
                System.out.println("Entrez la nouvelle dent : ");
                Long nouvelleDent = scanner.nextLong();
                intervention.setDent(nouvelleDent);
                break;
            case 4:
                System.out.println("Voici les catégories d'acte disponibles :");
                CategorieActe[] categories = CategorieActe.values();

                for (int i = 0; i < categories.length; i++) {
                    System.out.println((i + 1) + ". " + categories[i].name());
                }

                System.out.println("Veuillez choisir le numéro de la nouvelle catégorie d'acte : ");
                int choixActe = scanner.nextInt();

                if (choixActe >= 1 && choixActe <= categories.length) {
                    CategorieActe nouvelleCategorie = categories[choixActe - 1];
                    System.out.println("Nouvelle catégorie d'acte choisie : " + nouvelleCategorie);
                    intervention.setActe(nouvelleCategorie);
                } else {
                    System.out.println("Choix invalide. Veuillez choisir un numéro de catégorie valide.");
                }
                break;
            case 5:
                System.out.println("Opération de mise à jour terminée.");
                break;
            default:
                System.out.println("Choix invalide.");
                break;
        }
        interventionMedecinDao.update(intervention);
    }


    @Override
    public void supprimerInterventionMedecin(long interventionId) throws FormulaireException {
        InterventionMedecin intervention = interventionMedecinDao.findById(interventionId);
        if (intervention != null) {
            interventionMedecinDao.delete(intervention);
            System.out.println("L'intervention médicale avec l'ID " + interventionId + " a été supprimée.");
        } else {
            throw new FormulaireException("L'intervention médicale avec l'ID " + interventionId + " n'existe pas.");
        }
    }

    @Override
    public void creerConsultation() throws FormulaireException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez saisir l'identifiant du dossier pour la consultation : ");
        Long idDossier = scanner.nextLong();
        scanner.nextLine();

        if (dossierMedicalDao.findById(idDossier) != null) {
            Consultation nouvelleConsultation = new Consultation();
            nouvelleConsultation.setIddossierMedicale(idDossier);
            System.out.println("Veuillez saisir L'identifiant de la consultation: ");
            Long iden = scanner.nextLong();
            nouvelleConsultation.setIdConsultation(iden);
            System.out.println("Veuillez saisir la date de la consultation (AAAA-MM-JJ) : ");
            String dateConsultationStr = scanner.nextLine();
            LocalDate dateConsultation = LocalDate.parse(dateConsultationStr);
            nouvelleConsultation.setDateConsultation(dateConsultation);
            System.out.println("Veuillez saisir le type de consultation : ");
            System.out.println("1. Consultation générale");
            System.out.println("2. Consultation suivi");
            System.out.println("3. Consultation en Urgence");
            int choixTypeConsultation = scanner.nextInt();
            scanner.nextLine();
            TypeConsultation typeConsultation;
            if (choixTypeConsultation == 1) {
                typeConsultation = TypeConsultation.Consultation_Generale;
            } else if (choixTypeConsultation == 2) {
                typeConsultation = TypeConsultation.SUIVI;
            }  else if (choixTypeConsultation == 3) {
                typeConsultation = TypeConsultation.URGENCE;}
            else {
                throw new FormulaireException("Choix invalide pour le type de consultation.");
            }
            nouvelleConsultation.setTypeConsultation(typeConsultation);
            creerInterventionMedecin();
            s.creerNouvelleFacture();
            consultationDao.save(nouvelleConsultation);
            DossierMedicale d =dossierMedicalDao.findById(idDossier);
            List<Consultation> c = d.getConsultations();
            c.add(nouvelleConsultation);
            d.setConsultations(c);
            dossierMedicalDao.update(d);
            System.out.println("La consultation a été créée avec succès pour le Patient : " + d.getPatient());
        } else {
            throw new FormulaireException("Le dossier avec l'ID " + idDossier + " n'existe pas.");
        }
    }


    @Override
    public void mettreAJourConsultation() throws FormulaireException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez saisir l'identifiant de la consultation à mettre à jour : ");
        Long idConsultation = scanner.nextLong();
        scanner.nextLine();

        Consultation consultation = consultationDao.findById(idConsultation);
        if (consultation != null) {
            System.out.println("Consultation actuelle : " + consultation);

            System.out.println("Que souhaitez-vous mettre à jour ?");
            System.out.println("1. Date de consultation");
            System.out.println("2. Type de consultation");
            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.println("Veuillez saisir la nouvelle date de la consultation (AAAA-MM-JJ) : ");
                    String nouvelleDateStr = scanner.nextLine();
                    LocalDate nouvelleDate = LocalDate.parse(nouvelleDateStr);
                    consultation.setDateConsultation(nouvelleDate);
                    break;
                case 2:
                    System.out.println("Veuillez saisir le nouveau type de consultation : ");
                    System.out.println("1. Consultation générale");
                    System.out.println("2. Consultation de suivi");
                    System.out.println("3. Consultation en urgence");
                    int choixType = scanner.nextInt();
                    scanner.nextLine();

                    TypeConsultation nouveauType;
                    if (choixType == 1) {
                        nouveauType = TypeConsultation.Consultation_Generale;
                    } else if (choixType == 2) {
                        nouveauType = TypeConsultation.SUIVI;
                    } else if (choixType == 3) {
                        nouveauType = TypeConsultation.URGENCE;
                    } else {
                        throw new FormulaireException("Choix invalide pour le type de consultation.");
                    }
                    consultation.setTypeConsultation(nouveauType);
                    break;
                default:
                    System.out.println("Choix invalide.");
                    break;
            }

            consultationDao.update(consultation);
            System.out.println("Consultation mise à jour avec succès : " + consultation);
        } else {
            throw new FormulaireException("La consultation avec l'ID " + idConsultation + " n'existe pas.");
        }
    }

    @Override
    public void supprimerConsultation(long consultationId) throws FormulaireException {
        Consultation consultation = consultationDao.findById(consultationId);

        if (consultation != null) {
            consultationDao.delete(consultation);
            System.out.println("La consultation avec l'ID " + consultationId + " a été supprimée avec succès.");
        } else {
            throw new FormulaireException("La consultation avec l'ID " + consultationId + " n'existe pas.");
        }
    }




}
