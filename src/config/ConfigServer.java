package config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigServer {

	
	private Properties properties = new Properties();
	private final String configPath;
	
	public ConfigServer(String configPath) {
		this.configPath = configPath;
		loadFile();
	}
	
	/**
	 * Carga el archivo de configuracion.
	 * @throws RuntimeException Si ocurre un error al cargar el archivo.
	 */
	public void loadFile () {
		this.properties = new Properties();
		try (FileInputStream in = new FileInputStream(configPath)) {
			properties.load(in);
		} catch (IOException e) {
			throw new RuntimeException("Error al cargar el archivo de configuracion: " + configPath, e);
		}
	}
	
	/**
	 * Actualiza el archivo de configuracion.
	 * @throws RuntimeException Si ocurre un error al actualizar el archivo.
	 */
	public void updateFile() {
		try (FileOutputStream out = new FileOutputStream(configPath)) {
			properties.store(out, "Actualizacion de configuracion");
		} catch (IOException e) {
			throw new RuntimeException("Error al actualizar el archivo de configuracion: " + configPath, e);
		}
	}
	
	/**
	 * Busca un puerto disponible para un servidor. Este queda iniciado pero no activo.
	 * Activo significa que el servidor esta en linea y escuchando conexiones. 
	 * <b>Servidor primario.<\b>
	 * @return El puerto disponible o -1 si no se encontro ninguno.
	 * @throws IOException
	 */
	public synchronized Integer buscarPuerto() {		
		String key = "server.port";
		loadFile();
			
		if (!properties.containsKey(key)) {
			return -1;
		}
		return Integer.valueOf(properties.getProperty(key));
	}
	
}
