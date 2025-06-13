package persistencia.txt;

import java.util.ArrayList;
import java.util.List;

import model.Mensaje;
import persistencia.MensajeDeserializador;

public class TxtMensajeDeserializador implements MensajeDeserializador {

	@Override
	public List<Mensaje> deserializar(String contenido) {
		List<Mensaje> mensajes = new ArrayList<>();
		String[ ] lineas ;
		
		if (contenido == null || contenido.isEmpty())
			return List.of(); // Retorna una lista vacía si el contenido es nulo o vacío
	
		lineas = contenido.split("\n");
		for (String linea : lineas) {
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
		return mensajes;
	}

}
