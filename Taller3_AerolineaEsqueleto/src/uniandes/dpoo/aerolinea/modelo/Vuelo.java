package uniandes.dpoo.aerolinea.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifas;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class Vuelo {
	private String fecha;
	private Ruta ruta;
	private Avion avion; 
	private List<Tiquete> tiquetes;
	
	public Vuelo(Ruta ruta, String fecha, Avion Avion) {
		this.ruta = ruta;
		this.fecha = fecha;
		this.avion = Avion;
		this.tiquetes = new ArrayList<>();
	}
	
	public Ruta getRuta() {
		return this.ruta;
	}
	
	public String getFecha() {
		return this.fecha;
	}
	
	public Avion getAvion() {
		return this.avion;
	}
	
	public Collection<Tiquete> getTiquetes() {
		return this.tiquetes;
	}
	
	public int venderTiquetes(Cliente cliente, CalculadoraTarifas calculadora, int cantidad) {
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }
	    Vuelo otroVuelo = (Vuelo) obj;
	    
	    return fecha.equals(otroVuelo.fecha) &&
	           ruta.equals(otroVuelo.ruta) &&
	           avion.equals(otroVuelo.avion);
	}

	
	
	
	
	

	
	

}
