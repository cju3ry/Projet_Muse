package application;

import java.io.File;

import java.util.ArrayList;

import gestion_donnees.Conferencier;

import gestion_donnees.DonneesApplication;
import gestion_donnees.Employe;

import gestion_donnees.Exposition;

import gestion_donnees.Visite;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ControleurImporterLocal {
	

	
	private static final String MESSAGE_FICHIER_SELECTIONNE = "     Fichier séléctionné";
	private static String initialFilePath = null;

	public static String getInitialFilePath() {
		return initialFilePath;
	}


	public static String getCheminFichierEmployes() {
		return cheminFichierEmployes;
	}
	
	private static boolean importationConferenciersOk = false;
	
	private static boolean importationExpositionsOk = false;
	
	private static boolean importationEmployesOk = false;
	
	private static boolean cheminFichierConferencierChoisit = false;
	
	private static boolean cheminFichierEmployesChoisit = false;
	
	private static boolean cheminFichierExpositionsChoisit = false;
	
	private static boolean cheminFichierVisitesChoisit = false;
	
	private boolean donnesChargeesSauvegarder;
	
	private boolean donnesChargeesLocal;

	private  static StringBuilder strConferencier;
	
	private  static StringBuilder strEmployes;
	
	private  static StringBuilder strExpositions;
	
	private  static StringBuilder strVisites;
	
	private static boolean donneesConferencierChargees = false;
	
	private static boolean donneesEmployesChargees = false;
	
	private static boolean donneesExpositionsChargees = false;
	
	private static boolean donneesVisitesChargees = false;
	
	public static boolean isDonneesConferencierChargees() {
		return donneesConferencierChargees;
	}

	public static boolean isDonneesEmployesChargees() {
		return donneesEmployesChargees;
	}
	
	public static boolean isDonneesExpositionsChargees() {
		return donneesExpositionsChargees;
	}
	
	public static boolean isDonneesVisitesChargees() {
		return donneesVisitesChargees;
	}

	public static StringBuilder getStrConferencier() {
		return strConferencier;
	}
	
	public static StringBuilder getStrEmployes() {
		return strEmployes;
	}
	
	public static StringBuilder getStrExpositions() {
		return strExpositions;
	}
	
	public static StringBuilder getStrVisites() {
		return strVisites;
	}

	public static String cheminFichierEmployes;
	
	public static String cheminFichierConferenciers;
	
	public static String cheminFichierVisites;
	
	public static String cheminFichierExpositions;

    @FXML
    private Button btnChoisirFichierConferenciers;

    @FXML
    private Button btnChoisirFichierEmployes;

    @FXML
    private Button btnChoisirFichierExpositions;

    @FXML
    private Button btnChoisirFichierVisites;

    @FXML
    private Button btnConsulter;

    @FXML
    private Button btnExporter;

    @FXML
    private Button btnImporter;

    @FXML
    private Button btnImporterFichiers;

    @FXML
    private Button btnNotice;

    @FXML
    private Button btnQuitter;

    @FXML
    private Button btnRevenirArriere;

    @FXML
    private Button choisirFicherVisites;

    @FXML
    private Button choisirFichierEmployes;
    
    @FXML
    private Button btnImporterFichierConferenciers;

    @FXML
    private Button btnImporterFichierEmployes;

    @FXML
    private Button btnImporterFichierExpositions;

    @FXML
    private Button btnImporterFichiervisites;
    
    @FXML
    private Label labelFichierConferencier;

    @FXML
    private Label labelFichierEmployes;

    @FXML
    private Label labelFichierExpositions;

    @FXML
    private Label labelFichierVisites;

	@FXML
	private Label labelConferencierImporte;

	@FXML
	private Label labelEmployesImporte;

	@FXML
	private Label labelExpositionsImporte;

	@FXML
	private Label labelVisitesImporte;
	
	@FXML
	private Button btnSauvegarder;
	
	@FXML
	void sauvegarder(ActionEvent event) {
		Main.sauvegarder();
	}

	private static DonneesApplication donnees = new DonneesApplication();

	public static DonneesApplication getDonnees() {
		return donnees;
	}

	@FXML
    public void initialize() {
        // Désactive le bouton d'importation des visites au démarrage
        btnImporterFichiervisites.setDisable(true);
        btnImporterFichierConferenciers.setDisable(true);
        btnImporterFichierEmployes.setDisable(true);
        btnImporterFichierExpositions.setDisable(true);
        
    }
    
    // Méthode pour mettre à jour l'état du bouton d'importation des visites
    private void mettreAJourEtatBtnVisites() {
        // Si les trois importations précédentes sont faites, active le bouton des visites
        btnImporterFichiervisites.setDisable(!(importationConferenciersOk && importationExpositionsOk && importationEmployesOk && cheminFichierVisitesChoisit));
    }
    
 // Méthode pour mettre à jour l'état du bouton d'importation des conférenciers
    private void mettreAJourEtatBtnImporterConferenciers() {
        // Si l'utilisateur n'a pas entré de chemin pour le fichier alors il ne peut pas cliquer sur le bouton importer
        btnImporterFichierConferenciers.setDisable(!(cheminFichierConferencierChoisit));
    }
    
    private void mettreAJourEtatBtnImporterEmployes() {
        btnImporterFichierEmployes.setDisable(!(cheminFichierEmployesChoisit));
    }
    
    private void mettreAJourEtatBtnImporterExpositions() {
        btnImporterFichierExpositions.setDisable(!(cheminFichierExpositionsChoisit));
    }

	@FXML
	void choisirFichierConferencier(ActionEvent event) {
		File fichier = ouvrirFileChooser("Choisissez le fichier correspondant aux conférenciers");
		if (fichier != null) {
			cheminFichierConferenciers = fichier.getAbsolutePath();
			if (initialFilePath == null) {
				initialFilePath = cheminFichierConferenciers;
				System.out.println("Initial file path set to: " + initialFilePath);
			}
			labelFichierConferencier.setText(MESSAGE_FICHIER_SELECTIONNE);
			cheminFichierConferencierChoisit = true;
			mettreAJourEtatBtnImporterConferenciers();
		}
	}

	@FXML
	void choisirFichierEmployes(ActionEvent event) {
		File fichier = ouvrirFileChooser("Choisissez le fichier correspondant aux employés");
		if (fichier != null) {
			cheminFichierEmployes = fichier.getAbsolutePath();
			if (initialFilePath == null) {
				initialFilePath = cheminFichierEmployes;
				System.out.println("Initial file path set to: " + initialFilePath);
			}
			labelFichierEmployes.setText(MESSAGE_FICHIER_SELECTIONNE);
			cheminFichierEmployesChoisit = true;
			mettreAJourEtatBtnImporterEmployes();
		}
	}

	@FXML
	void choisirFichierExpositions(ActionEvent event) {
		File fichier = ouvrirFileChooser("Choisissez le fichier correspondant aux expositions");
		if (fichier != null) {
			cheminFichierExpositions = fichier.getAbsolutePath();
			if (initialFilePath == null) {
				initialFilePath = cheminFichierExpositions;
				System.out.println("Initial file path set to: " + initialFilePath);
			}
			labelFichierExpositions.setText(MESSAGE_FICHIER_SELECTIONNE);
			cheminFichierExpositionsChoisit = true;
			mettreAJourEtatBtnImporterExpositions();
		}
	}

	@FXML
	void choisirFichierVisites(ActionEvent event) {
		File fichier = ouvrirFileChooser("Choisissez le fichier correspondant aux visites");
		if (fichier != null) {
			cheminFichierVisites = fichier.getAbsolutePath();
			if (initialFilePath == null) {
				initialFilePath = cheminFichierVisites;
				System.out.println("Initial file path set to: " + initialFilePath);
			}
			labelFichierVisites.setText(MESSAGE_FICHIER_SELECTIONNE);
			cheminFichierVisitesChoisit = true;
			mettreAJourEtatBtnVisites();
		}
	}

    @FXML
    void consulter(ActionEvent event) {
    	Main.setPageConsulter();
    }

    @FXML
    void exporter(ActionEvent event) {
    	Main.setPageExporter();
    }

    @FXML
    void importer(ActionEvent event) {
    	Main.setPageImporter();
    }

    //TODO modifier dans la classe conférencier le toStrinfg pour mettre nom et prenom a la ligne
    //TODO supprimer les # dans les spécialitée substring 
    //TODO si 2 indisponibilité les meme 16/10/2024 et 16/10/2024 en mettre qu'une 
    @FXML
    void importerFichierConferenciers(ActionEvent event) {
    	strConferencier = new StringBuilder();
    	donnesChargeesSauvegarder = ControleurPageDeGarde.isDonneesSaveChargees();
    	if(donnesChargeesSauvegarder) {
    		donnees.clearListConferenciers();
    	}
    	try {
			donnees.importerConferenciers(DonneesApplication.LireCsv(cheminFichierConferenciers));
			importationConferenciersOk = true;
			labelConferencierImporte.setText("Conférenciers");

			Alert alerteOk = new Alert(AlertType.INFORMATION);
	    	alerteOk.setTitle("Importation réussie");
	    	alerteOk.setHeaderText("Les données relatives aux conférenciers, on était importées dans l'application");
	    	alerteOk.showAndWait();
	    	 // Mettre à jour l'état du bouton Visites
            mettreAJourEtatBtnVisites();
            donneesConferencierChargees = true;
            ArrayList<Conferencier> listeDesConfernciers = donnees.getConferenciers();
            strConferencier.append("\n");
            for (int i = 0; i < listeDesConfernciers.size(); i++) {
            	strConferencier.append(listeDesConfernciers.get(i).toString() + "\n");
            }
//            System.out.print("liste des conférencier to string " + strConferencier);

    	} catch(IllegalArgumentException e) {
//    		System.out.print("Une erreur s'est produite avec le fichier des conférenciers");
			Alert alerteNok = new Alert(AlertType.WARNING);
			alerteNok.setTitle("Importation echouée");
			alerteNok.setHeaderText("L'importation du fichiers des conférenciers a echouée");
		    alerteNok.setContentText(e.getMessage());
		    alerteNok.showAndWait();
    	}

    }
    //TODO rajouter nom : et prenom : a la lgine dans le to String d'employes 
    @FXML
    void importerFichierEmployes(ActionEvent event) {
		strEmployes = new StringBuilder();
		donnesChargeesSauvegarder = ControleurPageDeGarde.isDonneesSaveChargees();
    	if(donnesChargeesSauvegarder) {
    		donnees.clearListEmployes();
    	}
    	try {
			donnees.importerEmployes(DonneesApplication.LireCsv(cheminFichierEmployes));
			importationEmployesOk = true;
			labelEmployesImporte.setText("Employés");
			Alert alerteOk = new Alert(AlertType.INFORMATION);
	    	alerteOk.setTitle("Importation réussie");
	    	alerteOk.setHeaderText("Les données relatives aux employes, on était importées dans l'application");
	    	alerteOk.showAndWait();
	    	 // Mettre à jour l'état du bouton Visites
            mettreAJourEtatBtnVisites();
            donneesEmployesChargees = true;
            ArrayList<Employe> listeDesEmployes = donnees.getEmployes();
            strEmployes.append("\n");
            for (int i = 0; i < listeDesEmployes.size(); i++) {
            	strEmployes.append(listeDesEmployes.get(i).toString() + "\n");
            }

    	} catch(IllegalArgumentException e) {
			Alert alerteNok = new Alert(AlertType.WARNING);
			alerteNok.setTitle("Importation echouée");
			alerteNok.setHeaderText("L'importation du fichiers des employes a echouée");
		    alerteNok.setContentText(e.getMessage());
		    alerteNok.showAndWait();
    	}

    }

    
    //TODO enlever les # des mots cle (toString)
    @FXML
    void importerFichierExpositions(ActionEvent event) {
		strExpositions = new StringBuilder();
		donnesChargeesSauvegarder = ControleurPageDeGarde.isDonneesSaveChargees();
    	if(donnesChargeesSauvegarder) {
    		donnees.clearListExpositions();
    	}
    	try {
			donnees.importerExpositions(DonneesApplication.LireCsv(cheminFichierExpositions));
			importationExpositionsOk = true;
			labelExpositionsImporte.setText("Expositions");
			Alert alerteOk = new Alert(AlertType.INFORMATION);
	    	alerteOk.setTitle("Importation réussie");
	    	alerteOk.setHeaderText("Les données relatives aux expositions, on était importées dans l'application");
	    	alerteOk.showAndWait();	
	    	 // Mettre à jour l'état du bouton Visites
            mettreAJourEtatBtnVisites();
            donneesExpositionsChargees = true;
            ArrayList<Exposition> listeDesExpositions = donnees.getExpositions();
            strExpositions.append("\n");
            for (int i = 0; i < listeDesExpositions.size(); i++) {
            	strExpositions.append(listeDesExpositions.get(i).toString() + "\n");
            }

    	} catch(IllegalArgumentException e) {
			Alert alerteNok = new Alert(AlertType.WARNING);
			alerteNok.setTitle("Importation echouée");
			alerteNok.setHeaderText("L'importation du fichiers des expositions a echouée");
		    alerteNok.setContentText(e.getMessage());
		    alerteNok.showAndWait();
    	}

    }

    

    @FXML
    void importerFichierVisites(ActionEvent event) {
		strVisites = new StringBuilder();
		donnesChargeesSauvegarder = ControleurPageDeGarde.isDonneesSaveChargees();
    	if(donnesChargeesSauvegarder) {
    		donnees.clearListVisites();
    	}
    	try {
			donnees.importerVisites(DonneesApplication.LireCsv(cheminFichierVisites));
			Alert alerteOk = new Alert(AlertType.INFORMATION);
	    	alerteOk.setTitle("Importation réussie");
	    	alerteOk.setHeaderText("Les données relatives aux visites, on était importées dans l'application");
	    	alerteOk.showAndWait();
	    	donneesVisitesChargees = true;
			labelVisitesImporte.setText("Visites");
			ArrayList<Visite> listeDesVistes = donnees.getVisites();
	        strVisites.append("\n");
	        for (int i = 0; i < listeDesVistes.size(); i++) {
	        	strVisites.append(listeDesVistes.get(i).toString() + "\n");
	        }

    	} catch(IllegalArgumentException e) {
			Alert alerteNok = new Alert(AlertType.WARNING);
			alerteNok.setTitle("Importation echouée");
			alerteNok.setHeaderText("L'importation du fichiers des visites a echouée");
		    alerteNok.setContentText(e.getMessage());
		    alerteNok.showAndWait();
    	}

    }

    @FXML
    void notice(ActionEvent event) {

    }

    @FXML
    void quitter(ActionEvent event) {
    	Main.quitterApllication();
    }

    @FXML
    void revenirArriere(ActionEvent event) {
    	Main.setPageImporter();

    }
    
    private File ouvrirFileChooser(String titre) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(titre);

		if (initialFilePath != null) {
			File initialFile = new File(initialFilePath);
			fileChooser.setInitialDirectory(initialFile.getParentFile());
		} else {
			File userDirectory = new File(System.getProperty("user.home"));
			fileChooser.setInitialDirectory(userDirectory);
		}

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Fichiers Texte", "*.csv"),
            new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        Stage stage = (Stage) btnChoisirFichierConferenciers.getScene().getWindow(); // meme fenetre pour les 4 boutons
        return fileChooser.showOpenDialog(stage);
    }

}
