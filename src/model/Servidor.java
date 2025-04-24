package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Set;
import java.util.Observable;
import java.util.Observer;

import exceptions.FueraDeRangoException;

@SuppressWarnings("deprecation")
public class Servidor extends Observable implements Runnable{

	private int puerto;
	private ServerSocket serverSocket;
	private HashMap<String,Cliente> directorio;
	
	public Servidor(int puerto) throws IOException, FueraDeRangoException {
		if (this.puerto < 0 || this.puerto > 65535)
			throw new FueraDeRangoException();
		else
			this.puerto = puerto;
		
		this.serverSocket = new ServerSocket(puerto);
	}

	@Override
	public void run() {
		try {
			while (true) {
				System.out.println("Escuchando en puerto "+ this.puerto +"...");
				Socket socket = serverSocket.accept();
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); //Va a leer un objeto en lugar de un string
		        
				//Verifico de que tipo es el objeto recibido (mensaje, request (login, logout, directorio)
				
				Mensaje mensaje = (Mensaje) in.readObject();
		        this.setChanged();
		        this.notifyObservers(mensaje);
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void agregarUsuario(Usuario usuario) throws IOException{
		//Si se logeo anteriormente, el nickname debe existir, solo inicia sesion
		if (directorio.containsKey(usuario.getNickname())) {
			
		}
		//Si es la primera vez que se logea, se debe crear.
		else {
			directorio.put(usuario.getNickname(),new Cliente(new Socket(usuario.getIp(),usuario.getPuerto())));
		}
	}
	
	public Set<String> getDirectorio() {
		return this.directorio.keySet();
	}
	
	public Contacto getContacto (String nickname) {
		Cliente cliente = directorio.get(nickname);
		Contacto contacto = new Contacto(nickname,cliente.getPuertoSocket(),cliente.getIpSocket());
		contacto.setConversacion(new Conversacion());
		return contacto;
	}
	
	public void cerrarSesion (Usuario usuario) {
		if (directorio.containsKey(usuario.getNickname())) {
			Cliente c = directorio.get(usuario.getNickname());
			c.setEstado(false);
		}
	}
	
	public void iniciarSesion (Usuario usuario) throws UnknownHostException, IOException {
		//Si el usuario se logeo anteriormente, se busca si existe y se establece estado en true (activo)
		if (directorio.containsKey(usuario.getNickname())) {
			Cliente c = directorio.get(usuario.getNickname());
			c.setEstado(true);
			
			for(Mensaje msj: c.getMensajesPendientes()) {
				
			}
		}
		// Si el usuario se logea por primera vez, se crea
		else {
			Cliente c = new Cliente(new Socket(usuario.getIp(),usuario.getPuerto()));
			directorio.put(usuario.getNickname(), c);
		}
	}
	
	public void setObservador(Observer o) {
		this.addObserver(o);
	}
	
	

}
