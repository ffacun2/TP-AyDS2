package interfaces;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public interface IEnviable extends Serializable {
	
	public void manejarRequest(IServidor servidor, Socket socket) throws IOException;
}
