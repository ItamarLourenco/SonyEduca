package br.sony.sonyeduca.fragments;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import br.sony.sonyeduca.R;
import br.sony.sonyeduca.classes.MySession;
import br.sony.sonyeduca.classes.ServiceHandler;
import br.sony.sonyeduca.classes.Webservice;

@SuppressLint({ "NewApi", "ValidFragment" })
public class LoginFragment extends Fragment {
	public Activity act;
	public ProgressDialog progress_dialog;
	public MySession my_session;
	
	public LoginFragment(Activity act){
		this.act = act;
	}
	

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		my_session = new MySession(act);
		
		if(my_session.check()){
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
			fragmentTransaction.add(R.id.frame_container, new MeusVideosFragment(act));
			fragmentTransaction.commit();
			
			return null;
		}
		
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        
        final EditText email = (EditText) rootView.findViewById(R.id.email);
        final EditText password = (EditText) rootView.findViewById(R.id.password);
        ImageView login = (ImageView) rootView.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(email.getText().toString().equals("")){
					Toast.makeText(act, "Por favor entre com o e-mail!", Toast.LENGTH_LONG).show();
					return;
				}
				
				if(password.getText().toString().equals("")){
					Toast.makeText(act, "Por favot entre com a senha!", Toast.LENGTH_LONG).show();
					return;
				}
				
				
				new AsyncTask<Void, Void, Void>()
			    {
			    	@Override
					protected void onPreExecute() {
						super.onPreExecute();
						progress_dialog = new ProgressDialog(act);
						progress_dialog.setTitle("Login");
						progress_dialog.setMessage("Aguarde...");
						progress_dialog.setCancelable(true);
						progress_dialog.show();
					}

					@Override
					protected Void doInBackground(Void... params) {
						List<NameValuePair> sendParams = new LinkedList<NameValuePair>();
						sendParams.add(new BasicNameValuePair("login", email.getText().toString()));
						sendParams.add(new BasicNameValuePair("senha", password.getText().toString()));
						
						Webservice ws = new Webservice();
						
						final String json = ws.makeServiceCall(ws.getUrlLogin(), ServiceHandler.POST, sendParams);
						if(json == null){							 
							Toast.makeText(act, "Sem Conex‹o.", Toast.LENGTH_LONG).show();
						}else{
							try {
								final JSONObject json_obj = new JSONObject(json);
								final String json_obj_msg = json_obj.getString("msg");
								if(json_obj.getString("status").toString().equalsIgnoreCase("0")){
									act.runOnUiThread(new Runnable() {
										public void run() {
											Toast.makeText(act, json_obj_msg, Toast.LENGTH_SHORT).show();
										}			
									});						
								}else{
									final JSONObject object = new JSONObject(json_obj.getString("object").toString());
									MySession my_session = new MySession(act);
									my_session.setId(object.getString("id"));
									my_session.setNome(object.getString("nome"));
									my_session.setLogin(email.getText().toString());
									my_session.setPassword(password.getText().toString());
									act.runOnUiThread(new Runnable() {
										public void run() {
											Toast.makeText(act, json_obj_msg, Toast.LENGTH_SHORT).show();
											FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    										fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
    										fragmentTransaction.add(R.id.frame_container, new MeusVideosFragment(act));
    										fragmentTransaction.commit();
										}			
									});
								}								
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
						}
						
						progress_dialog.dismiss();
						return null;
					}			    	
			    }.execute();
			}
		});
        
        
        return rootView;
    }
}
