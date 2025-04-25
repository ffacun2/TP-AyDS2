package requests;

import java.util.ArrayList;

import model.Contacto;

public class DirectoriosResponse {
	
	private ArrayList<Contacto> nicks;

	public DirectoriosResponse (ArrayList<Contacto> nicks) {
		this.nicks = nicks;
	}
	
	public ArrayList<Contacto> getNicks () {
		return this.nicks;
	}
}
