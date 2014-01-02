package iut.projet.jardindesverbes;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class StoryManager {

	public static StoryManager instance;

	public List<Histoire> lesHistoires;
	public List<ObjetHistoire> lesObjetsHistoires;


	public void loadStory(Context context, List objetHistoire) {
		InputStream is = context.getResources().openRawResource(R.raw.histoires);
		XMLStoryLoader xmlStories = new XMLStoryLoader();
		this.lesHistoires = xmlStories.load(is);
		this.lesObjetsHistoires = objetHistoire;
	}

	/**
	 *  Cette méthode vérifie l'existence d'un profil dans la liste.
	 *  Les profils sont identifiés par leur attribut Username.
	 *  Retourne TRUE si un profil du même nom existe.
	 * @param profilVerif
	 * @return exists
	 */
	public boolean checkExists(String histoireVerif){
		Histoire story = this.getHistoire(histoireVerif);
		for(ObjetHistoire objet : lesObjetsHistoires){
			if(story.getTitre().equals(objet.getReference())){
				return true;
			}
			else{
				return false;
			}

		}

		//n'arrive jamais
		return false; 
	}

	public Histoire getHistoire(String nomHistoire){
		for(Histoire histoire : lesHistoires){
			if(nomHistoire.equals(histoire.getTitre())){
				return histoire;
			}
		}
		return null; 	
	}

	public ObjetHistoire getObjetHistoire(String nomHistoire){
		for(ObjetHistoire histoire : lesObjetsHistoires){
			if(nomHistoire.equals(histoire.getReference())){
				return histoire;
			}
		}
		return null; 	
	}

	public static StoryManager getInstance(){
		if(instance==null){
			instance=new StoryManager();
			return instance;
		}else{
			return instance;
		}
	}
}

