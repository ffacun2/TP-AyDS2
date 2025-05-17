package requests;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import interfaces.IEnviable;
import interfaces.IServidor;
import servidor.HandleCliente;
import servidor.Servidor;

public class Pulso implements IEnviable {
	
	private static final long serialVersionUID = 1L;
	private String mensaje;
	private ConcurrentHashMap<String, HandleCliente> directorios;
	
	public Pulso(String mensaje) {
		this.mensaje = mensaje;
		this.directorios = null;
	}
	
	public Pulso(String mensaje, ConcurrentHashMap<String, HandleCliente> directorios) {
		this.mensaje = mensaje;
		this.directorios = directorios;
	}

	@Override
	public void manejarRequest(IServidor servidor, Socket socket) throws IOException {
		((Servidor)servidor).handleHeartBeat(this,socket);		
	}

	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public ConcurrentHashMap<String, HandleCliente> getDirectorios(){
		return this.directorios;
	}
	
	public void setDirectorios (ConcurrentHashMap<String, HandleCliente> directorios) {
		this.directorios = directorios;
	}
	
}
