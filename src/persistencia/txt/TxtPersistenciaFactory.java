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
		return new TxtContactoSerializador(getDirectorio()+getNickname()+"-contactos.txt");
	}

	@Override
	public MensajeSerializador crearMensajeSerializador() {
		return new TxtMensajeSerializador(getDirectorio()+getNickname()+"-mensajes.txt");
	}

	@Override
	public ContactoDeserializador crearContactoDeserializador() {
		return new TxtContactoDeserializador(getDirectorio()+getNickname()+"-contactos.txt");
	}

	@Override
	public MensajeDeserializador crearMensajeDeserializador() {
		return new TxtMensajeDeserializador(getDirectorio()+getNickname()+"-mensajes.txt");
	}
	
}
