package persistencia.xml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import model.Mensaje;
import persistencia.MensajeDeserializador;
import persistencia.MensajeList;

public class XmlMensajeDeserializador implements MensajeDeserializador {

	private String path;
	private XmlMapper mapper;
	
	public XmlMensajeDeserializador(String path) {
		this.path = path;
		this.mapper = new XmlMapper();
	}
	
	@Override
	public List<Mensaje> deserializar() {
		try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
			String contenido = reader.lines().collect(Collectors.joining());
			if (contenido == null || contenido.isBlank() || contenido.isEmpty())
				return List.of(); // Retorna una lista vacía si el archivo está vacío
			
			return mapper.readValue(contenido, MensajeList.class).getMensajes();
		} 
		catch (Exception e) {
			throw new RuntimeException("Error al deserializar el mensaje desde XML,"+e.getMessage(), e);
		}
	}

}
