package config;

import encriptacion.EncriptacionCaesar;
import encriptacion.EncriptacionXOR;
import encriptacion.TecnicaEncriptacion;

public class ConfigEncriptado {
	public String tecnicaEncriptado;

	public static TecnicaEncriptacion getTecnicaEncriptado(String tecnica) {
		TecnicaEncriptacion tecnicaEncriptacion;
		
		if(tecnica.equals("XOR")) {
			tecnicaEncriptacion = new EncriptacionXOR();
		}else{
			tecnicaEncriptacion = new EncriptacionCaesar(); //Metodo por defecto
		}
		
		return tecnicaEncriptacion;
	}
}	
