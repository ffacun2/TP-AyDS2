package exceptions;

public class ContactoRepetidoException extends Exception{
	private static final long serialVersionUID = 1L;

	public ContactoRepetidoException() {
		super("Ya existe un contacto en ese puerto");
	}
}
