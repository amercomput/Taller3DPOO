package uniandes.dpoo.aerolinea.consola;

import java.io.IOException;

import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.persistencia.CentralPersistencia;
import uniandes.dpoo.aerolinea.persistencia.TipoInvalidoException;

public class ConsolaAerolinea extends ConsolaBasica
{
    private Aerolinea unaAerolinea;

    /**
     * Es un método que corre la aplicación y realmente no hace nada interesante: sólo muestra cómo se podría utilizar la clase Aerolínea para hacer pruebas.
     */
    public void correrAplicacion( )
    {
        try
        {
            unaAerolinea = new Aerolinea( );
          
            String archivoAerolinea = System.getProperty("user.dir") + "/datos/aerolinea.json";
            unaAerolinea.cargarAerolinea(archivoAerolinea, CentralPersistencia.JSON);
            // String archivo = this.pedirCadenaAlUsuario( "Digite el nombre del archivo json con la información de una aerolinea" );
            String archivo = System.getProperty("user.dir") + "/datos/tiquetes.json";
            unaAerolinea.cargarTiquetes( archivo, CentralPersistencia.JSON );
            System.out.println("Carga completada. Clientes registrados: " + unaAerolinea.getClientes().size());
            System.out.println("Tiquetes cargados: " + unaAerolinea.getTiquetes().size());


        }
        catch( TipoInvalidoException e )
        {
            e.printStackTrace( );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        catch( InformacionInconsistenteException e )
        {
            e.printStackTrace();
        }
    }

    public static void main( String[] args )
    {
        ConsolaAerolinea ca = new ConsolaAerolinea( );
        ca.correrAplicacion( );
    }
}
