package requests;

import model.Servidor;

public class RequestDirectorio extends Request{

	@Override
	public void manejarRequest(Servidor servidor) {
		servidor.handleDirectorio(this);
	}

}
