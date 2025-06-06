package main;



import config.ConfigServer;
import controller.ControladorServidor;
import utils.Utils;

public class MainServidor {

	public static void main(String[] args) {
		int puerto;
			ConfigServer config = new ConfigServer(".properties");
			
			puerto = config.buscarPuertoDisponible();
			if (puerto == -1)
				Utils.mostrarError("No se encontraron puertos disponibles para el servidor.", null);
			else 
				new ControladorServidor(puerto);
			
	}	
}
