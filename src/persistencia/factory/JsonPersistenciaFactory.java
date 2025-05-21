package persistencia.factory;

import persistencia.dao.PersistenciaJSON;
import persistencia.dao.PersistenciaTemplate;

public class JsonPersistenciaFactory implements PersistenciaFactory{


	@Override
	public <T> PersistenciaTemplate<T> crearSerializador(String path) {
		return new PersistenciaJSON<T>(path);
	}

}
