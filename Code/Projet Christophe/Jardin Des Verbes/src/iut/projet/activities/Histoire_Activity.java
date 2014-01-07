package iut.projet.activities;

import iut.projet.jardindesverbes.Histoire;
import iut.projet.jardindesverbes.ObjetHistoire;
import iut.projet.jardindesverbes.Police;
import iut.projet.jardindesverbes.R;
import iut.projet.jardindesverbes.Utils;
import iut.projet.jardindesverbes.XMLStoryLoader;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


public class Histoire_Activity extends Activity {
	private int compteur = 0;
	private Histoire histoire;
	private boolean histoire_finie = false;
	private int scoreFinal = 0;
	private String nomObjet = "";
	private int scoreVerbe = 40;
	private int NB_ESSAIS = 3;
	private int nbTentatives;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.histoire);

		// Police d'écriture
		Police police = new Police(getApplicationContext());
		police.setupLayoutTypefaces(getWindow().getDecorView());

		// Récupère l'inputStream, données du fichier histoires.xml
		InputStream is = getResources().openRawResource(R.raw.histoires);
		XMLStoryLoader xmlStories = new XMLStoryLoader();
		// Charge la liste des histoires
		ArrayList<Histoire> list = xmlStories.load(is);


		//Récupère le nom de l'objet choisit 
		Bundle extras = getIntent().getExtras();
		nomObjet = extras.getString("story");

		Log.e("JDV",nomObjet);
		//System.out.println(list.get(0).getTitre());


		// Parcours la liste des histoires pour récupèrer dans "histoire" celle choisie
		// A voir pour refaire avec un while niveau opti
		int i=0;
		while(!list.get(i).getTitre().equals(nomObjet)){
			i++;
		}
		histoire = list.get(i);
		Log.e("JDV",histoire.getTitre());
		Log.e("JDV",histoire.getPhrases().get(0).toString());
		// Récupère le layout
		FrameLayout fl=(FrameLayout) this.findViewById(R.layout.histoire);
		// Récupère l'id de l'image utilisee comme background pour cette histoire
		int resID = getResources().getIdentifier("histoire_"+nomObjet, "drawable", getPackageName());
		// Redéfinit le background du layout
		fl.setBackgroundResource(resID);

		// Récupère le TextView utilisé pour afficher l'histoire
		final TextView textHistoire = (TextView) this.findViewById(R.id.texteHistoire);
		Log.e("JDV","phrase : "+histoire.getPhrases().get(0));
		//Affiche le début de l'histoire jusqu'au premier verbe
		String texte = (String) histoire.getPhrases().get(0);
		textHistoire.setText(texte);

		// Affiche dans un second textview (au dessus de l'edittext) l'infinitif du 1er verbe à conjuguer
		final TextView verbeInfinitif =  (TextView) this.findViewById(R.id.verbeEtTemps);
		verbeInfinitif.setText(" ( " + histoire.getInfinitifs().get(0) + " - " + histoire.getTemps().get(0) + " ) ");

		final TextView scoreTotal = (TextView) this.findViewById(R.id.scoreTotal);
		scoreTotal.setText("Score Total : 0");
		final TextView scoreDuVerbe = (TextView) this.findViewById(R.id.scoreVerbe);
		scoreDuVerbe.setText("Points pour ce verbe : 40");
		// vérifie ce que contient la liste de groupes de verbes
		Log.e("JDV","size : " + histoire.getGroupes().size() + "groupe: " + histoire.getGroupes().get(0));

		for(int d=0; d<histoire.getGroupes().size();d++)
			System.out.println(histoire.getGroupes().get(d));

		//Correspond au textView utilisé pour fenetre_score
		final TextView score = (TextView) this.findViewById(R.id.score);
		final LinearLayout fenetreScore = (LinearLayout) this.findViewById(R.id.fenetreScores);
		// EditText où l'utilisateur écrit le verbe conjugué
		final EditText entreeUtilisateur;
		entreeUtilisateur = (EditText) this.findViewById(R.id.entreeUtilisateur);
		entreeUtilisateur.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(histoire_finie){
					Log.e("JDV", "Affichage des résultats");
					fenetreScore.setVisibility(View.VISIBLE);
					score.setText("Vous avez obtenu "+scoreFinal+" points ! \n\n\nBravo !!");
					return true;
				}
				else{
					return false;
				}
			}
		});
		//Listener sur le bouton "Done" ( en français " OK ", peut être amené à changer)
		entreeUtilisateur.setOnEditorActionListener(new OnEditorActionListener() {        


			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if(actionId==EditorInfo.IME_ACTION_DONE){
					Log.e("JDV", "verbe en cours de traitement");
					// Vérifie que l'histoire n'est pas terminée avant de chercher dans la liste pour éviter un IndexOutOfBounds
					if(!histoire_finie){

						Log.e("JDV", ""+nbTentatives);
						// Si l'entrée correspond au verbe conjugué et que tous les verbes n'ont pas été trouvés : affiche le verbe et continue l'histoire
						if(verificationConjugaison(entreeUtilisateur.getText().toString().trim(), compteur)){
							Log.e("JDV", "entrée correcte");	
							Log.e("JDV", "pre score "+scoreFinal);
							//ajout du score obtenu pour ce verbe au score final
							scoreFinal += scoreVerbe;

							Log.e("JDV", "score added "+scoreFinal);

							//remise à zéro du nombre de tentatives
							nbTentatives = 0;
							//remise au maximim du scoreVerbe;
							scoreVerbe = 40;

							// incrèmente compteur après l'avoir utilisé pour le verbe, et affiche la phrase suivante
							textHistoire.setText(textHistoire.getText() + " " + histoire.getVerbes().get(compteur++) + " " 
									+ histoire.getPhrases().get(compteur) );
							if(compteur<=histoire.getVerbes().size()-1){
								verbeInfinitif.setText(" ( " + histoire.getInfinitifs().get(compteur) + " - " + histoire.getTemps().get(compteur) + " ) ");
							}
							else{
								verbeInfinitif.setVisibility(View.INVISIBLE);
								histoire_finie = true;			
							}
							//PAS FAIT : Sinon, fin de la partie, affichage des scores et récapitulé des aides, 
							// enregistrement de la progression dans le xml
							// déblocage d'autres histoires

						}
						// Sinon l'entrée est fausse, la gestion des erreurs entre en jeu
						else {
							nbTentatives++;
							if(nbTentatives<2)
								scoreVerbe = 40;
							if(nbTentatives==2)
								scoreVerbe = 25;
							else if(nbTentatives>2)
								scoreVerbe = 15;
							Log.e("JDV", "entrée incorrecte");	
							// Première erreur, notifié au joueur
							if(nbTentatives==1){
								Toast toast = Toast.makeText(getBaseContext(), "Ce n'est pas ça, essaies encore !", MS_TIME_TO_EXIT);
								toast.show();
							}	
							else if(nbTentatives==2){
								//appel de la première aide
								// ficheAide1();
							}
							else if (nbTentatives==3){
								//						Utils.showToastText(getBaseContext(), "Profite des aides pour progresser !");
							}
						}

					}

					// réinitialise le champs après chaque validation (bouton "OK" ) de l'utilisateur
					entreeUtilisateur.setText("");
					scoreTotal.setText("Score total : "+scoreFinal);
					scoreDuVerbe.setText("Points pour ce verbe : "+scoreVerbe);
					if(histoire_finie){
						entreeUtilisateur.setText("Appuyez pour valider");
					}
				}
				return false;
			}
		});

	}

	public void retourJardin(View v) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("nomObjet",nomObjet);
		returnIntent.putExtra("scoreHistoire",scoreFinal);
		setResult(RESULT_OK,returnIntent);   
		finish();					
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/**
		 * Non demandé, retourne false
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


	long lastPress;
	final int MS_TIME_TO_EXIT = 3000;
	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if(currentTime - lastPress > MS_TIME_TO_EXIT){
			lastPress = currentTime;
			Toast toast = Toast.makeText(getBaseContext(), "Appuyez deux fois sur retour\n  pour revenir en arrière", MS_TIME_TO_EXIT);
			toast.show();

		}else{
			super.onBackPressed();
		}
	}

}




