package controller;

import java.awt.event.ActionListener;
import java.io.IOException;

import servidor.Servidor;
import utils.Utils;
import view.VentanaServidor;

public class ControladorServidor implements ActionListener {

	private VentanaServidor ventana;
	private Servidor servidor;
	private int puerto;
	
	public ControladorServidor(int puerto) {
		this.puerto = puerto;
		this.ventana = new VentanaServidor(puerto);
		this.ventana.setControlador(this);
		this.ventana.setVisible(true);
		this.ventana.setLocationRelativeTo(null);
		this.startServer();
	}

	/*
	 * Captura los eventos de los botones de la ventana
	 * INICIAR_SERVER: inicia el servidor
	 * DETENER_SERVER: detiene el servidor
	 */
	@Override
	public void actionPerformed(java.awt.event.ActionEvent e) {
		String comando = e.getActionCommand();
		if (comando.equals(Utils.INICIAR_SERVER)) {
			this.startServer();
		}
		else if (comando.equals(Utils.DETENER_SERVER)) {
			this.detenerServidor();
		}
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
			this.ventana.setEnableButtonFinalizar(true);
			this.ventana.setEnableButtonInicio(false);
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
	
	/**
	 * Detiene el servidor
	 */
	public void detenerServidor() {
		if (this.servidor == null) {
			Utils.mostrarError("El servidor no se ha iniciado", this.ventana);
			return;
		}
		else {
			this.servidor.setEstado(false);
			this.ventana.setStartLabel("Servidor detenido...");
			this.servidor.cerrarSocketServer();
			this.servidor = null;
			this.ventana.setEnableButtonFinalizar(false);
			this.ventana.setEnableButtonInicio(true);
			
		}
	}

	public void mostrarMensaje(String cuerpo) {
		this.ventana.mostrarMensaje(cuerpo);
	}
	
}
