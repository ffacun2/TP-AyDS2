package requests;

import java.io.IOException;
import java.net.Socket;

import model.Servidor;

public class RequestLogin extends Request{
	
	public RequestLogin (String nickname) {
		super(nickname);
	}

	@Override
	public void manejarRequest(Servidor servidor, Socket socket) throws IOException {
		servidor.handleIniciarSesion(this, socket);
	}

}
