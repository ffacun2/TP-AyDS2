package config;

import encriptacion.EncriptacionCaesar;
import encriptacion.EncriptacionXOR;
import encriptacion.TecnicaEncriptacion;

public class ConfigEncriptado {
	public String clave;
	public String tecnicaEncriptado;
	
	public ConfigEncriptado(String clave, String tecnicaEncriptado) {
		this.clave = clave;
		this.tecnicaEncriptado = tecnicaEncriptado;
	}
	
	public String getClave() {
		return clave;
	}
	
	public TecnicaEncriptacion getTecnicaEncriptado() {
		TecnicaEncriptacion tecnica;
		if(this.tecnicaEncriptado.toUpperCase().equals("XOR")) {
			tecnica = new EncriptacionXOR();
		}else {
			tecnica = new EncriptacionCaesar();
		}
		
		
		return tecnica;
	}
}	
