package persistencia.factory;

import persistencia.Persistencia;
import persistencia.PersistenciaJSON;

public class JsonPersistenciaFactory extends PersistenciaFactory{


	@Override
	public Persistencia crearSerializador(String path) {
		return new PersistenciaJSON(path);
	}

}
