package main;


import exceptions.ContactoRepetidoException;
import model.Contacto;
import model.Conversacion;
import model.Mensaje;
import model.Usuario;
import persistencia.Persistencia;
import persistencia.factory.JsonPersistenciaFactory;
import persistencia.factory.PersistenciaFactory;
import persistencia.factory.TxtPersistenciaFactory;
import persistencia.factory.XmlPersistenciaFactory;

public class PruebaPersistencia {

	
	public static void main(String[] args) {
		PersistenciaFactory factory = new JsonPersistenciaFactory();
		PersistenciaFactory factoryxml = new XmlPersistenciaFactory();
		PersistenciaFactory factorytxt = new TxtPersistenciaFactory();
		
		Usuario usuario = new Usuario("facu",null);
		Contacto contacto = new Contacto("Contacto1");
		Mensaje mensaje1 = new Mensaje("Emisor","Receptor","Hola");
		Mensaje mensaje2 = new Mensaje("Emisor","Receptor","Como estas?");
		try {
			usuario.agregarContacto(contacto);
			usuario.getContactos().get(0).setConversacion(new Conversacion());
			usuario.getContactos().get(0).agregarMensaje(mensaje1);
			usuario.getContactos().get(0).agregarMensaje(mensaje2);
		} catch (ContactoRepetidoException e) {
			e.printStackTrace();
		}
		
		Persistencia json = factory.crearSerializador("user.json");
		
		try {
			json.guardar(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Persistencia xml = factoryxml.crearSerializador("user.xml");
		try {
			xml.guardar(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Persistencia txt = factorytxt.crearSerializador("user.txt");
		try {
			txt.guardar(usuario);
			txt.guardar(contacto);
			txt.guardar(mensaje1);
			txt.guardar(mensaje2);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("termino.");
	}
}
