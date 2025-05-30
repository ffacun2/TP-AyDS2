package encriptacion;

import model.Mensaje;

public class EncriptacionXOR implements TecnicaEncriptacion{

	@Override
	public Mensaje encriptarMensaje(Mensaje mensaje, String clave) {
		char[] arrayCuerpo = mensaje.getCuerpo().toCharArray();
		char[] arrayClave = clave.toCharArray();
		String textoEncriptado= "";
		
		int j=0;
		for(char c: arrayCuerpo) {
			if(j == clave.length())
				j =0;
			
			textoEncriptado += (char)(c^arrayClave[j++]);
		}
		
		Mensaje mensajeEncriptado = new Mensaje(mensaje.getNickEmisor(), mensaje.getNickReceptor(), textoEncriptado);
		return mensajeEncriptado;
	}

	@Override
	public Mensaje desencriptarMensaje(Mensaje mensaje, String clave) {
		mensaje = this.encriptarMensaje(mensaje, clave);
		return mensaje;
	}

}
