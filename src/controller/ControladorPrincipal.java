package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

import cliente.ClienteAPI;
import exceptions.ContactoRepetidoException;
import model.Usuario;
import utils.Utils;
import view.DialogSeleccionarContacto;
import view.VentanaPrincipal;


public class ControladorPrincipal implements ActionListener {
	
	private VentanaPrincipal ventanaPrincipal;
	private DialogSeleccionarContacto dialogContactos;
	private Usuario usuario;
	
	public ControladorPrincipal(Usuario usuario) {
		this.usuario = usuario;
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
			this.mostrarDirectorio();
		}
		else if (comando.equals(Utils.CREAR_CONVERSACION)) {
			//Llama a la ventada de dialog con los contactos
			mostrarDialogContactos(ventanaPrincipal, this, this.usuario.getAgenda(), Utils.CREAR_CONVERSACION);
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
				this.agregarConversacion(nickContacto);
				this.ventanaPrincipal.setNickActivo(nickContacto);
				this.ventanaPrincipal.cargarConversacion(this.usuario.obtenerContacto(nickContacto).getConversacion());
				this.ventanaPrincipal.bloquearMsj(false);
			}			
		}else if(comando.equals(Utils.SELEC_CONVERSACION)) {
			//Cuando el usuario apreta el boton de una conversacion
			JButton boton =(JButton) e.getSource();
			String nickContacto = (String) boton.getClientProperty("nickname"); //Devuelve el objeto Contacto asociado al boton
			boton.setText(nickContacto);
			this.ventanaPrincipal.setNickActivo(nickContacto);
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
			mostrarDialogContactos(ventanaPrincipal, this, this.usuario.getAgenda(), Utils.MOSTRAR_AGENDA);
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
	public void crearUsuario(String nickname, ClienteAPI servidor) {
			this.usuario = new Usuario(nickname, servidor,this);
	}
	
	public void agregarContacto(String nickContacto) {
		try {
			this.usuario.agregarContacto(nickContacto);
		}
		catch (ContactoRepetidoException e) {
			Utils.mostrarError("El contacto ya se encuentra agendado", this.ventanaPrincipal);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void mostrarDirectorio() {	
		ArrayList<String> nicks;
		try {
			nicks = this.usuario.getDirectorio();
			mostrarDialogContactos(this.ventanaPrincipal,this, nicks, Utils.MODO_AGR_CONTACTO);
		} 
		catch (Exception ex) {
			Utils.mostrarError(ex.getMessage(), ventanaPrincipal);
		}
	}
	
	/**
	 * Crea una nueva conversacion con el contacto seleccionado
	 * @param contacto - contacto seleccionado de la lista de contactos
	 */
 	public void agregarConversacion(String nickContacto) {
 		try {
 			if (this.usuario.crearConversacion(nickContacto))
 				agregarBotonConversacion(nickContacto);
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

 	
 	public void notificar(String nickname) {
 		if (nickname.equals(this.ventanaPrincipal.getNickActivo()))
 			this.ventanaPrincipal.cargarConversacion(this.usuario.obtenerContacto(nickname).getConversacion());
 		else
 			this.ventanaPrincipal.notificacion(nickname);
 	}
 	
 	public void agregarBotonConversacion(String nickname) {
 		this.ventanaPrincipal.agregarNuevoBotonConversacion(nickname);
 	}
 	
 	public void setTitulo(String title) {
 		this.ventanaPrincipal.setTitle(title);
 	}
 	
 	public void mostrarVentanaPrincipal(JFrame ventana) {
 		this.ventanaPrincipal = new VentanaPrincipal();
 		this.ventanaPrincipal.setControlador(this);
 		this.ventanaPrincipal.setLocationRelativeTo(ventana);
 		this.ventanaPrincipal.setVisible(true);
 		this.ventanaPrincipal.bloquearMsj(true);
 		this.ventanaPrincipal.bloqueoNueConv(false);
 	}
 	
 	public void cerrarConfig() {
 		this.ventanaPrincipal.bloqueoAgrContacto(false);
 	}
 	

 	public void cerrarSesion() {
 		try {
 			this.usuario.cerrarSesion();
		} catch (IOException e) {
			Utils.mostrarError(e.getMessage(), ventanaPrincipal);
		}
 	}
 	
 	public void mostrarError(String mensajeError) {
 		Utils.mostrarError(mensajeError, this.ventanaPrincipal);
 		this.ventanaPrincipal.dispose();
 	}
 	
 	public void mostrarDialogContactos(VentanaPrincipal ventana, ControladorPrincipal controlador, ArrayList<String> nicks, String modo) {
 		this.dialogContactos = new DialogSeleccionarContacto(ventana, controlador, nicks, modo);
 		this.dialogContactos.setVisible(true);
 	}
 	
}