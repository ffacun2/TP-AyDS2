package monitor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import requests.Pulso;

/*
 * Clase que representa el HeartBeat del sistema
 * Se encarga de enviar un mensaje al servidor activo para verificar si esta activo
 * Si el servidor no responde, se cambia el servidor activo por el secundario
 */
public class PingEcho implements Runnable {

	
	private Monitor monitor;
	private boolean estado; // Indica si el HeartBeat esta activo
	private int puertoNuevo = -1; // Puerto del servidor activo, se asigna desde el Monitor
	
	public PingEcho(Monitor monitor) {
		this.monitor = monitor;
		this.estado = true;
	}
	
	/*
	 * Inicia el HeartBeat
	 * Se encarga de enviar un mensaje al servidor activo para verificar si esta activo
	 * Primero busca un servidor disponible en la lista de servidores secundarios
	 * Si lo encuentra, lo asigna como servidor activo y lo monitorea. 
	 * Si no lo encuentra, espera 1 segundos y vuelve a buscar
	 */
	@Override
	public void run() {

		while (estado) {
			puertoNuevo = monitor.getPuertoServidorActivo();
			while (puertoNuevo != -1) {
				//Por cada iteracion del bucle, creo un nuevo socket para no bloquear el serversocket del servidor
				try ( 
					Socket socket = new Socket("localhost", puertoNuevo);
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					)  
				{
					
					out.writeObject(new Pulso("PING"));
					out.flush();
					
					socket.setSoTimeout(4000);//Timeout para esperar la rta
					Pulso respuesta = (Pulso) in.readObject();
					
					if (respuesta == null || !respuesta.getMensaje().equals("PONG")) {
						throw new IOException("El servidor no responde");
					}

					delay(1000);
				} 
				catch (Exception e) {
					puertoNuevo = -1;
				}
			}
			monitor.manejoFallo(); // Maneja el fallo del servidor activo
			delay(500);
		}
	}
	

	public void cerrarPingEcho() {
		this.puertoNuevo = -1; // Resetea el puerto del servidor activo
		this.estado = false; // Cambia el estado a false para detener el HeartBeat
	}
	
	private void delay (int timeSleep) {
		try {
			Thread.sleep(timeSleep);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Restablece el estado de interrupción
		}
	}
	
}
