package monitor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * Clase que representa el HeartBeat del sistema
 * Se encarga de enviar un mensaje al servidor activo para verificar si esta activo
 * Si el servidor no responde, se cambia el servidor activo por el secundario
 */
public class HeartBeat implements Runnable {

	
	private Monitor monitor;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	
	
	public HeartBeat(Monitor monitor) {
		this.monitor = monitor;
		try {
			setSocket(new Socket("localhost", monitor.getPuertoServidorActivo()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este metodo se encarga de enviar un mensaje al servidor activo para verificar si esta activo
	 * Si el servidor no responde, se cambia el servidor activo por el secundario
	 */
	@Override
	public void run() {
		while (true) {
			try {
				this.out.writeObject("ping");
				this.out.flush();
				
				// Timeout para esperar la respuesta del servidor
				socket.setSoTimeout(5000);
				
				String respuesta = (String) this.in.readObject();
				if (respuesta == null) {
					this.monitor.cambioServidor();
				}
			}
			catch (Exception e) {
				// Si no se recibe respuesta, se cambia el servidor activo por el secundario
				this.monitor.cambioServidor();
				System.out.println("Servidor inactivo, cambiando al servidor secundario");
			}
			
			// Espera 5 segundos antes de volver a enviar el ping
			try {
				Thread.sleep(5000); // Espera 5 segundos antes de volver a enviar el ping
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public void setSocket(Socket socket) {
		this.socket = socket;
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
