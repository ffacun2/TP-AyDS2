package servidor;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import config.ConfigServer;
import interfaces.IEnviable;
import requests.Sync;

public class Resincronizacion implements Runnable{

	private int puerto; // Puerto del servidor activo
	private boolean estado; // Indica si la resincronizacion esta activa
	private List<Integer> conexionesPasivas; // Lista de conexiones pasivas
	private BlockingQueue<IEnviable> peticiones; // Cola de peticiones a distribuir
	private Servidor servidor; // Referencia al servidor activo
	
	public Resincronizacion(int puerto,Servidor servidor) {
		this.puerto = puerto;
		this.servidor = servidor; // Referencia al servidor activo
		this.estado = true; // Por defecto, la resincronizacion esta activa
		this.conexionesPasivas = new ArrayList<Integer>();
		this.peticiones = new LinkedBlockingQueue<IEnviable>(); // Cola de peticiones a distribuir
		obtenerConexionesPasivas();
	}

	@Override
	public void run() {
		while (estado) {
			try {
				IEnviable peticion = peticiones.poll(2, TimeUnit.SECONDS); // Espera a que haya una peticion en la cola
				obtenerConexionesPasivas(); // Actualiza las conexiones pasivas

				if (peticion != null)
					distribuirServidores(peticion); // Distribuye la peticion a todas las conexiones pasivas
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Obtiene las conexiones pasivas del servidor activo.
	 * Crea una conexion a cada puerto pasivo y envia un snapshot del servidor activo.
	 * Las conexiones pasivas son aquellas que no estan activas pero pueden recibir peticiones.
	 */
	public void obtenerConexionesPasivas() {
		ConfigServer config = new ConfigServer(".properties");
		
		for (Integer puerto : config.obtenerPuertosPasivos()) {
			if (!conexionesPasivas.contains(puerto) && puerto != this.puerto) { // No crear conexion si ya existe o es el mismo puerto del servidor activo
				ConexionPasivos conexion;
				System.out.println(puerto);
				try {
					conexion = new ConexionPasivos(new Socket("localhost", puerto));
					conexionesPasivas.add(puerto);
					conexion.getOutputStream().writeObject(new Sync(this.servidor.generarSnapShot()));
					conexion.cerrar();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Establece el estado de la resincronizacion. En caso de que se establezca a false, se detiene el hilo.
	 * @param estado : booleano
	 */
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	/**
	 * Guarda una peticion en la cola de peticiones para ser distribuida.
	 * Esta peticion sera distribuida a todas las conexiones pasivas.
	 * Esto permite que las peticiones no se solapen y se manejen de forma ordenada.
	 * @param peticion : IEnviable
	 */
	public void distribuirPeticion(IEnviable peticion) {
		System.out.println("Peticion recibida para resincronizacion" );
		peticiones.add(peticion);
		System.out.println(peticiones.size() + " peticiones en cola para resincronizacion");
	}
	
	/**
	 * Distribuye desde la cola de peticiones a todas las conexiones pasivas.
	 * @param peticion : IEnviable
	 */
	private void distribuirServidores(IEnviable peticion) {
		
		for (Integer conexion : conexionesPasivas) {
			try {
				System.out.println("distribuyendo port:"+conexion);
				ConexionPasivos conn = new ConexionPasivos(new Socket("localhost",conexion));
				conn.getOutputStream().writeObject(peticion);
				conn.getOutputStream().flush(); // Asegura que la peticion se envie inmediatamente
				conn.cerrar(); // Cierra la conexion una vez enviada la peticion
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
