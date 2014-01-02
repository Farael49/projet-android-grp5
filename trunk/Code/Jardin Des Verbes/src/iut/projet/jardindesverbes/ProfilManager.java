package iut.projet.jardindesverbes;

import java.util.List;

import android.content.Context;
import android.util.Log;

public class ProfilManager {

	public static ProfilManager instance;
	
	public List<Profil> lesProfils;

	public void loadProfils(Context context) {
		this.lesProfils = XMLProfilLoader.loadProfils(context);
	}

	/**
	 * Ajout d'un nouveau profil.
	 * Retourne faux si le profil n'a pas été ajouté.
	 * @param profil
	 * @return etatAjout
	 */
	public boolean addProfil(Context context, Profil profil) {
		if(!checkExists(profil)){
			lesProfils.add(profil);
			XMLProfilWriter.saveProfils(context, lesProfils);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Récupère le profil correspondant à un nom d'utilisateur.
	 * Retourne null si le profil est introuvable.
	 * @param username
	 * @return
	 */
	public Profil getProfil(String username){
		for(Profil profil : lesProfils){
			if(username.equals(profil.getUsername())){
				return profil;
			}
		}
		return null; 
		
	}
	
	/**
	 * Supprime le profil donné en paramètre de la liste.
	 * @param profilSupr
	 */
	public void deleteProfil(Profil profilSupr){
		for(Profil profil : lesProfils){
			if(profilSupr.getUsername().equals(profil.getUsername())){
				lesProfils.remove(profil);
				break;
			}
		}
	}
	
	/**
	 *  Cette méthode vérifie l'existence d'un profil dans la liste.
	 *  Les profils sont identifiés par leur attribut Username.
	 *  Retourne TRUE si un profil du même nom existe.
	 * @param profilVerif
	 * @return exists
	 */
	public boolean checkExists(Profil profilVerif){
		for(Profil profil : lesProfils){
			if(profilVerif.getUsername().equals(profil.getUsername()))
				return true;
		}
		return false; 
	}

	
	/**
	 * Affiche le contenu de la liste [debug]
	 */
	public void afficherListe(){
		Log.e("JDV","[ DEBUG ] Les Profils :");
		int i = 0;
		for(Profil profil : lesProfils){
			i++;
			Log.e("JDV","["+i+"] "+profil.getUsername());
		}
	}
	
	/**
	 * Supprimes toutes les entrées du XML profil et vide la liste.
	 */
	public void viderListe(Context context){
		lesProfils.clear();
		XMLProfilWriter.saveProfils(context, lesProfils);
	}
	
	/**
	 * Patron Saint Gleton pour le gestionnaire de profils.
	 * @return
	 */
	public static ProfilManager getInstance(){
		if(instance==null){
			instance=new ProfilManager();
			return instance;
		}else{
			return instance;
		}
	}
}
