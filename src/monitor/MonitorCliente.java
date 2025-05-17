package monitor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * Se encarga de recibir los mensajes de los clientes
 * que piden el puerto del servidor activo para conectase
 * a el.
 */
public class MonitorCliente implements Runnable {
	
	private Monitor monitor;
	private Socket socket;
	
	public MonitorCliente(Monitor monitor, Socket socket) {
		this.monitor = monitor;
		this.socket = socket;
	}


	@Override
	public void run() {
		try(		
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		){
			String peticion = (String) in.readObject();
			if (peticion != null && peticion.equals("SOLICITAR_PUERTO")){
				int puertoActivo = monitor.getPuertoServidorActivo();
				out.writeObject(puertoActivo);
				out.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			
		}
		
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

	
	

}
