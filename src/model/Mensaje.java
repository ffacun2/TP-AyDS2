package model;

import java.io.Serializable;
import java.util.Date;

public class Mensaje implements Serializable{
	private String cuerpo;
	private String nickEmisor;
	private Date  hora;
	private String ip;
	private int puerto;
	
	public Mensaje(String nickEmisor,int puerto, String ip,String cuerpo) {
		this.cuerpo = cuerpo;
		this.nickEmisor = nickEmisor;
		this.ip = ip;
		this.puerto = puerto;
		this.hora = new Date();
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public String getNickEmisor() {
		return nickEmisor;
	}

	public Date getHora() {
		return hora;
	}

	public String getIp() {
		return ip;
	}

	public int getPuerto() {
		return puerto;
	}
	
}
