package cliente;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import config.ConfigServer;
import interfaces.IEnviable;
import interfaces.IRecibible;
import model.Mensaje;
import requests.MensajeResponse;
import requests.OKResponse;
import requests.RequestFactory;
import utils.Utils;

@SuppressWarnings("deprecation")
public class ClienteAPI extends Observable implements Runnable {
	
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private volatile boolean estado;
	
	private RequestFactory reqFactory;
	private final Object lock = new Object();
	private boolean controladorListo;
	private ArrayList<Mensaje> bufferMensajes;
	
	private IRecibible lastResponse;

	public ClienteAPI() {
		this.lastResponse = null;
		this.estado = true;
		this.controladorListo = false;
		this.bufferMensajes = new ArrayList<Mensaje>();
		this.reqFactory = new RequestFactory();
	}
	
	@Override
	public void run() {
		System.out.println("Iniciando ServerAPI " + socket.getPort());
		while (estado && socket != null && !socket.isClosed()) {
			try {
				socket.setSoTimeout(3000);
				IRecibible res = (IRecibible) this.input.readObject();
				res.manejarResponse(this);
			}
			catch (SocketTimeoutException e) {
			}
			catch (EOFException | SocketException e) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				this.setChanged();
				this.notifyObservers(Utils.RECONEXION);
				break;
			} 
			catch (ClassNotFoundException | IOException e) {
				this.setChanged();
				this.notifyObservers(Utils.RECONEXION);
				break; 
			}
		}
		System.out.println("Finalizando hilo ServerAPI");
	}
	
	public void enviarRequest(IEnviable env) throws IOException {
		try {
			this.output.writeObject(env);
			this.output.flush();
		}
		catch (Exception e) { //Reintento
			e.printStackTrace();
			try {
				Thread.sleep(6000);
			}
			catch (InterruptedException ie) {
				throw new RuntimeException("Reintento interrumpido",ie);
			}
		
			System.out.println("EnviarRequest ServidorAPI 2 intento");
			this.output.writeObject(env);
			this.output.flush();
		}
	}
	

	public void setResponse(IRecibible res) {
		System.out.println("LLego rta");
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
	
	/** Setea el estado de la API cuando se <b>cierra sesion</b>
	 * @param estado: nuevo estado
	 */
	public void setEstado(boolean estado) {
		this.estado = estado;
		this.controladorListo = false;
	}
	public boolean getEstado() {
		return this.estado;
	}
	
	public void mensajeRecibido(MensajeResponse mensaje) {
		System.out.println("Recibi msj");
		synchronized (this) {
			Mensaje mensajeRecibido = new Mensaje(mensaje.getNickEmisor(), mensaje.getNickReceptor(), mensaje.getCuerpo());
			System.out.println("Dentro bloque sync");
			if (this.controladorListo) {
				this.setChanged();
				this.notifyObservers(mensajeRecibido);
			}else {
				this.bufferMensajes.add(mensajeRecibido);
				System.out.println("controlador no listo");
			}
		}
	}
	
	/** 
	 *  Indica que el controlador esta listo, y envia todos los mensajes pendientes que llegaron ubicados en el bufer
	 */
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
	
	public void iniciarApi (int puerto) throws UnknownHostException, IOException {
		System.out.println("> metodo iniciarApi -> "+puerto);
		this.setEstado(true);
		this.socket = new Socket("localhost",puerto);
		this.output = new ObjectOutputStream(socket.getOutputStream());
		output.flush();
		this.input = new ObjectInputStream(socket.getInputStream());
	}
	
	public Integer getPuertoServidorActivo() {
		ConfigServer config = new ConfigServer(".properties");
		return config.obtenerPuertoActivo();
	}

	public void cerrarConexion() {
	    try {
	        if (input != null) input.close();
	        if (output != null) output.close();
	        if (socket != null && !socket.isClosed()) socket.close();
	    } catch (IOException e) {
	        System.err.println("Error cerrando conexión: " + e.getMessage());
	    }
	}
	
	public boolean reconectar(String nickUsuario) throws IOException {
		cerrarConexion();

		int puerto = this.getPuertoServidorActivo();
		if (puerto == -1) return false;

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}

		this.iniciarApi(puerto);
		Thread hilo = new Thread(this);
		hilo.setDaemon(true);
		hilo.start();

		this.enviarRequest(this.reqFactory.getRequest(Utils.ID_LOGIN, nickUsuario));
		OKResponse res = (OKResponse) this.getResponse();

		if (res.isSuccess()) {
			this.setControladorListo();
			return true;
		}
		return false;
	}
	
	public void enviarRequestLogout(String nickname) throws IOException{
		this.enviarRequest(this.reqFactory.getRequest(Utils.ID_LOGOUT, nickname));
	}
	
	public void enviarRequestDirectorio(String nickname) throws IOException{
		this.enviarRequest(this.reqFactory.getRequest(Utils.ID_DIRECTORIO, nickname)); 
	}
	
	public void enviarRequestRegistrar(String nickname) throws IOException {
		this.enviarRequest(this.reqFactory.getRequest(Utils.ID_REGISTRO, nickname));
	}
	
	public void enviarRequestLogin(String nickname) throws IOException {
		this.enviarRequest(this.reqFactory.getRequest(Utils.ID_LOGIN, nickname));
	}
}
