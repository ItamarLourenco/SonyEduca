package br.sony.sonyeduca.fragments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.sony.sonyeduca.R;
import br.sony.sonyeduca.classes.ImageDownloader;
import br.sony.sonyeduca.classes.MySession;
import br.sony.sonyeduca.classes.ServiceHandler;
import br.sony.sonyeduca.classes.Video;
import br.sony.sonyeduca.classes.Webservice;
import br.sony.sonyeduca.trivialdrivesample.util.IabHelper;

@SuppressLint({ "NewApi", "ValidFragment" })
public class VideoFragments extends Fragment {	
	static final String TAG = "Sony";	
	public Activity act;
	public Video video;
	public MySession my_session;
	public String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgjJOfxNq5IpKcj/HJ1SfM9NplKHN+9V3sJcjUhqcX42ntES4LqucBXo4H4HmoVR1QA2eS/S18UBntfD/A0fM7mIZd0v7btIJNEFRE3Zgt14do+LiOlF014IfAzWVCPZvOdnYLBqR2zG+JjNFF8xK52eWFpgmq6W32TqnMlfRdMTQJt7DKCmi6kUUwhi1YBppnHnnGAarKfxTs96ZvyI851TFy5uB/4ppk5Nu3k8cxpjEGY8sAQda3Ne80By2amspfyMtVQfYPBh3RbqEer3fFZ2RWxnFrrQRl2eeazCnMeFOT2bltsnBW3zl0SGXwg1ZNxFRcXYKBpdTRbJrkD3eUwIDAQAB";
	public IabHelper mHelper;
	static final String SKU = "android.test.canceled";
	public final int RC_REQUEST = 1;
	public boolean g_pay = false;
	public ProgressDialog progress_dialog = null;
	public LinearLayout pay, pay_assistir, pay_loader;
	
	public VideoFragments(Activity act, Video video){
		this.act = act;
		this.video = video;
		my_session = new MySession(act);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
        final View rootView = inflater.inflate(R.layout.fragment_video, container, false);        
        DecimalFormat format_real = new DecimalFormat("R$ ###,###,##0.00", new DecimalFormatSymbols(new Locale("pt","BR")));
        
        pay_assistir = (LinearLayout) rootView.findViewById(R.id.pay_assistir);
		pay_loader = (LinearLayout) rootView.findViewById(R.id.pay_loader);
        pay = (LinearLayout) rootView.findViewById(R.id.pay);
        
        TextView aula_nome = (TextView) rootView.findViewById(R.id.aula_nome);
        aula_nome.setText(video.getNomeAula());
        
        TextView aula_posicao = (TextView) rootView.findViewById(R.id.aula_posicao);
        aula_posicao.setText(video.getOrdem());
        
        TextView valor = (TextView) rootView.findViewById(R.id.valor);
        valor.setText(format_real.format(video.getValor()));
        
        ImageView image = (ImageView) rootView.findViewById(R.id.image);
        ImageDownloader imageDownloader = new ImageDownloader();
		imageDownloader.download(video.getArquivoImg(), image);
        
        TextView descricao = (TextView) rootView.findViewById(R.id.descricao);
        descricao.setText(video.getDescricao());
        
        pay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(my_session.check()){
					g_pay = true;
					comprarVideo();
				}else{
					AlertDialog.Builder alert = new AlertDialog.Builder(act);
			        alert.setTitle("Acesso negado!");
			        alert.setMessage("Para fazer a compra desse vídeo você precisa ser cadastrado. \nCaso já seja cadastrado por favor realize o Login.");
			        alert.setPositiveButton("Cadastro", new DialogInterface.OnClickListener() {
			            public void onClick (DialogInterface dialog, int id) {			           
			            	my_session.setEscolha("cadastro");
			            	act.finish();
							act.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			            }
			        });
			        alert.setNegativeButton("Login", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			            	my_session.setEscolha("login");
			            	act.finish();
							act.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			            }
			        });
			        alert.show();
				}
			}
		});
        
        
        mHelper = new IabHelper(act, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(br.sony.sonyeduca.trivialdrivesample.util.IabResult result) {
                if (mHelper == null) return;
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
        

        
        //Verificar se o vídeo já foi comprado pelo usuário
        new AsyncTask<Void, Void, Void>(){
			@Override
			protected Void doInBackground(Void... params) {
				try {
					JSONObject json = new JSONObject();
					json.put("videos_id", video.getId());
					json.put("cadastros_id", my_session.getId());
					
					List<NameValuePair> sendParams = new LinkedList<NameValuePair>();
					sendParams.add(new BasicNameValuePair("json", String.valueOf(json)));
					Webservice ws = new Webservice();
					
					final String json_retorno = ws.makeServiceCall(ws.getUrlCheck(), ServiceHandler.POST, sendParams);
					
					final boolean check = new JSONObject(json_retorno).getBoolean("status");
					
					act.runOnUiThread(new Runnable() {
						public void run() {
							pay_loader.setVisibility(View.GONE);
							if(check){
								pay_assistir.setVisibility(View.VISIBLE);
								pay_assistir.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent();
								        intent.setAction(Intent.ACTION_VIEW);
								        intent.setDataAndType(Uri.parse(video.getArquivo()), "video/mp4");
								        act.startActivity(intent);
								        act.overridePendingTransition(R.anim.top_down_in, R.anim.top_down_out);
									}
								});
							}else{
								pay.setVisibility(View.VISIBLE);
							}
						}			
					});		
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}
        	
        }.execute();
        
        
        return rootView;
    }

	protected void comprarVideo() {
		String payload = "";
        mHelper.launchPurchaseFlow(act, SKU, RC_REQUEST, mPurchaseFinishedListener, payload);
	}
	
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(br.sony.sonyeduca.trivialdrivesample.util.IabResult result, br.sony.sonyeduca.trivialdrivesample.util.Inventory inventory) {
        }
    };
    
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(br.sony.sonyeduca.trivialdrivesample.util.IabResult result, br.sony.sonyeduca.trivialdrivesample.util.Purchase purchase) {            
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(br.sony.sonyeduca.trivialdrivesample.util.Purchase purchase, br.sony.sonyeduca.trivialdrivesample.util.IabResult result) {            
        }
    };
    
    public void onResume(){
    	super.onResume();
    	if(g_pay == true){
    		g_pay = false;
    		realizarCompras();
    	}
    }

	private void realizarCompras() {
		try {
			final JSONObject json = new JSONObject();
			json.put("videos_id", video.getId());
			json.put("cadastros_id", my_session.getId());
			json.put("valor_compra", video.getValor());
			
			new AsyncTask<Void, Void, Void>(){
		    	@Override
				protected void onPreExecute() {
					super.onPreExecute();
					progress_dialog = new ProgressDialog(act);
					progress_dialog.setTitle("Compras");
					progress_dialog.setMessage("Aguarde...");
					progress_dialog.setCancelable(true);
					progress_dialog.show();
				}

				@Override
				protected Void doInBackground(Void... params) {
					Webservice ws = new Webservice();						
					List<NameValuePair> sendParams = new LinkedList<NameValuePair>();
					sendParams.add(new BasicNameValuePair("json", json.toString()));
					
					Log.i(TAG, json.toString());
					final String json_retorno = ws.makeServiceCall(ws.getUrlCompras(), ServiceHandler.POST, sendParams);
					
					Log.i(TAG, json_retorno);
					try {
						final JSONObject json_obj = new JSONObject(json_retorno);
						final String msg = json_obj.getString("msg");
						
						if(json_obj.getString("status").toString().equalsIgnoreCase("0")){
							act.runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
									
								}
							});
							progress_dialog.dismiss();
						}else{
							act.runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
									my_session.setEscolha("login");
					            	act.finish();
									act.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
								}
							});
							progress_dialog.dismiss();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					progress_dialog.dismiss();
					return null;
				}
			}.execute();
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
