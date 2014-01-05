package iut.projet.jardindesverbes;

import java.util.List;

import android.content.Context;
import android.util.Log;

public class VerbeManager {

	public static VerbeManager instance;
	
	public List<Verbe> lesVerbes;

	public void loadVerbes(Context context) {
		this.lesVerbes = XMLVerbeLoader.load(context);
	}

	/**
	 * R�cup�re le verbe correspondant � l'infinitif donn�.
	 * Retourne null si le verbe est introuvable.
	 * @param infinitif
	 * @return
	 */
	public Verbe getVerbe(String infinitif){
		
		if(lesVerbes == null)
			return null;
		
		for(Verbe verbe : lesVerbes){
			if(infinitif.equals(verbe.getInfinitif())){
				return verbe;
			}
		}
		return null; 
		
	}
	
	
	/**
	 *  Cette m�thode v�rifie l'existence d'un verbe dans la liste.
	 *  Les profils sont identifi�s par leur attribut Infinitif.
	 *  Retourne TRUE si un verbe du m�me infinitif existe.
	 * @param verbeVerif
	 * @return exists
	 */
	public boolean checkExists(Verbe verbeVerif){
		for(Verbe verbe : lesVerbes){
			if(verbeVerif.getInfinitif().equals(verbe.getInfinitif()))
				return true;
		}
		return false; 
	}

	
	/**
	 * Affiche le contenu de la liste [debug]
	 */
	public void afficherListe(){
		Log.e("JDV","[ DEBUG ] Les verbes :");
		int i = 0;
		for(Verbe verbe : lesVerbes){
			i++;
			Log.e("JDV","["+i+"] "+verbe.getConjugaison());
		}
	}
	
	

	/**
	 * Retourne l'ensemble des verbes de l'application
	 * @return verbes
	 */
	public List<Verbe> getVerbes(){
		return lesVerbes;
	}
	
	/**
	 * Patron Saint Gleton pour le gestionnaire de verbe.
	 * @return
	 */
	public static VerbeManager getInstance(){
		if(instance==null){
			instance=new VerbeManager();
			return instance;
		}else{
			return instance;
		}
	}
	
}
