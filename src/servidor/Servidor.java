package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import controller.ControladorServidor;
import interfaces.IEnviable;
import interfaces.IServidor;
import model.Contacto;
import model.Mensaje;
import requests.OKResponse;
import requests.Pulso;
import requests.RequestDirectorio;
import requests.RequestLogin;
import requests.RequestLogout;
import requests.RequestRegistro;
import requests.Sync;

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
	private ControladorServidor controlador;
	private Resincronizacion resincronizacion; //Encargada de enviar la peticion a los servidores secundarios
	
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
			e.printStackTrace();
		}
		finally {
			this.cerrarSocketServer();
			this.resincronizacion.setEstado(false); //Detiene la resincronizacion
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
			
			resincronizacion.distribuirPeticion(new Sync(generarSnapShot()));
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
			System.out.println(mensaje.toString());
			this.controlador.mostrarMensaje(mensaje.getCuerpo());
			cliente.enviarMensaje(mensaje);
		}else {
			cliente.addMensajePendiente(mensaje);
			resincronizacion.distribuirPeticion(new Sync(generarSnapShot()));
		}
	}
	
	
	/**
	 * Maneja el pulso de tipo PING, enviando un PONG de vuelta al monitor.
	 * Este metodo se utiliza para verificar que el servidor esta activo y
	 * responde a las peticiones de los clientes.
	 * 
	 * @param pulso Objeto Pulso que contiene el mensaje PING
	 * @param socket Socket de conexion entre servidor y monitor
	 * @throws IOException Si se pierde la conexion
	 */
	public void handleHeartBeat(Pulso pulso,Socket socket) throws IOException {
		if (pulso.getMensaje().equals("PING")) {
			if (this.resincronizacion == null) {
				this.resincronizacion = new Resincronizacion(this.puerto,this);
				new Thread(this.resincronizacion).start();
				
				for (String cliente : this.directorio.keySet()) {
					System.out.println(cliente);
				}
			}
			out.writeObject(new Pulso("PONG"));
			out.flush();
		}
	}
	
	/**
	 * Genera una lista de los clientes registrados en el servidor, para enviarlo a
	 * los servidores secundarios y mantener la sincronizacion.
	 * @return Una lista de objetos HandleClienteDTO que contienen el nick, mensajes pendientes y estado de cada cliente.
	 */
	public List<HandleClienteDTO> generarSnapShot() {
		List<HandleClienteDTO> snapshot = new ArrayList<>();
		
		for ( Map.Entry<String, HandleCliente> elemento : this.directorio.entrySet()) {
			String nick = elemento.getKey();
			HandleCliente cliente = elemento.getValue();
			
			HandleClienteDTO dto = new HandleClienteDTO(nick,cliente.getMensajesPendientes(),cliente.getEstado());
			snapshot.add(dto);
		}
		return snapshot;
	}
	
	
	/**
	 * Guarda la lista de clientes y sus mensajes pendientes en el directorio del servidor.
	 * Esta lista es enviada por el servidor activo a los servidores secundarios.
	 * @param snapshot
	 */
	public void guardarSnapShot(List<HandleClienteDTO> snapshot) {
		System.out.println("snaphost");
		for (HandleClienteDTO elemento : snapshot) {
			HandleCliente cliente = new HandleCliente(elemento.getMensajesPendientes(),this);
			this.directorio.put(elemento.getNickName(), cliente);
		}
	}
	
	public void cerrarSocketServer() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setControlador(ControladorServidor controlador) {
		this.controlador = controlador;
	}

}
