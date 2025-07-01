package persistencia;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import model.Mensaje;

@JacksonXmlRootElement(localName = "mensajes")
public class MensajeList {

	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "mensaje")
	private List<Mensaje> mensajes;
	
	public MensajeList() {
	}
	
	public MensajeList(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}
	
	public List<Mensaje> getMensajes() {
		return mensajes;
	}
	
	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}
}
