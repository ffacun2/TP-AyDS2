package persistencia.factory;

import persistencia.template.PersistenciaJSON;
import persistencia.template.PersistenciaTemplate;

public class JsonPersistenciaFactory implements PersistenciaFactory{


	@Override
	public <T> PersistenciaTemplate<T> crearSerializador(String path) {
		return new PersistenciaJSON<T>(path);
	}

}
