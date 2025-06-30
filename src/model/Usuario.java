package model;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cliente.ServidorAPI;
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

public class Usuario {
	private String nickname;
	private ArrayList<Contacto> contactos;
	@JsonIgnore
	private ServidorAPI servidor;
	
	private PersistenciaFactory factory;
	private ContactoSerializador contactoSerializador;
	private MensajeSerializador mensajeSerializador;
	private Encriptador encriptador;
	private String claveEncriptado;
	
	
	
	@SuppressWarnings("exports")
	public Usuario(String nickname, ServidorAPI servidor) {
		this.nickname = nickname;
		this.servidor = servidor;
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

	public void agregarContacto(Contacto contacto) throws ContactoRepetidoException{
		for(Contacto aux: this.contactos) {
			if(aux.equals(contacto)) {
				throw new ContactoRepetidoException();
			}
		}
		this.contactos.add(contacto);
		this.contactoSerializador.serializar(contacto);
	}
	
	public boolean crearConversacion(Contacto contacto) throws NullPointerException {
		if (contacto == null) 
			throw new NullPointerException("El contacto no puede ser nulo");
		
		if (contacto.getConversacion() == null) {
			contacto.setConversacion(new Conversacion());
			return true;
		}
		return false;
	}

	public Contacto cargarMensaje(Mensaje mensaje) throws ContactoRepetidoException {
		Contacto contacto = new Contacto(mensaje.getNickEmisor());
		Contacto nuevo = null;
		ArrayList<Contacto> agenda = getContactos();
		int i;
		//La comparacion se hace por nombre y no por referencia
		if (agenda.contains(contacto)) {
			i = agenda.indexOf(contacto);
			if (i != -1 && agenda.get(i) != null) {
				//obtengo el objeto con la referencia correcta
				nuevo = agenda.get(i);
				if (nuevo.getConversacion() == null)
					crearConversacion(nuevo);
				nuevo.getConversacion().agregarMensaje(mensaje);
			}
			return nuevo;
		}
		else {
			agregarContacto(contacto);
			crearConversacion(contacto);
			contacto.getConversacion().agregarMensaje(mensaje);
			return contacto;
		}
	}
	
	
	public void iniciarSesion (String ext) throws ExtensionNotFoundException {
		inicializarPersistencia(ext);
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
	public void enviarMensaje(Mensaje mensaje) throws UnknownHostException, IOException {
		Mensaje mensajeEncriptado = this.encriptador.encriptarMensaje(mensaje, this.claveEncriptado);
		this.servidor.enviarRequest(mensajeEncriptado);
		this.mensajeSerializador.serializar(mensaje);
	}
	
	
	public void inicializarPersistencia(String ext) throws ExtensionNotFoundException {
		Optional<String> extension = PersistenciaFactory.buscoArchivo(".", nickname);
		
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
//				this.getVentanaPrincipal().agregarNuevoBotonConversacion(contacto);
				if (!contacto.isVisto()) {
//					this.ventanaPrincipal.notificacion(contacto);
				}
			}
		}
	}

	private PersistenciaFactory persistenciaFactory(String extension, String nickname) {
 		switch (extension) {
			case "JSON":
				return new JsonPersistenciaFactory(".",nickname);
			case "XML":
				return new XmlPersistenciaFactory(".",nickname);
			case "TXT":
				return new TxtPersistenciaFactory(".",nickname);
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
	
	
	
}
