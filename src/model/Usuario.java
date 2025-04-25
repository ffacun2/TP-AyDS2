package model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import api.ServidorAPI;
import exceptions.ContactoRepetidoException;
import exceptions.FueraDeRangoException;

public class Usuario{
	private String nickname;
	private int puerto;
	private String ip;
	private ArrayList<Contacto> contactos;
	private ServidorAPI servidor;
	
	public Usuario(String nickname,int puerto,String ip, ServidorAPI servidor) {
		this.nickname = nickname;
		this.puerto = puerto;
		this.ip = ip; 
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


	public void agregarContacto(Contacto contacto) throws ContactoRepetidoException{
		Iterator<Contacto> it = this.contactos.iterator();
		Contacto aux;
		
		while (it.hasNext()) {
			aux = it.next();
			if (aux.equals(contacto))
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
		try {
			Socket socket = new Socket();
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(mensaje);
			out.flush();
			out.close();
			socket.close();
		}
		catch (FueraDeRangoException e){
			throw e;
		}

	}
}
