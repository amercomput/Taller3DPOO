package uniandes.dpoo.aerolinea.modelo.cliente;

public class ClienteNatural extends Cliente {
    public static String NATURAL = "Natural";
    private String nombre;
    private String identificador;
    
    public ClienteNatural(String nombre){
    	this.nombre = nombre;
    	
    }
    
	@Override
	public String getTipoCliente() {
		return NATURAL;
	}
	
	public void setIdentificador(String id) {
		this.identificador = id;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	@Override
	public String getIdentificador() {
		return identificador;
	}
}
