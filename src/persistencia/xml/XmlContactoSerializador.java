package persistencia.xml;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import model.Contacto;
import persistencia.ContactoSerializador;

public class XmlContactoSerializador implements ContactoSerializador {
	
	private XmlMapper mapper = new XmlMapper();

	@Override
	public String serializar(Contacto contacto) {
		return null;
	}
	
}
