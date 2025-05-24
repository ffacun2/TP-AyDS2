package persistencia.template;

public class PersistenciaTXT<T> extends PersistenciaTemplate<T> {

	public PersistenciaTXT(String archivo) {
		super(archivo);
	}

	@Override
	protected void serializar(T objeto) throws Exception {
		
	}

	@Override
	protected T deserializar(Class<T> clase) throws Exception {
		return null;
	}

}
