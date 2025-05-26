package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import cliente.ServidorAPI;
import exceptions.ContactoRepetidoException;
import model.Contacto;
import model.Conversacion;
import model.Mensaje;
import model.Usuario;
import persistencia.Persistencia;
import persistencia.PersistenciaJSON;
import persistencia.PersistenciaTXT;
import persistencia.factory.JsonPersistenciaFactory;
import persistencia.factory.PersistenciaFactory;
import persistencia.factory.TxtPersistenciaFactory;
import persistencia.factory.XmlPersistenciaFactory;
import requests.DirectoriosResponse;
import requests.OKResponse;
import requests.RequestDirectorio;
import requests.RequestLogin;
import requests.RequestLogout;
import utils.Utils;
import view.DialogSeleccionarContacto;
import view.VentanaPrincipal;


@SuppressWarnings("deprecation")
public class ControladorPrincipal implements ActionListener, Observer {
	
	private ControladorConfiguracion controladorConfiguracion;
	private VentanaPrincipal ventanaPrincipal;
	private DialogSeleccionarContacto dialogContactos;
	private Usuario usuario;
	private Contacto contactoActivo; //representa el contacto que tiene el chat abierto
	private ServidorAPI servidor;
	private PersistenciaFactory factory;
	private Persistencia persistencia;
	
	public ControladorPrincipal(ControladorConfiguracion controladorConfiguracion, ServidorAPI servidor) {
		this.controladorConfiguracion = controladorConfiguracion;
		this.servidor = servidor;
		this.servidor.addObserver(this);
		this.mostrarVentanaPrincipal();
	}
	
	public VentanaPrincipal getVentanaPrincipal() {
		return this.ventanaPrincipal;
	}
	
	/**
	 * Si el evento se obtiene del boton CREAR_CONTACTO, se abre la una ventana para ingresar los datos del contacto y crearlo.
	 * Si el evento se obtiene del boton CREAR_CONVERSACION, se muestra la lista de contactos y al seleccionar uno se abre el chat.
	 * Si el evento se obtiene del boton ENVIAR_MENSAJE, se llama al metodo enviarMensaje y se envia el mensaje al contacto seleccionado.
	 * Si el evento se obtiene del boton CONFIRMAR_CONTACTO, se agrega el contacto a la lista de contactos y se abre el chat.
	 * Si el evento se obtiene del boton MENSAJE, el obtiene la conversacion del contacto seleccionado y la muestra en el JTextArea.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		
		if (comando.equals(Utils.MOSTRAR_DIRECTORIO)) {
			try {
				this.mostrarDirectorio();
			} catch (ClassNotFoundException e1) {
				Utils.mostrarError(e1.getMessage(), ventanaPrincipal);
			}
		}
		else if (comando.equals(Utils.CREAR_CONVERSACION)) {
			//Llama a la ventada de dialog con los contactos
			this.dialogContactos = new DialogSeleccionarContacto(ventanaPrincipal, this, this.usuario.getContactos(), Utils.CREAR_CONVERSACION);
			this.dialogContactos.setVisible(true);	
		}
		else if ( comando.equals(Utils.ENVIAR_MENSAJE)) {
			if (!this.ventanaPrincipal.getMensaje().isEmpty()) {
				this.enviarMensaje(this.ventanaPrincipal.getMensaje());
				this.ventanaPrincipal.limpiarTxtField();
			}
			
		}else if(comando.equals(Utils.CONFIRMAR_CONTACTO)){
			//Esto se llama desde el boton del dialog
			Contacto contacto = this.dialogContactos.getContactoElegido();
			if (contacto == null) {
				Utils.mostrarError("Seleccione un contacto", this.ventanaPrincipal);
			}
			else {
				this.dialogContactos.dispose();
				this.crearConversacion(contacto);
			}			
			
		}else if(comando.equals(Utils.MENSAJE)) {			
			JButton boton =(JButton) e.getSource();
			Contacto contacto = (Contacto) boton.getClientProperty("contacto"); //Devuelve el objeto Contacto asociado al boton
			this.contactoActivo = contacto;
			boton.setText(contacto.getNickname());
			this.ventanaPrincipal.setBorder(boton, null);
			this.ventanaPrincipal.cargarConversacion(contactoActivo.getConversacion());
			this.ventanaPrincipal.bloquearMsj(false);

		}else if(comando.equals(Utils.AGREGAR_CONTACTO)) {
			Contacto contacto = this.dialogContactos.getContactoElegido();
				if (contacto == null) {
					Utils.mostrarError("Seleccione un contacto valido", this.ventanaPrincipal);
				}
				else {
					this.dialogContactos.dispose();
					this.agregarContacto(contacto);
				}
			this.ventanaPrincipal.bloqueoAgrContacto(false);
		}else if(comando.equals(Utils.MOSTRAR_AGENDA)) {
			this.dialogContactos = new DialogSeleccionarContacto(ventanaPrincipal, this, this.usuario.getContactos(), Utils.MOSTRAR_AGENDA);
			this.dialogContactos.setVisible(true);
		}
	}
	
	/**
	 *  Crea un nuevo usuario en la aplicacion, si este se crea correctamente se cierra
	 *  la ventana de configuracion y se abre la ventana principal.
	 *  Caso contrario se muestra un mensaje de error.
	 *  Quien determine si el usuario se crea correctamente es el cliente usuario que lanza error de socket.
	 *  
	 *  @param ip - ip del usuario
	 *  @param puerto - puerto del usuario
	 *  @param nickname - nombre del usuario
	 */
	public void crearUsuario(String ip, int puerto, String nickname, ServidorAPI servidor) {
			this.usuario = new Usuario(nickname, puerto, ip,servidor);
	}
	
	public void agregarContacto(Contacto contacto) {
		try {
			this.usuario.agregarContacto(contacto);
			this.persistencia.guardar( (persistencia instanceof PersistenciaTXT) ? contacto : this.usuario);
		}
		catch (ContactoRepetidoException e) {
			Utils.mostrarError("El contacto ya se encuentra agendado", this.controladorConfiguracion.getVentanaConfig());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	/**
	 * Crea un nuevo contacto en la lista de contactos
	 * 
	 * @param ip - ip del contacto
	 * @param puerto - numero del contacto
	 * @param nickname - nombre del contacto
	 * @throws ClassNotFoundException 
	 */
	public void mostrarDirectorio() throws ClassNotFoundException{	
		try {
			servidor.enviarRequest(new RequestDirectorio(this.usuario.getNickname()));
			DirectoriosResponse agenda = (DirectoriosResponse)this.servidor.getResponse();
			this.dialogContactos  = new DialogSeleccionarContacto(this.ventanaPrincipal, this, agenda.getNicks(), Utils.MODO_AGR_CONTACTO);
			this.dialogContactos.setVisible(true);
		} catch (IOException e) {
			try {
				Thread.sleep(2000);
			}
			catch(InterruptedException ie) {}
			try {
				if(this.servidor.getEstado())
					servidor.enviarRequest(new RequestDirectorio(this.usuario.getNickname()));
			}
			catch (Exception ex) {
			}
		}

	}
	
	/**
	 * Crea una nueva conversacion con el contacto seleccionado
	 * @param contacto - contacto seleccionado de la lista de contactos
	 */
 	public void crearConversacion(Contacto contacto) {
 		if(contacto == null) {
			Utils.mostrarError("No se selecciono ningun contacto", ventanaPrincipal);
		}else {
			if(contacto.getConversacion() == null) {
				contacto.setConversacion(new Conversacion());
				this.ventanaPrincipal.agregarNuevoBotonConversacion(contacto);
			}else {
				this.ventanaPrincipal.cargarConversacion(contacto.getConversacion());
			}
		}
 	}
 	
 	/**
 	 *  Envia un mensaje al contacto activo, es decir, al contacto con el que se esta conversando.
 	 *  El mensaje sale del campo de texto.
 	 * @param mensaje - mensaje a enviar
 	 */
 	protected void enviarMensaje(String mensaje) {
 		Mensaje msjObj = new Mensaje(this.usuario.getNickname(),this.contactoActivo.getNickname(),mensaje);
// 		System.out.println("Emisor: " + msjObj.getNickEmisor()+"\n" + "Receptor: " + msjObj.getNickReceptor());
			try {
				this.usuario.enviarMensaje(msjObj, contactoActivo);
				System.out.println(msjObj.toString());
				this.contactoActivo.agregarMensaje(msjObj);
				this.ventanaPrincipal.cargarConversacion(this.contactoActivo.getConversacion());
				this.persistencia.guardar( (persistencia instanceof PersistenciaTXT) ? msjObj : this.usuario);
			} catch (IOException e) {
				try {
					Thread.sleep(2000);
				}
				catch(InterruptedException ie) {}
				if(this.servidor.getEstado())
					enviarMensaje(mensaje);
			} catch (Exception e) {
				e.printStackTrace();
			}
 	}
 	
 	/**
 	 * Esta observando al servidor, si recibe un mensaje el servidor lo notifica y este metodo lo recibe.
 	 * En base a si el contacto que envio el mensaje existe o no, se crea una nueva conversacion o 
 	 * se agrega el mensaje a la conversacion existente.
 	 */
 	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof Mensaje) {
			this.cargarMensaje((Mensaje)arg);
		}
		else if (arg instanceof String) {
			if(((String)arg).equals(Utils.RECONEXION))
				this.reconectar();
		}
	}
 	
 	public void cargarMensaje(Mensaje mensaje) {
		Contacto contacto = new Contacto( mensaje.getNickEmisor());
		ArrayList<Contacto> agenda = this.usuario.getContactos();
		int i;

		//Si el contacto existe, se agrega el mensaje a la conversacion y se Modifica el panel del contacto
		//Para avisar que tiene un nuevo mensaje
		if (agenda.contains(contacto)) {
			i = agenda.indexOf(contacto);
			if(i != -1 && agenda.get(i) != null) {
				Contacto nuevo = agenda.get(i);
				if (nuevo.getConversacion() == null )
					this.crearConversacion(nuevo);
				nuevo.getConversacion().agregarMensaje(mensaje);
				
				if (this.contactoActivo != null && this.contactoActivo.equals(agenda.get(i))) {
					this.ventanaPrincipal.cargarConversacion(contactoActivo.getConversacion());
				}
				if (this.contactoActivo == null || (this.contactoActivo != null && !this.contactoActivo.equals(agenda.get(i)))) {
					this.ventanaPrincipal.notificacion(agenda.get(i));
				}
			
			}
		}
		else {
			//Si el contacto no existe, se agrega a la lista de contactos y se crea una nueva conversacion
			this.agregarContacto(contacto);
			contacto.setConversacion(new Conversacion());
			contacto.getConversacion().agregarMensaje(mensaje);
			this.ventanaPrincipal.agregarNuevoBotonConversacion(contacto);
			this.ventanaPrincipal.notificacion(contacto);
			this.ventanaPrincipal.bloqueoNueConv(false);
		}
		
		try {
			this.persistencia.guardar( (persistencia instanceof PersistenciaTXT) ? mensaje : this.usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
 	}
 	
 	public void setTitulo(String title) {
 		this.ventanaPrincipal.setTitle(title);
 	}
 	
 	public void mostrarVentanaPrincipal() {
 		this.ventanaPrincipal = new VentanaPrincipal();
 		this.ventanaPrincipal.setControlador(this);
 		this.ventanaPrincipal.setLocationRelativeTo(this.controladorConfiguracion.getVentanaConfig());
 		this.ventanaPrincipal.setVisible(true);
 		this.ventanaPrincipal.bloquearMsj(true);
 		this.ventanaPrincipal.bloqueoNueConv(false);
 	}
 	
 	public void cerrarConfig() {
 		this.ventanaPrincipal.bloqueoAgrContacto(false);
 	}
 	
 	public void cerrarSesion() {
 		try {
 			this.servidor.setEstado(false);
			this.servidor.enviarRequest(new RequestLogout(this.usuario.getNickname()));
		} catch (IOException e) {
//			this.reconectar();
		}
 	}
 	
 	public boolean reconectar() {
 		try {
 			int puerto = this.servidor.getPuertoServidorActivo();
 			this.servidor.setEstado(false);
 			
 			this.servidor = new ServidorAPI();
			this.servidor.iniciarApi("localhost", puerto);
			new Thread(this.servidor).start();
			this.usuario.setServidor(this.servidor);
			this.servidor.addObserver(this);
			this.servidor.setControladorListo();
			
			this.servidor.enviarRequest(new RequestLogin(this.usuario.getNickname()));
			OKResponse res = (OKResponse)this.servidor.getResponse();
			if (res.isSuccess())
				return true; //Deberia ser siempre true
			else {
				Utils.mostrarError(res.getMensajeError(), ventanaPrincipal);
				this.servidor.setEstado(false);
				return false;
			}
		} catch (IOException e) {
			Utils.mostrarError("Se perdio la conexion con los servidores", ventanaPrincipal);
			this.servidor.setEstado(false);
			this.ventanaPrincipal.dispose();
			return false;
		}
 	}
 	
 	
 	public void crearSesion(String ip, int puerto, String nickname, ServidorAPI servidor,String ext) {
 		Optional<String> extension = PersistenciaFactory.buscoArchivo(".", nickname);
 		if (extension.isPresent()) {
 			this.setPersistencia(extension.get(), nickname);
 			try {
				this.usuario = (Usuario) this.persistencia.cargar(Usuario.class);
				this.usuario.setServidor(servidor);
				
				for(Contacto contacto : this.usuario.getContactos()) {
					this.getVentanaPrincipal().agregarNuevoBotonConversacion(contacto);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
 		}
 		else {
 			this.crearUsuario(ip, puerto, nickname, servidor);
 			this.setPersistencia(ext, nickname);
 			try {
				this.persistencia.guardar(this.usuario);
			} catch (Exception e) {
				e.printStackTrace();
			}
 		}
 	}
 	
 	
 	public void setPersistencia(String extension, String nickname) {
 		switch (extension) {
			case "json":
				this.factory = new JsonPersistenciaFactory();
				this.persistencia = this.factory.crearSerializador(nickname+".json");
				break;
			case "xml":
				this.factory = new XmlPersistenciaFactory();
				this.persistencia = this.factory.crearSerializador(nickname+".xml");
				break;
			case "txt":
				this.factory = new TxtPersistenciaFactory();
				this.persistencia = this.factory.crearSerializador(nickname+".txt");
				break;
		}
 	}
 	
}