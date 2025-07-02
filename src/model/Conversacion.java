package model;

import java.util.ArrayList;

public class Conversacion {
	private ArrayList<Mensaje> mensajes;
	
	public Conversacion() {
		this.mensajes = new ArrayList<Mensaje>();
	}

	public void setMensajes(ArrayList<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public ArrayList<Mensaje> getMensajes() {
		return this.mensajes;
	}
	
	public void agregarMensaje(Mensaje mensaje) {
		this.mensajes.add(mensaje);
	}

	@Override
	public String toString() {
		return "{["+mensajes+"]}\n";
	}
	
	
}
