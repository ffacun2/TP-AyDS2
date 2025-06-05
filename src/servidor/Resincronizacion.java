package servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Resincronizacion implements Runnable{

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private boolean estado = true; // Indica si la resincronizacion esta activa
	
	public Resincronizacion(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
		this.socket = socket;
		this.in = in;
		this.out = out;
	}

	@Override
	public void run() {
		while (estado) {
			
		}
	}
	
	
	
}
