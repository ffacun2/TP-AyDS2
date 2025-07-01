package encriptacion;

import model.Mensaje;

public class EncriptacionCaesar implements TecnicaEncriptacion{
	private char[] alfabeto = new char[256];
	
	public EncriptacionCaesar() {
		for (int i=0; i<256; i++) {
			alfabeto[i]= (char)i;
		}
	}
	
	@Override
	public Mensaje encriptarMensaje(Mensaje mensaje, String clave) {
		
		int salto = this.calcularSalto(clave);
		
		char[] alfabetoMod = this.alfabetoModificado(salto);
		
		char[] arrayTexto = mensaje.getCuerpo().toCharArray();
		
		String textoEncriptado = "";
		
		int i=0;
		for(char c: arrayTexto) {
			
			while(c != alfabeto[i]) {
				i++;
			}
			
			textoEncriptado += alfabetoMod[i];
			i=0;
		}
		
		Mensaje mensajeEncriptado = new Mensaje(mensaje.getNickEmisor(), mensaje.getNickReceptor(), textoEncriptado);
		
		return mensajeEncriptado;
	}

	@Override
	public Mensaje desencriptarMensaje(Mensaje mensaje, String clave) {
		int salto = this.calcularSalto(clave);
		
		char[] alfabetoMod = this.alfabetoModificado(salto);
		char[] arrayTexto = mensaje.getCuerpo().toCharArray();
		String textoDescifrado = "";
		
		int i=0;
		for(char c:arrayTexto) {
			while(c != alfabetoMod[i])
				i++;
			
			textoDescifrado += alfabeto[i];
			i=0;
		}
		
		mensaje.setCuerpo(textoDescifrado);
		return mensaje;
	}
	
	private char[] alfabetoModificado(int salto) {
		char[] mod = new char[alfabeto.length];
		
		for(int i=0; i<mod.length; i++) {
			if(salto == alfabeto.length)
				salto = 0;
			
			mod[i] = alfabeto[salto++];
		}
		
		
		return mod;
	}

	private int calcularSalto(String clave) {
		char[] arrayClave = clave.toCharArray();
		int salto = 0;
			
		for(char c: arrayClave) {
			salto += (int)c;
		}
		
		System.out.println("La suma de clave da: " + salto);
			
		salto = salto%26 + 1;
		
		System.out.println("El salto da: " + salto);
		
		return salto;
	}
	
}
