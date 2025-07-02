package requests;

import java.io.IOException;
import java.io.Serializable;

import cliente.ClienteAPI;
import interfaces.IRecibible;

public class OKResponse implements IRecibible, Serializable{

	private static final long serialVersionUID = 1L;
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
	public void manejarResponse(ClienteAPI servidor) throws IOException {
		servidor.setResponse(this);
	}
}
