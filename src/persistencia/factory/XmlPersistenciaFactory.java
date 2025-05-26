package persistencia.factory;

import persistencia.Persistencia;
import persistencia.PersistenciaXML;

public class XmlPersistenciaFactory extends PersistenciaFactory{

	@Override
	public Persistencia crearSerializador(String path) {
		return new PersistenciaXML(path);
	}


}
