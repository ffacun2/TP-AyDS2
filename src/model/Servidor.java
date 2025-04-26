package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import interfaces.IEnviable;
import interfaces.IServidor;
import requests.OKResponse;
import requests.RequestDirectorio;
import requests.RequestLogin;
import requests.RequestLogout;
import requests.RequestMensaje;
import requests.RequestRegistro;


public class Servidor implements Runnable, IServidor{

	private int puerto;
	private ServerSocket serverSocket; //recibe
	private ConcurrentHashMap<String,HandleCliente> directorio;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
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
				// Esta instruccion solo se ejecuta cuando se crea un usuario
				
				System.out.println("Conexion con: "+socket.getPort());
		
				System.out.println(">>  (Servidor) Creando OutputStream");
				this.out = new ObjectOutputStream(socket.getOutputStream());
				this.out.flush();
				System.out.println(">>  (Servidor) Creado y flusheado");
				System.out.println(">>  (Servidor) Creando InputStream");
				this.in = new ObjectInputStream(socket.getInputStream());
				
				System.out.println(">>  (Servidor) Leyendo");
				IEnviable req = (IEnviable)in.readObject();
				System.out.println(">> Recibido una request "+ req);
				req.manejarRequest(this,socket);
				System.out.println(">> Manejada la request "+ req);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Contacto> getAgenda(String nickname) {
		ArrayList<Contacto> contacto = new ArrayList<Contacto>();
		Set<String> nicknames = this.directorio.keySet();
		Iterator<String> it = nicknames.iterator();
		
		String aux;
		while(it.hasNext()) {
			aux = (String) it.next();
			if (aux != nickname) {
				contacto.add(new Contacto(aux));
			}
			else
				it.next();
		}
		return contacto;
	}
	
	
	/**
	 * Al recibir el request re registro, se verifica que el nick no este en uso y se procede a crear en caso
	 * que no exista. informa al usuario a traves del objeto OKResponse indicando el estado de la peticion.
	 * 
	 * @param req Objeto requestRegister que contiene los datos necesarios para registrar un usuario (nickname)
	 * @param socket Socket de conexion entre servidor y usuario a registrar
	 * @throws IOException Se lanza si la conexion se pierde
	 */
	public void handleRegistro(RequestRegistro req, Socket socket) throws IOException { //OK
		String nick = req.getNickname();
		
		System.out.println("Recibido request de registro...");
		if (this.directorio.containsKey(nick)) {
			this.out.writeObject(new OKResponse(false));
		}
		else {
			HandleCliente hCliente = new HandleCliente(socket,this);
			hCliente.setInput(this.in);
			hCliente.setOutput(this.out);
			new Thread(hCliente).start();
			this.directorio.put(nick, hCliente);
			this.out.writeObject(new OKResponse(true));
		}
	}
	
	/**
	 * Para iniciar sesion se recibe un request con el nick del usuario y verifica
	 * si se encuentra en la lista de directorios, en caso afirmativo se loguea
	 * caso contrario avisa que el usuario no existe a traves del objeto
	 * OKResponse que contiene true o false de acuerdo a si se pudo loguear o no.
	 * 
	 * @param req Objeto RequestLogin con los datos necesarios para loguearse
	 * @param socket Socket de conexion entre servidor y usuario a loguear
	 * @throws IOException Si se pierde la conexion
	 */
	public void handleIniciarSesion(RequestLogin req, Socket socket) throws IOException { 
		String nick = req.getNickname();
		HandleCliente cliente;
		
		if (this.directorio.containsKey(nick)) {
			cliente = this.directorio.get(nick);
			//Actualiza socket, input y output
			cliente.setSocket(socket);
			cliente.setInput(this.in);
			cliente.setOutput(this.out);
			new Thread(cliente).start();
			cliente.setEstado(true);
			cliente.getOutput().writeObject(new OKResponse(true));
			cliente.mandarMsjPendientes();
		}
		else {
			this.out.writeObject(new OKResponse(false));
		}
	}
	
	/**
	 * Se recibe un request para cerrar sesion con el nickname correspondiente,
	 * se asigna a estado false para indicar que esta offline y se cierra el socket.
	 * Le avisa al usuario con el objeto OKResponse si se pudo cerrar conexion.
	 * 
	 * @param req
	 * @param socket
	 * @throws IOException
	 */
	public void handleCerrarSesion(RequestLogout req, Socket socket) throws IOException {
		String nick = req.getNickname();
		HandleCliente cliente;
		
		if (this.directorio.containsKey(nick)) {
			cliente = this.directorio.get(nick);
			cliente.setEstado(false);
			cliente.getOutput().writeObject(new OKResponse(true));
			cliente.getSocket().close();
		}
	}
	
	/**
	 * Al recibir la peticion de directorios, se envia una lista con los nicks 
	 * de los usuarios registrados en el servidor al socket que realizo dicha 
	 * peticion.
	 * 
	 * @param req
	 * @param socket
	 * @throws IOException
	 */
	public void handleDirectorio(RequestDirectorio req, Socket socket) throws IOException {
		String nick = req.getNickname();
		this.directorio.get(nick).enviarDirectorio(this.getAgenda(nick));
	}
	
	public void handleMensaje(Mensaje mensaje) throws IOException {
		String nickReceptor = null;
		HandleCliente cliente;
	
		cliente = this.directorio.get(nickReceptor);
		if (cliente.getEstado()) {
			out = new ObjectOutputStream(cliente.getSocket().getOutputStream());
			out.writeObject(new RequestMensaje(mensaje));
		}
		else {
			cliente.addMensajePendiente(mensaje);
		}	
	}
	
}
