package persistencia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import interfaces.SerializableTxt;
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
		mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, objeto);
	}

	@Override
	protected Usuario deserializar(Usuario usuario) throws Exception{
		Map<String,Contacto> contactos = new HashMap<String, Contacto>();
		JsonNode arrayNode = mapper.readTree(this.archivo);
		
		for (JsonNode nodo : arrayNode) {
			if (!nodo.has("idTipo")) continue;
			String tipo = nodo.get("idTipo").asText();
			
			switch (tipo) {
				case Utils.ID_CONTACTO  -> {
					Contacto c = mapper.treeToValue(nodo, Contacto.class);
					c.setConversacion(new Conversacion());
					contactos.put(c.getNickname(), c);
				}
				case Utils.ID_MENSAJE -> {
					Mensaje m = mapper.treeToValue(nodo, Mensaje.class);
					String nickContacto = m.getNickEmisor();
					if (nickContacto == usuario.getNickname())
						nickContacto = m.getNickReceptor();
					Contacto c = contactos.get(nickContacto);
					c.agregarMensaje(m);
				}
			}
		}
		ArrayList<Contacto> listaC = new ArrayList<Contacto>(contactos.values());
		usuario.setContactos(listaC);
		
		return usuario;
	}

}
