package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import interfaces.IEnviable;
import interfaces.IServidor;
import model.Contacto;
import model.Mensaje;
import requests.OKResponse;
import requests.RequestDirectorio;
import requests.RequestLogin;
import requests.RequestLogout;
import requests.RequestRegistro;

/*
 * Clase que representa el servidor del sistema
 * Se encarga de gestionar las conexiones de los clientes
 * y de enviar los mensajes entre ellos
 * Crea el socket del servidor y lo pone a escuchar en el puerto indicado
 * Recibe los mensajes de los clientes y los procesa
 * Crea un hilo para cada cliente que se conecta
 * El servidor se ejecuta en un hilo separado y se detiene cuando se cierra la ventana
 * 
 */
public class Servidor implements Runnable, IServidor {

	private int puerto;
	private boolean estado; //true si el servidor esta activo, false si no lo esta
	private ConcurrentHashMap<String,HandleCliente> directorio;
	private ServerSocket serverSocket; //socket del servidor para escuchar conexiones de Usuarios
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public Servidor(int puerto) throws IOException, IllegalArgumentException {
		this.puerto = puerto;
		this.estado = true; //el servidor se inicia en estado activo
		this.serverSocket = new ServerSocket(this.puerto);
		this.directorio = new ConcurrentHashMap<String,HandleCliente>();
	}

	@Override
	public void run() {
		try {
			while (this.estado) {
				Socket socket = serverSocket.accept(); //Este socket establece la conexion entre server y usuario
				// Esta instruccion solo se ejecuta cuando se crea un usuario
				
				this.out = new ObjectOutputStream(socket.getOutputStream());
				this.in = new ObjectInputStream(socket.getInputStream());
								
				IEnviable req = (IEnviable)in.readObject();
				req.manejarRequest(this,socket);
			}
		} catch (Exception e) {
			System.out.println("Se cerró el socket del servidor :"+this.puerto);
		}
		finally {
			this.cerrarSocketServer();
		}
		System.out.println("Cerrando");
	}
	
	public ArrayList<Contacto> getAgenda(String nickname) {
		ArrayList<Contacto> contacto = new ArrayList<Contacto>();
		Set<String> nicknames = this.directorio.keySet();
		
		for (String aux : nicknames) {
		    if (!aux.equals(nickname)) {
		        contacto.add(new Contacto(aux));
		    }
		}
		return contacto;
	}
	
	
	public void setEstado(boolean estado) {
		this.estado = estado;
		if (!estado) 
			this.detenerServidor();
	}
	
	public boolean getEstado() {
		return this.estado;
	}
	
	public void detenerServidor() {
		try {
			serverSocket.close();
		}
		catch (IOException e) {
		}
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
		if (this.directorio.containsKey(nick)) {
			this.out.writeObject(new OKResponse(false,"Usuario ya registrado"));
			socket.close();
		}
		else {
			this.out.writeObject(new OKResponse(true));
			HandleCliente hCliente = new HandleCliente(socket,this);
			hCliente.setOutput(this.out);
			hCliente.setInput(this.in);
			
			Thread hilo = new Thread(hCliente);
			hCliente.setHilo(hilo);
			
			this.directorio.put(nick, hCliente);
			hilo.start();
			
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
			if (!cliente.getEstado()) {
				//Actualiza socket, input y output
				cliente.setSocket(socket);
				cliente.setOutput(this.out);
				cliente.setInput(this.in);
				cliente.setEstado(true);

				new Thread(cliente).start();
				cliente.getOutput().writeObject(new OKResponse(true));
				cliente.getOutput().flush();
				cliente.mandarMsjPendientes();
			}
			else {
				out.writeObject(new OKResponse(false,"El usuario ya esta conectado."));
			}
		}
		else {
			out.writeObject(new OKResponse(false,"Usuario no registrado"));
			socket.close();
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
			cliente.getOutput().writeObject(new OKResponse(true));
			cliente.setEstado(false);
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
	
	/**
	 * Maneja el mensaje recibido de un cliente, verifica si el receptor esta conectado.
	 * Si el receptor esta conectado, se envia el mensaje al cliente correspondiente.
	 * Si el receptor no esta conectado, se guarda el mensaje pendiente en el cliente
	 * y se envia un snapshot de los mensajes pendientes a los servidores secundarios.
	 * @param mensaje Objeto Mensaje que contiene el nick del receptor y el cuerpo del mensaje
	 * 
	 */
	public void handleMensaje(Mensaje mensaje) throws IOException {
		String nickReceptor = mensaje.getNickReceptor();
		HandleCliente cliente = this.directorio.get(nickReceptor);
		
		if(cliente.getEstado()) {
			cliente.enviarMensaje(mensaje);
		}else {
			cliente.addMensajePendiente(mensaje);
		}
	}
	
	public void cerrarSocketServer() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
