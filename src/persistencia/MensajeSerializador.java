package persistencia;


import java.io.File;

import model.Mensaje;

public abstract class MensajeSerializador {
	
	private String path;
	
	protected MensajeSerializador(String path) {
		this.path = path;
	}
	
	public abstract void serializar(Mensaje mensaje);
	
	public void crearArchivoMensaje() throws Exception {
		File file = new File(this.path);
		if (!file.exists()) {
			if (!file.createNewFile()) {
				throw new Exception("No se pudo crear el archivo: " + file.getAbsolutePath());
			}
		}
		else {
			throw new Exception("El archivo ya existe: " + file.getAbsolutePath());
		}
	}
	
	protected String getPath() {
		return this.path;
	}
}
