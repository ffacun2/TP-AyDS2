package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import exceptions.FueraDeRangoException;

@SuppressWarnings("deprecation")
public class Servidor extends Observable implements Runnable{

	private int puerto;
	private ServerSocket serverSocket;
	private HashMap<String,Cliente> directorio;
	
	public Servidor(int puerto) throws IOException, FueraDeRangoException {
		if (this.puerto < 0 || this.puerto > 65535)
			throw new FueraDeRangoException();
		else
			this.puerto = puerto;
		
		this.serverSocket = new ServerSocket(puerto);
	}

	@Override
	public void run() {
		try {
			while (true) {
				System.out.println("Escuchando en puerto "+ this.puerto +"...");
				Socket socket = serverSocket.accept();
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); //Va a leer un objeto en lugar de un string
		        Mensaje mensaje = (Mensaje) in.readObject();
		        this.setChanged();
		        this.notifyObservers(mensaje);
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setObservador(Observer o) {
		this.addObserver(o);
	}

}
