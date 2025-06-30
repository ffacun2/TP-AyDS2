package requests;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import cliente.ServidorAPI;
import interfaces.IRecibible;
import model.Contacto;

public class DirectoriosResponse implements Serializable, IRecibible{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Contacto> nicks;

	public DirectoriosResponse (ArrayList<Contacto> nicks) {
		this.nicks = nicks;
	}
	
	public ArrayList<String> getNicks () {
		Iterator<Contacto> it = this.nicks.iterator();
		ArrayList<String> lista = new ArrayList<String>();
		
		while (it.hasNext()) {
			lista.add(it.next().getNickname());
		}
		return lista;
	}

	@Override
	public void manejarResponse(ServidorAPI servidor) throws IOException {
		servidor.setResponse(this);
	}
	
}
