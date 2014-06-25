package br.sony.sonyeduca.classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySession{
	public Activity act;
	public SharedPreferences sessions;
	public static final String MY_PREFERENCES = "MySession";
	public static final String id = "id";
	public static final String login = "login";
	public static final String senha = "senha";
	public static final String busca = "busca";
	public static final String escolha = "escolha";
	public static final String nome = "nome";
	
	public MySession(Activity act){
		this.act = act;
		sessions = act.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
	}
	
	public void setLogin(String set_login){
		Editor editor = sessions.edit();
		editor.putString(login, set_login);
		editor.commit();
	}
	
	public String getLogin(){
		return sessions.getString(login, "");
	}
	
	public void setPassword(String set_senha){
		Editor editor = sessions.edit();
		editor.putString(senha, set_senha);
		editor.commit();
	}
	
	public String getPassword(){
		return sessions.getString(senha, "");
	}
	
	public boolean check(){
		if(!getLogin().equals("")  && !getPassword().equals("")){
			return true;
		}else{
			return false;
		}
	}
	
	
	public void setBusca(String set_busca){
		Editor editor = sessions.edit();
		editor.putString(busca, set_busca);
		editor.commit();
	}
	
	public String getBusca(){
		return sessions.getString(busca, "");
	}
	
	public void setEscolha(String set_escolha){
		Editor editor = sessions.edit();
		editor.putString(escolha, set_escolha);
		editor.commit();
	}
	
	public String getEscolha(){
		return sessions.getString(escolha, "");
	}
	
	
	public void setId(String set_id){
		Editor editor = sessions.edit();
		editor.putString(id, set_id);
		editor.commit();
	}
	
	public String getId(){
		return sessions.getString(id, "");
	}
	
	public void setNome(String set_nome){
		Editor editor = sessions.edit();
		editor.putString(nome, set_nome);
		editor.commit();
	}
	
	public String getNome(){
		return sessions.getString(nome, "");
	}
	
	public void lougout(){
		Editor editor = sessions.edit();
		editor.clear();
	    editor.commit();
	}
}
