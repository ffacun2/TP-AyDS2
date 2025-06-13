package persistencia.xml;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import model.Mensaje;
import persistencia.MensajeDeserializador;
import persistencia.MensajeList;

public class XmlMensajeDeserializador implements MensajeDeserializador {

	private XmlMapper mapper = new XmlMapper();
	
	@Override
	public List<Mensaje> deserializar(String contenido) {
		try {
			return mapper.readValue(contenido, MensajeList.class).getMensajes();
		} 
		catch (Exception e) {
			throw new RuntimeException("Error al deserializar el mensaje desde XML", e);
		}
	}

}
