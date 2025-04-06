package exceptions;

import java.net.UnknownHostException;

public class FueraDeRangoException extends UnknownHostException{
	
	public FueraDeRangoException() {
		super("El puerto esta fuera de rango");
	}
}
