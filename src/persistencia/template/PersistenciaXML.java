package persistencia.template;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class PersistenciaXML<T> extends PersistenciaTemplate<T>{

	private final XmlMapper mapper = new XmlMapper();
	
	public PersistenciaXML(String archivo) {
		super(archivo);
	}


	@Override
	protected void serializar(T usuario) throws Exception {
		mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, usuario);
	}

	@Override
	protected T deserializar(Class<T> clase) throws Exception {
		return mapper.readValue(archivo, clase);
	}
	
}
