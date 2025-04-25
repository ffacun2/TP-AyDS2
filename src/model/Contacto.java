package model;

public class Contacto {
	private String nickname;
	private Conversacion conversacion;

	
	public Contacto(String nickname) {
		this.nickname = nickname;

		this.conversacion = null;
	}

	public String getNickname() {
		return nickname;
	}

	public Conversacion getConversacion() {
		return conversacion;
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
		return nickname.equals(contacto.getNickname());
	}
	
	@Override
	public String toString() {
		String texto = this.nickname;
		return texto;
	}
}
