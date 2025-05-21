package main;


import exceptions.ContactoRepetidoException;
import model.Contacto;
import model.Conversacion;
import model.Mensaje;
import model.Usuario;
import persistencia.dao.PersistenciaTemplate;
import persistencia.factory.JsonPersistenciaFactory;
import persistencia.factory.PersistenciaFactory;
import persistencia.factory.XmlPersistenciaFactory;

public class PruebaPersistencia {

	
	public static void main(String[] args) {
		PersistenciaFactory factory = new JsonPersistenciaFactory();
		PersistenciaFactory factoryxml = new XmlPersistenciaFactory();
		
		Usuario usuario = new Usuario("facu",1234,"localhost",null);
		try {
			usuario.agregarContacto(new Contacto("Contacto1"));
			usuario.getContactos().get(0).setConversacion(new Conversacion());
			usuario.getContactos().get(0).agregarMensaje(new Mensaje("Emisor","Receptor","HOla"));
			usuario.getContactos().get(0).agregarMensaje(new Mensaje("Receptor","Emisor","Como estas"));
		} catch (ContactoRepetidoException e) {
			e.printStackTrace();
		}
		
		PersistenciaTemplate<Usuario> json = factory.crearSerializador("user.json");
		
		try {
			json.guardar(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PersistenciaTemplate<Usuario> xml = factoryxml.crearSerializador("user.xml");
		try {
			xml.guardar(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
