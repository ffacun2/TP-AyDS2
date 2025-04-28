package interfaces;

import java.io.IOException;
import java.net.Socket;

import model.Mensaje;
import requests.RequestDirectorio;
import requests.RequestLogin;
import requests.RequestLogout;
import requests.RequestRegistro;

public interface IServidor {
	
	public void handleRegistro(RequestRegistro req, Socket socket) throws IOException;
	public void handleDirectorio(RequestDirectorio req, Socket socket) throws IOException;
	public void handleIniciarSesion(RequestLogin req, Socket socket) throws IOException;
	public void handleCerrarSesion(RequestLogout req, Socket socket) throws IOException;
	public void handleMensaje(Mensaje mensaje) throws IOException;

}
