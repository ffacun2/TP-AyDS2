package requests;

import java.io.IOException;
import java.net.Socket;

import interfaces.IServidor;

public class RequestDirectorio extends Request {
	private static final long serialVersionUID = 1L;

	public RequestDirectorio(String nickname) {
		super(nickname);
	}

	@Override
	public void manejarRequest(IServidor servidor, Socket socket) throws IOException {
		servidor.handleDirectorio(this, socket);
	}

}
