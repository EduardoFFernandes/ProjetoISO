package models;

public abstract class Gerenciador implements Runnable{

	private String nome;
	private int uid;
	
	public Gerenciador(String nome, int uid) {
		this.nome = nome;
		this.uid = uid;
	}
	
	
	public String getNome() {
		return nome;
	}
	public int getUid() {
		return uid;
	}
	
}
