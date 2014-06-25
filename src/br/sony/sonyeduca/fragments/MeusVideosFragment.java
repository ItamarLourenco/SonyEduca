package br.sony.sonyeduca.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import br.sony.sonyeduca.R;
import br.sony.sonyeduca.adapter.VideoAdapter;
import br.sony.sonyeduca.classes.MySession;
import br.sony.sonyeduca.classes.ServiceHandler;
import br.sony.sonyeduca.classes.Video;
import br.sony.sonyeduca.classes.Webservice;

@SuppressLint({ "NewApi", "ValidFragment" })
public class MeusVideosFragment extends Fragment{
	public Activity act;
	public ProgressDialog progress_dialog = null;
	public MySession my_session;
	public TextView text_sem_meus_videos = null;
	public GridView gv;
	public ArrayList<Video> video_list = new ArrayList<Video>();
	
	public MeusVideosFragment(Activity act){
		this.act = act;
		my_session = new MySession(act);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
        final View rootView = inflater.inflate(R.layout.fragments_meusvideos, container, false);
        
        gv = (GridView) rootView.findViewById(R.id.gridView);
        gv.setOnItemClickListener(new GridView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {			
				Video video = video_list.get(position);
			
				Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.setDataAndType(Uri.parse(video.getArquivo()), "video/mp4");
		        act.startActivity(intent);
		        act.overridePendingTransition(R.anim.top_down_in, R.anim.top_down_out);
			}
        });
        
        text_sem_meus_videos = (TextView) rootView.findViewById(R.id.text_sem_meus_videos);
        text_sem_meus_videos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
				fragmentTransaction.add(R.id.frame_container, new HomeFragment(act));
				fragmentTransaction.commit();
			}
		});
        
        
        
        new AsyncTask<Void, Void, Void>(){
        	@Override
        	protected void onPreExecute(){
        		super.onPreExecute();
				progress_dialog = new ProgressDialog(act);
				progress_dialog.setTitle("Meus V’deos");
				progress_dialog.setMessage("Aguarde...");
				progress_dialog.setCancelable(true);
				progress_dialog.show();
        	}
			@Override
			protected Void doInBackground(Void... params) {
				//Pegandos os videos
				Webservice ws = new Webservice();
				Log.d("sony", ws.getUrlMeusVideos(my_session.getId()));
				final String json = ws.makeServiceCall(ws.getUrlMeusVideos(my_session.getId()), ServiceHandler.GET);
				
				if(json != null){
					try {
						JSONArray json_array = new JSONArray(json);
						if(json_array.length() == 0){
							act.runOnUiThread(new Runnable() {
								public void run() {
									text_sem_meus_videos.setVisibility(View.VISIBLE);
									gv.setVisibility(View.GONE);
									progress_dialog.dismiss();
								}
							});
						}else{
							act.runOnUiThread(new Runnable() {
								public void run() {
									text_sem_meus_videos.setVisibility(View.GONE);
									gv.setVisibility(View.VISIBLE);
									progress_dialog.dismiss();
								}
							});
							
							for(int i=0; i<json_array.length(); i++)
							{
								JSONObject videos = json_array.getJSONObject(i).getJSONObject("Videos");
								
								Video add_video = new Video();
									add_video.setId(Integer.parseInt(videos.getString("id")));
									add_video.setNomeAula(videos.getString("nome"));; 
									add_video.setOrdem(videos.getString("ordem"));
									add_video.setValor(Float.parseFloat(videos.getString("valor")));
									add_video.setDescricao(videos.getString("descricao"));; 
									add_video.setArquivo(videos.getString("arquivo"));
									add_video.setArquivoImg(videos.getString("arquivo_img"));;								
								video_list.add(add_video);
							}
							
							act.runOnUiThread(new Runnable() {
								public void run() {
									gv.setAdapter(null);
									if(video_list.size() > 0){
										gv.setAdapter(new VideoAdapter(act, video_list));
									}else{
										video_list = null;
										gv.setVisibility(View.GONE);
										text_sem_meus_videos.setVisibility(View.VISIBLE);
									}
									progress_dialog.dismiss();
								}
							});
							
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					act.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(act, "Sem Conex‹o!", Toast.LENGTH_LONG).show();
						}
					});
					progress_dialog.dismiss();
				}
				return null;
			}
        	
        }.execute();
        
        
        
        return rootView;
    }
	
}
