package requests;

import java.io.IOException;
import java.net.Socket;

import interfaces.IEnviable;
import interfaces.IServidor;
import model.Mensaje;

public class RequestMensaje implements IEnviable {

	private Mensaje mensaje;
	
	public RequestMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

	@Override
	public void manejarRequest(IServidor servidor, Socket socket) throws IOException {
		servidor.handleMensaje(mensaje);
	}
	
	public Mensaje getMensaje() {
		return mensaje;
	}
}
