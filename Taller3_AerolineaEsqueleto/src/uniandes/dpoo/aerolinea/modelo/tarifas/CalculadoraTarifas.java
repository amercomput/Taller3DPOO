package uniandes.dpoo.aerolinea.modelo.tarifas;

import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;

public abstract class CalculadoraTarifas {
	public static final double IMPUESTO = 0.28;
	
	public int calcularTarifa(Vuelo vuelo, Cliente cliente) {
        int costoBase = calcularCostoBase(vuelo, cliente);
        int impuestos = calcularValorImpuestos(costoBase);
        
        return (int) (costoBase + impuestos -costoBase*calcularPorcentajeDescuento(cliente));	
	}
	
	protected int calcularValorImpuestos(int costoBase) {
		int valor = (int) (costoBase*IMPUESTO);
		return valor;
	}
	
	protected abstract  int calcularCostoBase(Vuelo vuelo, Cliente cliente);	
	protected abstract int calcularDistanciaVuelo(Ruta ruta);
	protected abstract double calcularPorcentajeDescuento(Cliente cliente);

	
	
}
