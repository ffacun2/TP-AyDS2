package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import model.Contacto;
import model.Conversacion;
import model.Mensaje;
import model.Usuario;
import utils.Utils;
import view.VentanaPrincipal;


public class ControladorPrincipal implements ActionListener, Observer {
	
	private ControladorConfiguracion controladorConfiguracion;
	private VentanaPrincipal ventanaPrincipal;
	private Usuario usuario;
	private Contacto contactoActivo; //representa el contacto que tiene el chat abierto
	
	
	public ControladorPrincipal() {
		//TODO Implementar
		this.controladorConfiguracion = new ControladorConfiguracion();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		
		if (comando.equals(Utils.CREAR_CONTACTO)) {
			this.controladorConfiguracion.mostrarVentanaConfiguracion(Utils.TITULO_AGR_CONTACTO,Utils.MODO_AGR_CONTACTO);
			this.ventanaPrincipal.bloqueoAgrContacto(true); //Si quiero agregar un contacto pero despues me arrepiento se queda bloqueado el boton
			//TODO terminar de implementar la creacion de contactos
		}
		else if (comando.equals(Utils.CREAR_CONVERSACION)) {
			//Se abre una ventana con la lista de contactos ??
		}
		else if ( comando.equals(Utils.ENVIAR_MENSAJE)) {
			if (!this.ventanaPrincipal.getMensaje().isEmpty()) {
				Mensaje mensaje = new Mensaje(this.usuario.getNickname(),this.usuario.getPuerto(),this.usuario.getIp(),this.ventanaPrincipal.getMensaje());
				try {
					this.usuario.enviarMensaje(mensaje, contactoActivo);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
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
	public boolean crearUsuario(String ip, int puerto, String nickname) {
		try {
			this.usuario = new Usuario(ip, puerto, nickname);	
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	
	/**
	 * Obtiene la conversacion que se ha seleccionado para abrir el chat
	 * 
	 * @return Conversacion - devuelve la conversacion del contacto seleccionado.
	 */
	public Conversacion mostrarConversacionSeleccionada() {	
		//ventanaPrincipal.getContactoSeleccionado();
		
		//Al seleccionar un contacto, que me devuelve? objeto contacto?
		//Se puede hacer que los contactos sean botones, cuando se apreta el controlador le devuelve el contacto a la ventana y se actualiza -G
		return null;
	}
	
	/**
	 * Crea un nuevo contacto en la lista de contactos
	 * 
	 * @param ip - ip del contacto
	 * @param puerto - numero del contacto
	 * @param nickname - nombre del contacto
	 */
	public void crearContacto(String ip, int puerto, String nickname) {
		//Tengo que validar que el contacto exista? es decir, que el socket este abierto?
		//Lo deberia validar el constructor del contacto, si no se puede construir que tire una excepcion y que se catchee aca, y se muestra el mensaje de  -G
		this.usuario.agregarContacto(new Contacto(ip, puerto, nickname));
	}
	
	/**
	 * Crea una nueva conversacion con el contacto seleccionado
	 * @param contacto - contacto seleccionado de la lista de contactos
	 */
 	public void crearConversacion(Contacto contacto) {
 		Contacto contact = null;
 		if (this.usuario.getContactos().contains(contacto) ) {
 			for (Contacto c : this.usuario.getContactos()) {
 				if (c.equals(contacto)) {
 					contact = c;
 				}
 			}
 		//contact.createConversacion();
 		}
 	}
 	
 	@Override
	public void update(Observable o, Object arg) {
		Mensaje mensaje = (Mensaje) arg;
		System.out.println(mensaje.getNickEmisor() + ": " + mensaje.getCuerpo());
		
		Contacto contacto = new Contacto(mensaje.getIp(), mensaje.getPuerto(), mensaje.getNickEmisor());
		if (this.usuario.getContactos().contains(contacto)) {
			//Si el contacto existe, se agrega el mensaje a la conversacion y se Modifica el panel del contacto
			//Para avisar que tiene un nuevo mensaje
		}
		else {
			//Si el contacto no existe, se agrega a la lista de contactos y se crea una nueva conversacion
			this.usuario.agregarContacto(contacto);
			//this.crearConversacion(contacto);
		}
	}
 	
 	
 	public void mostrarVentanaPrincipal() {
 		this.ventanaPrincipal = new VentanaPrincipal("Sistema de Mensajeria Instantanea");
 		this.ventanaPrincipal.setLocationRelativeTo(null);
 		this.ventanaPrincipal.setVisible(true);
 	}
 	
 	public void cerrarConfig() {
 		this.ventanaPrincipal.bloqueoAgrContacto(false);
 	}
}