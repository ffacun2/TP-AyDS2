package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class ServidorAPI extends Observable implements Runnable{
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public ServidorAPI(String ip, int puerto) throws UnknownHostException, IOException {
		this.socket = new Socket(ip,puerto);
	}
	
	@Override
	public void run() {
		try {
			this.input = new ObjectInputStream(socket.getInputStream());
			Mensaje mensaje;
			while(true) {
				if((mensaje = (Mensaje)input.readObject()) != null) {
					this.setChanged();
					this.notifyObservers(mensaje);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void enviarMensaje(Mensaje mensaje) throws IOException {
		this.output.writeObject(mensaje);
		this.output.flush();
	}
	
	public void cerrarConexion() {
		
	}
	
}
