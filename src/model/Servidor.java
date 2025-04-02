package model;

public class Servidor implements Runnable{
	private String ip;
	private int puerto;
	private Usuario usuario;
	
	public Servidor(String ip, int puerto, Usuario usuario){
		this.ip = ip;
		this.puerto = puerto;
		this.usuario = usuario;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
