package interfaces;

import java.io.IOException;
import java.net.Socket;

import api.ServidorAPI;

public interface IRecibible {
	public void manejarResponse(ServidorAPI servidor) throws IOException;
}
