package gestion_donnees;

import com.itextpdf.kernel.pdf.PdfDocument;

import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.Document;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.property.VerticalAlignment;

//import org.slf4j.SimpleLoggerFactory;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Cette classe permet de générer un fichier PDF
 */

public class FichierPdf {

	// String pour le titre de la page
	private String titre;

	// Liste contenant les filtres appliqués
	private ArrayList<String> listeDesFiltres= new ArrayList<>();

	// Liste contenant le contenu du fichier
	private String contenuFichier;

	public FichierPdf(String titre, ArrayList<String> listeDesFiltres, String contenuFichier) {

		if (titre == null || listeDesFiltres == null || contenuFichier == null) {
			throw new IllegalArgumentException("Les paramètres ne peuvent pas être nuls");
		}

		this.titre = titre;
		this.listeDesFiltres = listeDesFiltres;
		this.contenuFichier = contenuFichier;

	}

	public boolean genererPdf(String filePath) {

		// Chemin du fichier PDF à générer placé
		// dans le dossier de téléchargement de l'utilisateur
		String dest = filePath
				+ System.currentTimeMillis() + ".pdf";
		try {
			// Créer un PdfWriter
			PdfWriter writer = new PdfWriter(dest);

			// Créer un PdfDocument en utilisant le PdfWriter
			PdfDocument pdf = new PdfDocument(writer);

			// Créer un document iText (contenu du PDF)
			Document document = new Document(pdf);

			LocalDate date = LocalDate.now();
			int dayOfMonth = date.getDayOfMonth();
			int year = date.getYear();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", Locale.FRENCH);
			String monthInFrench = date.format(formatter);

			document.add(new Paragraph("Rapport du " + dayOfMonth  + " " + monthInFrench + " " +  year)
					.setTextAlignment(TextAlignment.CENTER)
					.setFontColor(new DeviceRgb(255, 0, 0))
					.setFontSize(35));

			document.add(new Paragraph("\nRapport portant sur les " + titre).setFontSize(25));
			document.add(new Paragraph("Les filtres qui ont été appliqués sont :").setFontSize(15));
			int compteur = 1;
			for (String filtre : listeDesFiltres) {
				document.add(new Paragraph(compteur + ". " + filtre).setFontSize(15).setMarginLeft(20));
				compteur++;
			}
			document.add(new Paragraph("\nRésultat\n").setFontSize(25));
			document.add(new Paragraph("\n" + contenuFichier).setFontSize(15));

			document.add(new Paragraph("\n Ce rapport contient des informations confidentielles sur le musée et ses employés." +
					"Ne divulgez ces informations sous aucun prétexte.").setItalic());
			// Fermer le document pour générer le PDF
			document.close();

			System.out.println("Le fichier PDF a été généré avec succès !");
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

	}
}
