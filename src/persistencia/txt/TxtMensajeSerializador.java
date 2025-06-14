package persistencia.txt;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import model.Mensaje;
import persistencia.MensajeSerializador;

public class TxtMensajeSerializador implements MensajeSerializador {

	private String path;
	
	public TxtMensajeSerializador (String path) {
		this.path = path;
	}
	
	@Override
	public void serializar(Mensaje mensaje) {
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.path,true))) {
			writer.write(mensaje.getNickEmisor() + 
					"," + mensaje.getNickReceptor() + 
					"," + mensaje.getHora() + 
					","+mensaje.getCuerpo()
					);
			writer.newLine();
		}
		catch (IOException e) {
			throw new RuntimeException("Error al abrir el archivo "+this.path, e);
		}
	}

}
