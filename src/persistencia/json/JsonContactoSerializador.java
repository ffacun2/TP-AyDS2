package persistencia.json;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import model.Contacto;
import persistencia.ContactoSerializador;

public class JsonContactoSerializador extends ContactoSerializador {
	
	private JsonMapper mapper;
	
	public JsonContactoSerializador(String path) {
		super(path);
		this.mapper = new JsonMapper();
	}
	
	@Override
	public void serializar(Contacto contacto) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getPath(), true))){
			writer.write(mapper.writeValueAsString(contacto));
			writer.newLine();
		} 
		catch(JsonProcessingException e) {
			throw new RuntimeException("Error serializando contacto a JSON", e);
		}
		catch (IOException e) {
			throw new RuntimeException("Error al escribir en el archivo: " + getPath(), e);
		}
	}

	

}
