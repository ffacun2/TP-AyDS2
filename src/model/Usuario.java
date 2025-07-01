package model;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cliente.ClienteAPI;
import config.ConfigEncriptado;
import controller.ControladorPrincipal;
import encriptacion.Encriptador;
import exceptions.ContactoRepetidoException;
import exceptions.ExtensionNotFoundException;
import persistencia.ContactoDeserializador;
import persistencia.ContactoSerializador;
import persistencia.MensajeDeserializador;
import persistencia.MensajeSerializador;
import persistencia.PersistenciaFactory;
import persistencia.json.JsonPersistenciaFactory;
import persistencia.txt.TxtPersistenciaFactory;
import persistencia.xml.XmlPersistenciaFactory;
import requests.DirectoriosResponse;
import requests.OKResponse;
import utils.Utils;

@SuppressWarnings("deprecation")
public class Usuario implements Observer {
	private String nickname;
	private ArrayList<Contacto> contactos;
	@JsonIgnore
	private ClienteAPI servidor;
	private ControladorPrincipal controlador;
	private PersistenciaFactory factory;
	private ContactoSerializador contactoSerializador;
	private MensajeSerializador mensajeSerializador;
	private Encriptador encriptador;
	private String claveEncriptado;
	
	
	
	@SuppressWarnings("exports")
	public Usuario(String nickname, ClienteAPI servidor,ControladorPrincipal controller) {
		this.nickname = nickname;
		this.servidor = servidor;
		this.servidor.addObserver(this);
		this.controlador = controller;
		this.contactos = new ArrayList<Contacto>();
	}
	
	public Usuario(String nickname) {
		this.nickname = nickname;
		this.contactos = new ArrayList<Contacto>();
	}

	public Usuario() {
	}
	
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public void setContactos(ArrayList<Contacto> contactos) {
		this.contactos = contactos;
	}

	public String getNickname() {
		return nickname;
	}

	public ArrayList<Contacto> getContactos() {
		return contactos;
	}
	
	@SuppressWarnings("exports")
	public void setControlador(ControladorPrincipal controlador) {
		this.controlador = controlador;
	}

	/**
	 * Crea y agrega un nuevo contacto a la agenda del usuario.
	 * Si este contacto no existe en la agenda, lo agrega y lo serializa.
	 * Si el contacto ya existe, lanza una excepción ContactoRepetidoException.
	 * 
	 * @param nickContacto : nick del usuario a agregar.
	 * 
	 * @throws ContactoRepetidoException : Si el contacto ya existe en la agenda del usuario.
	 */
	public void agregarContacto(String nickContacto) throws ContactoRepetidoException{
		for(Contacto aux: this.contactos) {
			if(aux.getNickname() == nickContacto) {
				throw new ContactoRepetidoException();
			}
		}
		Contacto nuevo = new Contacto(nickContacto);
		this.contactos.add(nuevo);
		this.contactoSerializador.serializar(nuevo);
	}
	
	/**
	 * Busca un contacto por su nickname en la agenda del usuario para obtener
	 * la referencia del mismo.
	 * 
	 * @param nickname : nick del contacto a buscar.
	 * 
	 * @return Contacto : El contacto encontrado o null si no existe.
	 */
	public Contacto obtenerContacto(String nickname) {
		int i = 0;
		
		while (i < this.contactos.size() && !this.contactos.get(i).getNickname().equals(nickname)) {
			i++;
		}
		if (i < this.contactos.size())
			return this.contactos.get(i);
		else
			return null;
	}
	
	/**
	 * Crea una nueva conversacion para el contacto especificado. Esta situacion
	 * se da cuando el usuario quiere iniciar una conversacion con un contacto
	 * de la agenda que no tiene una conversacion activa.
	 * 
	 * @param nickContacto : nickname del contacto proveniente de la agenda.
	 * 
	 * @return : true si se creo la conversacion, false si ya existia una conversacion activa.
	 * 
	 * @throws NullPointerException : Si el contacto es nulo, lanza una excepción.
	 */
	public boolean crearConversacion(String nickContacto) throws NullPointerException {
		Contacto contacto = obtenerContacto(nickContacto);
		
		if (contacto == null) 
			throw new NullPointerException("El contacto no puede ser nulo");
		
		if (contacto.getConversacion() == null) {
			contacto.setConversacion(new Conversacion());
			return true;
		}
		return false;
	}
	
	/**
	 * Inicia la configuracion del usuario, la encriptacion, la persistencia;
	 * 
	 * @param ext : extension del archivo de persistencia (json, xml, txt).
	 * @param clave : clave de encriptado del usuario.
	 * @param tecnicaEncriptado : tecnica de encriptado a utilizar (AES, DES, RSA).
	 * 
	 * @throws ExtensionNotFoundException : Si la extension no es valida, lanza una excepción.
	 * @throws Exception : Si hay un problema al inicializar la persistencia, lanza una excepción.
	 */
	public void iniciarSesion (String ext,String clave,String tecnicaEncriptado) throws ExtensionNotFoundException, Exception {
		inicializarPersistencia(ext);
		this.encriptador = new Encriptador();
		this.encriptador.setTecnica(ConfigEncriptado.getTecnicaEncriptado(tecnicaEncriptado));
		this.claveEncriptado = clave;
		this.servidor.setControladorListo();
	}
	
	
	
	/**
	 * Envia un mensaje al contacto. En caso de no poder establecer la conexión, lanza una exception. 
	 * En este metodo se crea el objeto mensaje, se encripta, se envia al servidor, se agrega a la 
	 * conversacion localmente y se serializa.
	 * 
	 * @param mensaje: Tiene que tener todos los campos declarados y validados. 
	 * @param contacto: Tiene que tener todos los campos declarados y validados. 
	 * 
	 * @throws UnknownHostException: No se puede conectar con el contacto. 
	 * @throws IOException: Hay un problema al escribir los datos en el stream.  
	 */
	public void enviarMensaje(String cuerpo, String nickContacto) throws UnknownHostException, IOException {
		Mensaje mensaje = new Mensaje(this.nickname,nickContacto,cuerpo);
		Mensaje mensajeEncriptado = this.encriptador.encriptarMensaje(mensaje, this.claveEncriptado);
		this.servidor.enviarRequest(mensajeEncriptado);
		this.obtenerContacto(nickContacto).agregarMensaje(mensaje);
		this.mensajeSerializador.serializar(mensaje);
		this.controlador.notificar(nickContacto);
	}
	
	
	/**
	 * Carga la persistencia del usuario. Si ya existe un archivo de persistencia, lo carga.
	 * Si no existe, crea un nuevo archivo de persistencia con la extension indicada tanto para 
	 * mensajes como para contactos.
	 * 
	 * @param ext : extension del archivo de persistencia (json, xml, txt).
	 * 
	 * @throws ExtensionNotFoundException : Si la extension no es valida, lanza una excepción.
	 * @throws Exception : Si hay un problema al inicializar la persistencia, lanza una excepción.
	 */
	public void inicializarPersistencia(String ext) throws ExtensionNotFoundException, Exception {
		Optional<String> extension = PersistenciaFactory.buscoArchivo(".", nickname);
		
		if (extension.isPresent()) {
			this.factory = persistenciaFactory(extension.get(), getNickname());
			if (this.factory == null)
				throw new ExtensionNotFoundException("La extension " + extension.get() + " no es valida");
			this.contactoSerializador = this.factory.crearContactoSerializador();
			this.mensajeSerializador = this.factory.crearMensajeSerializador();
			cargarPersistencia();
		}
		else {
			this.factory = persistenciaFactory(ext.toUpperCase(), getNickname());
			if (this.factory == null)
				throw new ExtensionNotFoundException("La extension " + ext + " no es valida");
			this.contactoSerializador = this.factory.crearContactoSerializador();
			this.mensajeSerializador = this.factory.crearMensajeSerializador();
			//Crear archivosss
			this.contactoSerializador.crearArchivoContacto();
			this.mensajeSerializador.crearArchivoMensaje();
		}
	}
	
	/**
	 * Se encarga de cargar la persistencia del usuario, es decir, los contactos y mensajes.
	 * Se deserializan los contactos y mensajes desde los archivos de persistencia.
	 */
	private void cargarPersistencia() {
		ContactoDeserializador contactoDeserializador = factory.crearContactoDeserializador();
		MensajeDeserializador mensajeDeserializador = factory.crearMensajeDeserializador();
		List<Contacto> contactosList = contactoDeserializador.deserializar();
		List<Mensaje> mensajes = mensajeDeserializador.deserializar();
		Map<String, Contacto> mapa = contactosList.stream().collect(Collectors.toMap(Contacto::getNickname,c -> c));
		
		//Le cargo los mensajes a los contactos
		for(Mensaje mensaje : mensajes) {
			Contacto contacto = mapa.get(mensaje.getNickReceptor());
			if (contacto == null)
				contacto = mapa.get(mensaje.getNickEmisor());
			if (contacto.getConversacion() == null)
				contacto.setConversacion(new Conversacion());
			contacto.agregarMensaje(mensaje);
		}
		setContactos(new ArrayList<>(mapa.values()));
		// Le seteo los botones de conversacion al usuario
		for(Contacto contacto : getContactos()) {
			if (contacto.getConversacion() != null) {
				this.controlador.agregarBotonConversacion(contacto.getNickname());
			}
		}
	}

	/**
	 * Crea una instancia de la persistencia correspondiente a la extension indicada.
	 * @param extension : extension del archivo de persistencia (json, xml, txt).
	 * @param nickname : nickname del usuario, para identificar el archivo de persistencia.
	 * @return : Una instancia de PersistenciaFactory correspondiente a la extension indicada.
	 */
	private PersistenciaFactory persistenciaFactory(String extension, String nickname) {
 		switch (extension.toUpperCase()) {
			case "JSON":
				return new JsonPersistenciaFactory("",nickname);
			case "XML":
				return new XmlPersistenciaFactory("",nickname);
			case "TXT":
				return new TxtPersistenciaFactory("",nickname);
		}
		return null;
 	}
	
	@Override
	public String toString() {
		return "Usuario {"+
					"nickname: "+nickname+
					"Contactos: ["+contactos+
					"]\n}\n";
	}
	
	/**
	 * Devuelve una lista con los nicknames de los contactos del usuario.
	 * 
	 * @return ArrayList<String> : Lista de nicknames de los contactos.
	 */
	public ArrayList<String> getAgenda(){
		Iterator<Contacto> it = this.contactos.iterator();
		ArrayList<String> listaNicknames = new ArrayList<String>();
		
		while (it.hasNext()) {
			listaNicknames.add(it.next().getNickname());
		}
		return listaNicknames;
	}

	/**
	 * Metodo que recibe un mensaje y lo desencripta, luego lo agrega a la conversacion del contacto.
	 * Si el contacto no existe, lo crea y lo agrega a la agenda del usuario.
	 * Luego serializa el mensaje y notifica al controlador.
	 * 
	 * @param mensaje : Mensaje recibido.
	 */
	public void mensajeRecibido(Mensaje mensaje) {
		boolean wasNull = false;
		Mensaje msjDesencriptado = this.encriptador.desencriptarMensaje(mensaje, claveEncriptado);
		Contacto contacto = this.obtenerContacto(mensaje.getNickEmisor());
		
		if (contacto == null) { //El contacto es nuevo
			wasNull = true;
			try {
				this.agregarContacto(mensaje.getNickEmisor());
				contacto = this.obtenerContacto(mensaje.getNickEmisor());
			}
			catch (ContactoRepetidoException e) {
				System.out.println("Error fatal");
			}
		}
		if (wasNull) {
			this.controlador.agregarConversacion(mensaje.getNickEmisor());
		}
		contacto.agregarMensaje(msjDesencriptado);
		this.mensajeSerializador.serializar(msjDesencriptado);
		this.controlador.notificar(mensaje.getNickEmisor());

	}

	/**
	 * El observable (clienteAPI) notifica al usuario (observador) que ha recibido un objeto.
	 * Si el objeto es un Mensaje, se procesa el mensaje recibido.
	 * Si el objeto es un String, se verifica si es una señal de reconexión y se intenta reconectar al servidor.
	 */
 	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof Mensaje) {
			this.mensajeRecibido((Mensaje)arg);
		}
		else if (arg instanceof String) {
			if (((String) arg).equals(Utils.RECONEXION)) {
				try {
					if (!this.servidor.reconectar(this.nickname)) {
						this.controlador.mostrarError("Conexion perdida");
					}
				} catch (IOException e) {
					this.controlador.mostrarError(e.getMessage());
				}
			}
		}
	}
 	
 	/**
 	 * Extablece una conexion con el servidor. Busca un servidor activo e
 	 * intenta comunicarse con el mismo, si no hay servidores activos, 
 	 * lanza una excepción IOException.
 	 * 
 	 * @throws UnknownHostException : No se puede conectar con el servidor.
 	 * @throws IOException : No se puede establecer la conexion con el servidor.
 	 */
 	public void conectarApi() throws UnknownHostException, IOException{
 		this.servidor = new ClienteAPI();
 		Integer puerto = this.servidor.getPuertoServidorActivo();
	
		if (puerto != null && puerto != -1) {
			this.servidor.iniciarApi(puerto);
			new Thread(this.servidor).start();
			this.servidor.addObserver(this);
		}
		else {
			this.servidor.setEstado(false);
			throw new IOException("No hay servidores activos");
		}
 	}
 	
 	public void cerrarSesion() throws IOException{
		this.servidor.setEstado(false);
		this.servidor.enviarRequestLogout(this.nickname);
 	}
 	
 	public ArrayList<String> getDirectorio() throws IOException{
 		this.servidor.enviarRequestDirectorio(this.nickname);
 		DirectoriosResponse res = (DirectoriosResponse)servidor.getResponse();
 		return res.getNicks();
 	}
 	
 	@SuppressWarnings("exports")
	public OKResponse registrarse() throws IOException{
 		this.servidor.enviarRequestRegistrar(nickname);
 		return (OKResponse)servidor.getResponse();
 	}
 	
 	@SuppressWarnings("exports")
	public OKResponse iniciarSesion() throws IOException{
 		this.servidor.enviarRequestLogin(nickname);
 		return (OKResponse)servidor.getResponse();
 	} 
 	
}
