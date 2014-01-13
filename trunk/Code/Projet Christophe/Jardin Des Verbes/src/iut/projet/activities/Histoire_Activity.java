package iut.projet.activities;

import iut.projet.jardindesverbes.Histoire;
import iut.projet.jardindesverbes.ObjetHistoire;
import iut.projet.jardindesverbes.Police;
import iut.projet.jardindesverbes.R;
import iut.projet.jardindesverbes.Utils;
import iut.projet.jardindesverbes.Verbe;
import iut.projet.jardindesverbes.VerbeManager;
import iut.projet.jardindesverbes.XMLStoryLoader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.R.color;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


public class Histoire_Activity extends Activity {
	private int compteur = 0;
	private Histoire histoire;
	private boolean histoire_finie = false;
	boolean fiche_parcourues = false;
	private int scoreFinal = 0;
	private String nomObjet = "";
	private int scoreVerbe = 40;
	private int nbTentatives;
	private int aide;
	VerbeManager verManager = VerbeManager.getInstance();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.histoire);



		//D�clare un SlidingMenu permettant d'afficher le niveau et l'exp�rience du joueur 
		SlidingMenu menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setSecondaryMenu(R.layout.menu_score_histoire);
		menu.setBehindOffset(1000);

		// Police d'�criture
		Police police = new Police(getApplicationContext());
		police.setupLayoutTypefaces(getWindow().getDecorView());

		// R�cup�re l'inputStream, donn�es du fichier histoires.xml
		InputStream is = getResources().openRawResource(R.raw.histoires);
		XMLStoryLoader xmlStories = new XMLStoryLoader();
		// Charge la liste des histoires
		ArrayList<Histoire> list = xmlStories.load(is);

		//charge la liste des verbes d'aide
		verManager.loadVerbes(this);

		final TextView expUtilisateur = (TextView) this.findViewById(R.id.expUtilisateur);
		final TextView niveautilisateur = (TextView) this.findViewById(R.id.niveauUtilisateur);
		final TextView verbeProgress = (TextView) this.findViewById(R.id.verbeProgress);

		//R�cup�re le nom de l'objet choisit 
		Bundle extras = getIntent().getExtras();
		nomObjet = extras.getString("story");

		expUtilisateur.setText(extras.getInt("experience") + "%");
		niveautilisateur.setText("Niveau " + extras.getInt("niveau"));
		




		// Parcours la liste des histoires pour r�cup�rer dans "histoire" celle choisie
		int i=0;
		while(!list.get(i).getTitre().equals(nomObjet)){
			i++;
		}
		histoire = list.get(i);

		verbeProgress.setText("0/" + histoire.getVerbes().size());
		
		if(compteur/histoire.getVerbes().size() > 0.5)
			verbeProgress.setTextColor(color.holo_purple);
		
		final ProgressBar progressBarExp = (ProgressBar) this.findViewById(R.id.progressBarExperience);
		progressBarExp.setProgress(extras.getInt("experience"));
		progressBarExp.setMax(100);

		final ProgressBar progressBarVerbes = (ProgressBar) this.findViewById(R.id.progressBarVerbes);
		progressBarVerbes.setMax(histoire.getVerbes().size());

		// R�cup�re le layout
		FrameLayout fl=(FrameLayout) this.findViewById(R.layout.histoire);
		// R�cup�re l'id de l'image utilisee comme background pour cette histoire
		int resID = getResources().getIdentifier("histoire_"+nomObjet, "drawable", getPackageName());
		// Red�finit le background du layout
		fl.setBackgroundResource(resID);

		// R�cup�re le TextView utilis� pour afficher l'histoire
		final TextView textHistoire = (TextView) this.findViewById(R.id.texteHistoire);

		// Affiche le d�but de l'histoire jusqu'au premier verbe
		String texte = (String) histoire.getPhrases().get(0);
		textHistoire.setText(texte);

		// Affiche dans un second textview (au dessus de l'edittext) l'infinitif du 1er verbe � conjuguer
		final TextView verbeInfinitif =  (TextView) this.findViewById(R.id.verbeEtTemps);
		verbeInfinitif.setText(" ( " + histoire.getInfinitifs().get(0) + " - " + histoire.getTemps().get(0) + " ) ");

		// EditText o� l'utilisateur �crit le verbe conjugu�
		final EditText entreeUtilisateur;
		entreeUtilisateur = (EditText) this.findViewById(R.id.entreeUtilisateur);


		// R�cup�re le layout du textView verbeInfinitif pour l'adapter lors de l'affichage du clavier
		final FrameLayout.LayoutParams verbeParams = (FrameLayout.LayoutParams)verbeInfinitif.getLayoutParams();
		final int vHeight = verbeParams.height;
		final int vLeftMargin = verbeParams.leftMargin;
		final int vRightMargin = verbeParams.rightMargin;
		final int vBottomMargin = verbeParams.bottomMargin;

		final FrameLayout.LayoutParams entreeUtilisateurParams = (FrameLayout.LayoutParams)entreeUtilisateur.getLayoutParams();
		final int eHeight = entreeUtilisateurParams.height;
		final int eLeftMargin = entreeUtilisateurParams.leftMargin;
		final int eRightMargin = entreeUtilisateurParams.rightMargin;
		final int eBottomMargin = entreeUtilisateurParams.bottomMargin;

		final TextView scoreTotal = (TextView) this.findViewById(R.id.scoreTotal);
		scoreTotal.setText("Score Total : 0");
		final TextView scoreDuVerbe = (TextView) this.findViewById(R.id.scoreVerbe);
		scoreDuVerbe.setText("Points pour ce verbe : 40");


		//Pour d�tecter lorsque le clavier apparait
		final View activityRootView = findViewById(android.R.id.content);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			public void onGlobalLayout() {
				int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
				if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
					System.out.println("Checked a keyboard");
					verbeParams.setMargins(verbeParams.leftMargin, verbeParams.topMargin, verbeParams.rightMargin, 10);
					entreeUtilisateurParams.setMargins(entreeUtilisateurParams.leftMargin, entreeUtilisateurParams.topMargin, entreeUtilisateurParams.rightMargin, 0);
					verbeInfinitif.setLayoutParams(verbeParams);
				}
				else{
					System.out.println("KBoard disabled");
					verbeParams.setMargins(vLeftMargin, vHeight, vRightMargin, vBottomMargin); //substitute parameters for left, top, right, bottom
					entreeUtilisateurParams.setMargins(entreeUtilisateurParams.leftMargin, entreeUtilisateurParams.topMargin, entreeUtilisateurParams.rightMargin, eBottomMargin);
					verbeInfinitif.setLayoutParams(verbeParams);
				}
			}
		});

		//Correspond au bouton d'aide qui apparait � partir de 2 erreurs sur un m�me verbe
		final Button aideConjugaison = (Button) this.findViewById(R.id.aideConjugaison);

		//Listener sur le click de l'editText, permet de valider la fin de partie
		entreeUtilisateur.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(histoire_finie){
					Log.e("JDV", "Affichage des r�sultats");
					//fenetreScore.setVisibility(View.VISIBLE);
					//score.setText("Vous avez obtenu "+scoreFinal+" points ! \n\n\nBravo !!");
					affichageScore(v);
					if(!fiche_parcourues){
						for(int i=0; i<compteur; i++){
							String temps = (String) histoire.getTemps().get(i);
							String infinitif = (String) histoire.getInfinitifs().get(i);
							Verbe verbe = verManager.getVerbe(infinitif, temps);
							ficheAide(v, verbe);
							fiche_parcourues = true;
						}
					}
				}

			}


		});

		//Listener sur le bouton "Done" ( en fran�ais " OK ")
		entreeUtilisateur.setOnEditorActionListener(new OnEditorActionListener() {        

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// Si l'utilisateur a confirm� son entr�e en appuyant sur OK et que l'entr�e n'est pas vide
				if(actionId==EditorInfo.IME_ACTION_DONE && entreeUtilisateur.getText().length()>0){
					Log.e("JDV", "verbe en cours de traitement");
					// V�rifie que l'histoire n'est pas termin�e avant de chercher dans la liste pour �viter un IndexOutOfBounds
					if(!histoire_finie){

						Log.e("JDV", ""+nbTentatives);
						// Si l'entr�e correspond au verbe conjugu� et que tous les verbes n'ont pas �t� trouv�s : affiche le verbe et continue l'histoire
						if(verificationConjugaison(entreeUtilisateur.getText().toString().trim(), compteur)){

							//ajout du score obtenu pour ce verbe au score final
							scoreFinal += scoreVerbe;

							//On cache le bouton d'aide pour le prochain Verbe
							aideConjugaison.setVisibility(View.INVISIBLE);
							//remise � z�ro du nombre de tentatives
							nbTentatives = 0;
							//remise au maximim du scoreVerbe;
							scoreVerbe = 40;

							// incr�mente compteur apr�s l'avoir utilis� pour le verbe, et affiche la phrase suivante
							textHistoire.setText(textHistoire.getText() + " " + histoire.getVerbes().get(compteur++) + " " 
									+ histoire.getPhrases().get(compteur) );

							//actualise la progressBar du nombre de verbes corrects
							progressBarVerbes.setProgress(compteur);
							verbeProgress.setText(compteur + "/" + histoire.getVerbes().size());
							
							//actualise l'infinitif et le temps pour correspondre au verbe suivant
							if(compteur<=histoire.getVerbes().size()-1){
								verbeInfinitif.setText(" ( " + histoire.getInfinitifs().get(compteur) + " - " + histoire.getTemps().get(compteur) + " ) ");
							}
							else{
								// tous les verbes ont �t� compl�t�s -> fin d'histoire, cache verbeInfinitif et d�clare l'histoire comme finie
								verbeInfinitif.setVisibility(View.INVISIBLE);
								histoire_finie = true;			
							}
						}
						// Sinon l'entr�e est fausse, la gestion des erreurs entre en jeu
						else {
							nbTentatives++;
							if(nbTentatives<2)
								scoreVerbe = 40;
							if(nbTentatives==2)
								scoreVerbe = 25;
							else if(nbTentatives>2)
								scoreVerbe = 15;
							Log.e("JDV", "entr�e incorrecte");	
							// Premi�re erreur, notifi� au joueur
							if(nbTentatives==1){
								Toast toast = Toast.makeText(getBaseContext(), "Ce n'est pas �a, essaies encore !", MS_TIME_TO_EXIT);
								toast.show();
							}	
							else if(nbTentatives==2||nbTentatives==3){
								//appel de l'aide, qui g�re selon le nombre de tentatives r�alis�es l'aide � afficher
								ficheAide(new View(getBaseContext()));
								aideConjugaison.setVisibility(View.VISIBLE);
							}
							else if (nbTentatives>3){
								Toast toast = Toast.makeText(getBaseContext(), "Profite de l'aide pour r�ussir !", MS_TIME_TO_EXIT);
								toast.show();
							}
						}

					}

					// r�initialise le champs apr�s chaque validation (bouton "OK" ) de l'utilisateur
					entreeUtilisateur.setText("");
					scoreTotal.setText("Score total : "+scoreFinal);
					scoreDuVerbe.setText("Points pour ce verbe : "+scoreVerbe);
					// modifie l'editText pour indiquer � l'utilisateur qu'il peut appuyer dessus lorsqu'il a fini de lire l'histoire pour la valider
					if(histoire_finie){
						entreeUtilisateur.setText("Appuyez pour valider");
						entreeUtilisateur.setFocusable(false);
					}
				}
				return false;
			}
		});

	}

	public void retourJardin(View v) {
		// fonction de retour au Jardin, renvoie les donn�es du jeu demand�es par l'onActivityResult
		Intent returnIntent = new Intent();
		returnIntent.putExtra("nomObjet",nomObjet);
		returnIntent.putExtra("scoreHistoire",scoreFinal);
		setResult(RESULT_OK,returnIntent);   
		finish();					
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/**
		 * Non utilis�, retourne false
		 */
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	public boolean verificationConjugaison(String entreeUtilisateur, int compteur){
		//Si la conjugaison est bonne
		if(entreeUtilisateur.equals(histoire.getVerbes().get(compteur))){
			return true;
		}
		// sinon
		return false;
	}

	public void ficheAide(View v){
		/*
		 *  fiche d'aide disponible � partir de 2 erreurs
		 *  cherche le verbe utilis� dans verbes.xml, et en affiche l'aide selon le temps
		 */

		int groupe = Integer.parseInt((String) histoire.getGroupes().get(compteur));
		String temps = (String) histoire.getTemps().get(compteur);
		String infinitif = (String) histoire.getInfinitifs().get(compteur);
		String infinitifVerbe = "";
		Verbe verbe = new Verbe(null, null, 0, null);
		final Dialog dialog = new Dialog(this);
		if(nbTentatives==2){
			for(Verbe verbeCorrespondant : VerbeManager.getInstance().getVerbes()){
				if(groupe==verbeCorrespondant.getGroupe() && temps.equals(verbeCorrespondant.getTemps()) && !infinitif.equals(verbeCorrespondant.getInfinitif())){
					verbe = verManager.getVerbe(verbeCorrespondant.getInfinitif(), temps);
					break;
				}
			}
		}
		else{
			verbe = verManager.getVerbe(infinitif, temps);
		}

		dialog.setContentView(R.layout.aide_1);
		dialog.setTitle("Un petit coup de main ?");

		// set the custom dialog components - text, image and button
		TextView aide_Titre = (TextView) dialog.findViewById(R.id.aide_titre);
		TextView aide_Conjugaison = (TextView) dialog.findViewById(R.id.aide_conjugaison);

		aide_Titre.setText("Conjugaison du verbe '"+verbe.getInfinitif()+"' au temps : "+verbe.getTemps());

		aide_Conjugaison.setText(verbe.getConjugaison());
		aide_Conjugaison.setX(300);
		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog

		dialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	public void ficheAide(View v, Verbe verbe){
		/*
		 * Du m�me type � la fiche d'aide pr�c�dente, celle-ci ne n�cessite pas de rechercher un verbe "similaire"
		 * mais uniquement d'afficher un verbe pr�sent dans l'histoire, pass� en param�tre
		 */
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.aide_1);
		dialog.setTitle("R�capitulatif des verbes");

		// set the custom dialog components - text, image and button
		TextView aide_Titre = (TextView) dialog.findViewById(R.id.aide_titre);
		TextView aide_Conjugaison = (TextView) dialog.findViewById(R.id.aide_conjugaison);

		aide_Titre.setText("Conjugaison du verbe '"+verbe.getInfinitif()+"' au temps :"+verbe.getTemps());

		aide_Conjugaison.setText(verbe.getConjugaison());
		aide_Conjugaison.setX(300);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setText("Suivant");
		// if button is clicked, close the custom dialog

		dialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}


	public void affichageScore(View v){
		/*
		 * Dialogue permettant d'afficher le score en fin de partie
		 */
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.fenetre_score);
		dialog.setTitle("Votre score");

		// set the custom dialog components - text, image and button
		TextView score = (TextView) dialog.findViewById(R.id.score);

		score.setText("Vous avez obtenu "+scoreFinal+" points !");


		Button validerHistoire = (Button) dialog.findViewById(R.id.finHistoire);
		// if button is clicked, close the custom dialog

		validerHistoire.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				retourJardin(v);			
			}
		});
		dialog.show();

	}


	long lastPress;
	final int MS_TIME_TO_EXIT = 3000;
	@Override

	public void onBackPressed() {
		/*
		 * Red�finit la pression du bouton retour pour �viter de perdre l'avanc�e
		 * � cause d'une l�g�re erreur de manipulation
		 */
		long currentTime = System.currentTimeMillis();
		if(currentTime - lastPress > MS_TIME_TO_EXIT){
			lastPress = currentTime;
			Toast toast = Toast.makeText(getBaseContext(), "Appuyez deux fois sur retour\n  pour revenir en arri�re", MS_TIME_TO_EXIT);
			toast.show();

		}else{
			super.onBackPressed();
		}
	}


}




