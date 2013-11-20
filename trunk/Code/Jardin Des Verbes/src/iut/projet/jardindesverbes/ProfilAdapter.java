package iut.projet.jardindesverbes;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ProfilAdapter extends BaseAdapter{
	
	List<Profil> lesProfils;
	
	LayoutInflater inflater;

	public ProfilAdapter(Context context, List<Profil> mesProfils){
		inflater = LayoutInflater.from(context);
		this.lesProfils = mesProfils;
	}
	
	@Override
	public int getCount() {
		return lesProfils.size();
	}

	@Override
	public Object getItem(int arg0) {
		return lesProfils.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	private class ViewHolder{
		TextView Profil_List_Username;
		TextView Profil_List_informations;
		ImageButton Profil_List_Delete_Button;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.profil_ligne_adapter2, null);

			holder.Profil_List_Username = (TextView)convertView.findViewById(R.id.Profil_List_Username);
			holder.Profil_List_informations = (TextView)convertView.findViewById(R.id.Profil_List_informations);
			holder.Profil_List_Delete_Button = (ImageButton)convertView.findViewById(R.id.Profil_List_Delete_Button);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		holder.Profil_List_informations.setText("Niveau: "+lesProfils.get(position).getNiveau());
		holder.Profil_List_Username.setText("Nom: "+lesProfils.get(position).getUsername());
		holder.Profil_List_Delete_Button.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Profil bob = (Profil) ProfilAdapter.this.getItem(position);
				Toast.makeText( (Activity) ProfilAdapter.this.inflater.getContext(), "Profil supprimé "+ bob.getUsername(), Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;

	}

}
