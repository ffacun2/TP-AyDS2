package requests;

import java.io.IOException;
import java.net.Socket;

import model.Servidor;

public class RequestDirectorio extends Request {
	
	public RequestDirectorio(String nickname) {
		super(nickname);
	}

	@Override
	public void manejarRequest(Servidor servidor, Socket socket) throws IOException {
		servidor.handleDirectorio(this, socket);
	}

}
