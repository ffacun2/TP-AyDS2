package model;

import java.io.Serializable;
import java.time.LocalTime;

public class Mensaje implements Serializable{
	private String cuerpo;
	private String nickEmisor;
	private LocalTime  hora;
	private String ip;
	private int puerto;
	
	public Mensaje(String nickEmisor,int puerto, String ip,String cuerpo) {
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
	
	@Override
	public String toString() {
		String texto = this.cuerpo + " - " + this.hora + " - " + this.nickEmisor;
		return texto;
	}
}
