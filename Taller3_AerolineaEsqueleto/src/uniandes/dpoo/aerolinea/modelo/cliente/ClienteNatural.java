package uniandes.dpoo.aerolinea.modelo.cliente;

import org.json.JSONObject;

public class ClienteNatural extends Cliente {
    public static String NATURAL = "Natural";
    private String nombre;
    private String identificador;
    
    public ClienteNatural(String nombre, String identificador){
    	this.nombre = nombre;
    	this.identificador = identificador;
    	
    }
    
	@Override
	public String getTipoCliente() {
		return NATURAL;
	}
		
	public String getNombre() {
		return this.nombre;
	}
	@Override
	public String getIdentificador() {
		return this.identificador;
	}
	
	  public static ClienteNatural cargarDesdeJSON( JSONObject cliente )
	    {

	        String iden = cliente.getString("identificador");
	        String nombre = cliente.getString("nombre");
	        return new ClienteNatural(nombre, iden );
	    }




}
