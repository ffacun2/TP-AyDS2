package api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import interfaces.IEnviable;
import interfaces.IRecibible;

@SuppressWarnings("deprecation")
public class ServidorAPI extends Observable implements Runnable{
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private final Object lock = new Object();
	
	private IRecibible lastResponse;
	
	public ServidorAPI(String ip, int puerto) throws UnknownHostException, IOException {
		this.socket = new Socket(ip,puerto);
		this.input = new ObjectInputStream(socket.getInputStream());
		this.output = new ObjectOutputStream(socket.getOutputStream());
		this.output.flush();
		this.lastResponse = null;
	}
	
	@Override
	public void run() {
		try {
			while(true) {


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
		this.output.reset();

		this.output.writeObject(env);
		this.output.flush();
	}
	
//	public IRecibible enviarRequest(IEnviable env) throws IOException, ClassNotFoundException{
//		System.out.println("Enviando enviable al servidor: "+env);
//		this.output.reset();
//		this.output.writeObject(env);
//		this.output.flush();
//		IRecibible res;
//		
//		while (true) {
//			if((res = (IRecibible)this.input.readObject()) != null) {
//				System.out.println(">>  Recibido(enviarReq): "+ res);
//				return res;
//			}
//		}
//	}
	
//	public void enviarRequest(Mensaje mensaje) throws IOException {
//		this.output.writeObject(mensaje);
//		this.output.flush();
//	}

//	/**
//	 * Este metodo envia al servidor el pedido con el nickname para registrar o hacer login y se queda esperando la confirmacion
//	 * @param login
//	 * @return true si se pudo crear y false si no se pudo
//	 * @throws IOException 
//	 * @throws ClassNotFoundException 
//	 */
//	public OKResponse enviarRequest(Request request) throws IOException, ClassNotFoundException{
//		this.output.reset();
//		this.output.writeObject(request);
//		this.output.flush();
//
//		OKResponse confirmacion;
//		while(true) {	
//			if((confirmacion = (OKResponse)input.readObject()) != null) {
//				return confirmacion;
//			}
//		}	
//	}

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
	
//	public DirectoriosResponse enviarRequest(RequestDirectorio request) throws IOException, ClassNotFoundException {
//		this.output.reset();
//		this.output.writeObject(request);
//		this.output.flush();
//		
//		DirectoriosResponse agenda;
//		while(true) {	
//			if((agenda = (DirectoriosResponse)input.readObject()) != null) {
//				return agenda;
//			}
//		}
//	}
	
	public void cerrarConexion() {
		
	}


//	@Override
//	public void handleMensaje(Mensaje mensaje) throws IOException {
//		this.setChanged();
//		this.notifyObservers(mensaje);
//	}

}
