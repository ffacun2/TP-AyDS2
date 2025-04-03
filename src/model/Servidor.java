package model;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Servidor extends Observable implements Runnable{
	private String ip;
	private int puerto;
	
	public Servidor(String ip, int puerto){
		this.ip = ip;
		this.puerto = puerto;
	}

	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(puerto);
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

}
