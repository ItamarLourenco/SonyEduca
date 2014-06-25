package br.sony.sonyeduca.fragments;

import br.sony.sonyeduca.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

@SuppressLint({ "NewApi", "ValidFragment" })
public class ContatoFragment extends Fragment {
	public Activity act;
	public ContatoFragment(Activity act) {}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
        View rootView = inflater.inflate(R.layout.fragment_contato, container, false);
        
        ImageView email = (ImageView) rootView.findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_EMAIL, "falecom@sonyeduca.com.br" );
				startActivity(Intent.createChooser(intent, "Enviar e-mail:"));
			}
		});
        
        return rootView;
    }

}
