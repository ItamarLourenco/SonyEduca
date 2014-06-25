package br.sony.sonyeduca.adapter;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.sony.sonyeduca.R;
import br.sony.sonyeduca.classes.SlideDrawer;

public class SliderAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<SlideDrawer> slides = null;
	
	public SliderAdapter(Context context, ArrayList<SlideDrawer> slides){
		this.context = context;
		this.slides = slides;
	}
	
	@Override
	public int getCount(){
		return slides.size();
	}

	@Override
	public Object getItem(int position) {
		return slides.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.slide_list_layout, null);
		}
		
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);       
        txtTitle.setText(slides.get(position).getTitle());
		
		return convertView;
	}
	
	

}
