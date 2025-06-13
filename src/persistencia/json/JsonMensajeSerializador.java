package persistencia.json;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import model.Mensaje;
import persistencia.MensajeSerializador;

public class JsonMensajeSerializador implements MensajeSerializador {

	private JsonMapper mapper = new JsonMapper();
	
	@Override
	public String serializar(Mensaje mensaje) {
		try {
			return mapper.writeValueAsString(mensaje);
		} 
		catch (JsonProcessingException e) {
			throw new RuntimeException("Error serializando mensaje a JSON, ", e);
		}
	}

}
