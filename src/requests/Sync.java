package requests;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import interfaces.IEnviable;
import interfaces.IServidor;
import servidor.HandleClienteDTO;
import servidor.Servidor;

public class Sync implements IEnviable { 
	
	private static final long serialVersionUID = 1L;
	private ArrayList<HandleClienteDTO> snapshot;
	
	public Sync(ArrayList<HandleClienteDTO> snapshot) {
		this.snapshot = snapshot;
	}
	
	public ArrayList<HandleClienteDTO> getSnapshot() {
		return this.snapshot;
	}

	@Override
	public void manejarRequest(IServidor servidor, Socket socket) throws IOException {
		((Servidor)servidor).guardarSnapShot(snapshot);
	}
}
