package persistencia.factory;

import persistencia.Persistencia;
import persistencia.PersistenciaTXT;

public class TxtPersistenciaFactory extends PersistenciaFactory {


	@Override
	public Persistencia crearSerializador(String path) {
		return new PersistenciaTXT(path);
	}

}
