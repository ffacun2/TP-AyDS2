package model;

import java.io.IOException;
import java.net.UnknownHostException;

public class PruebaModelo {

	public static void main(String[] args) {
		
		/**
		 * Se simulan dos instancias del sistema de mensajeria con dos usuarios, usuario1 y usuario2
		 * que mandan un mensaje despues de 1seg y despues de 5seg respectivamente. 
		 */
		
		Thread tUsuario1 = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	Usuario usuario1;
				try {
					usuario1 = new Usuario("Usuario1", 8888, "localhost", new ServidorAPI("localhost", 9999));
					Contacto contCliente = new Contacto("Usuario2", 9999, "localhost");
					Thread.sleep(1000);
						usuario1.enviarMensaje(new Mensaje(
								usuario1.getNickname(), 
								contCliente.getPuerto(),
								contCliente.getIp(),
								"Hola como va"), contCliente);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();}
			    
				}
		});
		
		Thread tUsuario2 = new Thread(new Runnable() {

			@Override
			public void run() {
				Usuario usuario2;
				try {
					usuario2 = new Usuario("Usuario2", 9999, "localhost",new ServidorAPI("localhost", 9999));
					Contacto contServidor = new Contacto("Usuario1", 8888, "localhost");
					Thread.sleep(5000);
					usuario2.enviarMensaje(new Mensaje(
							usuario2.getNickname(), 
							contServidor.getPuerto(),
							contServidor.getIp(),
							"todo bien"), contServidor);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		tUsuario1.start();
		tUsuario2.start();
	}

}
