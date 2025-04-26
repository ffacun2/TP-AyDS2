package api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import interfaces.IEnviable;
import interfaces.IRecibible;
import model.Mensaje;
import requests.DirectoriosResponse;
import requests.OKResponse;

@SuppressWarnings("deprecation")
public class ServidorAPI extends Observable implements Runnable{
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private boolean estado;
	
	private final Object lock = new Object();
	
	private IRecibible lastResponse;
	
	public ServidorAPI(String ip, int puerto) throws UnknownHostException, IOException {
		this.socket = new Socket(ip,puerto);
		this.output = new ObjectOutputStream(socket.getOutputStream());
		this.output.flush();
		this.lastResponse = null;
		this.estado = true;
	}
	
	@Override
	public void run() {
		try {
			this.input = new ObjectInputStream(socket.getInputStream());
			while(estado) {
				System.out.println("Esperando respuesta del servidor...");
				IRecibible res = (IRecibible)this.input.readObject();
				
				System.out.println(">>  Recibido "+res);
				res.manejarResponse(this);
			}
			
		} catch (IOException | ClassNotFoundException e) {
			//TODO Sesion cerrada
		}
	}
	
	public void enviarRequest(IEnviable env) throws IOException {
		System.out.println("(ServidorAPI) Enviando request");
		
		this.output.writeObject(env);
		this.output.flush();
	}
	

	public void setResponse(IRecibible res) {
		synchronized (lock) {
			this.lastResponse = res;
			lock.notifyAll();
		}
	}
	
	public IRecibible getResponse() {
		synchronized (lock) {
			while (this.lastResponse == null) {
				try {
					lock.wait();
				}catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return null;
				}
			}
		IRecibible res = this.lastResponse;
		this.lastResponse = null;
		return res;
		}
	}
	
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public void mensajeRecibido(MensajeResponse mensaje) {
		Mensaje mensajeRecibido = new Mensaje(mensaje.getNickEmisor(), mensaje.getNickReceptor(), mensaje.getCuerpo());
		this.setChanged();
		this.notifyObservers(mensajeRecibido);
	}
	
	public void directorioRecibido(DirectoriosResponse directorio) {
		this.setChanged();
		this.notifyObservers(directorio);
	}
	
	public void okResponseRecibido(OKResponse okResponse) {
		this.setChanged();
		this.notifyObservers(okResponse);
	}

}
