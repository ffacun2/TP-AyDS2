package model;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cliente.ServidorAPI;
import exceptions.ContactoRepetidoException;
import interfaces.SerializableTxt;

public class Usuario implements SerializableTxt {
	private String nickname;
	private int puerto;
	private String ip;
	private ArrayList<Contacto> contactos = new ArrayList<Contacto>();
	@JsonIgnore
	private ServidorAPI servidor;
	
	public Usuario(String nickname,int puerto,String ip, ServidorAPI servidor) {
		this.nickname = nickname;
		this.puerto = puerto;
		this.ip = ip; 
		this.servidor = servidor;
	}

	public Usuario() {
		
	}
	
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void setContactos(ArrayList<Contacto> contactos) {
		this.contactos = contactos;
	}

	public String getNickname() {
		return nickname;
	}

	public int getPuerto() {
		return puerto;
	}
	
	public String getIp() {
		return this.ip;
	}

	public ArrayList<Contacto> getContactos() {
		return contactos;
	}

	public void setServidor(ServidorAPI server) {
		this.servidor = server;
	}

	public void agregarContacto(Contacto contacto) throws ContactoRepetidoException{
		for(Contacto aux: this.contactos) {
			if(aux.equals(contacto)) {
				throw new ContactoRepetidoException();
			}
		}
		this.contactos.add(contacto);
	}

	
	/**
	 * Envia un mensaje al contacto. En caso de no poder establecer la conexión, lanza una exception. 
	 * 
	 * @param mensaje: Tiene que tener todos los campos declarados y validados. 
	 * @param contacto: Tiene que tener todos los campos declarados y validados. 
	 * 
	 * @throws UnknownHostException: No se puede conectar con el contacto. 
	 * @throws IOException: Hay un problema al escribir los datos en el stream.  
	 * @throws  
	 */
	public void enviarMensaje(Mensaje mensaje, Contacto contacto) throws UnknownHostException, IOException {
		this.servidor.enviarRequest(mensaje);
	}

	@Override
	public String toString() {
		return "Usuario {"+
					"nickname: "+nickname+
					"IP: "+ip+
					"Puerto: "+puerto+
					"Contactos: ["+contactos+
					"]\n}\n";
	}
	
	
	@Override
	public String toTxt() {
		return "#Usuario:"+nickname+" | "+ip+ " | "+puerto;
	}
	
}
