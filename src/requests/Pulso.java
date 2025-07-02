package requests;

import java.io.IOException;
import java.net.Socket;

import interfaces.IEnviable;
import interfaces.IServidor;
import servidor.Servidor;

public class Pulso implements IEnviable {
	
	private static final long serialVersionUID = 1L;
	private String mensaje;
	
	public Pulso(String mensaje) {
		this.mensaje = mensaje;
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

}
