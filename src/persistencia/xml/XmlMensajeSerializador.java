package persistencia.xml;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import model.Mensaje;
import persistencia.MensajeList;
import persistencia.MensajeSerializador;
import utils.FileUtil;

public class XmlMensajeSerializador implements MensajeSerializador {

	private XmlMapper mapper = new XmlMapper();
	
	@Override
	public String serializar(Mensaje mensajes) {
		return null;
	}

}
