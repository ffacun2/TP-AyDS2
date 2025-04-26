package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import api.ServidorAPI;
import exceptions.FueraDeRangoException;
import requests.OKResponse;
import requests.Request;
import requests.RequestLogin;
import requests.RequestRegistro;
import utils.Utils;
import view.VentanaConfiguracion;

public class ControladorConfiguracion implements ActionListener{

	private VentanaConfiguracion ventanaConfiguracion;
	private ControladorPrincipal controladorPrincipal;
	
	public ControladorConfiguracion() {
		//Esta ventana config es la que se abre al inicio de la aplicacion, para crear el usuario
		this.mostrarVentanaConfiguracion(Utils.TITULO, Utils.MODO_CONFIG);
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
		
		if (comando.equals(Utils.INGRESAR) || comando.equals(Utils.REGISTRARSE)) {
			this.iniciarSesion(comando);
		}
	}

	/**
	 * Manda una request al servidor para crear un nuevo usuario
	 * la ventana de configuracion y se abre la ventana principal.
	 * Caso contrario se muestra un mensaje de error.
	 */
	private void iniciarSesion(String modo) { //TODO Volver a chequear responsabilidades, si es de este controlador o del otro crear el usuario
		try {
			String nickname = this.ventanaConfiguracion.getNickname();
			String ip = this.ventanaConfiguracion.getIp();
			String puerto = this.ventanaConfiguracion.getPuerto();
			
			if(!nickname.equals("") && !ip.equals("") && !puerto.equals("")) {
				Request request;
				if(modo.equals(Utils.INGRESAR)) {
					request = new RequestLogin(nickname);
				}else {
					request = new RequestRegistro(nickname);
				}
				
				ServidorAPI servidor = new ServidorAPI("localhost", 8888);
				Thread hiloServer = new Thread(servidor);
				hiloServer.start();
				
				OKResponse response = (OKResponse)servidor.enviarRequest(request);
				
//				OKResponse response = (OKResponse) servidor.getResponse();
				
				if((response != null) && (response.isSuccess() == true)) {
					this.controladorPrincipal = new ControladorPrincipal(this, servidor);
					servidor.addObserver(controladorPrincipal);
					this.controladorPrincipal.crearUsuario(ip, Integer.parseInt(puerto), nickname, servidor);
					this.ventanaConfiguracion.dispose();
					this.controladorPrincipal.mostrarVentanaPrincipal();
					
				}else {
					Utils.mostrarError(response.getMensajeError(), this.ventanaConfiguracion); //Esto se puede remplazar por un mensaje del servidor
				}
			}else {
				Utils.mostrarError("Por favor, ingrese todo los campos.", ventanaConfiguracion);
			}
			
		} 
		catch (FueraDeRangoException e) {
			Utils.mostrarError(e.getMessage(), ventanaConfiguracion);
		}
		catch (IOException e) {
			Utils.mostrarError("No se pudo conectar al servidor", this.ventanaConfiguracion);
		}
	}

	/**
	 *	 Muestra la ventana de configuracion, para agregar un contacto. 
	 */
	public void mostrarVentanaConfiguracion(String title, String mode) {
		this.ventanaConfiguracion = new VentanaConfiguracion(title, this, mode);
		this.ventanaConfiguracion.setLocationRelativeTo(null);
		this.ventanaConfiguracion.setVisible(true);
	}
	
	public void cerrar() {
		this.controladorPrincipal.cerrarConfig();
	}

}