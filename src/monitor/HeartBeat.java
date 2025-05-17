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
public class HeartBeat implements Runnable {

	
	private Monitor monitor;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	
	public HeartBeat(Monitor monitor) {
		this.monitor = monitor;
	}
	
	/*
	 * Inicia el HeartBeat
	 * Se encarga de enviar un mensaje al servidor activo para verificar si esta activo
	 * Primero busca un servidor disponible en la lista de servidores secundarios
	 * Si lo encuentra, lo asigna como servidor activo y lo monitorea. 
	 * Si no lo encuentra, espera 5 segundos y vuelve a buscar
	 */
	@Override
	public void run() {
		int puertoNuevo = -1;
		while (true) {
		
			//Paso 1 : Busco un servidor disponible.
			System.out.println("Buscando servidor disponible...");
			while (puertoNuevo == -1) {
				System.out.println("Entro bucle buscando servidor");
				puertoNuevo = this.buscoServidorDisponible();
				if (puertoNuevo != -1) {
					this.monitor.cambioServidor(puertoNuevo);
				}
				else {
					try {
						Thread.sleep(3000); // Espera 3 segundos antes de volver a verificar
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("puerto en bucle de busqueda:"+puertoNuevo);
			}
			cerrarSocket();
			this.monitor.setPuertoServidorActivo(puertoNuevo);
			this.monitor.sincronizacion();
			
			System.out.println("sali bucle buscando servidor disponible: " + puertoNuevo);
			//Paso 2 : Monitoreo el servidor activo
			while (puertoNuevo != -1) {
				System.out.println("Entro bucle monitoreando servidor");
				//Por cada iteracion del bucle, creo un nuevo socket para no bloquear el serversocket del servidor
				try ( 
					Socket socket = new Socket("localhost", puertoNuevo);
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					)  {
					
					System.out.println("Mando ping");
					out.writeObject(new Pulso("PING"));
					out.flush();
					
					socket.setSoTimeout(4000);//Timeout para esperar la rta
					Pulso respuesta = (Pulso) in.readObject();
					
					if (respuesta == null || !respuesta.getMensaje().equals("PONG")) {
						System.out.println("El server no responde");
						throw new IOException("El servidor no responde");
					}
					//Siempre recibo PONG y el directorio del servidor activo
					Thread.sleep(4000);
				} 
				catch (Exception e) {
					puertoNuevo = -1;
				}
				finally {
					cerrarSocket();
				}
			}
		}
	}
	
	/*
	 * Busca un servidor disponible en la lista de servidores secundarios
	 * @return el puerto del servidor disponible, -1 si no hay servidores disponibles
	 */
	protected int buscoServidorDisponible() {
		int i = 0;
		int encontrado = -1;
		
		while ( i < this.monitor.getPuertosSecundarios().size() && encontrado == -1) {
			int puerto = this.monitor.getPuertosSecundarios().poll();
			
			//lo agrego si no lo encontre
			if (!this.verificoServidor(puerto)) 
				this.monitor.getPuertosSecundarios().add(puerto); // Agrego el puerto al final de la cola
			else 
				encontrado = puerto; //si lo encontre, lo guardo
			i++;
		}
		return encontrado;
	}
	
	/*
	 * Verifica si el servidor esta activo
	 * @param puerto : puerto del servidor a verificar
	 * @return true si el servidor esta activo, false si no lo esta
	 */
	protected boolean verificoServidor(int puerto) {
		try {
			System.out.println("Entro metodo verificoServidor");
			this.socket = new Socket("localhost", puerto);
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
			
			this.out.writeObject(new Pulso("PING"));
			this.out.flush();
			System.out.println("Mande punso, espero rta");
			
			socket.setSoTimeout(4000); // Timeout para esperar la respuesta del servidor
			Pulso respuesta = (Pulso) this.in.readObject();
			System.out.println("LLego rta");
			
			return respuesta != null && respuesta.getMensaje().equals("PONG");
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 *	 Cierra el socket y los streams 
	 */
	public void cerrarSocket() {
		try {
			if (this.socket != null) {
				this.socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.socket = null;
		this.out = null;
		this.in = null;
	}
	
	
	
}
