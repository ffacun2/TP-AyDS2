package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

import model.Contacto;
import model.Conversacion;
import model.Mensaje;
import model.Usuario;
import utils.Utils;
import view.DialogSeleccionarContacto;
import view.VentanaPrincipal;


public class ControladorPrincipal implements ActionListener, Observer {
	
	private ControladorConfiguracion controladorConfiguracion;
	private VentanaPrincipal ventanaPrincipal;
	private DialogSeleccionarContacto dialogContactos;
	private Usuario usuario;
	private Contacto contactoActivo; //representa el contacto que tiene el chat abierto
	
	
	public ControladorPrincipal(ControladorConfiguracion controladorConfiguracion) {
		this.controladorConfiguracion = controladorConfiguracion;
	}
	
	/**
	 * Si el evento se obtiene del boton CREAR_CONTACTO, se abre la una ventana para ingresar los datos del contacto y crearlo.
	 * Si el evento se obtiene del boton CREAR_CONVERSACION, se muestra la lista de contactos y al seleccionar uno se abre el chat.
	 * Si el evento se obtiene del boton ENVIAR_MENSAJE, se llama al metodo enviarMensaje y se envia el mensaje al contacto seleccionado.
	 * Si el evento se obtiene del boton CONFIRMAR_CONTACTO, se llama al metodo
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		
		if (comando.equals(Utils.CREAR_CONTACTO)) {
			this.controladorConfiguracion.mostrarVentanaConfiguracion(Utils.TITULO_AGR_CONTACTO,Utils.MODO_AGR_CONTACTO);
			this.ventanaPrincipal.bloqueoAgrContacto(false); //Si quiero agregar un contacto pero despues me arrepiento se queda bloqueado el boton
		}
		else if (comando.equals(Utils.CREAR_CONVERSACION)) {
			
			//Llama a la ventada de dialog con los contactos
			this.dialogContactos = new DialogSeleccionarContacto(ventanaPrincipal, this, this.usuario.getContactos());
			this.dialogContactos.setVisible(true);	
		}
		else if ( comando.equals(Utils.ENVIAR_MENSAJE)) {
			if (!this.ventanaPrincipal.getMensaje().isEmpty()) {
				this.enviarMensaje(this.ventanaPrincipal.getMensaje());
			}
			
		}else if(comando.equals(Utils.CONFIRMAR_CONTACTO)){
			//Esto se llama desde el boton del dialog
			Contacto contacto = this.dialogContactos.getContactoElegido();
			this.dialogContactos.dispose();
			this.crearConversacion(contacto);
			
		}else if(comando.equals(Utils.MENSAJE)) {
			/**
			 * Cuando se apreta el boton de la conversacion se carga en el JTextArea
			 * la conversacion de ese contacto.
			 */
			
			JButton boton =(JButton) e.getSource();
			Contacto contacto = (Contacto) boton.getClientProperty("contacto"); //Devuelve el objeto Contacto asociado al boton
			this.contactoActivo = contacto;
			this.ventanaPrincipal.cargarConversacion(usuario.getNickname(), contacto);
			System.out.println(contacto.toString());
			System.out.println(this.usuario.getContactos().contains(contacto));
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
//		try {
			this.usuario = new Usuario(ip, puerto, nickname);	
			this.usuario.getServidor().setObservador(this);
			return true;
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
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
		
		
		//obtiene el contacto seleccionado y devuleve su conversacion. La ventana se encarga de mostrarla
		//tambien modificar el panel de contacto por si estaba como mensaje no leido
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
		this.usuario.agregarContacto(new Contacto(nickname, puerto, ip));
	}
	
	/**
	 * Crea una nueva conversacion con el contacto seleccionado
	 * @param contacto - contacto seleccionado de la lista de contactos
	 */
 	public void crearConversacion(Contacto contacto) {
 		Contacto contact = null;
 		if(contacto == null) {
			//mostrar error
		}else {
			if(contacto.getConversacion() == null) {
				contacto.setConversacion(new Conversacion());
				this.ventanaPrincipal.agregarNuevoBotonConversacion(contacto);
				this.ventanaPrincipal.setNuevaConversacion();
				this.contactoActivo = contacto; 
			}else {
				//mostrar error ya tiene conversacion, mostrar conversacion?
				System.out.println("ya tiene conversacion");
				this.ventanaPrincipal.cargarConversacion(usuario.getNickname(), contacto);
			}
		}
 	}
 	
 	/**
 	 *  Envia un mensaje al contacto activo, es decir, al contacto con el que se esta conversando.
 	 *  El mensaje sale del campo de texto.
 	 * @param mensaje - mensaje a enviar
 	 */
 	protected void enviarMensaje(String mensaje) {
 		Mensaje msjObj = new Mensaje(this.usuario.getNickname(),this.usuario.getPuerto(),this.usuario.getIp(),mensaje);
 		System.out.println("envio ("+mensaje+") a "+contactoActivo.toString());
		try {
			this.usuario.enviarMensaje(msjObj, contactoActivo);
			this.ventanaPrincipal.agregarMensaje(this.usuario.getNickname() + ": "+ mensaje);
		} catch (Exception exc) {
			Utils.mostrarError("No se ha podido enviar el mensaje.",this.ventanaPrincipal);
			exc.printStackTrace();
		}
 	}
 	
 	/**
 	 * Esta observando al servidor, si recibe un mensaje el servidor lo notifica y este metodo lo recibe.
 	 * En base a si el contacto que envio el mensaje existe o no, se crea una nueva conversacion o 
 	 * se agrega el mensaje a la conversacion existente.
 	 */
 	@Override
	public void update(Observable o, Object arg) {
		Mensaje mensaje = (Mensaje) arg;
		System.out.println("recibo de "+mensaje.getNickEmisor() + ": " + mensaje.getCuerpo());
		
		Contacto contacto = new Contacto( mensaje.getNickEmisor(), mensaje.getPuerto(),mensaje.getIp());
		List<Contacto> agenda = this.usuario.getContactos();
		int i = 0;
		
		if (agenda.contains(contacto)) {
			//Si el contacto existe, se agrega el mensaje a la conversacion y se Modifica el panel del contacto
			//Para avisar que tiene un nuevo mensaje
			while (!agenda.get(0).equals(contacto) && i < agenda.size()) {
				i++;
			}
			if (agenda.get(i).equals(contacto)) {
				agenda.get(i).getConversacion().agregarMensajeReceptor(mensaje);
				//this.ventanaPrincipal.modificarPanelContacto(contacto);
				if (this.contactoActivo != null && this.contactoActivo.equals(contacto)) {
					this.ventanaPrincipal.cargarConversacion(this.usuario.getNickname(), contacto);
				}
			}	
		}
		else {
			//Si el contacto no existe, se agrega a la lista de contactos y se crea una nueva conversacion
			System.out.println("Entra else update");
			this.usuario.agregarContacto(contacto);
			contacto.setConversacion(new Conversacion());
			contacto.getConversacion().agregarMensajeReceptor(mensaje);
			this.ventanaPrincipal.agregarNuevoBotonConversacion(contacto);
			
			System.out.println("recept conversacion se creo?: "+contacto.getConversacion() == null);
			
		}
	}
 	
 	
 	public void mostrarVentanaPrincipal() {
 		this.ventanaPrincipal = new VentanaPrincipal("Sistema de Mensajeria Instantanea");
 		this.ventanaPrincipal.setControlador(this);
 		this.ventanaPrincipal.setLocationRelativeTo(null);
 		this.ventanaPrincipal.setVisible(true);
 	}
 	
 	public void cerrarConfig() {
 		this.ventanaPrincipal.bloqueoAgrContacto(false);
 	}
 	
}