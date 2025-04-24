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
	
	public void setConversacion(Conversacion conversacion) {
		this.conversacion = conversacion;
	}
	
	public void agregarMensaje(Mensaje msj) {
		this.conversacion.agregarMensaje(msj);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Contacto contacto = (Contacto) obj;
		return puerto == contacto.puerto 
				&& ip.equals(contacto.ip) 
				&& nickname.equals(contacto.getNickname());
	}
	
	@Override
	public String toString() {
		String texto = this.nickname + " - " + this.ip + " - " + this.puerto;
		return texto;
	}
}
