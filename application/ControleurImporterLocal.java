package application;

import java.io.File;

import gestion_donnees.ConferencierException;
import gestion_donnees.DonneesApplication;
import gestion_donnees.EmployeException;
import gestion_donnees.ExpositionException;
import gestion_donnees.VisiteException;
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
	
	public static String cheminFichierEmployes;
	public String cheminFichierConferenciers;
	
	public String cheminFichierVisites;
	
	public String cheminFichierExpositions;

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
//    	        System.out.println("Fichier sélectionné : " + fichier.getAbsolutePath());
    	        cheminFichierConferenciers = fichier.getAbsolutePath();
    	        System.out.print("\ncheminFichierConferenciers : " + cheminFichierConferenciers);
    	    	labelFichierConferencier.setText(MESSAGE_FICHIER_SELECTIONNE);
    	    	cheminFichierConferencierChoisit = true;
    	    	mettreAJourEtatBtnImporterConferenciers();

    	    }
    }

    @FXML
    void choisirFichierEmployes(ActionEvent event) {
    	
    File fichier = ouvrirFileChooser("Choisissez le fichier correspondant aux employés");
   	if (fichier != null) {
//   	        System.out.println("Fichier sélectionné : " + fichier.getAbsolutePath());
   	        cheminFichierEmployes = fichier.getAbsolutePath();
   	        System.out.print("\ncheminFichierEmployes : " + cheminFichierEmployes);
	    	labelFichierEmployes.setText(MESSAGE_FICHIER_SELECTIONNE);
	    	cheminFichierEmployesChoisit = true;
	    	mettreAJourEtatBtnImporterEmployes();

   	    }
    }

    @FXML
    void choisirFichierExpositions(ActionEvent event) {

    File fichier = ouvrirFileChooser("Choisissez le fichier correspondant aux expositions");
   	if (fichier != null) {
//   	        System.out.println("Fichier sélectionné : " + fichier.getAbsolutePath());
   	        cheminFichierExpositions= fichier.getAbsolutePath();
   	        System.out.print("\ncheminFichierExpositions : " + cheminFichierExpositions);
	    	labelFichierExpositions.setText(MESSAGE_FICHIER_SELECTIONNE);
	    	cheminFichierExpositionsChoisit = true;
	    	mettreAJourEtatBtnImporterExpositions();

   	    }
    }

    @FXML
    void choisirFichierVisites(ActionEvent event) {
        File fichier = ouvrirFileChooser("Choisissez le fichier correspondant aux visites");
       	if (fichier != null) {
//       	        System.out.println("Fichier sélectionné : " + fichier.getAbsolutePath());
       	        cheminFichierVisites = fichier.getAbsolutePath();
       	        System.out.print("\ncheminFichierVisites : " + cheminFichierVisites);     
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

    
    @FXML
    void importerFichierConferenciers(ActionEvent event) {
    	DonneesApplication donnees = new DonneesApplication();
    	
    	try {
			donnees.importerConferenciers(DonneesApplication.LireCsv(cheminFichierConferenciers));
			importationConferenciersOk = true;
			Alert alerteOk = new Alert(AlertType.INFORMATION);
	    	alerteOk.setTitle("Importation réussie");
	    	alerteOk.setHeaderText("Les données relatives aux conférenciers, on était importées dans l'application");
	    	alerteOk.showAndWait();
	    	 // Mettre à jour l'état du bouton Visites
            mettreAJourEtatBtnVisites();

    	} catch(IllegalArgumentException e) {
//    		System.out.print("Une erreur s'est produite avec le fichier des conférenciers");
			Alert alerteNok = new Alert(AlertType.WARNING);
			alerteNok.setTitle("Importation echouée");
			alerteNok.setHeaderText("L'importation du fichiers des conférenciers a echouée");
		    alerteNok.setContentText(e.getMessage());
		    alerteNok.showAndWait();
    	}

    }

    @FXML
    void importerFichierEmployes(ActionEvent event) {
    	DonneesApplication donnees = new DonneesApplication();
    	
    	try {
			donnees.importerEmployes(DonneesApplication.LireCsv(cheminFichierEmployes));
			importationEmployesOk = true;
			Alert alerteOk = new Alert(AlertType.INFORMATION);
	    	alerteOk.setTitle("Importation réussie");
	    	alerteOk.setHeaderText("Les données relatives aux employes, on était importées dans l'application");
	    	alerteOk.showAndWait();
	    	
	    	 // Mettre à jour l'état du bouton Visites
            mettreAJourEtatBtnVisites();

    	} catch(IllegalArgumentException e) {
			Alert alerteNok = new Alert(AlertType.WARNING);
			alerteNok.setTitle("Importation echouée");
			alerteNok.setHeaderText("L'importation du fichiers des employes a echouée");
		    alerteNok.setContentText(e.getMessage());
		    alerteNok.showAndWait();
    	}

    }

    

    @FXML
    void importerFichierExpositions(ActionEvent event) {
    	DonneesApplication donnees = new DonneesApplication();
    	
    	try {
			donnees.importerExpositions(DonneesApplication.LireCsv(cheminFichierExpositions));
			importationExpositionsOk = true;
			Alert alerteOk = new Alert(AlertType.INFORMATION);
	    	alerteOk.setTitle("Importation réussie");
	    	alerteOk.setHeaderText("Les données relatives aux expositions, on était importées dans l'application");
	    	alerteOk.showAndWait();
	    	
	    	 // Mettre à jour l'état du bouton Visites
            mettreAJourEtatBtnVisites();

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
    	DonneesApplication donnees = new DonneesApplication();
		donnees.importerConferenciers(DonneesApplication.LireCsv(cheminFichierConferenciers));
		donnees.importerEmployes(DonneesApplication.LireCsv(cheminFichierEmployes));
		donnees.importerExpositions(DonneesApplication.LireCsv(cheminFichierExpositions));

    	try {
			donnees.importerVisites(DonneesApplication.LireCsv(cheminFichierVisites));
			Alert alerteOk = new Alert(AlertType.INFORMATION);
	    	alerteOk.setTitle("Importation réussie");
	    	alerteOk.setHeaderText("Les données relatives aux visites, on était importées dans l'application");
	    	alerteOk.showAndWait();

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
    	System.exit(0);
    }

    @FXML
    void revenirArriere(ActionEvent event) {
    	Main.setPageImporter();

    }
    
    private File ouvrirFileChooser(String titre) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(titre);

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Fichiers Texte", "*.csv"),
            new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        Stage stage = (Stage) btnChoisirFichierConferenciers.getScene().getWindow(); // meme fenetre pour les 4 boutons
        return fileChooser.showOpenDialog(stage);
    }

}
