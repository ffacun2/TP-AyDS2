package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import interfaces.IEnviable;
import interfaces.IRecibible;
import model.Mensaje;
import requests.MensajeResponse;
import utils.Utils;

@SuppressWarnings("deprecation")
public class ServidorAPI extends Observable implements Runnable {
	
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private boolean estado;
	
	private final Object lock = new Object();
	private boolean controladorListo;
	private ArrayList<Mensaje> bufferMensajes;
	
	private IRecibible lastResponse;
	
	//TODO agregar metodo para establecer nueva conexion
	//TODO agregar metodo para cortar la conexion actual
	public ServidorAPI() throws UnknownHostException, IOException { //TODO cambiar el constructor para que sea mas generico
		this.lastResponse = null;
		this.estado = true;
		this.controladorListo = false;
		this.bufferMensajes = new ArrayList<Mensaje>();
	}
	
	@Override
	public void run() {
		while(estado) {
			IRecibible res;
			try {
				res = (IRecibible)this.input.readObject();
				res.manejarResponse(this);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void enviarRequest(IEnviable env) throws IOException {
		try {
			this.output.writeObject(env);
			this.output.flush();
		}
		catch (IOException e) {
			try {
				Thread.sleep(3000);
			}
			catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				throw new RuntimeException("Reintento interrumpido",ie);
			}
		
			this.output.writeObject(env);
			this.output.flush();
		}
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
		this.controladorListo = false;
		//Habria que mandar el estado al servidor?
	}
	
	public void mensajeRecibido(MensajeResponse mensaje) {
		
		synchronized (this) {
			Mensaje mensajeRecibido = new Mensaje(mensaje.getNickEmisor(), mensaje.getNickReceptor(), mensaje.getCuerpo());
			if (this.controladorListo) {
				this.setChanged();
				this.notifyObservers(mensajeRecibido);
			}else
				this.bufferMensajes.add(mensajeRecibido);
		}
	}
	
	public void setControladorListo() {
		synchronized (this) {
			this.controladorListo = true;
			
			Iterator<Mensaje> it = this.bufferMensajes.iterator();
			while (it.hasNext()) {
				setChanged();
				notifyObservers(it.next());
			}
			this.bufferMensajes.clear();
		}
	}
	
	public void iniciarApi (String ip, int puerto) throws UnknownHostException, IOException {
		this.socket = new Socket(ip,puerto);
		this.output = new ObjectOutputStream(socket.getOutputStream());
		this.input = new ObjectInputStream(socket.getInputStream());
	}
	
	public Integer getPuertoServidorActivo() {
		try (
			Socket socket = new Socket("localhost",Utils.PUERTO_MONITOR);
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());			
			) {

			out.writeObject("SOLICITAR_PUERTO");
			out.flush();
			
			return (Integer) in.readObject();
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
