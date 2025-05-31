package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import interfaces.SerializableTxt;
import model.Contacto;
import model.Conversacion;
import model.Mensaje;
import model.Usuario;

public class PersistenciaTXT extends Persistencia {
	
	private BufferedWriter writer;
	private BufferedReader reader;

	public PersistenciaTXT(String archivo) {
		super(archivo);
	}

	@Override
	protected void serializar(Object objeto) throws Exception {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo,true))) {
			writer.write(((SerializableTxt)objeto).toTxt());
			writer.newLine();
		} catch (Exception e) {
			throw new Exception("Error al serializar el objeto: " + e.getMessage(), e);
		}
	}

	@Override
	protected Usuario deserializar(Usuario usuario) throws Exception {
		Map<String,Contacto> contactos = new HashMap<String, Contacto>();
		String linea;
		reader = new BufferedReader(new FileReader(archivo));
		while((linea = reader.readLine()) != null ) {
			if (linea.startsWith("#Usuario:")) {
				String[] partes = linea.substring(9).split("\\|");
				System.out.println(partes[1]+","+partes[2]+",");
				usuario.setNickname(partes[0].trim());
				usuario.setIp(partes[1].trim());
				usuario.setPuerto(Integer.valueOf(partes[2].trim()));
			}
			else if (linea.startsWith("#Contacto:")) {
				String nickname = linea.substring(10);
				Contacto contacto = new Contacto(nickname);
				contacto.setConversacion(new Conversacion());
				contactos.put(nickname, contacto);
			}
			else if (linea.startsWith("#Mensaje:")) {
				String[] partes = linea.substring(9).split("\\|");
				String emisor = partes[0].trim();
				String receptor = partes[1].trim();
				String cuerpo = partes[2].trim();
				Mensaje mensaje = new Mensaje(emisor,receptor,cuerpo);
				
				Contacto contacto = contactos.get(receptor);
				if (contacto != null)
					contacto.getConversacion().getMensajes().add(mensaje);
				else {
					contacto = contactos.get(emisor);
					contacto.getConversacion().getMensajes().add(mensaje);
				}
					
			}
		}
		usuario.setContactos(new ArrayList<>(contactos.values()));
		
		return usuario;
	}

}
