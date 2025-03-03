package uniandes.dpoo.aerolinea.persistencia;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import uniandes.dpoo.aerolinea.exceptions.AeropuertoDuplicadoException;
import uniandes.dpoo.aerolinea.exceptions.ClienteRepetidoException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.modelo.*;

public class PersistenciaAerolineaJson implements IPersistenciaAerolinea {

    @Override
    public void cargarAerolinea(String archivo, Aerolinea aerolinea) throws IOException, InformacionInconsistenteException, AeropuertoDuplicadoException, ClienteRepetidoException {
        String contenido = new String(Files.readAllBytes(Paths.get(archivo)));
        JSONObject json = new JSONObject(contenido);

        JSONArray aeropuertosJson = json.getJSONArray("aeropuertos");
        for (int i = 0; i < aeropuertosJson.length(); i++) {
            JSONObject obj = aeropuertosJson.getJSONObject(i);
            Aeropuerto aeropuerto = new Aeropuerto(
                obj.getString("nombre"), 
                obj.getString("codigo"), 
                obj.getString("ciudad"), 
                obj.getDouble("latitud"), 
                obj.getDouble("longitud")
            );

            if (aerolinea.getRuta(aeropuerto.getCodigo()) != null) {
                throw new AeropuertoDuplicadoException(aeropuerto.getCodigo());
            }
            aerolinea.agregarRuta(new Ruta(aeropuerto, aeropuerto, "00:00", "00:00", aeropuerto.getCodigo()));
        }

        JSONArray vuelosJson = json.getJSONArray("vuelos");
        for (int i = 0; i < vuelosJson.length(); i++) {
            JSONObject obj = vuelosJson.getJSONObject(i);
            Ruta ruta = aerolinea.getRuta(obj.getString("codigoRuta"));
            if (ruta == null) {
                throw new InformacionInconsistenteException("Vuelo con ruta inexistente: " + obj.getString("codigoRuta"));
            }
            Vuelo vuelo = new Vuelo(ruta, obj.getString("fecha"), aerolinea.getAvion(obj.getString("avion")));
            aerolinea.agregarVuelo(vuelo);
        }
    }

    @Override
    public void salvarAerolinea(String archivo, Aerolinea aerolinea) throws IOException {
        JSONObject json = new JSONObject();


        JSONArray aeropuertosJson = new JSONArray();
        for (Ruta ruta : aerolinea.getRutas()) {
            JSONObject obj = new JSONObject();
            Aeropuerto aeropuerto = ruta.getOrigen();
            obj.put("codigo", aeropuerto.getCodigo());
            obj.put("nombre", aeropuerto.getNombre());
            obj.put("ciudad", aeropuerto.getNombreCiudad());
            obj.put("latitud", aeropuerto.getLatitud());
            obj.put("longitud", aeropuerto.getLongitud());
            aeropuertosJson.put(obj);
        }
        json.put("aeropuertos", aeropuertosJson);

        JSONArray vuelosJson = new JSONArray();
        for (Vuelo vuelo : aerolinea.getVuelos()) {
            JSONObject obj = new JSONObject();
            obj.put("codigoRuta", vuelo.getRuta().getCodigoRuta());
            obj.put("fecha", vuelo.getFecha());
            obj.put("avion", vuelo.getAvion().getNombre());
            vuelosJson.put(obj);
        }
        json.put("vuelos", vuelosJson);

        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write(json.toString(4));
        }
    }
}
