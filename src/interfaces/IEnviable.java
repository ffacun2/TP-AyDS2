package interfaces;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import model.Servidor;

public interface IEnviable extends Serializable {
	
	public void manejarRequest(IServidor servidor, Socket socket) throws IOException;
}
