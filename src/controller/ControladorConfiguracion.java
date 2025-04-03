package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Usuario;

public class ControladorConfiguracion implements ActionListener{

	private VentanaConfiguracion ventanaConfiguracion;
	private ControladorPrincipal controladorPrincipal;
	
	public ControladorConfiguracion() {
		this.ventanaConfiguracion = new VentanaConfiguracion("Sistema de Mensajeria Instantanea");
		this.ventanaConfiguracion.setLocationRelativeTo(null);
		this.ventanaConfiguracion.setVisible(true);
		this.controladorPrincipal = new ControladorPrincipal();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		
		if (comando.equals("INGRESAR")) {
			
			// Si el metodo crear usuario devuelve true, se cierra la ventana de configuracion y se abre la ventana principal
			// Caso contrario se muestra un mensaje de error.
			if(controladorPrincipal.crearUsuario(
					this.ventanaConfiguracion.getIp(), 
					this.ventanaConfiguracion.getPuerto(), 
					this.ventanaConfiguracion.getNickname())) {
				this.ventanaConfiguracion.dispose();
				this.controladorPrincipal.mostrarVentanaPrincipal();
			}
			else {
				ventanaConfiguracion.mostrarError("Error al crear el usuario");
			}
		}
	}
	
	private void mostratError(String mensaje) {
		JOptionPane.showMessageDialog(ventanaConfiguracion, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

}