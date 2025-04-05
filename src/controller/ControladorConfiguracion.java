package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import utils.Utils;
import view.VentanaConfiguracion;

public class ControladorConfiguracion implements ActionListener{

	
	private VentanaConfiguracion ventanaConfiguracion;
	private ControladorPrincipal controladorPrincipal;
	
	public ControladorConfiguracion() {
		
		this.mostrarVentanaConfiguracion(Utils.TITULO, Utils.MODO_CONFIG);
		this.controladorPrincipal = new ControladorPrincipal(); //TODO Chequear si esta bien que el controlador config cree el otro controlador
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		
		if (comando.equals(Utils.INGRESAR)) {
			this.crearUsuario();
		}
		else if (comando.equals(Utils.CREAR_CONTACTO)) {
			this.agregarContacto();
		}
	}

	/**
	 * Crea un nuevo usuario en la aplicacion, si este se crea correctamente se cierra
	 * la ventana de configuracion y se abre la ventana principal.
	 * Caso contrario se muestra un mensaje de error.
	 * Quien determine si el usuario se crea correctamente es el cliente usuario que lanza error de socket.
	 */
	private void crearUsuario() { //TODO Volver a chequear responsabilidades, si es de este controlador o del otro crear el usuario
	
		if (controladorPrincipal
				.crearUsuario(
						this.ventanaConfiguracion.getIp(), 
						Integer.parseInt(this.ventanaConfiguracion.getPuerto()), 
						this.ventanaConfiguracion.getNickname()
						)
			) {
			this.ventanaConfiguracion.dispose();
			this.controladorPrincipal.mostrarVentanaPrincipal();
		}
		else {
			this.mostrarError("No se ha podido crear el usuario. Verifique los datos ingresados.");
		}
	}
	
	private void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(ventanaConfiguracion, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void agregarContacto() {
		//Creo que por tema de responsabilidades es mejor que este controlador solo tome los datos necesarios de la ventana y se lo pase al controlador principal, porque para agregar el contacto tmb necesito
		//el usuario, que lo tiene el controlador principal
		try {
			this.controladorPrincipal.crearContacto(
					this.ventanaConfiguracion.getIp(),
					Integer.parseInt(this.ventanaConfiguracion.getPuerto()),
					this.ventanaConfiguracion.getNickname());
			this.ventanaConfiguracion.dispose();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	public void mostrarVentanaConfiguracion(String title, String mode) {
		this.ventanaConfiguracion = new VentanaConfiguracion(title, this, mode);
		this.ventanaConfiguracion.setLocationRelativeTo(null);
		this.ventanaConfiguracion.setVisible(true);
	}
	
	public void cerrar() {
		this.controladorPrincipal.cerrarConfig();
	}
}