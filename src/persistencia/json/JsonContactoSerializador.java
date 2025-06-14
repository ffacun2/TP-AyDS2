package persistencia.json;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import model.Contacto;
import persistencia.ContactoSerializador;

public class JsonContactoSerializador implements ContactoSerializador {
	
	private String path;
	private JsonMapper mapper;
	
	public JsonContactoSerializador(String path) {
		this.path = path;
		this.mapper = new JsonMapper();
	}
	
	@Override
	public void serializar(Contacto contacto) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.path, true))){
			writer.write(mapper.writeValueAsString(contacto));
			writer.newLine();
		} 
		catch(JsonProcessingException e) {
			throw new RuntimeException("Error serializando contacto a JSON", e);
		}
		catch (IOException e) {
			throw new RuntimeException("Error al escribir en el archivo: " + this.path, e);
		}
	}
	

}
