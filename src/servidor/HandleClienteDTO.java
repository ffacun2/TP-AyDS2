package servidor;

import java.io.Serializable;
import java.util.List;

import model.Mensaje;

public class HandleClienteDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String nickname;
	private List<Mensaje> mensajesPendientes;
	private boolean estado;
	
	public HandleClienteDTO (String nickname, List<Mensaje> msjPendiente, boolean estado) {
		this.nickname = nickname;
		this.mensajesPendientes = msjPendiente;
		this.estado = estado;
	}
	
	public String getNickName() {
		return this.nickname;
	}
	
	public List<Mensaje> getMensajesPendientes() {
		return this.mensajesPendientes;
	}
	
	public boolean getEstado() {
		return this.estado;
	}
}
