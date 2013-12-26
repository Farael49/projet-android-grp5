package com.example.android_project1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {
	ImageButton ballon, train;
	List<ObjetHistoire> nomsObjets;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jardin_choix_objet);
		setObjectBackground();
	}

	private void setObjectBackground() {
		/*
		 * Permet d'assigner à chaque objet son image et son état
		 */
		nomsObjets = new ArrayList<ObjetHistoire>();
		ImageButton img;
		//les données de nomObjets doivent normalement provenir du profil XML
		int i=2;
		nomsObjets.add(new ObjetHistoire("pomme"));
		nomsObjets.add(new ObjetHistoire("train",i));
		nomsObjets.add(new ObjetHistoire("fleurs",i));
		nomsObjets.add(new ObjetHistoire("toupie",i));
		nomsObjets.add(new ObjetHistoire("ballon",i));
		nomsObjets.add(new ObjetHistoire("helico",i));
		nomsObjets.add(new ObjetHistoire("nuage",i));
		nomsObjets.add(new ObjetHistoire("arbre",i));
		for(ObjetHistoire obj : nomsObjets){
			int resImage = getResources().getIdentifier(obj.getObjetImageFilename(), "drawable", getPackageName());
			int resID = getResources().getIdentifier(obj.getObjectName(), "id", getPackageName());
			//int resID = R.id.pomme;
			//int resImage = R.drawable.objet_pomme;
			img = (ImageButton) findViewById(resID);
			img.setBackgroundResource(resImage);
		}

	}

	public void startStory(View v){
		String nomObjet=null;
		Toast toast = new Toast(getApplicationContext());
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
		int index_objet_choisit=0;
		// Parcourt le contenu de nomsObjets pour trouver l'objet choisit par l'utilisateur
		while(!nomsObjets.get(index_objet_choisit).getObjectName().equals(nomObjet)){
			index_objet_choisit++;
		}
		if(nomsObjets.get(index_objet_choisit).getEtat()==0){
			toast.cancel();
			toast = Toast.makeText(getApplicationContext(), "Histoire "+nomObjet+" non débloquée", Toast.LENGTH_SHORT);
			toast.show();
		}
		else{
			Intent intent = new Intent(MainActivity.this,
					ActivityHistoire.class);
			intent.putExtra("nomObjet", nomObjet);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			toast.cancel();
			toast = Toast.makeText(getApplicationContext(), "Chargement de l'histoire \""+nomObjet+"\"", Toast.LENGTH_SHORT);
			toast.show();
		}

	}



}