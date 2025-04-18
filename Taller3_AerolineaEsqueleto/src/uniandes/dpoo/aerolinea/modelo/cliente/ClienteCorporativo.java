package uniandes.dpoo.aerolinea.modelo.cliente;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

/**
 * Esta clase se usa para representar a los clientes de la aerolínea que son empresas
 */
public class ClienteCorporativo extends Cliente
{
    public String CORPORATIVO = "Corporativo";
    public int GRANDE= 1;
    public int MEDIANA = 2;
    public int PEQUENA = 3;
    private String nombreEmpresa;
    private static  int tamanoEmpresa;
    public List<Tiquete> tiquetes;
    private String identificador;
    private String nombre;
    
    public ClienteCorporativo(String nombreEmpresa, int tamano, String identificador) {
		this.nombreEmpresa = nombreEmpresa;
		ClienteCorporativo.tamanoEmpresa = tamano;
		tiquetes = new ArrayList<>();
		this.identificador = identificador;
		
	}

    public String getNombreEmpresa() {
    	 return this.nombreEmpresa;
    }
    
    public  int getTamanoEmpresa() {
    	return tamanoEmpresa;
    }
    
	@Override
	public String getTipoCliente() {
		return CORPORATIVO;
	}
	
	
	@Override
	public String getIdentificador() {
		return this.identificador;
	}

	/**
     * Crea un nuevo objeto de tipo a partir de un objeto JSON.
     * 
     * El objeto JSON debe tener dos atributos: nombreEmpresa (una cadena) y tamanoEmpresa (un número).
     * @param cliente El objeto JSON que contiene la información
     * @return El nuevo objeto inicializado con la información
     */
    public static ClienteCorporativo cargarDesdeJSON( JSONObject cliente )
    {
        String nombreEmpresa = cliente.getString( "nombreEmpresa" );
        int tam = cliente.getInt( "tamanoEmpresa" );
        String iden = cliente.getString("identificador");
        return new ClienteCorporativo( nombreEmpresa, tam, iden );
    }

    /**
     * Salva este objeto de tipo ClienteCorporativo dentro de un objeto JSONObject para que ese objeto se almacene en un archivo
     * @return El objeto JSON con toda la información del cliente corporativo
     */
    public JSONObject salvarEnJSON( )
    {
        JSONObject jobject = new JSONObject( );
        jobject.put( "nombreEmpresa", this.nombreEmpresa );
        jobject.put( "tamanoEmpresa", tamanoEmpresa );
        jobject.put( "tipo", CORPORATIVO );
        jobject.put("identificador", identificador);
        return jobject;
    }


	@Override
	public String getNombre() {
		// TODO Auto-generated method stub
		return this.nombre;
	}





}
