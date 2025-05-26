package persistencia;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

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
	protected Object deserializar(Class<?> clase) throws Exception {
		return mapper.readValue(archivo, clase);
	}
	
}
