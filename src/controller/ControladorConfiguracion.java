package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import view.VentanaConfiguracion;

public class ControladorConfiguracion implements ActionListener{

	private VentanaConfiguracion VentanaConfiguracion;
	private ControladorPrincipal controladorPrincipal;
	
	public ControladorConfiguracion() {
		this.VentanaConfiguracion = new VentanaConfiguracion("Sistema de Mensajeria Instantanea",this);
		this.VentanaConfiguracion.setLocationRelativeTo(null);
		this.VentanaConfiguracion.setVisible(true);
		this.controladorPrincipal = new ControladorPrincipal();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		
		if (comando.equals("INGRESAR")) {
			
			// Si el metodo crear usuario devuelve true, se cierra la ventana de configuracion y se abre la ventana principal
			// Caso contrario se muestra un mensaje de error.
			if(controladorPrincipal.crearUsuario(
					this.VentanaConfiguracion.getIp(), 
					this.VentanaConfiguracion.getPuerto(), 
					this.VentanaConfiguracion.getNickname())) {
				this.VentanaConfiguracion.dispose();
				this.controladorPrincipal.mostrarVentanaPrincipal();
			}
			else {
				VentanaConfiguracion.mostrarError("Error al crear el usuario");
			}
		}
	}
	
	private void mostratError(String mensaje) {
		JOptionPane.showMessageDialog(VentanaConfiguracion, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

}