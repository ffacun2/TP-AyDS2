package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import view.VentanaConfiguracion;

public class ControladorConfiguracion implements ActionListener{

	
	private VentanaConfiguracion ventanaConfiguracion;
	private ControladorPrincipal controladorPrincipal;
	
	public ControladorConfiguracion() {
		this.ventanaConfiguracion = new VentanaConfiguracion("Sistema de Mensajeria Instantanea",this);
		this.ventanaConfiguracion.setLocationRelativeTo(null);
		this.ventanaConfiguracion.setVisible(true);
		this.controladorPrincipal = new ControladorPrincipal();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		
		if (comando.equals("INGRESAR")) {
			this.crearUsuario();
		}
	}
	
	/**
	 * Crea un nuevo usuario en la aplicacion, si este se crea correctamente se cierra
	 * la ventana de configuracion y se abre la ventana principal.
	 * Caso contrario se muestra un mensaje de error.
	 * Quien determine si el usuario se crea correctamente es el cliente usuario que lanza error de socket.
	 */
	private void crearUsuario() {
		String ip = this.ventanaConfiguracion.getIp();
		int puerto = Integer.parseInt(this.ventanaConfiguracion.getPuerto());
		String nickname = this.ventanaConfiguracion.getNickname();
		
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

}