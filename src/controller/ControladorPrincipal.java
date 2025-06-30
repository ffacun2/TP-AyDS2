package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

import cliente.ServidorAPI;
import exceptions.ContactoRepetidoException;
import exceptions.ExtensionNotFoundException;
import model.Contacto;
import model.Mensaje;
import model.Usuario;
import requests.DirectoriosResponse;
import requests.OKResponse;
import requests.RequestFactory;
import utils.Utils;
import view.DialogSeleccionarContacto;
import view.VentanaPrincipal;


@SuppressWarnings("deprecation")
public class ControladorPrincipal implements ActionListener, Observer {
	
	private ControladorConfiguracion controladorConfiguracion;
	private VentanaPrincipal ventanaPrincipal;
	private DialogSeleccionarContacto dialogContactos;
	private Usuario usuario; //vuela
	private ServidorAPI servidor;
	private RequestFactory reqFactory; //vuela
	
	public ControladorPrincipal(ControladorConfiguracion controladorConfiguracion, ServidorAPI servidor) {
		this.controladorConfiguracion = controladorConfiguracion;
		this.servidor = servidor;
		this.servidor.addObserver(this);
		this.reqFactory = new RequestFactory();
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
			this.dialogContactos = new DialogSeleccionarContacto(ventanaPrincipal, this, this.usuario.getDirectorio(), Utils.CREAR_CONVERSACION);
			this.dialogContactos.setVisible(true);	
		}
		else if ( comando.equals(Utils.ENVIAR_MENSAJE)) {
			if (!this.ventanaPrincipal.getMensaje().isEmpty()) {
				this.enviarMensaje(this.ventanaPrincipal.getMensaje());
				this.ventanaPrincipal.limpiarTxtField();
			}
		}else if(comando.equals(Utils.CONFIRMAR_CONTACTO)){
			//Esto se llama desde el boton del dialog
			String nickContacto = this.dialogContactos.getContactoElegido();
			if (nickContacto == null) {
				Utils.mostrarError("Seleccione un contacto", this.ventanaPrincipal);
			}
			else {
				this.dialogContactos.dispose();
				this.crearConversacion(nickContacto);
				this.ventanaPrincipal.setNickActivo(nickContacto);
				this.ventanaPrincipal.cargarConversacion(this.usuario.obtenerContacto(nickContacto).getConversacion());
				this.ventanaPrincipal.bloquearMsj(false);
			}			
		}else if(comando.equals(Utils.SELEC_CONVERSACION)) {
			//Cuando el usuario apreta el boton de una conversacion
			JButton boton =(JButton) e.getSource();
			String nickContacto = (String) boton.getClientProperty("nickname"); //Devuelve el objeto Contacto asociado al boton
			boton.setText(nickContacto);
			this.ventanaPrincipal.setBorder(boton, null);
			this.ventanaPrincipal.cargarConversacion(this.usuario.obtenerContacto(nickContacto).getConversacion());
			this.ventanaPrincipal.bloquearMsj(false);

		}else if(comando.equals(Utils.AGREGAR_CONTACTO)) {
			String nickContacto = this.dialogContactos.getContactoElegido();
				if (nickContacto == null) {
					Utils.mostrarError("Seleccione un contacto valido", this.ventanaPrincipal);
				}
				else {
					this.dialogContactos.dispose();
					this.agregarContacto(nickContacto);
				}
			this.ventanaPrincipal.bloqueoAgrContacto(false);
		}else if(comando.equals(Utils.MOSTRAR_AGENDA)) {
			this.dialogContactos = new DialogSeleccionarContacto(ventanaPrincipal, this, this.usuario.getDirectorio(), Utils.MOSTRAR_AGENDA);
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
	public void crearUsuario(String nickname, ServidorAPI servidor) {
			this.usuario = new Usuario(nickname, servidor);
	}
	
	public void agregarContacto(String nickContacto) {
		try {
			this.usuario.agregarContacto(nickContacto);
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
			servidor.enviarRequest(this.reqFactory.getRequest(Utils.ID_DIRECTORIO, usuario.getNickname()));
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
					servidor.enviarRequest(this.reqFactory.getRequest(Utils.ID_DIRECTORIO, usuario.getNickname()));
			}
			catch (Exception ex) {
			}
		}

	}
	
	/**
	 * Crea una nueva conversacion con el contacto seleccionado
	 * @param contacto - contacto seleccionado de la lista de contactos
	 */
 	public void crearConversacion(String nickContacto) {
 		try {
 			if (this.usuario.crearConversacion(nickContacto))
 				this.ventanaPrincipal.agregarNuevoBotonConversacion(nickContacto);
 			else
				this.ventanaPrincipal.cargarConversacion(this.usuario.obtenerContacto(nickContacto).getConversacion());
 		}
 		catch(NullPointerException e) {
 			Utils.mostrarError("No se selecciono ningun contacto", ventanaPrincipal);
 		}
 		catch (Exception e) {
 			e.printStackTrace();
 		}
 	}
 	
 	/**
 	 *  Envia un mensaje al contacto activo, es decir, al contacto con el que se esta conversando.
 	 *  El mensaje sale del campo de texto.
 	 * @param mensaje - mensaje a enviar
 	 */
 	protected void enviarMensaje(String cuerpo) {
		try {
			this.usuario.enviarMensaje(cuerpo,this.ventanaPrincipal.getNickActivo());
			this.ventanaPrincipal.cargarConversacion(this.usuario.obtenerContacto(this.ventanaPrincipal.getNickActivo()).getConversacion());
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
 	
 	/**
 	 * Carga un mensaje en la conversacion del contacto activo.
 	 * Si el contacto no existe, lo agrega a la lista de contactos y crea una nueva conversacion.
 	 * Si el contacto existe, agrega el mensaje a la conversacion y modifica el panel del contacto para avisar que tiene un nuevo mensaje.
 	 * 
 	 * @param mensaje - mensaje a cargar
 	 */
 	public void cargarMensaje(Mensaje mensaje) {
 		try {
 			String contacto = this.usuario.cargarMensaje(mensaje);
 			String contactoActivo = this.ventanaPrincipal.getNickActivo();
 			
			if (contactoActivo != null && contactoActivo == contacto) 
				this.ventanaPrincipal.cargarConversacion(this.usuario.obtenerContacto(contactoActivo).getConversacion());
			if (contactoActivo == null || (contactoActivo != null && contactoActivo != contacto)) 
				this.ventanaPrincipal.notificacion(contacto);
		}
		catch (ContactoRepetidoException e) {
			Utils.mostrarError("El contacto ya se encuentra agendado", this.ventanaPrincipal);
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
 	
 	/**
 	 * Cierra la sesion del usuario, es decir, envia un request de logout al servidor y cierra la ventana principal.
 	 * Si el request es exitoso, se cierra la ventana principal y se setea el estado del servidor a false.
 	 * Si no es exitoso, se muestra un mensaje de error.
 	 */
 	public void cerrarSesion() {
 		try {
 			this.servidor.setEstado(false);
			servidor.enviarRequest(this.reqFactory.getRequest(Utils.ID_LOGOUT, usuario.getNickname()));
		} catch (IOException e) {
//			this.reconectar();
		}
 	}
 	
 	/**
 	 * Reconecta al servidor, se usa cuando se pierde la conexion con el servidor.
 	 * Crea un nuevo servidor y lo inicia en el mismo puerto que estaba antes.
 	 * Luego envia un request de login al servidor con el nickname del usuario.
 	 * Si el request es exitoso, devuelve true, caso contrario devuelve false.
 	 * 
 	 * @return true si la reconexion fue exitosa, false si no lo fue.
 	 */
 	public boolean reconectar() {
 		try {
 			int puerto = this.servidor.getPuertoServidorActivo();
 			if(puerto == -1)
 				throw new IOException();
 			
 			this.servidor.setEstado(false);
 			
 			this.servidor = new ServidorAPI();
			this.servidor.iniciarApi(puerto);
			new Thread(this.servidor).start();
			this.usuario.setServidor(this.servidor);
			this.servidor.addObserver(this);
			this.servidor.setControladorListo();
			
			servidor.enviarRequest(this.reqFactory.getRequest(Utils.ID_LOGIN, usuario.getNickname()));
			OKResponse res = (OKResponse)this.servidor.getResponse();
			if (res.isSuccess())
				return true; //Deberia ser siempre true
			else {
				Utils.mostrarError(res.getMensajeError(), ventanaPrincipal);
				this.servidor.setEstado(false);
				this.ventanaPrincipal.dispose();
				return false;
			}
		} catch (IOException e) {
			Utils.mostrarError("Se perdio la conexion con los servidores", ventanaPrincipal);
			this.servidor.setEstado(false);
			this.ventanaPrincipal.dispose();
			return false;
		}
 	}
 	
 	/**
 	 * * Este metodo se llama al iniciar la sesion, y se encarga de cargar los contactos y conversaciones del usuario.
 	 * * Si el archivo existe, se carga la informacion del usuario desde el archivo.
 	 * * Si el archivo no existe, se crea un nuevo archivo con la extension indicada.
 	 *
 	 * @param nickname - nombre del usuario
 	 * @param servidor - servidor al que se conecta el usuario
 	 * @param ext - extension del archivo de persistencia (json, xml, txt)
 	 */
 	public void crearSesion(String nickname, ServidorAPI servidor,String ext, String clave, String tecnicaEncriptado) {
 		try {
 			crearUsuario(nickname, servidor);
			this.usuario.iniciarSesion(ext,clave,tecnicaEncriptado);
		} 
 		catch (ExtensionNotFoundException e) {
			e.printStackTrace();
		}
 		catch (Exception e) {
 			e.printStackTrace();
 		}
 	}
 	
}