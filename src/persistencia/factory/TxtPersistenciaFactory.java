package persistencia.factory;

import persistencia.dao.PersistenciaTemplate;

public class TxtPersistenciaFactory implements PersistenciaFactory {


	@Override
	public <T> PersistenciaTemplate<T> crearSerializador(String path) {
		return null;
	}

}
