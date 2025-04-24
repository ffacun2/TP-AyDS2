package model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
	
	private Socket socket;
	private List<Mensaje> mensajesPendientes;
	private boolean estado;
	
	public Cliente (Socket socket) {
		this.socket = socket;
		this.mensajesPendientes = new ArrayList<>();
		this.estado = true;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public List<Mensaje> getMensajesPendientes() {
		return mensajesPendientes;
	}
	
	public void addMensajePendiente(Mensaje mensaje) {
		this.mensajesPendientes.add(mensaje);
	}
	
	public boolean getEstado() {
		return this.estado;
	}
	
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public int getPuertoSocket() {
		return this.socket.getPort();
	}
	
	public void enviarMensaje (Mensaje mensaje) {
		if (estado) {
			//envia el mensaje directamente al usuario online;
		}
		else {
			mensajesPendientes.add(mensaje);
		}
	}
}
