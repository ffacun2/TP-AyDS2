package persistencia.dao;


import com.fasterxml.jackson.databind.ObjectMapper;


public class PersistenciaJSON<T> extends PersistenciaTemplate<T> {
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	public PersistenciaJSON(String path) {
		super(path);
	}
	
	@Override
	protected void serializar(T objeto) throws Exception {
		mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, objeto);
	}

	@Override
	protected T deserializar(Class<T> clase) throws Exception{
		return mapper.readValue(archivo, clase);
	}

}
