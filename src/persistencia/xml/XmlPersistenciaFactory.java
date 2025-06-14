package persistencia.xml;

import persistencia.ContactoDeserializador;
import persistencia.ContactoSerializador;
import persistencia.MensajeDeserializador;
import persistencia.MensajeSerializador;
import persistencia.PersistenciaFactory;

public class XmlPersistenciaFactory extends PersistenciaFactory {
	
	public XmlPersistenciaFactory (String nickname, String extension) {
		super(nickname,extension);
	}

	@Override
	public ContactoSerializador crearContactoSerializador() {
		return new XmlContactoSerializador(getNickname()+"-contactos."+getExtension());
	}

	@Override
	public MensajeSerializador crearMensajeSerializador() {
		return new XmlMensajeSerializador(getNickname()+"-mensajes."+getExtension());
	}

	@Override
	public ContactoDeserializador crearContactoDeserializador() {
		return new XmlContactoDeserializador(getNickname()+"-contactos."+getExtension());
	}

	@Override
	public MensajeDeserializador crearMensajeDeserializador() {
		return new XmlMensajeDeserializador(getNickname()+"-mensajes."+getExtension());
	}
	
}
