package persistencia.xml;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import model.Contacto;
import persistencia.ContactoList;
import persistencia.ContactoSerializador;

public class XmlContactoSerializador extends ContactoSerializador {
	
	private XmlMapper mapper;
	private XmlContactoDeserializador deserializo;
	
	public XmlContactoSerializador (String path) {
		super(path);
		this.mapper = new XmlMapper();
		this.deserializo = new XmlContactoDeserializador(path);
		mapper.enable(SerializationFeature.INDENT_OUTPUT); // Para formatear el XML de salida
	}

	@Override
	public void serializar(Contacto contacto) {
		ArrayList<Contacto> contactosList;
		contactosList = new ArrayList<>(deserializo.deserializar());

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getPath()))) {
			contactosList.add(contacto);
			writer.write(mapper.writeValueAsString(new ContactoList(contactosList)));
		}
		catch (JsonProcessingException e) {
			throw new RuntimeException("Error al serializar contacto a XML", e);
		}
		catch (IOException e) {
			throw new RuntimeException("Error al escribir en el archivo: " + getPath(), e);
		}
		
	}
	
}
