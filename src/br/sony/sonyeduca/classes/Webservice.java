package br.sony.sonyeduca.classes;

public class Webservice extends ServiceHandler{
	final private String URL_VIDEOS = "http://192.168.0.10:8888/sonyeduca/videos/ws/";
	final private String TEMAS_VIDEOS =  "http://192.168.0.10:8888/sonyeduca/videos/temas/ws/";
	final private String SALVAR_CADASTRO = "http://192.168.0.10:8888/sonyeduca/cadastros/add/ws/";
	final private String MEUS_VIDEOS = "http://192.168.0.10:8888/sonyeduca/compras/ws/";
	final private String COMPRAS = "http://192.168.0.10:8888/sonyeduca/compras/ws/add/";
	final private String LOGIN = "http://192.168.0.10:8888/sonyeduca/cadastros/ws/";
	final private String CHECK = "http://192.168.0.10:8888/sonyeduca/compras/ws/check/";
	
	public String getUrlVideos(){
		return URL_VIDEOS;
	}
	public String getUrlVideos(String busca){
		if(busca == null){
			return URL_VIDEOS;
		}else{
			return URL_VIDEOS + busca;
		}		
	}
	
	public String getUrlTemas(){
		return TEMAS_VIDEOS;
	}
	public String getUrlTemas(String busca){
		return TEMAS_VIDEOS + busca;
	}
	
	public String getUrlCadastro(){
		return SALVAR_CADASTRO;
	}
	public String getUrlCadastro(String busca){
		return SALVAR_CADASTRO + busca;
	}
	
	public String getUrlMeusVideos(){
		return MEUS_VIDEOS;
	}
	public String getUrlMeusVideos(String busca){
		return MEUS_VIDEOS + busca;
	}
	
	public String getUrlLogin(){
		return LOGIN;
	}
	public String getUrlLogin(String busca){
		return LOGIN + busca;
	}
	
	public String getUrlCompras(){
		return COMPRAS;
	}
	
	public String getUrlCompras(String busca){
		return COMPRAS + busca;
	}
	
	public String getUrlCheck(){
		return CHECK;
	}
}
