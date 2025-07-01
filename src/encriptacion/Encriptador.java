package encriptacion;

import model.Mensaje;

public class Encriptador {
	private TecnicaEncriptacion tecnica;
	
	public void setTecnica(TecnicaEncriptacion tecnica) {
		this.tecnica = tecnica;
	}
	
	public Mensaje encriptarMensaje(Mensaje mensaje, String clave) {
		return this.tecnica.encriptarMensaje(mensaje, clave);
	}
	
	public Mensaje desencriptarMensaje(Mensaje mensaje, String clave) {
		return this.tecnica.desencriptarMensaje(mensaje, clave);
	}
}
