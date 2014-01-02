package iut.projet.jardindesverbes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XMLStoryLoader
{
	/**
	 * Code permettant de charger dans le mod�le les donn�es du fichier Bibliotheque.xml
	 * original provenant de developpez.com, puis modifi� pour correspondre � la structure 
	 * de la classe Film et au fonctionnement de l'application
	 */
	static org.jdom2.Document document;
	static Element racine;

	public ArrayList<Histoire> load(InputStream is)
	{
		System.out.println("loading");
		Histoire histoire = null;

		//On cr�e une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			//On cr�e un nouveau document JDOM avec en argument le fichier XML
			document = sxb.build(is);
		}
		catch(Exception e){
			e.printStackTrace();}
		ArrayList<Histoire> listeHistoires = new ArrayList<Histoire>();
		if(document==null){
			return null;
		}
		//On initialise un nouvel �l�ment racine avec l'�l�ment racine du document.
		racine = document.getRootElement();
		System.out.println(racine);
		//On cr�e une List contenant tous les noeuds "histoire" de l'Element racine
		List listStories = racine.getChildren("histoire");
		System.out.println(listStories.isEmpty());
		//On cr�e un Iterator sur notre liste
		Iterator i = listStories.iterator();
		while(i.hasNext())
		{

			//On recr�e l'Element courant � chaque tour de boucle afin de
			//pouvoir utiliser les m�thodes propres aux Element 
			Element histoire_elt = (Element)i.next();
			//On r�cup�re les �l�ments stock�s dans le xml
			String titre = histoire_elt.getAttributeValue("title");
			Element phrases = histoire_elt.getChild("phrases");
			List phrase_list = phrases.getChildren("value"); 
			//On cr�e un Iterator sur notre liste
			Iterator j = phrase_list.iterator();
			System.out.println(phrase_list.size());
			ArrayList<String> contenu_phrases = new ArrayList<String>();
			while(j.hasNext()){
				Element phr = (Element)j.next();
				// ajoute la phrase dans la liste contenu_phrases
				contenu_phrases.add(phr.getText());
				System.out.println(phr.getText());
			}
			Element verbes = histoire_elt.getChild("verbes");
			List verbes_list = verbes.getChildren("verbe"); 
			//On cr�e un Iterator sur notre liste
			Iterator k = verbes_list.iterator();
			System.out.println(verbes_list.size());
			ArrayList<String> contenu_verbes = new ArrayList<String>();
			ArrayList<String> attributs_groupes = new ArrayList<String>();
			ArrayList<String> attributs_temps = new ArrayList<String>();
			while(k.hasNext()){
				Element vrb = (Element)k.next();
				// ajoute le verbe dans la liste contenu_verbes
				contenu_verbes.add(vrb.getText());
				// ajoute le groupe du verbe dans la liste attributs_groupes
				attributs_groupes.add(vrb.getAttributeValue("groupe"));
				// ajoute le temps du verbe dans la liste contenu_phrases
				attributs_temps.add(vrb.getAttributeValue("temps"));
				System.out.println(vrb.getText());
			}
			System.out.println("ensemble des phrases : " + contenu_phrases);
			// cr�e une histoire avec les donn�es r�cup�r�es
			histoire = new Histoire(titre, contenu_phrases, contenu_verbes, attributs_groupes, attributs_temps);
			//ajoute l'histoire dans la liste listeHistoires
			listeHistoires.add(histoire);
		}
		//renvoie la liste des Histoires pr�sentes dans le fichier
		return listeHistoires;
	}
}


