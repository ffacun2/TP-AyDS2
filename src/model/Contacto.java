package model;

public class Contacto {
	private String nickname;
	private Conversacion conversacion;
	private int puerto;
	private String ip;
	
	public Contacto(String nickname, int puerto, String ip) {
		this.nickname = nickname;
		this.puerto = puerto;
		this.ip = ip;
		this.conversacion = null;
	}

	public String getNickname() {
		return nickname;
	}

	public Conversacion getConversacion() {
		return conversacion;
	}

	public int getPuerto() {
		return puerto;
	}

	public String getIp() {
		return ip;
	}
	
}
