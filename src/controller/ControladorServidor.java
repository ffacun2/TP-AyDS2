package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import model.Servidor;
import utils.Utils;
import view.VentanaServidor;

public class ControladorServidor implements ActionListener{

	private VentanaServidor ventana;
	private Servidor servidor;
	
	public ControladorServidor() {
		this.ventana = new VentanaServidor();
		this.ventana.setActionListener(this);
		this.ventana.setVisible(true);
		this.ventana.setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (command.equals("INICIAR_SERVIDOR")) {
			this.startServer();
		}
	}
	
	public void startServer() {
		try {
			int port = this.ventana.getPort();
			this.servidor = new Servidor(port);
			new Thread(this.servidor).start();
			this.ventana.startServer(port);	
		}
		catch (NumberFormatException e) {
			Utils.mostrarError("El dato a ingresar debe ser un numero natural", this.ventana);
		} 
		catch (IllegalArgumentException e) {
			Utils.mostrarError("El puerto debe estar en el rango [0,65535]", this.ventana);
		}
		catch (IOException e) {
			Utils.mostrarError("El puerto ingresado no puede ser utilizado", this.ventana);
		}
	}
	
}
