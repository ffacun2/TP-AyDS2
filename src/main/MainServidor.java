package main;

import controller.ControladorServidor;
import monitor.Monitor;
import utils.Utils;

public class MainServidor {

	public static void main(String[] args) {
		
		new ControladorServidor(Utils.PUERTO_SERVER1);
		new ControladorServidor(Utils.PUERTO_SERVER2);
		
		new Monitor(Utils.PUERTO_SERVER1, Utils.PUERTO_SERVER2).iniciar();
	
	}
}
