package persistencia.txt;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import model.Mensaje;
import persistencia.MensajeSerializador;

public class TxtMensajeSerializador extends MensajeSerializador {

	
	public TxtMensajeSerializador (String path) {
		super(path);
	}
	
	@Override
	public void serializar(Mensaje mensaje) {
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getPath(),true))) {
			writer.write(mensaje.getNickEmisor() + 
					"," + mensaje.getNickReceptor() + 
					"," + mensaje.getHora() + 
					","+mensaje.getCuerpo()
					);
			writer.newLine();
		}
		catch (IOException e) {
			throw new RuntimeException("Error al abrir el archivo "+getPath(), e);
		}
	}
}
