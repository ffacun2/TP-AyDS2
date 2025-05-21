package persistencia.dao;

import java.io.File;

public abstract class PersistenciaTemplate<T> {
	
	protected final File archivo;
	
	public PersistenciaTemplate(String archivo) {
		this.archivo = new File(archivo);
	}
	
	
	protected abstract void serializar(T objeto) throws Exception;
	protected abstract T deserializar(Class<T> clase) throws Exception;

	public final void guardar(T objeto) throws Exception {
		abrirArchivo();
		serializar(objeto);
		cerrarArchivo();
	}
	
	public final T cargar(Class<T> clase) throws Exception{
		abrirArchivo();
		T objeto = deserializar(clase);
		cerrarArchivo();
		return objeto;
	}
	
	protected void abrirArchivo() {
		
	}
	
	
	protected void cerrarArchivo() {
		
	}
	
}
