package encriptacion;

import model.Mensaje;

public interface TecnicaEncriptacion {
	public Mensaje encriptarMensaje(Mensaje mensaje, String clave);
	public Mensaje desencriptarMensaje(Mensaje mensaje, String clave);
}
