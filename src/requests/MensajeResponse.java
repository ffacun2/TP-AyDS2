package requests;

import java.io.IOException;
import java.time.LocalTime;

import cliente.ClienteAPI;
import interfaces.IRecibible;
import model.Mensaje;

public class MensajeResponse implements IRecibible{

	private static final long serialVersionUID = 1L;
	private String cuerpo;
	private String nickEmisor;
	private String nickReceptor;
	private LocalTime  hora;

	public MensajeResponse(Mensaje mensaje) {
		this.cuerpo = mensaje.getCuerpo();
		this.nickEmisor = mensaje.getNickEmisor();
		this.nickReceptor = mensaje.getNickReceptor();
		this.hora = LocalTime.now();
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public String getNickEmisor() {
		return nickEmisor;
	}

	public LocalTime getHora() {
		return hora;
	}

	
	public String getNickReceptor() {
		return this.nickReceptor;
	}
	
	@Override
	public String toString() {
		String texto = this.cuerpo + " - " + this.hora + " - " + this.nickEmisor;
		return texto;
	}

	@Override
	public void manejarResponse(ClienteAPI servidor) throws IOException {
		servidor.mensajeRecibido(this);
	}
	
}
