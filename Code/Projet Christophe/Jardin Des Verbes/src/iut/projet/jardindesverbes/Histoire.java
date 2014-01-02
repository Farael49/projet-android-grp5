package iut.projet.jardindesverbes;

import java.util.ArrayList;
import java.util.List;

public class Histoire {

	public String getTitre() {
		return titre;
	}

	public List getVerbes() {
		return verbes;
	}

	public List getPhrases() {
		return phrases;
	}
	
	public List getGroupes() {
		return groupes;
	}
	
	public List getTemps() {
		return temps;
	}
	
	public List getInfinitifs() {
		return infinitifs;
	}

	private String titre;
	private List verbes;
	private List phrases;
	private List groupes;
	private List temps;
	private List infinitifs;
	
	public Histoire(String titre, List listPhrases, List listVerbes, List groupes, List temps, List infinitifs){
		this.titre = titre;
		this.phrases = listPhrases;
		this.verbes = listVerbes;
		this.groupes = groupes;
		this.temps = temps;
		this.infinitifs = infinitifs;
	}
	
	@Override
	public String toString() {
		return "Histoire [titre=" + titre + ", verbes=" + verbes.get(1) + ", phrases="
				+ phrases.get(1) + "]";
	}
}
