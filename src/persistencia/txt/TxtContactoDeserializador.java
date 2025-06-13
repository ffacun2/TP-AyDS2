package persistencia.txt;

import java.util.ArrayList;
import java.util.List;

import model.Contacto;
import persistencia.ContactoDeserializador;

public class TxtContactoDeserializador implements ContactoDeserializador{

	@Override
	public List<Contacto> deserializar(String contenido) {
		List<Contacto> contactos = new ArrayList<>();
		String[] lineas = contenido.split("\n");
		
		for (String linea : lineas) {
			if (!linea.trim().isEmpty()) {
				Contacto contacto = new Contacto();
				contacto.setNickname(linea.trim());
				contactos.add(contacto);
			}
		}
		return contactos;
	}

}
