package persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import model.Contacto;
import model.Conversacion;
import model.Mensaje;
import model.Usuario;
import utils.Utils;

public class PersistenciaJSON extends Persistencia {
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	public PersistenciaJSON(String path) {
		super(path);
	}
	
	@Override
	protected void serializar(Object objeto) throws Exception {
		try (FileWriter fw = new FileWriter(archivo,true)) {
			ObjectNode nodo = mapper.valueToTree(objeto);
			if (objeto instanceof Contacto)
				nodo.put("idTipo",Utils.ID_CONTACTO); //Agrego identificadores para saber que clase son a la hora de leer
			else if (objeto instanceof Mensaje)
				nodo.put("idTipo", Utils.ID_MENSAJE);
			
//			String json = mapper.writeValueAsString(objeto);
			fw.write(mapper.writeValueAsString(nodo) + '\n');
		}
//		mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, objeto);
	}

//	@Override
//	protected void deserializar(Usuario usuario) throws Exception{
//		Map<String,Contacto> contactos = new HashMap<String, Contacto>();
//		JsonNode arrayNode = mapper.readTree(this.archivo);
//		
//		for (JsonNode nodo : arrayNode) {
//			if (!nodo.has("idTipo")) continue;
//			String tipo = nodo.get("idTipo").asText();
//			
//			switch (tipo) {
//				case Utils.ID_CONTACTO  -> {
//					Contacto c = mapper.treeToValue(nodo, Contacto.class);
//					c.setConversacion(new Conversacion());
//					contactos.put(c.getNickname(), c);
//				}
//				case Utils.ID_MENSAJE -> {
//					Mensaje m = mapper.treeToValue(nodo, Mensaje.class);
//					String nickContacto = m.getNickEmisor();
//					if (nickContacto == usuario.getNickname())
//						nickContacto = m.getNickReceptor();
//					Contacto c = contactos.get(nickContacto);
//					c.agregarMensaje(m);
//				}
//			}
//		}
//		ArrayList<Contacto> listaC = new ArrayList<Contacto>(contactos.values());
//		usuario.setContactos(listaC);
//	}
	
	protected void deserializar(Usuario usuario) throws Exception{
		Map<String, Contacto> contactos = new HashMap<String, Contacto>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			String linea;
			
			while ((linea = br.readLine()) != null) {
				JsonNode nodo = mapper.readTree(linea);
				if (!nodo.has("idTipo")) continue;
				String tipo = nodo.get("idTipo").asText();
				switch (tipo) {
				case Utils.ID_CONTACTO -> {
					Contacto c = mapper.treeToValue(nodo, Contacto.class);
					c.setConversacion(new Conversacion());
					contactos.put(c.getNickname(), c);
				}
				case Utils.ID_MENSAJE -> {
					Mensaje m = mapper.treeToValue(nodo,Mensaje.class);
					String nickContacto = m.getNickEmisor();
					if (nickContacto.equals(usuario.getNickname()))
						nickContacto = m.getNickReceptor();
					Contacto c = contactos.get(nickContacto);
					if (c != null)
						c.agregarMensaje(m);
				}
				}
			}
		}
		ArrayList<Contacto> agenda = new ArrayList<Contacto>(contactos.values());
		usuario.setContactos(agenda);
	}

}
