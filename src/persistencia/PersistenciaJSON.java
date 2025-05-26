package persistencia;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PersistenciaJSON extends Persistencia {
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	public PersistenciaJSON(String path) {
		super(path);
	}
	
	@Override
	protected void serializar(Object objeto) throws Exception {
		mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, objeto);
	}

	@Override
	protected Object deserializar(Class<?> clase) throws Exception{
		return mapper.readValue(archivo, clase);
	}

}
