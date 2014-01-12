package iut.projet.activities;

import iut.projet.jardindesverbes.ObjetHistoire;
import iut.projet.jardindesverbes.Profil;
import iut.projet.jardindesverbes.ProfilManager;
import iut.projet.jardindesverbes.R;
import iut.projet.jardindesverbes.StoryManager;
import iut.projet.jardindesverbes.Utils;
import iut.projet.jardindesverbes.XMLProfilWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Jardin_Activity extends Activity {

	private static final int SCORE_PAR_DEFAUT = 0;
	ImageButton ballon, train;
	//List<ObjetHistoire> nomsObjets;
	Profil profil;
	int newExperience;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.jardin_choix_objet);

		//Déclare un SlidingMenu permettant d'afficher le niveau et l'expérience du joueur 
		SlidingMenu menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setSecondaryMenu(R.layout.menu_score);
		menu.setBehindOffset(1000);
		
		//Extras pour récupérer les données envoyées d'une activité à l'autre
		Bundle extras = getIntent().getExtras();
		profil = ProfilManager.getInstance().getProfil(extras.getString("username"));
		StoryManager.getInstance().loadStory(this, profil.getUsername());
		
		// affiche / actualise les valeurs du Menu disponible en slidant vers la droite
		setMenu();
		// affiche / actualise les objets du Jardin
		setObjectBackground();
	}

	private void setMenu() {
		// permet d'afficher le niveau et l'expérience du joueur

		final ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
		progressBar.setProgress(profil.getExperience());
		progressBar.setMax(100);
		
		final TextView niveauUtilisateur = (TextView) this.findViewById(R.id.niveauUtilisateur);
		niveauUtilisateur.setText("Niveau " + profil.getNiveau());
		
		final TextView expUtilisateur = (TextView) this.findViewById(R.id.expUtilisateur);
		expUtilisateur.setText(profil.getExperience() + " / 100");
	}

	private void setObjectBackground() {
		// Permet d'assigner à chaque objet son image selon son état
		ImageButton img;

		System.out.println(profil.getLesObjets().size());
		for(ObjetHistoire obj : profil.getLesObjets()){
			int resImage = getResources().getIdentifier(obj.getObjetImageFilename(), "drawable", getPackageName());
			int resID = getResources().getIdentifier(obj.getReference(), "id", getPackageName());
			img = (ImageButton) findViewById(resID);
			img.setBackgroundResource(resImage);
		}

	}

	public void startStory(View v){
		// Lorsqu'un objet est sélectionné, cette fonction est appelée et lance l'activité Histoire_Activity
		// Elle passe via extras le nom de l'objet, et l'expérience et le niveau du joueur
		String nomObjet=null;
		boolean start = false;
		switch (v.getId()) {
		case R.id.fleurs:
			nomObjet = "fleurs";
			break;
		case R.id.pomme:
			nomObjet = "pomme";
			break;
		case R.id.train:
			nomObjet = "train";
			break;
		case R.id.nuage:
			nomObjet = "nuage";
			break;
		case R.id.toupie:
			nomObjet = "toupie";
			break;
		case R.id.ballon:
			nomObjet = "ballon";
			break;
		case R.id.helico:
			nomObjet = "helico";
			break; 
		case R.id.arbre:
			nomObjet = "arbre";
			break;
		}


		// Parcourt le contenu de nomsObjets pour trouver l'objet choisi par l'utilisateur
		for(ObjetHistoire hist : profil.getLesObjets()){
			if(hist.getReference().equals(nomObjet)){
				start = true;
				// Chargement histoire débloquée OU déjà faite.
				Intent intent = new Intent(Jardin_Activity.this,
						Histoire_Activity.class);
				intent.putExtra("story", hist.getReference());
				intent.putExtra("experience", profil.getExperience());
				intent.putExtra("niveau", profil.getNiveau());
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent,1);
				Utils.showToastText(this, "Chargement de l'histoire \""+nomObjet+"\"");
				break;
			} 
		}
		if(start==false)
			Utils.showToastText(this, "Histoire "+nomObjet+" non débloquée");

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != RESULT_CANCELED){
			if (requestCode == 1) {

				if(resultCode == RESULT_OK){      

					// récupère le nom de l'objet, et le score
					String nomHistoire=data.getStringExtra("nomObjet");  
					int score=data.getIntExtra("scoreHistoire", SCORE_PAR_DEFAUT);  
					
					// appelle la fonction getHistoireADebloquer pour savoir s'il doit débloquer une histoire ou non
					String storyToUnlock = StoryManager.getInstance().getHistoire(nomHistoire).getHistoireADebloquer();

					//Si l'histoire a débloquer n'est pas encore disponible, on l'ajoute
					if(!StoryManager.getInstance().checkExists(storyToUnlock)){
						profil.getLesObjets().add(new ObjetHistoire(storyToUnlock,ObjetHistoire.AVAILABLE));
						Utils.showToastText(this, "Histoire "+storyToUnlock+" débloquée");
						// plus qu'à s'occuper de l'histoire finie et la passer en DONE
					}
					
					for(ObjetHistoire hist : profil.getLesObjets()){
						if(hist.getReference().equals(nomHistoire))
							if(hist.getEtat()==ObjetHistoire.AVAILABLE)
								hist.setEtat(ObjetHistoire.DONE);
					}
					
					// ajoute l'expérience actuelle du joueur et le score obtenu avec l'histoire
					newExperience = score + profil.getExperience();
					// gestion des niveaux selon l'expérience
					if( newExperience >= 100){
						while( newExperience >= 100){
							newExperience = newExperience-100;
							profil.setNiveau(profil.getNiveau()+1);
							profil.setExperience(newExperience);
						}
					}
					else{
						profil.setExperience(newExperience);
					}

					XMLProfilWriter.saveProfils(this,ProfilManager.getInstance().getProfils());
					Log.e("JDV","RESULTAT OK");
				}
			}
			setObjectBackground();
			setMenu();
		}
		if (resultCode == RESULT_CANCELED) {    
			//Histoire non complétée jusqu'au bout
		}
	}


}