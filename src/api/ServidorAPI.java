package api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

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
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				
				IRecibible res = (IRecibible) this.input.readObject();
				// Esto esta bien hacerlo asi??????? -f
				synchronized(lock) {
					this.lastResponse = res;
					lock.notify();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public IRecibible enviarRequest(IEnviable env) throws IOException{
		this.output.writeObject(env);
		this.output.flush();
		
		//Y estooo ? andar anda
		synchronized (lock) {
			while (this.lastResponse == null) {
				try {
					lock.wait();
				}
				catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		IRecibible res = this.lastResponse;
		this.lastResponse = null;
		return res;
	}
	
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
		this.lastResponse = res;
	}
	
	public IRecibible getResponse() {
		synchronized (this) {
			while (this.lastResponse == null) {
				try {
					this.wait();
				}
				catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		IRecibible res = this.lastResponse;
		this.lastResponse = null;
		return res;
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
