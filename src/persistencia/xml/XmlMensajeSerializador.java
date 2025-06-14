package persistencia.xml;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import model.Mensaje;
import persistencia.MensajeList;
import persistencia.MensajeSerializador;

public class XmlMensajeSerializador implements MensajeSerializador {

	private String path;
	private XmlMapper mapper;
	private XmlMensajeDeserializador deserializador;
	
	public XmlMensajeSerializador(String path) {
		this.path = path;
		this.mapper = new XmlMapper();
		this.deserializador = new XmlMensajeDeserializador(this.path);
		mapper.enable(SerializationFeature.INDENT_OUTPUT); // Para formatear el XML de salida
	}
	
	@Override
	public void serializar(Mensaje mensaje) {
		ArrayList<Mensaje> mensajesList;
		mensajesList = new ArrayList<>(deserializador.deserializar());

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.path))) {
			mensajesList.add(mensaje);
			writer.write(mapper.writeValueAsString(new MensajeList(mensajesList)));
		} 
		catch (JsonProcessingException e) {
			throw new RuntimeException("Error al serializar mensaje a XML", e);
		}
		catch (IOException e) {
			throw new RuntimeException("Error al escribir en el archivo: " + this.path, e);
		}
	}

}
