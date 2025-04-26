package interfaces;

import java.io.IOException;
import java.io.Serializable;

import api.ServidorAPI;

public interface IRecibible extends Serializable {
	public void manejarResponse(ServidorAPI servidor) throws IOException;
}
