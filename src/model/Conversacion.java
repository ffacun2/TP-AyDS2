package model;

import java.util.ArrayList;

public class Conversacion {
	private ArrayList<Mensaje> mensajesUsuario;
	private ArrayList<Mensaje> mensajeContacto;
	
	public Conversacion() {
		this.mensajeContacto = new ArrayList<Mensaje>();
		this.mensajesUsuario = new ArrayList<Mensaje>();
	}

	public ArrayList<Mensaje> getMensajesUsuario() {
		return mensajesUsuario;
	}

	public ArrayList<Mensaje> getMensajeContacto() {
		return mensajeContacto;
	}
}
