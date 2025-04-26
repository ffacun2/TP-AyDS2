package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

import api.ServidorAPI;
import exceptions.ContactoRepetidoException;
import model.Contacto;
import model.Conversacion;
import model.Mensaje;
import model.Usuario;
import requests.DirectoriosResponse;
import requests.RequestDirectorio;
import requests.RequestLogout;
import utils.Utils;
import view.DialogSeleccionarContacto;
import view.VentanaPrincipal;


public class ControladorPrincipal implements ActionListener, Observer {
	
	private ControladorConfiguracion controladorConfiguracion;
	private VentanaPrincipal ventanaPrincipal;
	private DialogSeleccionarContacto dialogContactos;
	private Usuario usuario;
	private Contacto contactoActivo; //representa el contacto que tiene el chat abierto
	private ServidorAPI servidor;
	
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
		
		if (comando.equals(Utils.CREAR_CONTACTO)) {
			try {
				this.crearContacto();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
			this.dialogContactos.dispose();
			this.crearConversacion(contacto);
			
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
			this.dialogContactos.dispose();
			try {
				this.usuario.agregarContacto(contacto);
			} catch (ContactoRepetidoException e1) {
				Utils.mostrarError("El contacto ya se encuentra agendado", ventanaPrincipal);
			}
			this.ventanaPrincipal.bloqueoAgrContacto(false);
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


	/**
	 * Crea un nuevo contacto en la lista de contactos
	 * 
	 * @param ip - ip del contacto
	 * @param puerto - numero del contacto
	 * @param nickname - nombre del contacto
	 * @throws ClassNotFoundException 
	 */
	public void crearContacto() throws ClassNotFoundException{	
		try {
			servidor.enviarRequest(new RequestDirectorio(this.usuario.getNickname()));
			DirectoriosResponse agenda = (DirectoriosResponse)this.servidor.getResponse();
			this.dialogContactos  = new DialogSeleccionarContacto(this.ventanaPrincipal, this, agenda.getNicks(), Utils.MODO_AGR_CONTACTO);
			this.dialogContactos.setVisible(true);
		} catch (IOException e) {
			Utils.mostrarError("No se pudo conectar al servidor", ventanaPrincipal);
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
 		System.out.println("Emisor: " + msjObj.getNickEmisor()+"\n" + "Receptor: " + msjObj.getNickReceptor());
			try {
				this.usuario.enviarMensaje(msjObj, contactoActivo);
				this.contactoActivo.agregarMensaje(msjObj);
				this.ventanaPrincipal.cargarConversacion(this.contactoActivo.getConversacion());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				Utils.mostrarError("Se perdio la conexion", ventanaPrincipal);
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
			try {
				this.usuario.agregarContacto(contacto);
				contacto.setConversacion(new Conversacion());
				contacto.getConversacion().agregarMensaje(mensaje);
				this.ventanaPrincipal.agregarNuevoBotonConversacion(contacto);
				this.ventanaPrincipal.notificacion(contacto);
				this.ventanaPrincipal.bloqueoNueConv(false);
			}
			catch (ContactoRepetidoException e) {
				Utils.mostrarError(e.getMessage(), this.controladorConfiguracion.getVentanaConfig());
			}
			
		}
 	}
 	
 	public void setTitulo(String title) {
 		this.ventanaPrincipal.setTitle(title);
 	}
 	
 	public void mostrarVentanaPrincipal() {
 		this.ventanaPrincipal = new VentanaPrincipal();
 		this.ventanaPrincipal.setControlador(this);
 		this.ventanaPrincipal.setLocationRelativeTo(null);
 		this.ventanaPrincipal.setVisible(true);
 		this.ventanaPrincipal.bloquearMsj(true);
 		this.ventanaPrincipal.bloqueoNueConv(false);
 	}
 	
 	public void cerrarConfig() {
 		this.ventanaPrincipal.bloqueoAgrContacto(false);
 	}
 	
 	public void cerrarSesion() {
 		try {
 			System.out.println("se cierra sesion");
 			this.servidor.setEstado(false);
			this.servidor.enviarRequest(new RequestLogout(this.usuario.getNickname()));
		} catch (IOException e) {
			Utils.mostrarError("Se perdio la conexion con el servidor", ventanaPrincipal);
		}
 	}
 	
 	
 	
}