package servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConexionPasivos {

	
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public ConexionPasivos(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
		this.socket = socket;
		this.in = in;
		this.out = out;
	}
}
