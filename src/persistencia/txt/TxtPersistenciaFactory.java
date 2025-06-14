package persistencia.txt;

import persistencia.ContactoDeserializador;
import persistencia.ContactoSerializador;
import persistencia.MensajeDeserializador;
import persistencia.MensajeSerializador;
import persistencia.PersistenciaFactory;

public class TxtPersistenciaFactory extends PersistenciaFactory {

	public TxtPersistenciaFactory (String nickname, String extension) {
		super(nickname,extension);
	}
	
	@Override
	public ContactoSerializador crearContactoSerializador() {
		return new TxtContactoSerializador(getNickname()+"-contactos."+getExtension());
	}

	@Override
	public MensajeSerializador crearMensajeSerializador() {
		return new TxtMensajeSerializador(getNickname()+"-mensajes."+getExtension());
	}

	@Override
	public ContactoDeserializador crearContactoDeserializador() {
		return new TxtContactoDeserializador(getNickname()+"-contactos."+getExtension());
	}

	@Override
	public MensajeDeserializador crearMensajeDeserializador() {
		return new TxtMensajeDeserializador(getNickname()+"-mensajes."+getExtension());
	}
	
}
