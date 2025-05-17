package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import interfaces.IEnviable;
import model.Contacto;
import model.Mensaje;
import requests.DirectoriosResponse;
import requests.MensajeResponse;

@SuppressWarnings("deprecation")
public class HandleCliente extends Observable implements Runnable {
	
	private Servidor servidor;
	private Socket socket;
	private List<Mensaje> mensajesPendientes;
	private boolean estado;
	private Thread hilo;
	
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public HandleCliente (Socket socket, Servidor servidor) throws IOException {
		this.socket = socket;
		this.servidor = servidor;
		this.mensajesPendientes = new ArrayList<Mensaje>();
		this.estado = true;
	}
	
	public HandleCliente(List<Mensaje> msjPendiente, Servidor servidor) {
		this.mensajesPendientes = msjPendiente;
		this.servidor = servidor;
		this.estado = false;
	}
	
	@Override
	public void run() {
		System.out.println("Iniciando HandleCliente");
		while (estado && servidor.getEstado()) {
			try {
				IEnviable req;
				
				socket.setSoTimeout(2000);
				req = (IEnviable)this.input.readObject();
				req.manejarRequest(servidor,this.socket);
			}
			catch (SocketTimeoutException e) {
				//No hace nada, si se pasa de 2 seg vuelve a ejecutar
				//Es para controlar el estado del servidor
			}
			catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}	
		}
		try {
			socket.close();
		}catch (Exception e) {
		}
		System.out.println("Cerrando handleCliente");
	}

	public void mandarMsjPendientes() throws IOException {
		for(Mensaje mensaje: this.mensajesPendientes)
			enviarMensaje(mensaje);
		this.mensajesPendientes.clear();

	}
	
	public void enviarDirectorio(ArrayList<Contacto> directorio) throws IOException {
		this.output.reset();
		this.output.writeObject(new DirectoriosResponse(directorio));
		this.output.flush();
	}
	
	public void enviarMensaje(Mensaje mensaje) throws IOException {
		this.output.reset();
		this.output.writeObject(new MensajeResponse(mensaje));
		this.output.flush();
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public List<Mensaje> getMensajesPendientes() {
		return mensajesPendientes;
	}
	
	public void addMensajePendiente(Mensaje mensaje) {
		this.mensajesPendientes.add(mensaje);
	}
	
	public boolean getEstado() {
		return this.estado;
	}
	
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public int getPuertoSocket() {
		return this.socket.getLocalPort();
	}
	
	public String getIpSocket() {
		return this.socket.getLocalAddress().getHostAddress();
	}
	

	public void setObservador(Observer o) {
		this.addObserver(o);
	}

	public void setInput(ObjectInputStream input) {
		this.input = input;
	}

	public void setOutput(ObjectOutputStream output) {
		this.output = output;
	}

	public ObjectInputStream getInput() {
		return input;
	}

	public ObjectOutputStream getOutput() {
		return output;
	}
	
	public void setHilo(Thread hilo) {
		this.hilo = hilo;
	}
	
	public Thread getHilo() {
		return this.hilo;
	}
	
}
