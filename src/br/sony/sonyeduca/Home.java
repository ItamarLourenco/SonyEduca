package br.sony.sonyeduca;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import br.sony.sonyeduca.adapter.SliderAdapter;
import br.sony.sonyeduca.classes.MySession;
import br.sony.sonyeduca.classes.SlideDrawer;
import br.sony.sonyeduca.classes.Video;
import br.sony.sonyeduca.fragments.CadastroFragment;
import br.sony.sonyeduca.fragments.ContatoFragment;
import br.sony.sonyeduca.fragments.HomeFragment;
import br.sony.sonyeduca.fragments.LoginFragment;
import br.sony.sonyeduca.fragments.MeusVideosFragment;


@SuppressLint("NewApi")
public class Home extends ActionBarActivity {
	public ListView drawmer_list;
	public CharSequence title_menu = "";
	public String[] slide_menu_titles;
	public DrawerLayout drawmer_layout;
	public ActionBarDrawerToggle bar;
	public FragmentTransaction fragmentTransaction;
	final public static int HOME_FRAGMENT = 0;
	final public static int MEUS_VIDEOS_FRAGMENT = 1;
	final public static int SAIR_FRAGMENT = 2;
	final public static int CADASTRO_FRAGMENT = 3;
	final public static int CARRO_FRAGMENT = 4;
	final public static int CONTATO_FRAGMENT = 5;
	final public static int LOGIN_FRAGMENT = 6;
	public ImageView home = null;
	public ImageView cadastro_menu = null;
	public ImageView carro = null;
	public ImageView contato_menu = null;
	public MySession my_session;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		startActivityHome(savedInstanceState);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		final EditText busca_text = (EditText) findViewById(R.id.busca_text);
		busca_text.setText(my_session.getBusca());
		if(my_session.getEscolha().equals("")){
			displayView(HOME_FRAGMENT);
		}else{
			if(my_session.getEscolha().equals("cadastro")){
				displayView(CADASTRO_FRAGMENT);
			}else if(my_session.getEscolha().equals("login")){
				displayView(LOGIN_FRAGMENT);
				carro.setImageResource(R.drawable.car_hover);
			}
			my_session.setEscolha("");
		}
	}
	
	public void startActivityHome(Bundle savedInstanceState) {
		Drawable image = getResources().getDrawable(R.drawable.sony_educa_bar);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(image);
		getActionBar().setTitle("       ");
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

		my_session = new MySession(Home.this);
		
		slide_menu_titles = getResources().getStringArray(R.array.slide_menu);
		
		drawmer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawmer_list = (ListView) findViewById(R.id.list_slidermenu);
		title_menu = getTitle();
		
		ArrayList<SlideDrawer> slides = new ArrayList<SlideDrawer>();
		for(int i=0; i<slide_menu_titles.length; i++){
			slides.add(new SlideDrawer(slide_menu_titles[i]));
		}
		
		
		SliderAdapter slider_adapter = new SliderAdapter(Home.this, slides);
		drawmer_list.setAdapter(slider_adapter);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		drawmer_list.setOnItemClickListener(new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				displayView(position);
			}
		});
		bar = new ActionBarDrawerToggle(this, drawmer_layout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name){
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
			}
			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};
		
		drawmer_layout.setDrawerListener(bar);
		
		home = (ImageView) findViewById(R.id.home);
		cadastro_menu = (ImageView ) findViewById(R.id.cadastro_menu);
		carro = (ImageView) findViewById(R.id.carro);
		contato_menu = (ImageView) findViewById(R.id.contato_menu);
		Button.OnClickListener onClickMenu = new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				switch(v.getId())
				{
					case R.id.home:
						displayView(HOME_FRAGMENT);
					break;
					
					case R.id.cadastro_menu:
						displayView(CADASTRO_FRAGMENT);
					break;
					
					case R.id.carro:
						displayView(CARRO_FRAGMENT);
					break;
					
					case R.id.contato_menu:
						displayView(CONTATO_FRAGMENT);
					break;		
				}
			}
		};
		
		home.setOnClickListener(onClickMenu);
		cadastro_menu.setOnClickListener(onClickMenu);
		carro.setOnClickListener(onClickMenu);
		contato_menu.setOnClickListener(onClickMenu);
		home.setImageResource(R.drawable.home_hover);
		
		
		
		
		ImageView ok = (ImageView) findViewById(R.id.ok);
		final EditText busca_text = (EditText) findViewById(R.id.busca_text);
		busca_text.setText(my_session.getBusca());
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				String busca = busca_text.getText().toString();
				my_session.setBusca(busca);
				displayView(HOME_FRAGMENT, null);	
			}
		});
	}
	



	protected void tradeForNotHover() {
		home.setImageResource(R.drawable.home_);
		cadastro_menu.setImageResource(R.drawable.cadastro);
		carro.setImageResource(R.drawable.car);
		contato_menu.setImageResource(R.drawable.contato);
	}


	public void displayView(int position){
		displayView(position, null);
	}
	
	public void displayView(int position, Video video) {
		tradeForNotHover();
		android.app.Fragment fragment = null;
		switch (position) 
		{
			case HOME_FRAGMENT:
				fragment = new HomeFragment(this);
				home.setImageResource(R.drawable.home_hover);
			break;
			
			case MEUS_VIDEOS_FRAGMENT:
				fragment = new MeusVideosFragment(this);
			break;
			
			case CADASTRO_FRAGMENT:
				fragment = new CadastroFragment(this);
				cadastro_menu.setImageResource(R.drawable.cadastro_hover);
			break;
			
			case CARRO_FRAGMENT:
				fragment = new LoginFragment(this);
				carro.setImageResource(R.drawable.car_hover);
			break;
			
			case CONTATO_FRAGMENT:
				fragment = new ContatoFragment(this);
				contato_menu.setImageResource(R.drawable.contato_hover);
			break;
			
			case LOGIN_FRAGMENT:
				fragment = new LoginFragment(this);
			break;
				
			case SAIR_FRAGMENT:
				MySession my_session = new MySession(Home.this);
				my_session.lougout();
				Toast.makeText(this, "Logoff realizado com sucesso!", Toast.LENGTH_LONG).show();
				fragment = new HomeFragment(this);
				home.setImageResource(R.drawable.home_hover);
			break;				
				
			default:
				break;
		}

		if (fragment != null) {
			fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
			fragmentTransaction.add(R.id.frame_container, fragment);
			fragmentTransaction.commit();
			drawmer_list.setItemChecked(position, true);
			drawmer_list.setSelection(position);
			drawmer_layout.closeDrawer(drawmer_list);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (bar.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		bar.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		bar.onConfigurationChanged(newConfig);
	}


}
