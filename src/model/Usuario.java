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

import cliente.ServidorAPI;
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
import utils.Utils;

@SuppressWarnings("deprecation")
public class Usuario implements Observer{
	private String nickname;
	private ArrayList<Contacto> contactos;
	@JsonIgnore
	private ServidorAPI servidor;
	private ControladorPrincipal controlador;
	private PersistenciaFactory factory;
	private ContactoSerializador contactoSerializador;
	private MensajeSerializador mensajeSerializador;
	private Encriptador encriptador;
	private String claveEncriptado;
	
	
	
	@SuppressWarnings("exports")
	public Usuario(String nickname, ServidorAPI servidor,ControladorPrincipal controller) {
		this.nickname = nickname;
		this.servidor = servidor;
		this.servidor.addObserver(this);
		this.controlador = controller;
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
	public void setServidor(ServidorAPI server) {
		this.servidor = server;
	}

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
	
	public void iniciarSesion (String ext,String clave,String tecnicaEncriptado) throws ExtensionNotFoundException, Exception {
		inicializarPersistencia(ext);
		this.encriptador = new Encriptador();
		this.encriptador.setTecnica(ConfigEncriptado.getTecnicaEncriptado(tecnicaEncriptado));
		this.claveEncriptado = clave;
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
	public void enviarMensaje(String cuerpo, String nickContacto) throws UnknownHostException, IOException {
		Mensaje mensaje = new Mensaje(this.nickname,nickContacto,cuerpo);
		Mensaje mensajeEncriptado = this.encriptador.encriptarMensaje(mensaje, this.claveEncriptado);
		this.servidor.enviarRequest(mensajeEncriptado);
		this.obtenerContacto(nickContacto).agregarMensaje(mensaje);
		this.mensajeSerializador.serializar(mensaje);
		this.controlador.notificar(nickContacto);
	}
	
	
	public void inicializarPersistencia(String ext) throws ExtensionNotFoundException, Exception {
		Optional<String> extension = PersistenciaFactory.buscoArchivo("", nickname);
		
		if (extension.isPresent()) {
			this.factory = persistenciaFactory(extension.get(), getNickname());
			if (this.factory == null)
				throw new ExtensionNotFoundException("La extension " + ext + " no es valida");
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

	private PersistenciaFactory persistenciaFactory(String extension, String nickname) {
 		switch (extension) {
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
	
	public ArrayList<String> getAgenda(){
		Iterator<Contacto> it = this.contactos.iterator();
		ArrayList<String> listaNicknames = new ArrayList<String>();
		
		while (it.hasNext()) {
			listaNicknames.add(it.next().getNickname());
		}
		return listaNicknames;
	}

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

 	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof Mensaje) {
			this.mensajeRecibido((Mensaje)arg);
		}
		else if (arg instanceof String) {
			if(((String)arg).equals(Utils.RECONEXION))
				try {
//					this.servidor.setEstado(false);
					if (this.servidor.reconectar(this.nickname)) {
					}
					else {
						this.servidor.setEstado(false);
						this.controlador.mostrarError("Conexion perdida");
					}
				} catch (IOException e) {
					this.controlador.mostrarError(e.getMessage());
				}
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
}
