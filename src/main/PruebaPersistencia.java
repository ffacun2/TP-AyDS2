package main;


import exceptions.ContactoRepetidoException;
import model.Contacto;
import model.Conversacion;
import model.Mensaje;
import model.Usuario;
import persistencia.ContactoSerializador;
import persistencia.PersistenciaFactory;
import persistencia.json.JsonPersistenciaFactory;
import persistencia.txt.TxtPersistenciaFactory;
import persistencia.xml.XmlPersistenciaFactory;
import utils.FileUtil;

public class PruebaPersistencia {

	
	public static void main(String[] args) {
		PersistenciaFactory factoryjson = new JsonPersistenciaFactory();
		PersistenciaFactory factoryxml = new XmlPersistenciaFactory();
		PersistenciaFactory factorytxt = new TxtPersistenciaFactory();
		
		Usuario usuario = new Usuario("guido",null);
		Contacto contacto = new Contacto("Contacto1");
		Contacto contacto2 = new Contacto("Contacto2");
		Mensaje mensaje1 = new Mensaje("Emisor","Receptor","Hola");
		Mensaje mensaje2 = new Mensaje("Emisor","Receptor","Como estas?");
		try {
			usuario.agregarContacto(contacto);
			usuario.getContactos().get(0).setConversacion(new Conversacion());
			usuario.getContactos().get(0).agregarMensaje(mensaje1);
			usuario.getContactos().get(0).agregarMensaje(mensaje2);
			usuario.agregarContacto(contacto2);
		} catch (ContactoRepetidoException e) {
			e.printStackTrace();
		}
		
//		Persistencia json = factory.crearSerializador("user.json");
//		
//		try {
//			json.guardar(usuario);
//			json.guardar(mensaje1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		FileUtil.escribirArchivo("./guido-contacto.xml",factoryxml.crearContactoSerializador().serializar(usuario.getContactos()));
		
		
		ContactoSerializador contactSerializer = factoryjson.crearContactoSerializador();
		FileUtil.escribirArchivo("./guido-contacto.json", contactSerializer.serializar(contacto));
		FileUtil.escribirArchivo("./guido-contacto.json", contactSerializer.serializar(contacto2));
//		Persistencia txt = factorytxt.crearSerializador("user.txt");
//		try {
//			txt.guardar(usuario);
//			txt.guardar(contacto);
//			txt.guardar(mensaje1);
//			txt.guardar(mensaje2);
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
		System.out.println("termino.");
//		System.out.println(factoryxml.crearContactoDeserializador().deserializar(FileUtil.leerArchivo("./guido-contacto.xml")));
//		System.out.println(factorytxt.crearContactoDeserializador().deserializar(FileUtil.leerArchivo("./guido-contacto.txt")));
		System.out.println(factoryjson.crearContactoDeserializador().deserializar(FileUtil.leerArchivo("./guido-contacto.json")));
		
	}
}
