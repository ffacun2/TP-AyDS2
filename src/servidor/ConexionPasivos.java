package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConexionPasivos {
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public ConexionPasivos(Socket socket) {
		this.socket = socket;
		try {
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
			this.out.flush(); // Asegura que el stream se haya inicializado correctamente
			this.in = new ObjectInputStream(this.socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ObjectOutputStream getOutputStream() {
		return out;
	}
	
	public ObjectInputStream getInputStream() {
		return in;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void cerrar() {
		try {
			Thread.sleep(1000); // Espera para asegurar que todos los datos se envíen
			if (socket != null && !socket.isClosed()) socket.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
