package iut.projet.jardindesverbes;

import java.util.ArrayList;

import android.util.Log;

public class Verbe {

	private static String[] SUJET_CONSONNE = {"je ", "tu ", "il ", "nous ", "vous ", "ils "};
	private static String[] SUJET_VOYELLE = {"j'", "tu ", "il ", "nous ", "vous ", "ils "};

	
	private String infinitif;
	private String temps;
	private int groupe;
	private ArrayList<String> conjugaison;
	
	public Verbe(String infinitif, String temps, int groupe,
			ArrayList<String> conjugaison_verbe) {
		this.infinitif = infinitif;
		this.temps = temps;
		this.groupe = groupe;
		this.conjugaison = conjugaison_verbe;
	}
	
	public String getConjugaison(){
		String str = "";
		int i = 0;
		String[] sujets = getSujet();
		for(String conj : conjugaison){
			str = str + "- " + sujets[i] + conj + "\n";
			i++;
		}
		Log.e("JDV",str);
		return str;
	}
	
	public String[] getSujet(){
		if ("aeiou".indexOf(Character.toLowerCase(infinitif.charAt(0))) >= 0) {
			return SUJET_VOYELLE;
		} else {
			return SUJET_CONSONNE;
		}
	}
	
	/**
	 * Setter & Getter
	 */
	public String getInfinitif() {
		return infinitif;
	}
	public void setInfinitif(String infinitif) {
		this.infinitif = infinitif;
	}
	public String getTemps() {
		return temps;
	}
	public void setTemps(String temps) {
		this.temps = temps;
	}
	public int getGroupe() {
		return groupe;
	}
	public void setGroupe(int groupe) {
		this.groupe = groupe;
	}

	@Override
	public String toString() {
		return "Verbe [infinitif=" + infinitif + ", temps=" + temps
				+ ", groupe=" + groupe + ", conjugaison=" + conjugaison + "]";
	}
	
}
