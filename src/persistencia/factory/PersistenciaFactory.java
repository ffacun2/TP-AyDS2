package persistencia.factory;

import persistencia.dao.PersistenciaTemplate;

public interface  PersistenciaFactory {

	<T> PersistenciaTemplate<T> crearSerializador(String path);
}
