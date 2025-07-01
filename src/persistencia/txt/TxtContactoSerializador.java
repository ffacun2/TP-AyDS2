package persistencia.txt;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import model.Contacto;
import persistencia.ContactoSerializador;

public class TxtContactoSerializador extends ContactoSerializador {

	public TxtContactoSerializador (String path) {
		super(path);
	}
	
	@Override
	public void serializar(Contacto contacto) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getPath(), true))) {
			writer.write(contacto.getNickname());
			writer.newLine();
		}
		catch (IOException e) {
			throw new RuntimeException("Error al serializando contacto en txt",e);
		}
	}

}
