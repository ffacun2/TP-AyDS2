package persistencia.txt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Mensaje;
import persistencia.MensajeDeserializador;

public class TxtMensajeDeserializador implements MensajeDeserializador {
	
	private String path;
	
	public TxtMensajeDeserializador (String path) {
		this.path = path;
	}

	@Override
	public List<Mensaje> deserializar() {
		List<Mensaje> mensajes = new ArrayList<>();
		String linea;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
			while ((linea = reader.readLine()) != null) {
				if (!linea.trim().isEmpty()) {
					String[] partes = linea.split(",");
					if (partes.length == 4) {
						Mensaje mensaje = new Mensaje();
						mensaje.setNickEmisor(partes[0].trim());
						mensaje.setNickReceptor(partes[1].trim());
						mensaje.setHora(partes[2].trim());
						mensaje.setCuerpo(partes[3].trim());
						mensajes.add(mensaje);
					}
				}
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Error al leer el archivo "+this.path,e);
		}
		return mensajes;
	}

}
