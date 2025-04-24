package requests;

import java.io.IOException;
import java.net.Socket;

import model.Servidor;

public class RequestLogout extends Request{

	public RequestLogout (String nickname) {
		super(nickname);
	}
	
	@Override
	public void manejarRequest(Servidor servidor, Socket socket) throws IOException {
		servidor.handleCerrarSesion(this,socket);
	}

}
