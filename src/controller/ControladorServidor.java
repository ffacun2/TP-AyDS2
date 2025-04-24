package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import exceptions.FueraDeRangoException;
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
			this.ventana.startServer(port);	
		}
		catch (NumberFormatException e) {
			Utils.mostrarError("El dato a ingresar debe ser un numero natural", this.ventana);
		} 
		catch (FueraDeRangoException e) {
			Utils.mostrarError(e.getMessage(), this.ventana);
		}
		catch (IOException e) {
			Utils.mostrarError("El puerto ya esta siendo utilizado", this.ventana);
		}
	}
	
}
