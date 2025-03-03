package uniandes.dpoo.aerolinea.modelo.tarifas;

import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteCorporativo;

public class CalculadoraTarifasTemporadaBaja extends CalculadoraTarifas {
	protected static final int COSTO_POR_KM_NATURAL = 600;
	protected static final int  COSTO_POR_KM_CORPORATIVO = 900;
	protected static final double DESCUENTO_PEQ = 0.02;
	protected static final double DESCUENTO_MEDIANAS = 0.1;
	protected static final double DESCUENTO_GRANDES = 0.2;
	
	@Override
	public int calcularCostoBase(Vuelo vuelo, Cliente cliente) {
		Ruta ruta = vuelo.getRuta();
		int distancia = calcularDistanciaVuelo(ruta);
		
		if(cliente.getTipoCliente().equals("Corporativo")) {
			return distancia*COSTO_POR_KM_CORPORATIVO;
		}else {
			return distancia* COSTO_POR_KM_NATURAL;
		}
	}
	
    @Override
	public double calcularPorcentajeDescuento(Cliente cliente) {
	    double descuento = 0.0;
	    if (cliente instanceof ClienteCorporativo) {
	        ClienteCorporativo clienteCorp = (ClienteCorporativo) cliente; 
	        int tamano = clienteCorp.getTamanoEmpresa(); 
	        
	        if (tamano == 1) {
	            descuento = DESCUENTO_PEQ;
	        } else if (tamano == 2) {
	            descuento = DESCUENTO_MEDIANAS;
	        } else {
	            descuento = DESCUENTO_GRANDES;
	        }
	    }
	    return descuento;
	}

	@Override
	protected int calcularDistanciaVuelo(Ruta ruta) {
		Aeropuerto destino = ruta.getDestino();
		Aeropuerto origen = ruta.getOrigen();
		int distancia = Aeropuerto.calcularDistancia(destino, origen);
		return distancia;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}