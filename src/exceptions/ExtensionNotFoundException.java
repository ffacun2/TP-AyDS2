package exceptions;

public class ExtensionNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public ExtensionNotFoundException(String message) {
		super(message);
	}

	public ExtensionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExtensionNotFoundException(Throwable cause) {
		super(cause);
	}

}
