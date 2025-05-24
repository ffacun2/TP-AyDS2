package persistencia.factory;

import persistencia.template.PersistenciaTemplate;

public class TxtPersistenciaFactory implements PersistenciaFactory {


	@Override
	public <T> PersistenciaTemplate<T> crearSerializador(String path) {
		return null;
	}

}
