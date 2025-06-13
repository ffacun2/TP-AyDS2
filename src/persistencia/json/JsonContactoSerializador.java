package persistencia.json;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import model.Contacto;
import persistencia.ContactoSerializador;

public class JsonContactoSerializador implements ContactoSerializador {
	
	private JsonMapper mapper = new JsonMapper();
	
	@Override
	public String serializar(Contacto contacto) {
		try {
			return mapper.writeValueAsString(contacto);
		} 
		catch (JsonProcessingException e) {
			throw new RuntimeException("Error serializando contacto a JSON: " + contacto.getNickname(), e);
		}
	}

}
