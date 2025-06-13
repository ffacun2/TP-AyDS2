package persistencia.xml;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import model.Contacto;
import persistencia.ContactoDeserializador;
import persistencia.ContactoList;

public class XmlContactoDeserializador implements ContactoDeserializador {
	
	private XmlMapper mapper = new XmlMapper();

	@Override
	public List<Contacto> deserializar(String contenido) {
		try {
			return mapper.readValue(contenido, ContactoList.class).getContactos();
		} 
		catch (Exception e) {
			throw new RuntimeException("Error al deserializar el contacto desde XML", e);
		}
	}

}
