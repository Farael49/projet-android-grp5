package iut.projet.jardindesverbes;

public class ObjetHistoire {

	// Ces valeurs définissent l'état de l'objetHistoire
	// Par défaut toutes les histoires sont verouillées.
	
	// L'histoire est verouillée.
	public static final int LOCKED = 0;
	// L'histoire est accessible
	public static final int AVAILABLE = 1;
	// L'histoire a déjà été terminée.
	public static final int DONE = 2;
	
	private int etat;
	private String imageItemReference;
	
	// Constructeur item par défaut, verouillé.
	public ObjetHistoire(String itemName){
		this.imageItemReference = itemName;
		this.etat = LOCKED;
	}

	// Constructeur item accessible / terminé.
	public ObjetHistoire(String itemName, int etat){
		this.imageItemReference = itemName;
		this.etat = etat;
	}
	
	public void setEtat(int i){
		this.etat = i;
	}
	
	public int getEtat(){
		return this.etat;
	}
	
	public String getReference(){
		return this.imageItemReference;
	}
	
	public String getObjetImageFilename(){
		if(this.etat == LOCKED)
			return "objet_"+imageItemReference+"_g";
		if(this.etat == AVAILABLE)
			return "objet_"+imageItemReference+"_l";
		if(this.etat == DONE)
			return "objet_"+imageItemReference;
		return null; // N'arrive pas.
	}
}
