package requests;

import model.Servidor;

public class RequestLogin extends Request{

	@Override
	public void manejarRequest(Servidor servidor) {
		servidor.handleIniciarSesion(this);
	}

}
