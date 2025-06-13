package persistencia;


import model.Mensaje;

public interface MensajeSerializador {
	String serializar(Mensaje mensaje);
}
