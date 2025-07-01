package exceptions;

public class UserExistentException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public UserExistentException(String message) {
		super(message);
	}

	public UserExistentException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserExistentException(Throwable cause) {
		super(cause);
	}
	
	public UserExistentException() {
		super("El usuario ya existe");
	}

}
