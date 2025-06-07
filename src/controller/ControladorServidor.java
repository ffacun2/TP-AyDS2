package controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import config.ConfigServer;
import servidor.Servidor;
import utils.Utils;
import view.VentanaServidor;

public class ControladorServidor implements WindowListener{

	private VentanaServidor ventana;
	private Servidor servidor;
	private int puerto;
	
	public ControladorServidor() {
		try {
			ConfigServer config = new ConfigServer(".properties");
			this.puerto = config.buscarPuertoDisponible();
		}
		catch (Exception e) {
			Utils.mostrarError("No se pudo iniciar el servidor. "+e.getMessage(), null);
			return;
		}
		this.ventana = new VentanaServidor(puerto);
		this.ventana.addWindowListener(this);
		this.ventana.setVisible(true);
		this.ventana.setLocationRelativeTo(null);
		this.startServer();
	}

	
	/**
	 * Inicia el servidor y lo pone a escuchar en el puerto ingresado.
	 * @trhows IOException : si el puerto no puede ser utilizado
	 * @throws IllegalArgumentException : si el puerto no es valido
	 * @throws NumberFormatException : si el puerto no es un numero
	 */
	public void startServer() {
		try {
			this.servidor = new Servidor(this.puerto);
			this.servidor.setControlador(this);
			new Thread(this.servidor).start();
			this.ventana.setStartLabel("Servidor Iniciado...");
		}
		catch (NumberFormatException e) {
			Utils.mostrarError("El dato a ingresar debe ser un numero natural", this.ventana);
			this.ventana.dispose();
		} 
		catch (IllegalArgumentException e) {
			Utils.mostrarError("El puerto debe estar en el rango [0,65535]", this.ventana);
			this.ventana.dispose();
		}
		catch (IOException e) {
			Utils.mostrarError("El puerto ingresado no puede ser utilizado", this.ventana);
			this.ventana.dispose();
		}
		
	}
	


	public void mostrarMensaje(String cuerpo) {
		this.ventana.mostrarMensaje(cuerpo);
	}




	@Override
	public void windowClosing(WindowEvent e) {
		ConfigServer config = new ConfigServer(".properties");
		
		config.liberarServidor(puerto);
		servidor.cerrarSocketServer();
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
