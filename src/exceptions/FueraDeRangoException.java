package exceptions;

import java.net.UnknownHostException;

public class FueraDeRangoException extends UnknownHostException{
	
	private static final long serialVersionUID = 1L;

	public FueraDeRangoException() {
		super("El puerto esta fuera de rango");
	}
}
