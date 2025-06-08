package requests;

import interfaces.IEnviable;
import model.Mensaje;
import utils.Utils;

public class RequestFactory {
	
	public RequestFactory() {
	}
	
	public IEnviable getRequest(String idRequest, Object arg) {
		switch (idRequest) {
		case Utils.ID_DIRECTORIO -> {
			return new RequestDirectorio((String)arg);
		}
		case Utils.ID_LOGIN -> {
			return new RequestLogin((String)arg);
		}
		case Utils.ID_LOGOUT -> {
			return new RequestLogout((String)arg);
		}
		case Utils.ID_MENSAJE -> {
			return new RequestMensaje((Mensaje)arg);
		}
		case Utils.ID_REG -> {
			return new RequestRegistro((String)arg);
		}
		default -> {
			return null;
		}
		}
	}
}
