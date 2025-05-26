package persistencia.factory;

import java.io.File;
import java.util.Optional;

import persistencia.Persistencia;

public abstract class  PersistenciaFactory {

	public abstract Persistencia crearSerializador(String path);
	
	public static Optional<String> buscoArchivo(String directorio, String username) {
		File directory = new File(directorio);
		
		if (!directory.exists() || !directory.isDirectory()) return Optional.empty();
		
		
		File[] files = directory.listFiles((dir, name) -> name.startsWith(username+"."));
		if (files != null && files.length > 0) {
			
			String nombre = files[0].getName();
			int punto = nombre.lastIndexOf(".");
			
			if (punto != -1 && punto < nombre.length() - 1) {
                return Optional.of(nombre.substring(punto + 1)); // Devuelve "json", "txt", etc.
            }
		}
		return Optional.empty();
	}
}
