package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import interfaces.IEnviable;
import requests.RequestDirectorio;
import requests.RequestLogin;
import requests.RequestLogout;
import requests.RequestRegistro;


public class Servidor implements Runnable{

	private int puerto;
	private ServerSocket serverSocket; //recibe
	private ConcurrentHashMap<String,HandleCliente> directorio;
	
	public Servidor(int puerto) throws IOException, IllegalArgumentException {
		this.puerto = puerto;		
		this.serverSocket = new ServerSocket(puerto);
		this.directorio = new ConcurrentHashMap<String,HandleCliente>();
	}

	@Override
	public void run() {
		try {
			while (true) {
				System.out.println("Escuchando en el puerto: "+ this.puerto +" ...");
				Socket socket = serverSocket.accept(); //Este socket establece la conexion entre server y usuario
				
				//al establecer conexion recibe un objeto usuario para su registro en el servidor
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); 
				
				IEnviable req = (IEnviable)in.readObject();
				req.manejarRequest(this);
				
				
				Usuario user = (Usuario) in.readObject();
				
				//Cada conexion con el servidor va a un hilo 
				Thread clienteThread = new Thread(agregarUsuario(user,socket));
				clienteThread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public HandleCliente agregarUsuario(Usuario usuario, Socket socket) throws IOException {
		HandleCliente cliente;
		//Si se logeo anteriormente, el nickname debe existir, solo inicia sesion
		// 
		if (directorio.containsKey(usuario.getNickname())) {
			cliente = directorio.get(usuario.getNickname());
			cliente.setSocket(socket);
			cliente.setEstado(true);
		}
		//Si es la primera vez que se logea, se debe crear.
		else {
			cliente = new HandleCliente(socket,this);
			directorio.put(usuario.getNickname(), cliente);
		}
		return cliente;
	}
	
	public Set<String> getDirectorio() {
		return this.directorio.keySet();
	}
	
	public Contacto getContacto (String nickname) {
		HandleCliente cliente = directorio.get(nickname);
		Contacto contacto = new Contacto(nickname,cliente.getPuertoSocket(),cliente.getIpSocket());
		contacto.setConversacion(new Conversacion());
		return contacto;
	}
	
	public void cerrarSesion (Usuario usuario) {
		if (directorio.containsKey(usuario.getNickname())) {
			HandleCliente c = directorio.get(usuario.getNickname());
			c.setEstado(false);
		}
	}
	
	public void iniciarSesion (Usuario usuario) throws UnknownHostException, IOException {
		//Si el usuario se logeo anteriormente, se busca si existe y se establece estado en true (activo)
		if (directorio.containsKey(usuario.getNickname())) {
			HandleCliente c = directorio.get(usuario.getNickname());
			c.setEstado(true);
			
			for(Mensaje msj: c.getMensajesPendientes()) {
				
			}
		}
		// Si el usuario se logea por primera vez, se crea
		else {
			HandleCliente c = new HandleCliente(new Socket(usuario.getIp(),usuario.getPuerto()),this);
			directorio.put(usuario.getNickname(), c);
		}
	}
	
	
	public void handleRegistro(RequestRegistro req) {
		Socket socket = req.getSocket();
		String nick = req.getNickname();
		
		if (this.directorio.containsKey(nick)) {
			//TODO CREAR EXCEPCION PARA CUANDO EL NICK ESTA EN USO
		}
		else {
			HandleCliente hCliente = new HandleCliente(socket,this);
			this.directorio.put(nick, hCliente);
			
			
			boolean confirmacion = true;
			//mando la confirmacion al socket
			
		}
	}
	
	public void handleMensaje(Mensaje mensaje) {
		String nickEmisor = mensaje.getNickEmisor();
	}
	
	public void handleIniciarSesion(RequestLogin req) {
		
	}
	
	public void handleCerrarSesion(RequestLogout req) {
		
	}
	
	public void handleDirectorio(RequestDirectorio req) {
		
	}
}
