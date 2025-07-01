package persistencia;


import java.io.File;

import model.Contacto;

public abstract class ContactoSerializador {
	
	private String path;
	
	protected ContactoSerializador(String path) {
		this.path = path;
	}
	
	public abstract void serializar(Contacto contacto);
	
	public void crearArchivoContacto() throws Exception {
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
