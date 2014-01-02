package iut.projet.activities;

import iut.projet.jardindesverbes.ObjetHistoire;
import iut.projet.jardindesverbes.Profil;
import iut.projet.jardindesverbes.ProfilManager;
import iut.projet.jardindesverbes.R;
import iut.projet.jardindesverbes.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class Jardin_Activity extends Activity {

	ImageButton ballon, train;
	// List<ObjetHistoire> nomsObjets;
	Profil profil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.jardin_choix_objet);
		Bundle extras = getIntent().getExtras();
		profil = ProfilManager.getInstance().getProfil(
				extras.getString("username"));
		Log.e("JDV", "Valeur :" + R.drawable.histoire_helico);
		setObjectBackground();
		Utils.showToastText(this, profil.getUsername());
	}

	private void setObjectBackground() {
		ImageButton img;
		/*
		 * // Permet d'assigner à chaque objet son image et son état nomsObjets
		 * = new ArrayList<ObjetHistoire>(); //les données de nomObjets doivent
		 * normalement provenir du profil XML int i=2; nomsObjets.add(new
		 * ObjetHistoire("pomme")); nomsObjets.add(new
		 * ObjetHistoire("train",i)); nomsObjets.add(new
		 * ObjetHistoire("fleurs",i)); nomsObjets.add(new
		 * ObjetHistoire("toupie",i)); nomsObjets.add(new
		 * ObjetHistoire("ballon",i)); nomsObjets.add(new
		 * ObjetHistoire("helico",i)); nomsObjets.add(new
		 * ObjetHistoire("nuage",i)); nomsObjets.add(new
		 * ObjetHistoire("arbre",i));
		 */

		for (ObjetHistoire obj : profil.getLesObjets()) {
			int resImage = getResources().getIdentifier(
					obj.getObjetImageFilename(), "drawable", getPackageName());
			int resID = getResources().getIdentifier(obj.getReference(), "id",
					getPackageName());
			// int resID = R.id.pomme;
			// int resImage = R.drawable.objet_pomme;
			img = (ImageButton) findViewById(resID);
			img.setBackgroundResource(resImage);
		}

	}

	public void startStory(View v) {
		String nomObjet = null;
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

		ObjetHistoire histoire = null;
		// Parcourt le contenu de nomsObjets pour trouver l'objet choisit par
		// l'utilisateur
		for (ObjetHistoire hist : profil.getLesObjets()) {

			if (hist.getReference().equals(nomObjet)) {
				// Chargement histoire débloquée OU déjà faite.
				Intent intent = new Intent(Jardin_Activity.this,
						Histoire_Activity.class);
				intent.putExtra("story", hist.getReference());
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				Utils.showToastText(this, "Chargement de l'histoire \""
						+ nomObjet + "\"");
				break;
			}

		}
		if (histoire == null)
			Utils.showToastText(this, "Histoire " + nomObjet + " non débloquée");
	}

}