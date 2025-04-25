package model;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;

import interfaces.IEnviable;

public class MensajeServidor implements IEnviable{
	private String cuerpo;
	private String nickEmisor;
	private String nickReceptor;
	private LocalTime  hora;
	private String ip;
	private int puerto;
	
	public MensajeServidor(String nickEmisor,String nickReceptor,int puerto, String ip,String cuerpo) {
		this.cuerpo = cuerpo;
		this.nickEmisor = nickEmisor;
		this.ip = ip;
		this.puerto = puerto;
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

	public String getIp() {
		return ip;
	}

	public int getPuerto() {
		return puerto;
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
