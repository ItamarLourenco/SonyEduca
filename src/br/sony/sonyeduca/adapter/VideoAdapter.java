package br.sony.sonyeduca.adapter;

import java.util.ArrayList;

import br.sony.sonyeduca.classes.ImageDownloader;
import br.sony.sonyeduca.classes.Video;
import br.sony.sonyeduca.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
 
public class VideoAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Video> video = new ArrayList<Video>();
 
	public VideoAdapter(Context context, ArrayList<Video> video) {
		this.context = context;
		this.video = video;

	}
 
	
	View gridView;
	Video obj_video;
	
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		
		obj_video = video.get(position);
		if(convertView == null)
		{			
			gridView = new View(context);
			gridView = inflater.inflate(R.layout.gridview_videos_layout, null);
	
			TextView view_aula_nome = (TextView) gridView.findViewById(R.id.aula_nome);
			view_aula_nome.setText(obj_video.getNomeAula());
	
			TextView view_aula_posicao = (TextView) gridView.findViewById(R.id.aula_posicao);
			view_aula_posicao.setText(obj_video.getOrdem());	
			
			TextView view_id = (TextView) gridView.findViewById(R.id.id_video);
			view_id.setText(String.valueOf(obj_video.getId()));
			
			TextView view_valor = (TextView) gridView.findViewById(R.id.valor);
			view_valor.setText(String.valueOf(obj_video.getValor()));
			
			TextView view_descricao = (TextView) gridView.findViewById(R.id.descricao);
			view_descricao.setText(obj_video.getDescricao());
			
			TextView view_arquivo = (TextView) gridView.findViewById(R.id.arquivo);
			view_arquivo.setText(obj_video.getArquivo());
			
			ImageView img_thumb = (ImageView) gridView.findViewById(R.id.img_thumb);
			ImageDownloader imageDownloader = new ImageDownloader();
			imageDownloader.download(obj_video.getArquivoImg(), img_thumb);
		}else{
			gridView = convertView;
		}
		
 
		return gridView;
	}
 
	public int getCount() {
		return video.size();
	}
 
	public Object getItem(int position) {
		return position;
	}
 
	public long getItemId(int position) {
		return position;
	}

 
}