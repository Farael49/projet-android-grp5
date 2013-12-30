package iut.projet.jardindesverbes;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class SyntheseVocale implements TextToSpeech.OnInitListener {

	private static SyntheseVocale instance;

	private TextToSpeech synth;

	public SyntheseVocale(Context context) {
		synth = new TextToSpeech(context, this);
	}

	@Override
	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS)
			synth.setLanguage(Locale.FRANCE);
		else
			Log.e("JDV", "Synthèse vocale crashée.");
	}

	public static void voc(Context context, String str) {
		instance = new SyntheseVocale(context);
		instance.synth.speak(str, TextToSpeech.QUEUE_FLUSH, null);
		instance.synth.stop();
		instance.synth.shutdown();
	}

}
