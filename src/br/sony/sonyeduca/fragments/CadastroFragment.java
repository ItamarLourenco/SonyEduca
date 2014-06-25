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
import android.util.Log;
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
public class CadastroFragment extends Fragment {
	public Activity act;
	public ProgressDialog progress_dialog;
	public MySession my_session;
	public CadastroFragment(Activity act){
		this.act = act;
		my_session = new MySession(act);
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
		
		
        View rootView = inflater.inflate(R.layout.fragment_cadastro, container, false);
        
        final EditText nome = (EditText) rootView.findViewById(R.id.nome);
        final EditText email = (EditText) rootView.findViewById(R.id.email);
        final EditText password = (EditText) rootView.findViewById(R.id.password);        
        final ImageView register = (ImageView) rootView.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
        		if(nome.getText().toString().equals("")){
        			Toast.makeText(act, "Por favor entre com o nome!", Toast.LENGTH_LONG).show();
        			return;
        		}
        		
        		if(email.getText().toString().equals("")){
        			Toast.makeText(act, "Por favor entre com seu e-mail!", Toast.LENGTH_LONG).show();
        			return;
        		}
        		
        		if(password.getText().toString().equals("")){
        			Toast.makeText(act, "Por favor entre com uma senha!", Toast.LENGTH_LONG).show();
        			return;
        		}
        		
        		new AsyncTask<String, String, String>()
        	    {
        	    	protected void onPreExecute(){
        	    		super.onPreExecute();
        	    		progress_dialog = new ProgressDialog(act);
        	    		progress_dialog.setTitle("Cadastro");
        	    		progress_dialog.setMessage("Aguarde...");
        	    		progress_dialog.setCancelable(true);
        	    		progress_dialog.show();
        	    	}

        			@SuppressLint("NewApi")
					@Override
        			protected String doInBackground(String... params) {
        				JSONObject cadastro = new JSONObject();
        				try {
        					cadastro.put("nome", nome.getText().toString());
        					cadastro.put("email", email.getText().toString());
        					cadastro.put("senha", password.getText().toString());
        					String json = cadastro.toString();
        					
        					Webservice ws = new Webservice();
        					
        					List<NameValuePair> sendParams = new LinkedList<NameValuePair>();
        					sendParams.add(new BasicNameValuePair("json", String.valueOf(json)));				
        					final String json_retorno = ws.makeServiceCall(ws.getUrlCadastro(), ServiceHandler.POST, sendParams);
        					
        					if(json_retorno != null){
        						act.runOnUiThread(new Runnable() {
        							@Override
        							public void run() {
        								try {
        									JSONObject json_retorno_ws = new JSONObject(json_retorno);
        									
        									if(json_retorno_ws.get("status").toString().equalsIgnoreCase("1")){
        										Toast.makeText(act, json_retorno_ws.getString("msg"), Toast.LENGTH_LONG).show();

        										final JSONObject object = new JSONObject(json_retorno_ws.getString("object").toString());
        										
        										MySession my_session = new MySession(act);
        										my_session.setId(object.getString("id"));
        										my_session.setNome(object.getString("nome"));
        										my_session.setLogin(email.getText().toString());
        										my_session.setPassword(password.getText().toString());
        										
        										FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        										fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        										fragmentTransaction.add(R.id.frame_container, new MeusVideosFragment(act));
        										fragmentTransaction.commit();
        									}else{
        										Toast.makeText(act, json_retorno_ws.getString("msg"), Toast.LENGTH_LONG).show();
        									}
        								} catch (JSONException e) {
        									Log.i("DEBUG", "Erro ao ler json - cadastro");
        								}
        								
        							}
        						});
        					}else{
        						Toast.makeText(act, "Sem conex‹o", Toast.LENGTH_LONG).show();
        					}
        				} catch (JSONException e) {
        					Log.i("DEBUG", "Erro ao ler json - cadastro");
        				}
        				progress_dialog.dismiss();
        				return null;
        			}        	    	
        	    }.execute();
        	 
			}
		});
        
        
        ImageView login = (ImageView) rootView.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
				fragmentTransaction.add(R.id.frame_container, new LoginFragment(act));
				fragmentTransaction.commit();
			}
		});
        
        
        
        
        return rootView;
    }
}
