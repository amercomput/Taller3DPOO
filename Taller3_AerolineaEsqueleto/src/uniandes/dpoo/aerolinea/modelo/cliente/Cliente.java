package uniandes.dpoo.aerolinea.modelo.cliente;

import java.util.ArrayList;
import java.util.List;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public abstract class Cliente {
    protected List<Tiquete> tiquetes;
    
	public Cliente() {
	    this.tiquetes = new ArrayList<>();
	}
	
	public abstract String getTipoCliente();
	
    public abstract String getIdentificador();
    
    public void agregarTiquete(Tiquete tiquete) {
        tiquetes.add(tiquete);
    }
   
    public int calcularValorTotalTiquetes() {
        int total = 0;
        for (Tiquete t : tiquetes) {
            total += t.getTarifa(); 
        }
        return total;
    }
    
    
    public abstract String getNombre();

    public void usarTiquetes(Vuelo vuelo) {
        for (Tiquete t : tiquetes) {
            if (t.getVuelo().equals(vuelo) && !t.esUsado()) {
                t.marcarComoUsado();
         }
        }
    }

}
