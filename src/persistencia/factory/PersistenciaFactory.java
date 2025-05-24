package persistencia.factory;

import persistencia.template.PersistenciaTemplate;

public interface  PersistenciaFactory {

	<T> PersistenciaTemplate<T> crearSerializador(String path);
}
