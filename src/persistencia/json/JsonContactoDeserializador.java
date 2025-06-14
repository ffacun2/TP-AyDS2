package persistencia.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.json.JsonMapper;

import model.Contacto;
import persistencia.ContactoDeserializador;

public class JsonContactoDeserializador implements ContactoDeserializador {
	
	private String path;
	private JsonMapper mapper;

	public JsonContactoDeserializador (String path) {
		this.path = path;
		this.mapper = new JsonMapper();
	}
	
	@Override
	public List<Contacto> deserializar() {
		List<Contacto> contactos = new ArrayList<>();
		String linea;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
			while ((linea= reader.readLine()) != null) {
				if (!linea.trim().isEmpty()) {
					Contacto c = mapper.readValue(linea, Contacto.class);
					contactos.add(c);
				}
			}
		}
		catch (Exception e) {
			throw new RuntimeException("Error al deserializar los contactos desde JSON", e);
		}
		
		return contactos;
	}

}
