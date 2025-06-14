package persistencia.txt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Contacto;
import persistencia.ContactoDeserializador;

public class TxtContactoDeserializador implements ContactoDeserializador{

	private String path;
	
	public TxtContactoDeserializador (String path) {
		this.path = path;
	}
	
	@Override
	public List<Contacto> deserializar() {
		List<Contacto> contactos = new ArrayList<>();
		String linea;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
			while ((linea = reader.readLine()) != null) {
				if (!linea.trim().isEmpty()) {
					contactos.add(new Contacto(linea.trim()));
				}
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Error al leer el archivo: "+this.path, e);
		}
		return contactos;
	}

}
