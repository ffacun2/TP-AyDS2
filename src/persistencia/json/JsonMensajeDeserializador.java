package persistencia.json;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.json.JsonMapper;

import model.Mensaje;
import persistencia.MensajeDeserializador;

public class JsonMensajeDeserializador implements MensajeDeserializador {

	private JsonMapper mapper = new JsonMapper();
	
	@Override
	public List<Mensaje> deserializar(String contenido) {
		List<Mensaje> mensajes = new ArrayList<Mensaje>();
		String linea;
		
		try (BufferedReader reader = new BufferedReader(new StringReader(contenido))) {
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
