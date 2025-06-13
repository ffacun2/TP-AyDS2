package persistencia.txt;


import model.Contacto;
import persistencia.ContactoSerializador;

public class TxtContactoSerializador implements ContactoSerializador {

	@Override
	public String serializar(Contacto contacto) {
		return contacto.getNickname() + "\n";
	}
}
