package iut.projet.activities;

import iut.projet.jardindesverbes.Profil;
import iut.projet.jardindesverbes.ProfilManager;
import iut.projet.jardindesverbes.R;
import iut.projet.jardindesverbes.Utils;
import iut.projet.jardindesverbes.XMLProfilLoader;
import iut.projet.jardindesverbes.XMLProfilWriter;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main_Profil extends Activity {

	private EditText profilTextField;
	private ProfilManager profils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		// Récupération des éléments
		Button loadProfilButton = (Button) findViewById(R.id.Profil_ChargerProfilButton);
		Button addProfilButton = (Button) findViewById(R.id.Profil_CreerProfilButton);
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
	
	private void loadProfilAction() {
		String str = profilTextField.getText().toString();
		Profil profil = profils.getProfil(str);
		if(profil==null){
			Utils.showToastText(this, "Le profil "+str+" n'éxiste pas.");
		}else{
			System.out.println("charger le profil !");
		}
		//profils.viderListe(this);
		profils.afficherListe();
	}

	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
