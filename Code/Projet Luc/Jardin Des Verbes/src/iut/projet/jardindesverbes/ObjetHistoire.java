package iut.projet.jardindesverbes;

public class ObjetHistoire {

	// Ces valeurs d�finissent l'�tat de l'objetHistoire
	// Par d�faut toutes les histoires sont verouill�es.
	
	// L'histoire est verouill�e.
	public static final int LOCKED = 0;
	// L'histoire est accessible
	public static final int AVAILABLE = 1;
	// L'histoire a d�j� �t� termin�e.
	public static final int DONE = 2;
	
	private int etat;
	private String imageItemReference;
	
	// Constructeur item par d�faut, verouill�.
	public ObjetHistoire(String itemName){
		this.imageItemReference = itemName;
		this.etat = LOCKED;
	}

	// Constructeur item accessible / termin�.
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
