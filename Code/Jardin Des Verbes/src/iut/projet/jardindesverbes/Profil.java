package iut.projet.jardindesverbes;

public class Profil {

	private String username;
	private int niveau;
	private int experience;

	public Profil(String username, int niveau, int experience) {
		this.username = username;
		this.niveau = niveau;
		this.experience = experience;
	}

	public Profil(String username, int niveau) {
		this.username = username;
		this.niveau = niveau;
		this.experience = 0;
	}
	
	public Profil(String username) {
		this.username = username;
		this.niveau = 1;
		this.experience = 0;
	}
	
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

}
