package requests;

import java.io.IOException;
import java.net.Socket;

import interfaces.IEnviable;
import model.Mensaje;
import model.Servidor;

public class RequestMensaje implements IEnviable {

	private Mensaje mensaje;
	
	public RequestMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

	@Override
	public void manejarRequest(Servidor servidor, Socket socket) throws IOException {
		servidor.handleMensaje(mensaje);
	}
	
	public Mensaje getMensaje() {
		return mensaje;
	}
}
