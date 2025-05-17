package monitor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import requests.Sync;
import servidor.HandleClienteDTO;
import utils.Utils;

/*
 * Clase que representa el monitor del sistema
 * Se encarga de gestionar el servidor activo y los servidores secundarios
 * y de realizar el cambio de servidor en caso de que el servidor activo falle
 * Crea el HeartBeat que se encarga de enviar un mensaje al servidor activo para verificar si esta activo
 * y el MonitorCliente que se encarga de recibir los mensajes de los clientes
 * sobre que Servidor esta activo y conectarse.
 */
public class Monitor {

	private int puertoServidorActivo;
	private Queue<Integer> puertosSecundarios;
	private ServerSocket serverSocket; //socket del servidor para escuchar conexiones de Usuarios
	private ArrayList<HandleClienteDTO> backup;
	
	public Monitor(int... puertos) {
		this.backup = new ArrayList<>();
		this.puertosSecundarios = new ArrayDeque<>();
		for (int puerto: puertos) {
			this.puertosSecundarios.add(puerto);
		}
	}
	
	public void iniciar() {
		new Thread(new HeartBeat(this)).start();
		new Thread(this::clienteListener).start();
		new Thread(this::servidorBackupListener).start();
	}
	
	
	private void clienteListener() {
		try {
			this.serverSocket = new ServerSocket(Utils.PUERTO_MONITOR);		
			
			while(true) {
				System.out.println("Monitor escuchando...");
				//Por cada cliente que se conecta, se crea un nuevo socket
				Socket socket = serverSocket.accept(); //Este socket establece la conexion entre monitor y usuario
				//Puede pasar que se quieran conectar simultaneamente varios usuarios
				//para eso uso hilos por cada usuario
				new Thread(new ClienteListener(this, socket)).start();
			}		
		} catch (Exception e) {
			System.out.println("Error al crear el socket del monitor: " + e.getMessage());
			return;
		}
	}
	
	
	private void servidorBackupListener() {
		ObjectOutputStream out;
		ObjectInputStream in;
		try {
			ServerSocket serverSocket = new ServerSocket(Utils.PUERTO_SYNC);
			
			while (true) {
				Socket socket = serverSocket.accept();
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
				
				socket.setSoTimeout(3000);
				//Aca recibo el objeto con los datos del backup
				ArrayList<HandleClienteDTO> snapshot = (ArrayList<HandleClienteDTO>)in.readObject();
				this.backup = snapshot;
			}
		} 
		catch(Exception e) {
			System.out.println("Error al hacer el backup: " +e.getMessage());
			return;
		}
	}
	
	public void sincronizacion () {
		try (
			Socket socket = new Socket("localhost",this.puertoServidorActivo);	
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			){
			
			out.writeObject(new Sync(this.backup));
			out.flush();
			
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//Los metodos son sincronizador porque varios hilos pueden llamar a los metodos simultaneamente
	// Los hilos que los llaman los de monitorCliente
	
	public synchronized void cambioServidor(int puertoServerNuevo) {
		this.puertosSecundarios.add(this.puertoServidorActivo); //Agrego al final el servidor caido
		this.puertoServidorActivo = puertoServerNuevo;
	}
	
	
	public synchronized void setPuertoServidorActivo(int puertoServidorActivo) {
		this.puertoServidorActivo = puertoServidorActivo;
	}
	
	public synchronized int getPuertoServidorActivo() {
		return this.puertoServidorActivo;
	}
	
	public synchronized Queue<Integer> getPuertosSecundarios() {
		return this.puertosSecundarios;
	}


}