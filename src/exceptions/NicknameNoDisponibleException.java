package exceptions;

public class NicknameNoDisponibleException extends Exception{

	private static final long serialVersionUID = 1L;

	public NicknameNoDisponibleException(String msj) {
		super(msj);
	}
}
