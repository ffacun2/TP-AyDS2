package requests;

import model.Servidor;

public class RequestLogout extends Request{

	@Override
	public void manejarRequest(Servidor servidor) {
		servidor.handleCerrarSesion(this);
	}

}
