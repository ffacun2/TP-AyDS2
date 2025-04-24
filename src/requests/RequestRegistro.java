package requests;

import java.io.IOException;
import java.net.Socket;

import model.Servidor;

public class RequestRegistro extends Request {
	
	private static final long serialVersionUID = 1L;

	public RequestRegistro (String nickname) {
		super(nickname);
	}
	
	@Override
	public void manejarRequest(Servidor servidor, Socket socket) throws IOException {
		servidor.handleRegistro(this, socket);
	}

}
