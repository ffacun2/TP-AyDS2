package persistencia.json;

import persistencia.ContactoDeserializador;
import persistencia.ContactoSerializador;
import persistencia.MensajeDeserializador;
import persistencia.MensajeSerializador;
import persistencia.PersistenciaFactory;

public class JsonPersistenciaFactory extends PersistenciaFactory {
	
	
	public JsonPersistenciaFactory (String nickname, String extension) {
		super(nickname,extension);
	}

	@Override
	public ContactoSerializador crearContactoSerializador() {
		return new JsonContactoSerializador(getNickname()+"-contactos."+getExtension());
	}

	@Override
	public MensajeSerializador crearMensajeSerializador() {
		return new JsonMensajeSerializador(getNickname()+"-mensajes."+getExtension());
	}

	@Override
	public ContactoDeserializador crearContactoDeserializador() {
		return new JsonContactoDeserializador(getNickname()+"-contactos."+getExtension());
	}

	@Override
	public MensajeDeserializador crearMensajeDeserializador() {
		return new JsonMensajeDeserializador(getNickname()+"-mensajes."+getExtension());
	}
	
}
