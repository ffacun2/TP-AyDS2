package model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import interfaces.SerializableTxt;

@JsonIgnoreProperties(ignoreUnknown = true) //Para que Jackson ignore los atributos desconocidos cuando lee
public class Contacto implements Serializable, SerializableTxt {
	private static final long serialVersionUID = 1L;
	private String nickname;
	private boolean visto = true;
	@JsonIgnore
	private Conversacion conversacion;
	
	public Contacto() {
	}
	
	public Contacto(String nickname) {
		this.nickname = nickname;

		this.conversacion = null;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
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
		if (this.conversacion == null) {
			this.conversacion = new Conversacion();
		}
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
		return nickname;
	}

	@Override
	public String toTxt() {
		return "#Contacto:" + this.nickname;
	}
	
	public boolean isVisto() {
		return visto;
	}
	
	public void setVisto(boolean cond) {
		this.visto = cond;
	}

}
