package persistencia.factory;

import persistencia.template.PersistenciaTemplate;
import persistencia.template.PersistenciaXML;

public class XmlPersistenciaFactory implements PersistenciaFactory{

	@Override
	public <T> PersistenciaTemplate<T> crearSerializador(String path) {
		return new PersistenciaXML<T>(path);
	}


}
