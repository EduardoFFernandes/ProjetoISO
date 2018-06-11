package models;

public abstract class Gerenciador {

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
	
//	public abstract void print();
	
}
