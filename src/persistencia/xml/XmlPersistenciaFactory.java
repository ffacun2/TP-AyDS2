package persistencia.xml;

import persistencia.ContactoDeserializador;
import persistencia.ContactoSerializador;
import persistencia.MensajeDeserializador;
import persistencia.MensajeSerializador;
import persistencia.PersistenciaFactory;

public class XmlPersistenciaFactory extends PersistenciaFactory {

	@Override
	public ContactoSerializador crearContactoSerializador() {
		return new XmlContactoSerializador();
	}

	@Override
	public MensajeSerializador crearMensajeSerializador() {
		return new XmlMensajeSerializador();
	}

	@Override
	public ContactoDeserializador crearContactoDeserializador() {
		return new XmlContactoDeserializador();
	}

	@Override
	public MensajeDeserializador crearMensajeDeserializador() {
		return new XmlMensajeDeserializador();
	}
	
}
