package config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
	public synchronized Integer buscarPuertoDisponible() {
		int max = Integer.valueOf(properties.getProperty("servidores.max","10"));
		boolean encontro = false;
		int port = -1;
		boolean online;
		int i = 1;
		
		loadFile();
		while ( !encontro && i <= max) {
			String key = "servidor"+i;
			String portKey = key + ".port";
			String onlineKey = key + ".online";
			
			if (!properties.containsKey(portKey)) {
				i++;
				continue;
			}
			
			port = Integer.valueOf(properties.getProperty(portKey));
			online = Boolean.parseBoolean(properties.getProperty(onlineKey, "false"));
			
			if (!online) {
				encontro = true;
				properties.setProperty(key + ".online", "true");
				updateFile();
			}
			else
				i++;
		}
		if (encontro)
			return port;
		
		return -1; // No se encontro un servidor disponible
	}
	
	/**
	 * Determina un servidor online en activo.
	 * @return El puerto del servidor activo o -1 si no hay ninguno online.
	 */
	public synchronized Integer determinarServidorActivo() {
		int max = Integer.valueOf(properties.getProperty("servidores.max","10"));
		boolean encontro = false;
		int i = 1;
		loadFile();
		while (!encontro && i <= max) {
			String key = "servidor"+i;
			String onlineKey = key + ".online";
			
			if (!properties.containsKey(onlineKey)) {
				i++;
				continue;
			}
			
			if (Boolean.parseBoolean(properties.getProperty(onlineKey))) {
				String activeKey = key + ".active";
				properties.setProperty(activeKey, "true");
				updateFile();
				encontro = true;
			}
			else 
				i++;
		}
		if (encontro)
			return Integer.valueOf(properties.getProperty("servidor"+i+".port"));
		
		return -1;
	}
	
	/**
	 * Busca un puerto activo de un servidor. Un servidor activo es aquel que esta en linea y escuchando conexiones.
	 * @return El puerto activo de un servidors o -1 si no hay servidor activo.
	 */
	public Integer obtenerPuertoActivo () {
		boolean encontro = false;
		int max = Integer.valueOf(properties.getProperty("servidores.max","10"));
		int i = 1;
		
		loadFile();
		while (!encontro && i <= max) {
			String key = "servidor"+i;
			String onlineKey = key + ".online";
			String activeKey = key + ".active";
			
			if (!properties.containsKey(onlineKey)) {
				i++;
				continue;
			}
			
			if (Boolean.parseBoolean(properties.getProperty(activeKey, "false"))) {
				encontro = true;
			}
			else
				i++;
		}
		if (encontro)
			return Integer.valueOf(properties.getProperty("servidor"+i+".port"));
		
		return -1;
	}
	
	/**
	 *  Libera un servidor, es decir, lo pone como no activo y no online.
	 * @param port
	 */
	public synchronized void liberarServidor(int port) {
		String key = buscoPorPuerto(port);
		loadFile();
		if (key != null) {
			properties.setProperty(key + ".online", "false");
			properties.remove(key+ ".active");
			updateFile();
		} else {
			throw new RuntimeException("No se encontro un servidor con el puerto: " + port);
		}
	}
	
	
	/**
	 * Busca un servidor por su puerto. 
	 * @param puerto El puerto del servidor a buscar.
	 * @retorn El nombre del servidor (servidor1, servidor2, etc.) o null si no se encontro.
	 */
	public String buscoPorPuerto(int puerto) {
		
		int max = Integer.valueOf(properties.getProperty("servidores.max","10"));
		boolean encontro = false;
		String key = null;
		int i = 1;
		
		loadFile();
		while ( !encontro && i <= max) {
			String portKey = "servidor"+i+".port";
			
			if (!properties.containsKey(portKey)) {
				i++;
				continue;
			}
			
			if (Integer.valueOf(properties.getProperty(portKey)).equals(puerto)) {
				key = "servidor"+i;
				encontro = true;
			}
			else
				i++;
		}
		
		if (encontro)
			return key;
		
		return null; // No se encontro un servidor con ese puerto
	}
	
	/**
	 * Obtiene todos los puertos de servidores pasivos, es decir, aquellos que estan online pero no activos.
	 * @return Una lista de puertos de servidores pasivos.
	 */
	public List<Integer> obtenerPuertosPasivos() {
		List<Integer> puertos = new ArrayList<>();
		int max = Integer.valueOf(properties.getProperty("servidores.max","10"));
		
		loadFile();
		for (int i = 1; i <= max; i++) {
			String key = "servidor"+i;
			String portKey = key + ".port";
			String onlineKey = key + ".online";
			
			if (properties.containsKey(portKey) && 
				Boolean.parseBoolean(properties.getProperty(onlineKey, "false")))
			{
				puertos.add(Integer.valueOf(properties.getProperty(portKey)));
			}
		}
		
		return puertos;
	}
	
}
