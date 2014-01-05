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

import android.content.Context;
import android.util.Log;

public class XMLVerbeLoader
{
	/**
	 * Code permettant de charger dans le mod�le les donn�es du fichier histoires.xml
	 * original provenant de developpez.com, puis modifi� pour correspondre � la structure 
	 * de la classe verbe et au fonctionnement de l'application
	 */
	
	public static String VERBES = "verbes";
	public static String VERBE = "verbe";
	public static String INFINITIF = "infinitif";
	public static String GROUPE = "groupe";
	public static String TEMPS = "temps";
	public static String CONJUGAISON = "conjugaison";
	public static String VALUE = "value";
	
	
	static org.jdom2.Document document;
	static Element racine;

	public static ArrayList<Verbe> load(Context context)
	{
		InputStream is = context.getResources().openRawResource(R.raw.verbes);
		
		Verbe verbe = null;

		//On cr�e une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			//On cr�e un nouveau document JDOM avec en argument le fichier XML
			document = sxb.build(is);
		}
		catch(Exception e){
			e.printStackTrace();}
		ArrayList<Verbe> listeVerbes = new ArrayList<Verbe>();
		if(document==null){
			return null;
		}
		//On initialise un nouvel �l�ment racine avec l'�l�ment racine du document.
		racine = document.getRootElement();
		//On cr�e une List contenant tous les noeuds "verbe" de l'Element racine
		List listVerbes = racine.getChildren(VERBE);
		//On cr�e un Iterator sur notre liste
		Iterator i = listVerbes.iterator();
		while(i.hasNext())
		{
			//On recr�e l'Element courant � chaque tour de boucle afin de
			//pouvoir utiliser les m�thodes propres aux Element 
			Element verbe_elt = (Element)i.next();
			//On r�cup�re les �l�ments stock�s dans le xml
			String infinitif = verbe_elt.getAttributeValue(INFINITIF);
			int groupe = Integer.parseInt(verbe_elt.getAttributeValue(GROUPE));
			String temps = verbe_elt.getAttributeValue(TEMPS);
			
			Element phrases = verbe_elt.getChild(CONJUGAISON);
			List phrase_list = phrases.getChildren(VALUE); 
			//On cr�e un Iterator sur notre liste
			Iterator j = phrase_list.iterator();

			ArrayList<String> conjugaison_verbe = new ArrayList<String>();
			while(j.hasNext()){
				Element phr = (Element)j.next();
				conjugaison_verbe.add(phr.getText());
			}
			
			verbe = new Verbe(infinitif, temps, groupe, conjugaison_verbe);
			//ajoute l'histoire dans la liste listeHistoires
			listeVerbes.add(verbe);
		}
		for(Verbe v : listeVerbes){
			v.getConjugaison();
		}
		//renvoie la liste des Histoires pr�sentes dans le fichier
		return listeVerbes;
	}
}



