package iut.projet.activities;

import iut.projet.jardindesverbes.Histoire;
import iut.projet.jardindesverbes.R;
import iut.projet.jardindesverbes.XMLStoryLoader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Histoire_Activity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.histoire);
		// Récupère l'inputStream, données du fichier histoires.xml
		InputStream is = getResources().openRawResource(R.raw.histoires);
		XMLStoryLoader xmlStories = new XMLStoryLoader();
		// Charge la liste des histoires
		ArrayList<Histoire> list = xmlStories.load(is);


		//Récupère le nom de l'objet choisit 
		Bundle extras = getIntent().getExtras();
		String nomObjet = extras.getString("nomObjet");

		System.out.println(nomObjet);
		//System.out.println(list.get(0).getTitre());

		Histoire histoire = new Histoire(null,null,null, null, null);
		// Parcours la liste des histoires pour récupèrer dans "histoire" celle choisie
		// A voir pour refaire avec un while niveau opti
		int i=0;
		while(!list.get(i).getTitre().equals(nomObjet)){
			i++;
		}
		histoire = list.get(i);
		System.out.println(histoire.getTitre());
		System.out.println(histoire.getPhrases().get(0));
		// Récupère le layout
		FrameLayout fl=(FrameLayout) this.findViewById(R.layout.histoire);
		// Récupère l'id de l'image utilisee comme background pour cette histoire
		int resID = getResources().getIdentifier("histoire_"+nomObjet, "drawable", getPackageName());
		// Redéfinit le background du layout
		fl.setBackgroundResource(resID);

		// Récupère le TextView utilisé pour afficher l'histoire
		TextView text1 = (TextView) this.findViewById(R.id.textView1);
		System.out.println("phrase : "+histoire.getPhrases().get(0));
		//Affiche le début de l'histoire jusqu'au premier verbe, et tente d'afficher le groupe et le temps du premier verbe
		String texte = (String) histoire.getPhrases().get(0) + histoire.getGroupes().get(0) + histoire.getTemps().get(0);
		text1.setText(texte);
		
		// vérifie ce que contient la liste de groupes de verbes, actuellement semble faux
		System.out.println("groupe : " + histoire.getGroupes().size() + histoire.getGroupes().get(0));
		
		for(int d=0; d<histoire.getGroupes().size();d++)
			System.out.println(histoire.getGroupes().get(d));


	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/**
		 * Non demandé, retourne false
		 */
		getMenuInflater().inflate(R.menu.main, menu);
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




