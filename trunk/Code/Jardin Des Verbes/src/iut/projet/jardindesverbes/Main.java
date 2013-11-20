package iut.projet.jardindesverbes;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class Main extends Activity {

	List<Profil> lesProfils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		lesProfils = new ArrayList<Profil>();
		Toast.makeText( (Activity)Main.this, "Test", Toast.LENGTH_SHORT).show();
		RemplirProfils();
		
		ProfilAdapter adapter = new ProfilAdapter(this, lesProfils);
		ListView ViewList = (ListView)findViewById(R.id.Profil_ListView);
		ViewList.setAdapter(adapter);
		
		ViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
		    	 Toast.makeText( (Activity)Main.this, "Profil chargé", Toast.LENGTH_SHORT).show();
				
			}
		});
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void RemplirProfils() {

		lesProfils.clear();

		lesProfils.add(new Profil("Bob",1));

		lesProfils.add(new Profil("Jack",5));
		
		lesProfils.add(new Profil("John",566));
		
		lesProfils.add(new Profil("Joe",5));
		
		lesProfils.add(new Profil("JAcky",5));
		
		lesProfils.add(new Profil("Droopy",5));
		
		lesProfils.add(new Profil("Roddy",5));
		
		lesProfils.add(new Profil("EH BWANA",5));

	}

}
