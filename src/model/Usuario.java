package model;

import java.util.ArrayList;

public class Usuario {
	private String nickname;
	private int puerto;
	private String ip;
	private ArrayList<Contacto> contactos;
	private Servidor servidor;
	
	public Usuario(String nickname,int puerto,String ip) {
		this.nickname = nickname;
		this.puerto = puerto;
		this.ip = ip;
		this.contactos = new ArrayList<Contacto>();
	}

	public String getNickname() {
		return nickname;
	}

	public int getPuerto() {
		return puerto;
	}

	public ArrayList<Contacto> getContactos() {
		return contactos;
	}

	public Servidor getServidor() {
		return servidor;
	}

	public void agregarContacto(Contacto contacto) {
		this.contactos.add(contacto);
	}
}
