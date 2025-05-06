package monitor;

import java.util.ArrayDeque;
import java.util.Queue;

/*
 * Clase que representa el monitor del sistema
 * Se encarga de gestionar el servidor activo y los servidores secundarios
 * y de realizar el cambio de servidor en caso de que el servidor activo falle
 * Crea el HeartBeat que se encarga de enviar un mensaje al servidor activo para verificar si esta activo
 * y el MonitorCliente que se encarga de recibir los mensajes de los clientes
 * sobre que Servidor esta activo y conectarse.
 */
public class Monitor  {

	private int puertoServidorActivo;
	private Queue<Integer> puertosSecundarios;
	
	public Monitor(int... puertos) {
		this.puertosSecundarios = new ArrayDeque<>();
		for (int puerto: puertos) {
			this.puertosSecundarios.add(puerto);
		}
	}
	
	public void iniciar() {
		new Thread(new HeartBeat(this)).start();
		new Thread(new MonitorCliente(this)).start();
	}
	
	public synchronized void cambioServidor(int puertoServerNuevo) {
		this.puertosSecundarios.add(this.puertoServidorActivo); //Agrego al final el servidor caido
		this.puertoServidorActivo = puertoServerNuevo;
	}
	
	
	public synchronized void setPuertoServidorActivo(int puertoServidorActivo) {
		this.puertoServidorActivo = puertoServidorActivo;
	}
	
	public synchronized int getPuertoServidorActivo() {
		return this.puertoServidorActivo;
	}
	
	public synchronized Queue<Integer> getPuertosSecundarios() {
		return this.puertosSecundarios;
	}
}
