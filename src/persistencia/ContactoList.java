package persistencia;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import model.Contacto;

@JacksonXmlRootElement(localName = "contactos")
public class ContactoList {
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "contacto")
	private List<Contacto> contactos;
	
	public ContactoList() {
	}
	
	public ContactoList(List<Contacto> contactos) {
		this.contactos = contactos;
	}
	
	public List<Contacto> getContactos() {
		return contactos;
	}
	
	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}
}
