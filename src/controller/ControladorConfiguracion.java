package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;

import exceptions.FueraDeRangoException;
import model.Servidor;
import utils.Utils;
import view.VentanaConfiguracion;

public class ControladorConfiguracion implements ActionListener{

	
	private VentanaConfiguracion ventanaConfiguracion;
	private ControladorPrincipal controladorPrincipal;
	
	public ControladorConfiguracion() {
		//Esta ventana config es la que se abre al inicio de la aplicacion, para crear el usuario
		this.mostrarVentanaConfiguracion(Utils.TITULO, ventanaConfiguracion,Utils.MODO_CONFIG);
		this.controladorPrincipal = new ControladorPrincipal(this); //TODO Chequear si esta bien que el controlador config cree el otro controlador
	}
	
	public VentanaConfiguracion getVentanaConfig() {
		return this.ventanaConfiguracion;
	}
	
	/**
	 * Si el evento se obtiene del boton INGRESAR, se llama al metodo crearUsuario, es decir, cuando se inicia la aplicacion
	 * y el cliente se quiere conectar al servidor.
	 * 
	 * Si el evento se obtiene del boton CREAR_CONTACTO, se llama al metodo agregarContacto, es decir, cuando el cliente ya
	 * se encuentra conectado al servidor y quiere agregar un contacto a su lista de contactos.
	 * 
	 */
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
	 * Crea un nuevo usuario y servidor en la aplicacion, si este se crea correctamente se cierra
	 * la ventana de configuracion y se abre la ventana principal.
	 * Caso contrario se muestra un mensaje de error.
	 * Quien determine si el usuario se crea correctamente es el cliente usuario que lanza error de socket.
	 */
	@SuppressWarnings("deprecation")
	private void crearUsuario() { //TODO Volver a chequear responsabilidades, si es de este controlador o del otro crear el usuario
		
		String ip = this.ventanaConfiguracion.getIp();
		int puerto = Integer.parseInt(this.ventanaConfiguracion.getPuerto());
		
		try {
			Servidor server = new Servidor(ip, puerto);
			server.addObserver(controladorPrincipal);
			
			Thread hiloServer = new Thread(server);
			hiloServer.start();
			
			if (controladorPrincipal
					.crearUsuario(
							this.ventanaConfiguracion.getNickname(),
							Integer.parseInt(this.ventanaConfiguracion.getPuerto()), 
							this.ventanaConfiguracion.getIp()
							)
				) {
				this.controladorPrincipal.mostrarVentanaPrincipal(this);
				this.ventanaConfiguracion.dispose();
			}
			else {
				Utils.mostrarError("No se ha podido crear el usuario. Verifique los datos ingresados.",this.ventanaConfiguracion);
			}
			
		} 
		catch (FueraDeRangoException e) {
			Utils.mostrarError(e.getMessage(), ventanaConfiguracion);
		}
		catch (IOException e) {
			Utils.mostrarError("El puerto ya esta siendo utilizado", this.ventanaConfiguracion);
		}
	}
	
	
	
	/**
	 * Crea un nuevo contacto en la aplicacion, si este se crea correctamente se cierra.
	 * 
	 */
	private void agregarContacto() {
		//Creo que por tema de responsabilidades es mejor que este controlador solo tome los datos necesarios de la ventana y se lo pase al controlador principal, porque para agregar el contacto tmb necesito
		//el usuario, que lo tiene el controlador principal
		try {
				this.controladorPrincipal.crearContacto(
						this.ventanaConfiguracion.getIp(),
						Integer.parseInt(this.ventanaConfiguracion.getPuerto()),
						this.ventanaConfiguracion.getNickname()
				);
			this.ventanaConfiguracion.dispose();
			Utils.mostrarVentanaEmergente("Contacto agregado exitosamente", this.controladorPrincipal.getVentanaPrincipal());
		}
		catch(Exception e) {
			Utils.mostrarError(e.getMessage(),this.ventanaConfiguracion);
		}
	}

	/**
	 *	 Muestra la ventana de configuracion, para agregar un contacto. 
	 */
	public void mostrarVentanaConfiguracion(String title, JFrame componente, String mode) {
		this.ventanaConfiguracion = new VentanaConfiguracion(title, this, mode);
		this.ventanaConfiguracion.setLocationRelativeTo(componente);
		this.ventanaConfiguracion.setVisible(true);
	}
	
	public void cerrar() {
		this.controladorPrincipal.cerrarConfig();
	}
}