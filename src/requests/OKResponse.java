package requests;

import java.io.IOException;
import java.net.Socket;

import api.ServidorAPI;
import interfaces.IRecibible;
import interfaces.IServidor;

public class OKResponse implements IRecibible{

	private boolean success;
	private String mensajeError;
	
	/**
	 * Request que se manda desde el servidor para confirmar una operacion.
	 * @param success: True si se pudo concretar y False si no se pudo
	 */
	public OKResponse (boolean success) {
		this.success = success;
		this.mensajeError = "Error";
	}
	
	/**
	 * Sobrecargo el constructor para poder mandar un mensaje en caso de que no se pudiera con el por que
	 * @param success: True si se pudo concretar y False si no se pudo
	 * @param error: Mensaje con el detalle del error
	 */
	public OKResponse (boolean success, String error) {
		this.success = success;
		this.mensajeError = error;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public String getMensajeError() {
		return this.mensajeError;
	}

	@Override
	public void manejarResponse(ServidorAPI servidor) throws IOException {
		servidor.setResponse(this);
	}
}
