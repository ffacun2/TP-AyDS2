package utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Utils {
	public static String INGRESAR = "INGRESAR";
	public static String CREAR_CONVERSACION = "CREAR_CONVERSACION";
	public static String CREAR_CONTACTO = "CREAR_CONTACTO";
	public static String ENVIAR_MENSAJE = "ENVIAR_MENSAJE";
	public static String MODO_CONFIG = "MODO_CONFIGURACION";
	public static String MODO_AGR_CONTACTO = "MODO_AGREGAR_CONTACTO";
	public static String TITULO = "Sistema de mensajeria instantanea";
	public static String TITULO_AGR_CONTACTO = "Agregar contacto";
	public static String CONFIRMAR_CONTACTO = "Aceptar contacto nueva conversacion";
	public static String MENSAJE = "mensaje conversacion";
	public static String REGISTRARSE = "REGISTRARSE";
	public static String AGREGAR_CONTACTO = "AGREGAR_CONTACTO";
	public static String MOSTRAR_DIRECTORIO = "MOSTRAR_DIRECTORIO";
	public static String MOSTRAR_AGENDA = "MOSTRAR_AGENDA";
	public static int PUERTO_SERVER = 8888;
	
	
	public static void mostrarError(String mensaje,JFrame ventana) {
		JOptionPane.showMessageDialog(ventana, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void mostrarVentanaEmergente(String mensaje, JFrame ventana) {
		JOptionPane.showMessageDialog(ventana, mensaje, "Informacion" , JOptionPane.INFORMATION_MESSAGE);
	}
}
