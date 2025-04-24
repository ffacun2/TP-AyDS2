package requests;

import java.net.Socket;

import model.Servidor;

public class RequestRegistro extends Request{

	private Socket socket;
	
	@Override
	public void manejarRequest(Servidor servidor) {
		servidor.handleRegistro(this);
	}

	public Socket getSocket() {
		return this.socket;
	}
}
