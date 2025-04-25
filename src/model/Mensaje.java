package model;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.time.LocalTime;

import interfaces.IEnviable;

public class Mensaje implements Serializable,IEnviable {
	private String cuerpo;
	private String nickEmisor;
	private String nickReceptor;
	private LocalTime  hora;
	private String ip;
	private int puerto;
	
	public Mensaje(String nickEmisor,String nickReceptor,int puerto, String ip,String cuerpo) {
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
		servidor.handleMensaje(this);
	}
}
