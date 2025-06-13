package persistencia;

import java.util.List;

import model.Mensaje;

public interface MensajeDeserializador {
	List<Mensaje> deserializar(String contenido);
}
