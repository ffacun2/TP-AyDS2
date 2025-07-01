package interfaces;

import java.io.IOException;
import java.io.Serializable;

import cliente.ClienteAPI;

public interface IRecibible extends Serializable {
	public void manejarResponse(ClienteAPI servidor) throws IOException;
}
