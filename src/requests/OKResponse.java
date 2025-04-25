package requests;

import java.io.IOException;
import java.net.Socket;

import interfaces.IEnviable;
import model.Servidor;

public class OKResponse implements IEnviable{

	private boolean success;
	
	public OKResponse (boolean success) {
		this.success = success;
	}
	
	public boolean isSuccess() {
		return success;
	}

	@Override
	public void manejarRequest(Servidor servidor, Socket socket) throws IOException {
		
	}
}
