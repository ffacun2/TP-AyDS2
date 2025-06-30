package persistencia;

import java.io.File;
import java.util.Optional;

public abstract class  PersistenciaFactory {
	private String nickname;
	private String extension;
	
	public PersistenciaFactory(String nickname, String extension) {
		this.nickname = nickname;
		this.extension = extension;
	}

	public abstract ContactoSerializador crearContactoSerializador();
	public abstract MensajeSerializador crearMensajeSerializador();
	
	public abstract ContactoDeserializador crearContactoDeserializador();
	public abstract MensajeDeserializador crearMensajeDeserializador();
	
	/**
	 * Busca en la ruta especificada un archivo con el nombre del usuario. Si este existe
	 * devuelve la extension del mismo. (.txt, .json, .xml)
	 * @param directorio - ruta donde se encuentra el archivo
	 * @param username - nickname del usuario a loguearse
	 * @return null si no se encuentra o la extension.
	 */
	public static Optional<String> buscoArchivo(String directorio, String username) {
		File directory = new File(directorio);
		
		if (!directory.exists() || !directory.isDirectory()) return Optional.empty();
		
		
		File[] files = directory.listFiles((dir, name) -> name.startsWith(username));
		if (files != null && files.length > 0) {
			
			String nombre = files[0].getName();
			int punto = nombre.lastIndexOf(".");
			
			if (punto != -1 && punto < nombre.length() - 1) {
                return Optional.of(nombre.substring(punto + 1)); // Devuelve "json", "txt", etc.
            }
		}
		return Optional.empty();
	}
	
	
	public static void crearArchivo(String nombre, String ext) throws Exception {
		File file = new File(".", nombre + "." + ext);
		if (!file.exists()) {
			if (!file.createNewFile()) {
				throw new Exception("No se pudo crear el archivo: " + file.getAbsolutePath());
			}
		}
		else {
			throw new Exception("El archivo ya existe: " + file.getAbsolutePath());
		}
	}
	
	protected String getNickname() {
		return this.nickname;
	}
	
	protected String getExtension() {
		return this.extension;
	}
}
