package persistencia.txt;

import persistencia.ContactoDeserializador;
import persistencia.ContactoSerializador;
import persistencia.MensajeDeserializador;
import persistencia.MensajeSerializador;
import persistencia.PersistenciaFactory;

public class TxtPersistenciaFactory extends PersistenciaFactory {

	@Override
	public ContactoSerializador crearContactoSerializador() {
		return new TxtContactoSerializador();
	}

	@Override
	public MensajeSerializador crearMensajeSerializador() {
		return new TxtMensajeSerializador();
	}

	@Override
	public ContactoDeserializador crearContactoDeserializador() {
		return new TxtContactoDeserializador();
	}

	@Override
	public MensajeDeserializador crearMensajeDeserializador() {
		return new TxtMensajeDeserializador();
	}
	
}
