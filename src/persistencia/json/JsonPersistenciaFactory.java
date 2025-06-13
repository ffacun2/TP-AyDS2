package persistencia.json;

import persistencia.ContactoDeserializador;
import persistencia.ContactoSerializador;
import persistencia.MensajeDeserializador;
import persistencia.MensajeSerializador;
import persistencia.PersistenciaFactory;

public class JsonPersistenciaFactory extends PersistenciaFactory {

	@Override
	public ContactoSerializador crearContactoSerializador() {
		return new JsonContactoSerializador();
	}

	@Override
	public MensajeSerializador crearMensajeSerializador() {
		return new JsonMensajeSerializador();
	}

	@Override
	public ContactoDeserializador crearContactoDeserializador() {
		return new JsonContactoDeserializador();
	}

	@Override
	public MensajeDeserializador crearMensajeDeserializador() {
		return new JsonMensajeDeserializador();
	}
	
}
