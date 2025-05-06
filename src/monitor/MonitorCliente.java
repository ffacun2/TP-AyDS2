package monitor;

/*
 * Se encarga de recibir los mensajes de los clientes
 * que piden el puerto del servidor activo para conectase
 * a el.
 */
public class MonitorCliente implements Runnable {
	
	private Monitor monitor;
	
	public MonitorCliente(Monitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void run() {
		
	}

}
