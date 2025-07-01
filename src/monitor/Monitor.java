package monitor;

import config.ConfigServer;

/*
 * Clase que representa el monitor del sistema
 * Se encarga de gestionar el servidor activo y los servidores secundarios
 * y de realizar el cambio de servidor en caso de que el servidor activo falle
 * Crea el HeartBeat que se encarga de enviar un mensaje al servidor activo para verificar si esta activo
 * y el MonitorCliente que se encarga de recibir los mensajes de los clientes
 * sobre que Servidor esta activo y conectarse.
 */
public class Monitor {

	private int puertoServidorActivo;
	private ConfigServer configServer;
	private PingEcho pingEcho; 
	private Thread threadPingEcho; //Thread que se encarga de enviar un mensaje al servidor activo para verificar si esta activo
	
	
	public Monitor() {
		this.puertoServidorActivo = -1; // -1 significa que no hay servidor activo
		this.configServer = new ConfigServer(".properties");
		this.pingEcho = new PingEcho(this);
		this.threadPingEcho = new Thread(this.pingEcho);
		this.threadPingEcho.start();
	}
	
	
	public void cerrarMonitor() {
		this.pingEcho.cerrarPingEcho();
	}
	
	
	public void buscarServidor() {
		Integer port = configServer.obtenerPuertoActivo();
		this.puertoServidorActivo = port;
	}
	
	
	public void manejoFallo() {
		configServer.determinarServidorActivo();
		buscarServidor();
	}
	
	public void libero(int port) {
		configServer.liberarServidor(port);
	}
	
	public synchronized void setPuertoServidorActivo(int puertoServidorActivo) {
		this.puertoServidorActivo = puertoServidorActivo;
	}
	
	public synchronized int getPuertoServidorActivo() {
		return this.puertoServidorActivo;
	}
	

}