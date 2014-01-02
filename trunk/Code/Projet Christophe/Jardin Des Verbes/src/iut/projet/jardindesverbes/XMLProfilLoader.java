package iut.projet.jardindesverbes;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import android.content.Context;

public class XMLProfilLoader {

	// D�finition des constantes de nom de balises.
	static final String PROFIL = "profil";
	static final String USERNAME = "name";
	static final String XP = "exp";
	static final String LEVEL = "level";
	static final String SUPPORT = "support";
	static final String DONE_STORY = "done-story";
	static final String AVAILABLE_STORY = "available-story";

	// Valeur d'une donn�e consid�r�e nulle
	static final String EMPTY_DATA = " ";

	static Document document;
	static Element racine;

	/**
	 * Charge les profils.
	 * 
	 * @return
	 */
	public static List<Profil> loadProfils(Context context) {
		// On cr�e une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		try {
			
			// On cr�e un nouveau document JDOM avec en argument le fichier XML			
			FileInputStream _stream = context.openFileInput("profils.xml");
			
			document = sxb.build(_stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Profil> listeProfils = new ArrayList<Profil>();
		if (document == null) {
			return listeProfils;
		}
		// On initialise un nouvel �l�ment racine avec l'�l�ment racine du document.
		racine = document.getRootElement();

		// On cr�e une List contenant tous les noeuds "etudiant" de l'Element
		// racine
		List<Element> listProfils = racine.getChildren(PROFIL);
		// On cr�e un Iterator sur notre liste
		for (Element courant : listProfils) {
			System.out.println(courant.getTextNormalize());
			Profil profil = new Profil();

			profil.setUsername(courant.getAttributeValue(USERNAME));
			profil.setExperience((Integer.parseInt(courant.getChildText(XP))));
			profil.setNiveau((Integer.parseInt(courant.getChildText(LEVEL))));

			for (Element doneStory : courant.getChild("done-story")
					.getChildren()) {
				profil.getLesObjets().add(
						new ObjetHistoire(doneStory.getText(),
								ObjetHistoire.DONE));
			}

			for (Element availableStory : courant.getChild("available-story")
					.getChildren()) {
				profil.getLesObjets().add(
						new ObjetHistoire(availableStory.getText(),
								ObjetHistoire.AVAILABLE));
			}

			// ajout du film cr�e dans la liste
			listeProfils.add(profil);
		}
		// renvoie la liste des Films pr�sents dans le fichier
		return listeProfils;
	}

}
