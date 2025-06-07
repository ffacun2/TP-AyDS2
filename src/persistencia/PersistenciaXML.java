package persistencia;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import model.Contacto;
import model.Conversacion;
import model.Mensaje;
import model.Usuario;

public class PersistenciaXML extends Persistencia{

	private XmlMapper xmlMapper = new XmlMapper();
	
	public PersistenciaXML(String archivo) {
		super(archivo);
		xmlMapper.enable(SerializationFeature.INDENT_OUTPUT); // Para que el XML sea legible
	}


	/**
	 *	Serializa el objeto recibido de paramtro en formato xml
	 *
	 */
	@Override
	protected void serializar(Object object) throws Exception {
		try (
			FileWriter writer = new FileWriter(archivo, true)
		){
			xmlMapper.writeValue(writer, object);		
		}
	}

	/**
	 * Deserializa los objetos almacenados en el archivo indicado por el constructor. Estos
	 * objetos son almacenados en la clase usuario. Objetos serializable (Contacto, Mensaje)
	 */
	@Override
	protected void deserializar(Usuario usuario) throws Exception {
		try (
			BufferedReader reader = new BufferedReader(new FileReader(archivo))
		) {
			HashMap<String, Contacto> contactos = new HashMap<>();
			StringBuilder sb = new StringBuilder();
			String linea;
			
			while ((linea = reader.readLine()) != null) {
				sb.append(linea);
				
				if (linea.trim().endsWith("</Contacto>")) {
					Contacto contacto = xmlMapper.readValue(sb.toString(), Contacto.class);
					contacto.setConversacion(new Conversacion());
					contactos.put(contacto.getNickname(), contacto);
					sb.setLength(0); // Limpiar el StringBuilder para el siguiente contacto
				}
				else if (linea.trim().endsWith("</Mensaje>")) {
					Mensaje mensaje = xmlMapper.readValue(sb.toString(), Mensaje.class);
					
					Contacto contacto = contactos.get(mensaje.getNickReceptor());
					if (contacto != null)
						contacto.getConversacion().getMensajes().add(mensaje);
					else {
						contacto = contactos.get(mensaje.getNickEmisor());
						contacto.getConversacion().getMensajes().add(mensaje);
					}
					sb.setLength(0); // Limpiar el StringBuilder para el siguiente mensaje
				}
			}
			usuario.setContactos(new ArrayList<>(contactos.values()));
		} 
		catch (Exception e) {
			throw new Exception("Error al deserializar el usuario: " + e.getMessage(), e);
			
		}
		
	}
	
}
