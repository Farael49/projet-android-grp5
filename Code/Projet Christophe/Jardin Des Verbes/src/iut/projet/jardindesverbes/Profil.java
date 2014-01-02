

package iut.projet.jardindesverbes;

import java.util.ArrayList;
import java.util.List;

public class Profil {

	private String username;
	private int niveau;
	private int experience;
	private List<ObjetHistoire> lesObjets;
// utilisé lors de la création d'un profil
	public Profil(String username) {
		this.username = username;
		this.niveau = 1;
		this.experience = 0;
		this.lesObjets = new ArrayList<ObjetHistoire>();
		this.lesObjets.add(new ObjetHistoire("fleurs",ObjetHistoire.AVAILABLE));
	}

	public Profil() {
		this.lesObjets = new ArrayList<ObjetHistoire>();
	}

	// Getters & Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getNiveau() {
		return niveau;
	}

	public void setNiveau(int niveau) {
		this.niveau = niveau;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public List<ObjetHistoire> getLesObjets(){
		return lesObjets;
	}

	public String toString() {
		return "Profil [username=" + username + ", niveau=" + niveau
				+ ", experience=" + experience + ", lesObjets=" + lesObjets
				+ "]";
	}




}
