package controller;

import java.awt.event.ActionListener;
import java.io.IOException;

import model.Servidor;
import utils.Utils;
import view.VentanaServidor;

public class ControladorServidor {

	private VentanaServidor ventana;
	private Servidor servidor;
	
	public ControladorServidor() {
		this.ventana = new VentanaServidor();
		this.ventana.setVisible(true);
		this.ventana.setLocationRelativeTo(null);
		this.startServer();
	}

	public void startServer() {
		try {
			this.servidor = new Servidor(Utils.PUERTO_SERVER);
			new Thread(this.servidor).start();
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
