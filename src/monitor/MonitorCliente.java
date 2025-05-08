package monitor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import utils.Utils;

/*
 * Se encarga de recibir los mensajes de los clientes
 * que piden el puerto del servidor activo para conectase
 * a el.
 */
public class MonitorCliente implements Runnable {
	
	private Monitor monitor;
	private ServerSocket serverSocket; //socket del servidor para escuchar conexiones de Usuarios
	
	public MonitorCliente(Monitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void run() {
		try {
			this.serverSocket = new ServerSocket(Utils.PUERTO_MONITOR);		
			
			while(true) {
				//Por cada cliente que se conecta, se crea un nuevo socket
				Socket socket = serverSocket.accept(); //Este socket establece la conexion entre monitor y usuario
				
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				
				// in.readObject(); //Recibe el mensaje del cliente
				
				// Recibe peticion de usuario 
				// y le envia el puerto del servidor activo
				//Nuevo request?
				// out.writeObject(new nuevoRequest??(this.monitor.getPuertoServidorActivo()));
				// El usuario se encarga de cerrar el socket.
				
				
				//DEberia hacer un nuevo hilo para cada cliente que se conecta?
				// Por si el varios clientes se conectan al mismo tiempo
				// o no es necesario?
				
			}		
		} catch (Exception e) {
			System.out.println("Error al crear el socket del monitor: " + e.getMessage());
			return;
		}
	}

}
