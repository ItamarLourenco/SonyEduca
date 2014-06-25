package br.sony.sonyeduca.classes;

import java.io.Serializable;



public class Video implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id_video;
	private String nome_aula;
	private String ordem;
	private float valor;
	private String descricao;
	private String arquivo;
	private String arquivo_img;
	
	public int getId() {
		return id_video;
	}
	public String getNomeAula() {
		return nome_aula;
	}
	public String getOrdem() {
		return ordem;
	}
	public float getValor() {
		return valor;
	}
	public String getDescricao() {
		return descricao;
	}
	public String getArquivo() {
		return arquivo;
	}
	public String getArquivoImg() {
		return arquivo_img;
	}
	public void setId(int id_video) {
		this.id_video = id_video;
	}
	public void setNomeAula(String nome_aula) {
		this.nome_aula = nome_aula;
	}
	public void setOrdem(String ordem) {
		this.ordem = ordem;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}
	public void setArquivoImg(String arquivo_img) {
		this.arquivo_img = arquivo_img;
	}
	
}
