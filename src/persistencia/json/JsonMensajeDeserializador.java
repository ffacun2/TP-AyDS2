package persistencia.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.json.JsonMapper;

import model.Mensaje;
import persistencia.MensajeDeserializador;

public class JsonMensajeDeserializador implements MensajeDeserializador {

	private String path;
	private JsonMapper mapper;

	public JsonMensajeDeserializador (String path) {
		this.path = path;
		this.mapper = new JsonMapper();
	}
	
	@Override
	public List<Mensaje> deserializar() {
		List<Mensaje> mensajes = new ArrayList<Mensaje>();
		String linea;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
			while ((linea = reader.readLine()) != null) {
				if (!linea.trim().isEmpty()) {
					Mensaje m = mapper.readValue(linea, Mensaje.class);
					mensajes.add(m);
				}
			}
		} 
		catch (Exception e) {
			throw new RuntimeException("Error al deserializar los mensajes desde JSON", e);
		}
		
		
		return mensajes;
	}

}
