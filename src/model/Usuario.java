package model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import exceptions.ContactoRepetidoException;

public class Usuario{
	private String nickname;
	private int puerto;
	private String ip;
	private ArrayList<Contacto> contactos;
	private Servidor servidor;
	
	public Usuario(String nickname,int puerto,String ip) {
		this.nickname = nickname;
		this.puerto = puerto;
		this.ip = ip;
		this.contactos = new ArrayList<Contacto>();
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

	public Servidor getServidor() {
		return servidor;
	}

	public void agregarContacto(Contacto contacto) throws ContactoRepetidoException {
		Iterator<Contacto> it = this.contactos.iterator();
		Contacto aux;
		
		while (it.hasNext()) {
			aux = it.next();
			if (aux.getPuerto() == contacto.getPuerto() || aux.getIp() == contacto.getIp())
				throw new ContactoRepetidoException();
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
	 */
	public void enviarMensaje(Mensaje mensaje, Contacto contacto) throws UnknownHostException, IOException {
		Socket socket = new Socket(contacto.getIp(),contacto.getPuerto());
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(mensaje);
		out.flush();
		out.close();
		socket.close();
	}
}
