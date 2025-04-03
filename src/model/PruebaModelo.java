package model;

import java.io.IOException;

public class PruebaModelo {

	public static void main(String[] args) {
		
		/**
		 * Se simulan dos instancias del sistema de mensajeria con dos usuarios, usuario1 y usuario2
		 * que mandan un mensaje despues de 1seg y despues de 5seg respectivamente. 
		 */
		
		Thread tUsuario1 = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	Usuario usuario1 = new Usuario("Usuario1", 8888, "localhost");
		    	Contacto contCliente = new Contacto("Usuario2", 9999, "localhost");
		    	
		    	try {
					Thread.sleep(1000);
					usuario1.enviarMensaje(new Mensaje(
							usuario1.getNickname(), 
							contCliente.getPuerto(),
							contCliente.getIp(),
							"Hola como va"), contCliente);
				} catch (InterruptedException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    }
		});
		
		Thread tUsuario2 = new Thread(new Runnable() {

			@Override
			public void run() {
				Usuario usuario2 = new Usuario("Usuario2", 9999, "localhost");
				Contacto contServidor = new Contacto("Usuario1", 8888, "localhost");
				
				try {
					Thread.sleep(5000);
					usuario2.enviarMensaje(new Mensaje(
							usuario2.getNickname(), 
							contServidor.getPuerto(),
							contServidor.getIp(),
							"todo bien"), contServidor);
				} catch (InterruptedException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		tUsuario1.start();
		tUsuario2.start();
	}

}
