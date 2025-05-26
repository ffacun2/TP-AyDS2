package persistencia;

import java.io.File;

public abstract class Persistencia {
	
	protected final File archivo;
	
	public Persistencia(String archivo) {
		this.archivo = new File(archivo);
	}
	
	
	protected abstract void serializar(Object objeto) throws Exception;
	protected abstract Object deserializar(Class<?> clase) throws Exception;

	public final void guardar(Object objeto) throws Exception {
		serializar(objeto);
	}
	
	public final <T> T cargar(Class<T> clase) throws Exception{
		return clase.cast(deserializar(clase));
	}
	
	
}
