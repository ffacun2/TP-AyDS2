package main;


import model.Contacto;
import persistencia.ContactoSerializador;
import persistencia.PersistenciaFactory;
import persistencia.xml.XmlPersistenciaFactory;

public class MainPersisntencia {

	public static void main(String[] args) {
		PersistenciaFactory factory = new XmlPersistenciaFactory("user","xml");
		
		ContactoSerializador contactoSerializador = factory.crearContactoSerializador();
		try {
			PersistenciaFactory.crearArchivo(".", "user"+"-contactos", "xml");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		Contacto c1 = new Contacto("user1"); 
		Contacto c2 = new Contacto("user2"); 
		Contacto c3 = new Contacto("user3"); 
		Contacto c4 = new Contacto("user4");
		
		contactoSerializador.serializar(c1);		
		contactoSerializador.serializar(c2);		
		contactoSerializador.serializar(c3);		
		contactoSerializador.serializar(c4);
		
		
		
	}
}
