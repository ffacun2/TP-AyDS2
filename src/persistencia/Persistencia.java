package persistencia;

import java.io.File;

import model.Usuario;

public abstract class Persistencia {
	
	protected final File archivo;
	
	public Persistencia(String archivo) {
		this.archivo = new File(archivo);
	}
	
	
	protected abstract void serializar(Object objeto) throws Exception;
	protected abstract void deserializar(Usuario usuario) throws Exception;

	public final void guardar(Object objeto) throws Exception {
		serializar(objeto);
	}
	
	public final void cargar(Usuario usuario) throws Exception{
		deserializar(usuario);
	}
	
	
}
