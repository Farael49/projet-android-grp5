package iut.projet.activities;

import iut.projet.jardindesverbes.MusicService;
import iut.projet.jardindesverbes.Police;
import iut.projet.jardindesverbes.Profil;
import iut.projet.jardindesverbes.ProfilManager;
import iut.projet.jardindesverbes.R;
import iut.projet.jardindesverbes.Utils;
import iut.projet.jardindesverbes.Verbe;
import iut.projet.jardindesverbes.VerbeManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main_Profil_Activity extends Activity {

	private EditText profilTextField;
	private ProfilManager profils;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		Intent music = new Intent();
		music.setClass(this,MusicService.class);
		startService(music);

		// Police d'écriture
		Police police = new Police(getApplicationContext());
		police.setupLayoutTypefaces(getWindow().getDecorView());

		// Récupération des éléments
		Button loadProfilButton = (Button) findViewById(R.id.Profil_ChargerProfilButton);
		Button addProfilButton = (Button) findViewById(R.id.Profil_CreerProfilButton);
		//	Button debugProfilButton = (Button) findViewById(R.id.Profil_viderListe);
		//	Button debugDialogButton = (Button) findViewById(R.id.Profil_afficherDialog);

		profilTextField = (EditText) findViewById(R.id.Profil_ChargerProfilTextField);

		// Chargement des profils
		profils = ProfilManager.getInstance();
		profils.loadProfils(this);

		// Pression du bouton de création de profil.
		addProfilButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				createProfilAction();
			}
		});

		// Pression du bouton de chargement de profil.
		loadProfilButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				loadProfilAction();
			}
		});

		/*		debugProfilButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
					debugRemoveListAction();
			}
		});

		debugDialogButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				debugAfficherDialogAction();
			}
		});

		 */
		// Pression touche 'entrée' sur le clavier virtuel.
		// On considère que c'est le raccourcis pour charger le profil.
		profilTextField.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_ENTER)) {
					loadProfilAction();
					return true;
				}
				return false;
			}
		});
	}

	private void createProfilAction() {
		String str = profilTextField.getText().toString();

		if (str.length()!=0){
			if(profils.addProfil(this, new Profil(str))){
				Utils.showToastText(this, "Profil "+str+" crée !");
			} else{
				Utils.showToastText(this, "Ce nom est déjà utilisé!");
			}

		} else {
			Utils.showToastText(this, "Mes ton nom !");
		}
	}

	/**
	 * Actions effecutées lors du chargement d'un profil.
	 * Le nom d'utilisateur est envoyé à l'activité du Jardin des Verbes (interface 2nd niveau).
	 * L'envois d'objets complexes (objets comportants des structures de données) étant difficilement réalisable sous android, 
	 * nous avons choisis de n'envoyer que l'username. Un simple getProfil sur la classe ProfilManager permet de le récuperer.
	 */
	private void loadProfilAction() {
		String str = profilTextField.getText().toString();
		Profil profil = profils.getProfil(str);
		if(profil==null){
			Utils.showToastText(this, "Le profil "+str+" n'éxiste pas.");
		}else{
			//SyntheseVocale.voc(this, "gros test");
			Log.i("JDV","charger le profil !");
			Intent i = new Intent(this, Jardin_Activity.class);
			i.putExtra("username", profil.getUsername());
			startActivity(i);
		}
		profils.afficherListe();
	}

	private void debugRemoveListAction(){
		Utils.showToastText(this, "XML de profils vidé.");
		profils.viderListe(this);
	}

	private void debugAfficherDialogAction(){
		final Dialog dialog = new Dialog(this);

		VerbeManager verManager = VerbeManager.getInstance();
		verManager.loadVerbes(this);
		Verbe verbe = verManager.getVerbe("Balancer", "Present");		
		dialog.setContentView(R.layout.aide_1);
		dialog.setTitle("Un petit coup de main ?");

		// set the custom dialog components - text, image and button
		TextView aide_Titre = (TextView) dialog.findViewById(R.id.aide_titre);
		TextView aide_Conjugaison = (TextView) dialog.findViewById(R.id.aide_conjugaison);

		aide_Titre.setText("Conjugaison du verbe '"+verbe.getInfinitif()+"' au temps :"+verbe.getTemps());

		aide_Conjugaison.setText(verbe.getConjugaison());

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


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
