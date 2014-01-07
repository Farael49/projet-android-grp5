package iut.projet.jardindesverbes;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class StoryManager {

	public static StoryManager instance;

	public List<Histoire> lesHistoires;
	public List<ObjetHistoire> lesObjetsHistoires;


	public void loadStory(Context context, String username) {
		InputStream is = context.getResources().openRawResource(R.raw.histoires);
		XMLStoryLoader xmlStories = new XMLStoryLoader();
		this.lesHistoires = xmlStories.load(is);
		this.lesObjetsHistoires = ProfilManager.getInstance().getProfil(username).getLesObjets();
	}

	/**
	 *  Cette méthode vérifie l'existence d'une dans la liste.
	 */
	public boolean checkExists(String histoireVerif){
		Histoire story = this.getHistoire(histoireVerif);
		for(ObjetHistoire objet : lesObjetsHistoires){
			System.out.println(story.getTitre() + " - " + objet.getReference());
			if(story.getTitre().equals(objet.getReference())){
				return true;
			}
		}
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

