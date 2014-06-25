package br.sony.sonyeduca;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import br.sony.sonyeduca.classes.MySession;
import br.sony.sonyeduca.fragments.VideoFragments;

public class Video extends Activity{
	public MySession my_session;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
			
		my_session = new MySession(Video.this);
		findViewById(R.id.base_menu).setVisibility(View.GONE);
		
		
		Drawable image = getResources().getDrawable(R.drawable.sony_educa_bar);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(image);
		getActionBar().setTitle("   ");
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		Intent intent = getIntent();
		br.sony.sonyeduca.classes.Video video = (br.sony.sonyeduca.classes.Video) intent.getSerializableExtra("Video");
		
        if (savedInstanceState == null) {
        	FragmentManager fragmentManager = getFragmentManager();
        	Fragment fragment = new VideoFragments(this, video);
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
		}
        
        ImageView ok = (ImageView) findViewById(R.id.ok);
		final EditText busca_text = (EditText) findViewById(R.id.busca_text);
		busca_text.setText(my_session.getBusca());
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String busca = busca_text.getText().toString();
				my_session.setBusca(busca);
				finish();
			}
		});
		
	}
		
	@Override
	public void onBackPressed(){
		finish();
		overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	onBackPressed();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
}
