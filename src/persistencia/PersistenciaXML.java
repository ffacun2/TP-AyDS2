package persistencia;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import model.Usuario;

public class PersistenciaXML extends Persistencia{

	private final XmlMapper mapper = new XmlMapper();
	
	public PersistenciaXML(String archivo) {
		super(archivo);
	}


	@Override
	protected void serializar(Object usuario) throws Exception {
		mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, usuario);
	}

	@Override
	protected Usuario deserializar(Usuario usuario) throws Exception {
		return usuario;
	}
	
}
