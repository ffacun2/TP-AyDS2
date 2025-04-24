package model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")
public class HandleCliente extends Observable implements Runnable{
	
	private Servidor servidor;
	private Socket socket;
	private List<Mensaje> mensajesPendientes;
	private boolean estado;
	
	public HandleCliente (Socket socket, Servidor servidor) {
		this.socket = socket;
		this.servidor = servidor;
		this.mensajesPendientes = new ArrayList<>();
		this.estado = true;
	}
	
	@Override
	public void run() {
		//fuera del while envio mensajes pendientes
		while (true) {
			// Aca verifico los objetos recibidos (mensaje,login, logout, directorio, contacto)
		}
		
	}

	public Socket getSocket() {
		return this.socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
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
		return this.socket.getLocalPort();
	}
	
	public String getIpSocket() {
		return this.socket.getLocalAddress().getHostAddress();
	}
	
	public void enviarMensaje (Mensaje mensaje) {
		if (estado) {
			//envia el mensaje directamente al usuario online;
		}
		else {
			mensajesPendientes.add(mensaje);
		}
	}

	public void setObservador(Observer o) {
		this.addObserver(o);
	}
	
}
