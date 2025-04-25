package requests;

import java.io.Serializable;
import java.util.ArrayList;

import model.Contacto;

public class DirectoriosResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Contacto> nicks;

	public DirectoriosResponse (ArrayList<Contacto> nicks) {
		this.nicks = nicks;
	}
	
	public ArrayList<Contacto> getNicks () {
		return this.nicks;
	}
}
