package api;

import java.io.IOException;
import java.time.LocalTime;

import interfaces.IRecibible;
import model.Mensaje;

public class MensajeResponse implements IRecibible{
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
	public void manejarResponse(ServidorAPI servidor) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
}
