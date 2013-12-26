package iut.projet.jardindesverbes;

import java.io.FileOutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.content.Context;
import android.widget.Toast;

public class XMLProfilWriter {

	// Définition des constantes de nom de balises.
	static final String PROFILS = "profils";
	static final String PROFIL = "profil";
	static final String USERNAME = "name";
	static final String XP = "exp";
	static final String LEVEL = "level";
	static final String SUPPORT = "support";
	static final String DONE_STORY = "done-story";
	static final String AVAILABLE_STORY = "available-story";
	static final String VALUE = "value";

	// Valeur d'une donnée considérée nulle
	static final String EMPTY_DATA = " ";

	static Document document;
	static Element racine;

	/**
	 * Exporter un fichier XML.
	 * 
	 * @param profil
	 * @param profils
	 */
	public static void saveProfils(Context context, List<Profil> profils) {
		try {
			DocumentBuilderFactory fabrique = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder constructeur = fabrique.newDocumentBuilder();
			Document document = constructeur.newDocument();

			// Configuration de l'arborescence
			document.setXmlVersion("1.0");
			document.setXmlStandalone(true);

			// Création de l'arborescence DOM
			Element root = document.createElement(PROFILS);

			// Pour chaque film de la liste, on crée les éléments.
			for (Profil p : profils) {

				Element El_profil = document.createElement(PROFIL);
				El_profil.setAttribute(USERNAME, p.getUsername());
				root.appendChild(El_profil);

				Element El_niveau = document.createElement(LEVEL);
				El_niveau.setTextContent(Integer.toString(p.getExperience()));
				El_profil.appendChild(El_niveau);

				Element El_EXP = document.createElement(XP);
				El_EXP.setTextContent(Integer.toString(p.getExperience()));
				El_profil.appendChild(El_EXP);

				Element El_available_story = document
						.createElement(AVAILABLE_STORY);
				El_profil.appendChild(El_available_story);

				Element El_done_story = document.createElement(DONE_STORY);
				El_profil.appendChild(El_done_story);

				Element histoire;
				for (ObjetHistoire obj : p.getLesObjets()) {
					if (obj.getEtat() == ObjetHistoire.AVAILABLE) {
						histoire = document.createElement(VALUE);
						histoire.setTextContent(obj.getObjetImageFilename());
						El_available_story.appendChild(histoire);

					} else if (obj.getEtat() == ObjetHistoire.DONE) {
						histoire = document.createElement(VALUE);
						histoire.setTextContent(obj.getObjetImageFilename());
						El_done_story.appendChild(histoire);
					}
				}
			}

			document.appendChild(root);

			// Ecriture du contenu dans le XML
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			FileOutputStream _stream = context.openFileOutput("profils.xml", Context.MODE_PRIVATE);
			StreamResult result = new StreamResult(_stream);
			// Mise en forme / page du fichier XML
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, result);

			Toast.makeText(context, "Liste des profils chargée", Toast.LENGTH_LONG).show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
