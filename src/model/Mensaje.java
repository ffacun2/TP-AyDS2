package model;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;

import interfaces.IEnviable;

public class Mensaje implements IEnviable {
	private String cuerpo;
	private String nickEmisor;
	private String nickReceptor;
	private LocalTime  hora;
	
	public Mensaje(String nickEmisor,String nickReceptor,String cuerpo) {
		this.cuerpo = cuerpo;
		this.nickEmisor = nickEmisor;
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
	public void manejarRequest(Servidor servidor, Socket socket) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
