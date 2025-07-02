package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import model.Usuario;
import requests.OKResponse;
import utils.Utils;
import view.VentanaInicio;

public class ControladorInicio implements ActionListener {

	private VentanaInicio ventanaInicio;
	
	public ControladorInicio() {
		//Esta ventana config es la que se abre al inicio de la aplicacion, para crear el usuario
		this.mostrarVentanaInicio(Utils.TITULO, null);
	}
	
	/**
	 * Metodo que maneja los eventos de los botones de la ventana de inicio.
	 * Si el evento es del boton INGRESAR, se llama al metodo iniciarSesion.
	 * Si el evento es del boton REGISTRARSE, se llama al metodo registrarUsuario.
	 * Si el evento es del boton ACEPTAR_CONFIG_REGISTRO, viene de la ventana de configuracion
	 * para aceptar las configuraciones de clave, tecnica de encriptado y tipo de archivo.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		
		if (comando.equals(Utils.BTN_INGRESAR))
			this.iniciarSesion(comando);
		else if (comando.equals(Utils.BTN_REGISTRARSE))
			this.registrarUsuario(comando);
	}

	/**
	 * Registro del usuario, si este no existe en el servidor.
	 * * Envia request de registro y valida la respuesta del servidor.
	 * * * Si este es valido, se inicia la ventana principal de la aplicacion.
	 * * * Si no, se muestra un mensaje de error.
	 * 
	 * @param modo : modo de la ventana (registro o inicio de sesion) de configuracion
	 */
	public void registrarUsuario(String modo) {
		Usuario usuario;
		String nickname = this.ventanaInicio.getNickname();
		OKResponse isSuccess = null;
		
		if (!nickname.equals("")) {
			if(modo.equals(Utils.BTN_REGISTRARSE)) {
				usuario = new Usuario(nickname);
				try {
					usuario.conectarApi();
					isSuccess = usuario.registrarse();
					if (isSuccess == null ) {
						throw new IOException("No se pudo conectar al servidor");
					}
					if (isSuccess.isSuccess()) 
						iniciarVentanaPrincipal(usuario);
					else {
						Utils.mostrarError(isSuccess.getMensajeError(), this.ventanaInicio);
					}
				} 
				catch (UnknownHostException e) {
					Utils.mostrarError(e.getMessage(), this.ventanaInicio);
				} 
				catch (IOException e) {
					Utils.mostrarError(e.getMessage(), this.ventanaInicio);
				}
			}
		}
		else {
			Utils.mostrarError("Por favor, complete el campo.", ventanaInicio);
		}
	}
	
	/**
	 * Inicio sesion del usuario, si este ya existe en el servidor.
	 * Envia request de inicio de sesion y valida la respuesta del servidor.
	 * * Si este es valido, se inicia la ventana principal de la aplicacion.
	 * * Si no, se muestra un mensaje de error.
	 * 
	 * @param modo : modo de la ventana (registro o inicio de sesion) de configuracion
	 */
	public void iniciarSesion(String modo) {
		Usuario usuario;
		String nickname = this.ventanaInicio.getNickname();
		OKResponse isSuccess = null;
		
		if (!nickname.equals("")) {
			if (modo.equals(Utils.BTN_INGRESAR)) {
				usuario = new Usuario(nickname);
				try {
					usuario.conectarApi();
					isSuccess = usuario.iniciarSesion();
					if (isSuccess == null ) {
						throw new IOException("No se pudo conectar al servidor");
					}
					if (isSuccess.isSuccess()) 
						iniciarVentanaPrincipal(usuario);
					else {
						Utils.mostrarError(isSuccess.getMensajeError(), this.ventanaInicio);
					}
				} 
				catch (UnknownHostException e) {
					Utils.mostrarError(e.getMessage(), this.ventanaInicio);
				} 
				catch (IOException e) {
					Utils.mostrarError(e.getMessage(), this.ventanaInicio);
				}
			}
		}
		else {
			Utils.mostrarError("Por favor, complete el campo.", ventanaInicio);
		}
	}
	
	/**
	 * Visualiza la ventana principal de la aplicacion, una vez iniciado sesion o registrado.
	 * 
	 * @param usuario : usuario que inicia sesion o se registra
	 */
	public void iniciarVentanaPrincipal (Usuario usuario) {
		ControladorPrincipal controlador = new ControladorPrincipal(usuario);
		controlador.mostrarVentanaPrincipal(this.ventanaInicio);
		usuario.setControlador(controlador);
		usuario.crearSesion();
		
		this.ventanaInicio.dispose(); //Cierro la ventana de inicio
		controlador.setTitulo("Sistema de mensajeria - "+usuario.getNickname());
		
	}

	/**
	 *	 Visualiza la ventana de inicio de la aplicacion, una vez iniciado sesion o registrado.
	 * @param title : titulo de la ventana
	 * @param component : componente padre para centrar la ventana
	 * @param mode : modo de la ventana (registro o inicio de sesion) 
	 */
	public void mostrarVentanaInicio(String title,Component component) {
		this.ventanaInicio = new VentanaInicio(title);
		this.ventanaInicio.setLocationRelativeTo(component);
		this.ventanaInicio.setControlador(this);
		this.ventanaInicio.setVisible(true);
	}

}