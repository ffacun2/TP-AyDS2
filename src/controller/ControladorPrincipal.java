package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Contacto;
import model.Conversacion;


public class ControladorPrincipal implements ActionListener {
	
	private VentanaPrincipal ventanaPrincipal;
	private Usuario usuario;
	private Contacto contactoActivo; //representa el contacto que tiene el chat abierto
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		
		if (comando.equals("CREAR_CONTACTO")) {
			
		}
		else if (comando.equals("CREAR_CONVERSACION")) {
			
		}
		else if ( comando.equals("ENVIAR_MENSAJE")) {
			
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
		
	}

	
	/**
	 * Obtiene la conversacion que se ha seleccionado para abrir el chat
	 * 
	 * @return Conversacion - devuelve la conversacion del contacto seleccionado.
	 */
	public Conversacion onSeleccionarConversacion() {	
	}
	
	/**
	 * Crea un nuevo contacto en la lista de contactos
	 * 
	 * @param ip - ip del contacto
	 * @param puerto - numero del contacto
	 * @param nickname - nombre del contacto
	 */
	public void crearContacto(String ip, int puerto, String nickname) {
	}
	
	/**
	 * 
	 */
 	public void crearConversacion(Contacto contacto) {
 	}
 	
 	
 	public void mostrarVentanaPrincipal() {
 		this.ventanaPrincipal = new VentanaPrincipal("Sistema de Mensajeria Instantanea");
 		this.ventanaPrincipal.setLocationRelativeTo(null);
 		this.ventanaPrincipal.setVisible(true);
 	}