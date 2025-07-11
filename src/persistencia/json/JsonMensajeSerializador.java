package persistencia.json;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import model.Mensaje;
import persistencia.MensajeSerializador;

public class JsonMensajeSerializador extends MensajeSerializador {

	private JsonMapper mapper;
	
	public JsonMensajeSerializador (String path) {
		super(path);
		this.mapper = new JsonMapper();
	}
	
	@Override
	public void serializar(Mensaje mensaje) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getPath(), true))){
			writer.write(mapper.writeValueAsString(mensaje));
			writer.newLine();
		} 
		catch (JsonProcessingException e) {
			throw new RuntimeException("Error serializando mensaje a JSON", e);
		}
		catch (IOException e) {
			throw new RuntimeException("Error al leer el archivo: "+getPath(), e);
		}
	}

}
