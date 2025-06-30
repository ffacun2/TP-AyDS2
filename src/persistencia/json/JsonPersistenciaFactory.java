package persistencia.json;

import persistencia.ContactoDeserializador;
import persistencia.ContactoSerializador;
import persistencia.MensajeDeserializador;
import persistencia.MensajeSerializador;
import persistencia.PersistenciaFactory;

public class JsonPersistenciaFactory extends PersistenciaFactory {
	
	
	public JsonPersistenciaFactory (String directorio,String nickname) {
		super(directorio,nickname);
	}

	@Override
	public ContactoSerializador crearContactoSerializador() {
		return new JsonContactoSerializador(getDirectorio()+getNickname()+"-contactos.json");
	}

	@Override
	public MensajeSerializador crearMensajeSerializador() {
		return new JsonMensajeSerializador(getDirectorio()+getNickname()+"-mensajes.json");
	}

	@Override
	public ContactoDeserializador crearContactoDeserializador() {
		return new JsonContactoDeserializador(getDirectorio()+getNickname()+"-contactos.json");
	}

	@Override
	public MensajeDeserializador crearMensajeDeserializador() {
		return new JsonMensajeDeserializador(getDirectorio()+getNickname()+"-mensajes.json");
	}
	
}
