package persistencia.xml;

import persistencia.ContactoDeserializador;
import persistencia.ContactoSerializador;
import persistencia.MensajeDeserializador;
import persistencia.MensajeSerializador;
import persistencia.PersistenciaFactory;

public class XmlPersistenciaFactory extends PersistenciaFactory {
	
	public XmlPersistenciaFactory (String directorio, String nickname) {
		super(directorio,nickname);
	}

	@Override
	public ContactoSerializador crearContactoSerializador() {
		return new XmlContactoSerializador(getDirectorio()+getNickname()+"-contactos.xml");
	}

	@Override
	public MensajeSerializador crearMensajeSerializador() {
		return new XmlMensajeSerializador(getDirectorio()+getNickname()+"-mensajes.xml");
	}

	@Override
	public ContactoDeserializador crearContactoDeserializador() {
		return new XmlContactoDeserializador(getDirectorio()+getNickname()+"-contactos.xml");
	}

	@Override
	public MensajeDeserializador crearMensajeDeserializador() {
		return new XmlMensajeDeserializador(getDirectorio()+getNickname()+"-mensajes.xml");
	}
	
}
