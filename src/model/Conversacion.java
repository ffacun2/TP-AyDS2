package model;

import java.util.ArrayList;

public class Conversacion {
	private ArrayList<Mensaje> mensajes;
	
	public Conversacion() {
		this.mensajes = new ArrayList<Mensaje>();
	}

	public ArrayList<Mensaje> getMensajes() {
		return this.mensajes;
	}
	
	public void agregarMensaje(Mensaje mensaje) {
		this.mensajes.add(mensaje);
	}
}
