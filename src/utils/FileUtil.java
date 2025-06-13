package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

	
	public static void escribirArchivo(String ruta, String contenido) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta,true))) {
			writer.write(contenido);
			writer.newLine(); // Agrega una nueva línea al final del contenido
		} catch (IOException e) {
			throw new RuntimeException("Error al escribir en el archivo: " + ruta, e);
		}
	}
	
	public static String leerArchivo(String ruta) {
		try {
			System.out.println(ruta);
			return new String(Files.readAllBytes(Paths.get(ruta)));
		}
		catch (IOException e) {
			throw new RuntimeException("Error al leer el archivo: " + ruta, e);
		}
	}
}
