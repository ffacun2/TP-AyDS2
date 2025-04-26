package requests;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import api.ServidorAPI;
import interfaces.IRecibible;
import model.Contacto;

public class DirectoriosResponse implements Serializable, IRecibible{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Contacto> nicks;

	public DirectoriosResponse (ArrayList<Contacto> nicks) {
		this.nicks = nicks;
	}
	
	public ArrayList<Contacto> getNicks () {
		return this.nicks;
	}

	@Override
	public void manejarResponse(ServidorAPI servidor) throws IOException {
		servidor.directorioRecibido(this);
	}
	
}
