package model;

import java.util.ArrayList;

public class Conversacion {
	private ArrayList<Mensaje> mensajesUsuario;
	private ArrayList<Mensaje> mensajesContacto;
	private boolean visto;
	
	public Conversacion() {
		this.mensajesContacto = new ArrayList<Mensaje>();
		this.mensajesUsuario = new ArrayList<Mensaje>();
	}

	public ArrayList<Mensaje> getMensajesUsuario() {
		return mensajesUsuario;
	}

	public ArrayList<Mensaje> getMensajeContacto() {
		return mensajesContacto;
	}
	
	public void agregarMensajeEmisor(Mensaje mensaje) {
		this.mensajesContacto.add(mensaje);
	}
	
	public void agregarMensajeReceptor(Mensaje mensaje) {
		this.mensajesUsuario.add(mensaje);
	}
}
