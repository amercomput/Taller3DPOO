package uniandes.dpoo.aerolinea.modelo;

public class Avion {
	private String nombre;
	private int capacidad;
	
	public Avion(String nombre, int capacidad) {
		this.capacidad = capacidad;
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public int getCapacidad() {
		return this.capacidad;
	}

}
