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
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


public class Histoire_Activity extends Activity {
	private int compteur = 0;
	private Histoire histoire;
	private boolean histoire_finie = false;
	private int score = 0;
	private String nomObjet = "";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.histoire);
		
		// Police d'�criture
		Police police = new Police(getApplicationContext());
		police.setupLayoutTypefaces(getWindow().getDecorView());
		
		// R�cup�re l'inputStream, donn�es du fichier histoires.xml
		InputStream is = getResources().openRawResource(R.raw.histoires);
		XMLStoryLoader xmlStories = new XMLStoryLoader();
		// Charge la liste des histoires
		ArrayList<Histoire> list = xmlStories.load(is);


		//R�cup�re le nom de l'objet choisit 
		Bundle extras = getIntent().getExtras();
		nomObjet = extras.getString("story");

		Log.e("JDV",nomObjet);
		//System.out.println(list.get(0).getTitre());


		// Parcours la liste des histoires pour r�cup�rer dans "histoire" celle choisie
		// A voir pour refaire avec un while niveau opti
		int i=0;
		while(!list.get(i).getTitre().equals(nomObjet)){
			i++;
		}
		histoire = list.get(i);
		Log.e("JDV",histoire.getTitre());
		Log.e("JDV",histoire.getPhrases().get(0).toString());
		// R�cup�re le layout
		FrameLayout fl=(FrameLayout) this.findViewById(R.layout.histoire);
		// R�cup�re l'id de l'image utilisee comme background pour cette histoire
		int resID = getResources().getIdentifier("histoire_"+nomObjet, "drawable", getPackageName());
		// Red�finit le background du layout
		fl.setBackgroundResource(resID);

		// R�cup�re le TextView utilis� pour afficher l'histoire
		final TextView text1 = (TextView) this.findViewById(R.id.textView1);
		Log.e("JDV","phrase : "+histoire.getPhrases().get(0));
		//Affiche le d�but de l'histoire jusqu'au premier verbe
		String texte = (String) histoire.getPhrases().get(0);
		text1.setText(texte);

		// Affiche dans un second textview (au dessus de l'edittext) l'infinitif du 1er verbe � conjuguer
		final TextView verbeInfinitif =  (TextView) this.findViewById(R.id.textView2);
		verbeInfinitif.setText(" ( " + histoire.getInfinitifs().get(0) + " )");

		// v�rifie ce que contient la liste de groupes de verbes
		Log.e("JDV","size : " + histoire.getGroupes().size() + "groupe: " + histoire.getGroupes().get(0));

		for(int d=0; d<histoire.getGroupes().size();d++)
			System.out.println(histoire.getGroupes().get(d));

		// EditText o� l'utilisateur �crit le verbe conjugu�
		final EditText entreeUtilisateur;
		entreeUtilisateur = (EditText) this.findViewById(R.id.editText1);
		entreeUtilisateur.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(histoire_finie){
					Log.e("JDV", "Affichage des r�sultats");
					
					//apr�s l'affichage des r�sultats et des aides
					// retour au jardin
					Intent returnIntent = new Intent();
					returnIntent.putExtra("nomObjet",nomObjet);
				//	returnIntent.putExtra("score",score);
					setResult(RESULT_OK,returnIntent);   
					finish();
					return true;
				}
				else{
					return false;
				}
			}
		});
		//Listener sur le bouton "Done" ( en fran�ais " OK ", peut �tre amen� � changer)
		entreeUtilisateur.setOnEditorActionListener(new OnEditorActionListener() {        


			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if(actionId==EditorInfo.IME_ACTION_DONE){
					Log.e("JDV", "verbe en cours de traitement");
					// V�rifie que l'histoire n'est pas termin�e avant de chercher dans la liste pour �viter un IndexOutOfBounds
					if(!histoire_finie){
						// Si l'entr�e correspond au verbe conjugu� et que tous les verbes n'ont pas �t� trouv�s : affiche le verbe et continue l'histoire
						if(verificationConjugaison(entreeUtilisateur.getText().toString().trim(), compteur)){
							Log.e("JDV", "entr�e correcte");	
							// incr�mente compteur apr�s l'avoir utilis� pour le verbe, et affiche la phrase suivante
							text1.setText(text1.getText() + " " + histoire.getVerbes().get(compteur++) + " " 
									+ histoire.getPhrases().get(compteur) );
							if(compteur<=histoire.getVerbes().size()-1)
								verbeInfinitif.setText(" ( " + histoire.getInfinitifs().get(compteur) + " )");
							//PAS FAIT : Sinon, fin de la partie, affichage des scores et r�capitul� des aides, 
							// enregistrement de la progression dans le xml
							// d�blocage d'autres histoires
							else{
								verbeInfinitif.setVisibility(4);
								histoire_finie = true;			
							}
						}
						// Sinon l'entr�e est fausse, la gestion des erreurs entre en jeu
						else {
							Log.e("JDV", "entr�e incorrecte");	
						}

					}
					// r�initialise le champs apr�s chaque validation (bouton "OK" ) de l'utilisateur
					entreeUtilisateur.setText("");
					//
					if(histoire_finie){
						entreeUtilisateur.setText("Appuyez pour valider");
					}
				}
				return false;
			}
		});

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/**
		 * Non demand�, retourne false
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
			Toast toast = Toast.makeText(getBaseContext(), "Appuyez deux fois sur retour\n  pour revenir en arri�re", MS_TIME_TO_EXIT);
			toast.show();

		}else{
			super.onBackPressed();
		}
	}

}




