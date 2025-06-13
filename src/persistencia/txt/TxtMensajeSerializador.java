package persistencia.txt;


import model.Mensaje;
import persistencia.MensajeSerializador;

public class TxtMensajeSerializador implements MensajeSerializador {

	@Override
	public String serializar(Mensaje mensaje) {
			return mensaje.getNickEmisor() + 
					"," + mensaje.getNickReceptor() + 
					"," + mensaje.getHora() + 
					","+mensaje.getCuerpo() + 
					"\n";
	}

}
