package model;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import interfaces.IEnviable;
import interfaces.IServidor;

public class Mensaje implements IEnviable {
	private static final long serialVersionUID = 1L;
	private String cuerpo;
	private String nickEmisor;
	private String nickReceptor;
	private LocalTime  hora;
	
	public Mensaje(String nickEmisor,String nickReceptor,String cuerpo) {
		this.cuerpo = cuerpo;
		this.nickEmisor = nickEmisor;
		this.nickReceptor = nickReceptor;
		this.hora = LocalTime.now();
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public String getNickEmisor() {
		return nickEmisor;
	}

	public String getHora() {
		return hora.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}
	
	public String getNickReceptor() {
		return this.nickReceptor;
	}
	
	@Override
	public String toString() {
		String texto = this.cuerpo + " - " + this.hora + " - " + this.nickEmisor+", \n";
		return texto;
	}

	@Override
	public void manejarRequest(IServidor servidor, Socket socket) throws IOException {
		servidor.handleMensaje(this);//chequear esto
	}
}
