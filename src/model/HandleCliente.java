package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import interfaces.IEnviable;

@SuppressWarnings("deprecation")
public class HandleCliente extends Observable implements Runnable{
	
	private Servidor servidor;
	private Socket socket;
	private List<Mensaje> mensajesPendientes;
	private boolean estado;
	
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public HandleCliente (Socket socket, Servidor servidor) {
		this.socket = socket;
		this.servidor = servidor;
		this.mensajesPendientes = new ArrayList<Mensaje>();
		this.estado = true;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				
				IEnviable req = (IEnviable)this.input.readObject();
				req.manejarRequest(servidor,this.socket);
				System.out.println("Llegada la request");
			} 
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
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
	
	public void enviarMensajes () {
		//manda los mensajes pendientes
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
	
}
