package iut.projet.jardindesverbes;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * Cette classe gère la police d'affichage des éléments d'une classe.
 * La police est définie en utilisant l'attribut android:tag avec une valeur de l'enum.
 *
 */
public class Police {
    private AssetManager manager;

    public Police(Context context) {
    	manager = context.getAssets();
    }
    private enum AssetTypefaces {
        Suplexmentary
    }

    private Typeface getTypeface(AssetTypefaces font) {
        Typeface tf = null;
        switch (font) {
            case Suplexmentary:
                tf = Typeface.createFromAsset(manager,"fonts/Suplexmentary.ttf");
                break;
            default:
                tf = Typeface.DEFAULT;
                break;
        }
        return tf;
    }
    public void setupLayoutTypefaces(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    setupLayoutTypefaces(child);
                }
            } else if (v instanceof Button) {
                if (v.getTag().toString().equals(AssetTypefaces.Suplexmentary.toString())){
                    ((Button)v).setTypeface(getTypeface(AssetTypefaces.Suplexmentary));
                }//else if (v.getTag().toString().equals(AssetTypefaces.RobotoCondensedRegular.toString())) {
                 //   ((TextView)v).setTypeface(getTypeface(AssetTypefaces.RobotoCondensedRegular));
                //}
            } else if (v instanceof TextView) {
                if (v.getTag().toString().equals(AssetTypefaces.Suplexmentary.toString())){
                    ((TextView)v).setTypeface(getTypeface(AssetTypefaces.Suplexmentary));
                }//else if (v.getTag().toString().equals(AssetTypefaces.RobotoCondensedRegular.toString())) {
                 //   ((TextView)v).setTypeface(getTypeface(AssetTypefaces.RobotoCondensedRegular));
                //}
            } 
        } catch (Exception e) {}
    }
}
