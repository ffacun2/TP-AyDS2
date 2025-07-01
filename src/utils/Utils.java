package utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Utils {
	public static String BTN_INGRESAR = "INGRESAR";
	public static String CREAR_CONVERSACION = "CREAR_CONVERSACION";
	public static String CREAR_CONTACTO = "CREAR_CONTACTO";
	public static String ENVIAR_MENSAJE = "ENVIAR_MENSAJE";
	public static String MODO_CONFIG = "MODO_CONFIGURACION";
	public static String MODO_AGR_CONTACTO = "MODO_AGREGAR_CONTACTO";
	public static String TITULO = "Sistema de mensajeria instantanea";
	public static String TITULO_AGR_CONTACTO = "Agregar contacto";
	public static String CONFIRMAR_CONTACTO = "Aceptar contacto nueva conversacion";
	public static String SELEC_CONVERSACION = "mensaje conversacion";
	public static String BTN_REGISTRARSE = "REGISTRARSE";
	public static String AGREGAR_CONTACTO = "AGREGAR_CONTACTO";
	public static String MOSTRAR_DIRECTORIO = "MOSTRAR_DIRECTORIO";
	public static String MOSTRAR_AGENDA = "MOSTRAR_AGENDA";
	public static String RECONEXION = "RECONEXION";
	public static String BTN_ACCEPT_CONFIG_REGISTRO = "CONFIG_REGISTRO";
	
	public static String INICIAR_SERVER = "INICIAR_SERVER";
	public static String DETENER_SERVER = "DETENER_SERVER";
	
	public static int PUERTO_SYNC = 9998;
	
	public static String[] TIPOS_ARCHIVO = {"JSON", "XML", "TXT"};
	public static final String ID_CONVERSACION = "CONVERSACION";
	public static final String ID_MENSAJE = "MENSAJE";
	public static final String ID_CONTACTO = "CONTACTO";

	public static final String ID_LOGOUT = "LOGOUT";
	public static final String ID_REGISTRO = "REGISTRO";
	public static final String ID_DIRECTORIO = "DIRECTORIO";
	public static final String ID_LOGIN = "LOGIN";
	
	public static final String[] TECNICAS_ENCRIPTADO = {"XOR","CESAR"};
	
	public static void mostrarError(String mensaje,JFrame ventana) {
		JOptionPane.showMessageDialog(ventana, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void mostrarVentanaEmergente(String mensaje, JFrame ventana) {
		JOptionPane.showMessageDialog(ventana, mensaje, "Informacion" , JOptionPane.INFORMATION_MESSAGE);
	}
}
