package iut.projet.jardindesverbes;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class Utils {

	public static void showToastText(Context context, String text) {
		Toast.makeText((Activity) context, text, Toast.LENGTH_SHORT).show();
	}
	
}
