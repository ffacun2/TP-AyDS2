package persistencia.factory;

import persistencia.dao.PersistenciaTemplate;
import persistencia.dao.PersistenciaXML;

public class XmlPersistenciaFactory implements PersistenciaFactory{

	@Override
	public <T> PersistenciaTemplate<T> crearSerializador(String path) {
		return new PersistenciaXML<T>(path);
	}


}
