package uniandes.dpoo.aerolinea.persistencia;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import uniandes.dpoo.aerolinea.exceptions.AeropuertoDuplicadoException;
import uniandes.dpoo.aerolinea.exceptions.ClienteRepetidoException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.modelo.*;

public class PersistenciaAerolineaJson implements IPersistenciaAerolinea {

    @Override
    public void cargarAerolinea(String archivo, Aerolinea aerolinea) 
            throws IOException, InformacionInconsistenteException, AeropuertoDuplicadoException, ClienteRepetidoException {

        String contenido = new String(Files.readAllBytes(Paths.get(archivo)));
        JSONObject json = new JSONObject(contenido);

        HashMap<String, Aeropuerto> aeropuertos = new HashMap<>();

        JSONArray aeropuertosJson = json.getJSONArray("aeropuertos");
        for (int i = 0; i < aeropuertosJson.length(); i++) {
            JSONObject obj = aeropuertosJson.getJSONObject(i);
            String codigo = obj.getString("codigo");

            if (aeropuertos.containsKey(codigo)) {
                throw new AeropuertoDuplicadoException(codigo);
            }

            Aeropuerto aeropuerto = new Aeropuerto(
                obj.getString("nombre"),
                codigo,
                obj.getString("ciudad"),
                obj.getDouble("latitud"),
                obj.getDouble("longitud")
            );

            aeropuertos.put(codigo, aeropuerto);
        }

        JSONArray rutasJson = json.getJSONArray("rutas");
        for (int i = 0; i < rutasJson.length(); i++) {
            JSONObject obj = rutasJson.getJSONObject(i);
            String codigoRuta = obj.getString("codigoRuta");
            String codigoOrigen = obj.getString("origen");
            String codigoDestino = obj.getString("destino");

            Aeropuerto origen = aeropuertos.get(codigoOrigen);
            Aeropuerto destino = aeropuertos.get(codigoDestino);

            if (origen == null || destino == null) {
                throw new InformacionInconsistenteException("Ruta con aeropuertos inexistentes: " + codigoRuta);
            }

            Ruta nuevaRuta = new Ruta(origen, destino, obj.getString("horaSalida"), obj.getString("horaLlegada"), codigoRuta);
            aerolinea.agregarRuta(nuevaRuta);
        }

        JSONArray vuelosJson = json.getJSONArray("vuelos");
        for (int i = 0; i < vuelosJson.length(); i++) {
            JSONObject obj = vuelosJson.getJSONObject(i);
            String codigoRuta = obj.getString("codigoRuta");
            Ruta ruta = aerolinea.getRuta(codigoRuta);

            if (ruta == null) {
                throw new InformacionInconsistenteException("Vuelo con ruta inexistente: " + codigoRuta);
            }

            Avion avion = aerolinea.getAvion(obj.getString("avion"));
            if (avion == null) {
                throw new InformacionInconsistenteException("Vuelo con avión inexistente: " + obj.getString("avion"));
            }

            Vuelo vuelo = new Vuelo(ruta, obj.getString("fecha"), avion);
            aerolinea.agregarVuelo(vuelo);
        }
    }

    @Override
    public void salvarAerolinea(String archivo, Aerolinea aerolinea) throws IOException {
        JSONObject json = new JSONObject();

        JSONArray aeropuertosJson = new JSONArray();
        for (Ruta ruta : aerolinea.getRutas()) {
            Aeropuerto origen = ruta.getOrigen();
            Aeropuerto destino = ruta.getDestino();

            if (!aeropuertosJson.toString().contains(origen.getCodigo())) {
                JSONObject objOrigen = new JSONObject();
                objOrigen.put("codigo", origen.getCodigo());
                objOrigen.put("nombre", origen.getNombre());
                objOrigen.put("ciudad", origen.getNombreCiudad());
                objOrigen.put("latitud", origen.getLatitud());
                objOrigen.put("longitud", origen.getLongitud());
                aeropuertosJson.put(objOrigen);
            }

            if (!aeropuertosJson.toString().contains(destino.getCodigo())) {
                JSONObject objDestino = new JSONObject();
                objDestino.put("codigo", destino.getCodigo());
                objDestino.put("nombre", destino.getNombre());
                objDestino.put("ciudad", destino.getNombreCiudad());
                objDestino.put("latitud", destino.getLatitud());
                objDestino.put("longitud", destino.getLongitud());
                aeropuertosJson.put(objDestino);
            }
        }
        json.put("aeropuertos", aeropuertosJson);

        JSONArray rutasJson = new JSONArray();
        for (Ruta ruta : aerolinea.getRutas()) {
            JSONObject obj = new JSONObject();
            obj.put("codigoRuta", ruta.getCodigoRuta());
            obj.put("origen", ruta.getOrigen().getCodigo());
            obj.put("destino", ruta.getDestino().getCodigo());
            obj.put("horaSalida", ruta.getHoraSalida());
            obj.put("horaLlegada", ruta.getHoraLlegada());
            rutasJson.put(obj);
        }
        json.put("rutas", rutasJson);

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

