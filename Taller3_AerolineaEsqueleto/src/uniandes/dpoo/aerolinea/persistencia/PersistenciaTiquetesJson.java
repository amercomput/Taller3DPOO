package uniandes.dpoo.aerolinea.persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONObject;

import uniandes.dpoo.aerolinea.exceptions.ClienteRepetidoException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteTiqueteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteCorporativo;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteNatural;
import uniandes.dpoo.aerolinea.tiquetes.GeneradorTiquetes;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class PersistenciaTiquetesJson implements IPersistenciaTiquetes {

    private static final String IDENTIFICADOR_CLIENTE = "identificador";
    private static final String NOMBRE_CLIENTE = "nombre";
    private static final String TIPO_CLIENTE = "tipoCliente";
    private static final String CLIENTE = "cliente";
    private static final String USADO = "usado";
    private static final String TARIFA = "tarifa";
    private static final String CODIGO_TIQUETE = "codigoTiquete";
    private static final String FECHA = "fecha";
    private static final String CODIGO_RUTA = "codigoRuta";

    @Override
    public void cargarTiquetes(String archivo, Aerolinea aerolinea) throws IOException, InformacionInconsistenteException {
        String jsonCompleto = new String(Files.readAllBytes(new File(archivo).toPath()));
        JSONObject raiz = new JSONObject(jsonCompleto);

        cargarClientes(aerolinea, raiz.getJSONArray("clientes"));
        cargarTiquetes(aerolinea, raiz.getJSONArray("tiquetes"));
    }

    @Override
    public void salvarTiquetes(String archivo, Aerolinea aerolinea) throws IOException {
        JSONObject jobject = new JSONObject();

        salvarClientes(aerolinea, jobject);
        salvarTiquetes(aerolinea, jobject);

        try (PrintWriter pw = new PrintWriter(archivo)) {
            jobject.write(pw, 2, 0);
        }
    }

    private void cargarClientes(Aerolinea aerolinea, JSONArray jClientes) throws ClienteRepetidoException {
        for (int i = 0; i < jClientes.length(); i++) {
            JSONObject clienteJson = jClientes.getJSONObject(i);
            String tipoCliente = clienteJson.getString(TIPO_CLIENTE).trim();
            String identificador = clienteJson.getString(IDENTIFICADOR_CLIENTE).trim();
            String nombre = clienteJson.optString(NOMBRE_CLIENTE, "Sin Nombre");

            Cliente nuevoCliente;
            if (ClienteNatural.NATURAL.equalsIgnoreCase(tipoCliente)) {
                nuevoCliente = new ClienteNatural(nombre, identificador);
            } else {
                nuevoCliente = ClienteCorporativo.cargarDesdeJSON(clienteJson);
            }

            if (!aerolinea.existeCliente(nuevoCliente.getIdentificador())) {
                aerolinea.agregarCliente(nuevoCliente);
            } else {
                throw new ClienteRepetidoException(nuevoCliente.getTipoCliente(), nuevoCliente.getIdentificador());
            }
        }
    }

    private void salvarClientes(Aerolinea aerolinea, JSONObject jobject) {
        JSONArray jClientes = new JSONArray();
        for (Cliente cliente : aerolinea.getClientes()) {
            JSONObject jCliente = new JSONObject();
            jCliente.put(IDENTIFICADOR_CLIENTE, cliente.getIdentificador());
            jCliente.put(NOMBRE_CLIENTE, cliente.getNombre());
            jClientes.put(jCliente);
        }
        jobject.put("clientes", jClientes);
    }

    private void cargarTiquetes(Aerolinea aerolinea, JSONArray jTiquetes) throws InformacionInconsistenteTiqueteException {
        int tiquetesCargados = 0;
        for (int i = 0; i < jTiquetes.length(); i++) {
            JSONObject tiqueteJson = jTiquetes.getJSONObject(i);

            String codigoRuta = tiqueteJson.getString(CODIGO_RUTA);
            String fechaVuelo = tiqueteJson.getString(FECHA);
            String codigoTiquete = tiqueteJson.getString(CODIGO_TIQUETE);
            int tarifa = tiqueteJson.getInt(TARIFA);
            boolean tiqueteUsado = tiqueteJson.getBoolean(USADO);
            String identificadorCliente = tiqueteJson.getString(CLIENTE).trim();

            Ruta ruta = aerolinea.getRuta(codigoRuta);
            if (ruta == null) {
                throw new InformacionInconsistenteTiqueteException("ruta", codigoRuta);
            }

            Vuelo vuelo = aerolinea.getVuelo(codigoRuta, fechaVuelo);
            if (vuelo == null) {
                System.out.println("El vuelo para la ruta " + codigoRuta + " en fecha " + fechaVuelo + " no existe.");
                continue;
            }

            Cliente cliente = aerolinea.getCliente(identificadorCliente);
            if (cliente == null) {
                System.out.println("Cliente no encontrado: " + identificadorCliente);
                continue;
            }

            Tiquete nuevoTiquete = new Tiquete(codigoTiquete, vuelo, cliente, tarifa);
            if (tiqueteUsado) {
                nuevoTiquete.marcarComoUsado();
            }

            GeneradorTiquetes.registrarTiquete(nuevoTiquete);
            tiquetesCargados++;
        }
        System.out.println("Tiquetes cargados: " + tiquetesCargados);
    }


    private void salvarTiquetes(Aerolinea aerolinea, JSONObject jobject) {
        JSONArray jTiquetes = new JSONArray();
        for (Tiquete tiquete : aerolinea.getTiquetes()) {
            JSONObject jTiquete = new JSONObject();
            jTiquete.put(CODIGO_TIQUETE, tiquete.getCodigo());
            jTiquete.put(CODIGO_RUTA, tiquete.getVuelo().getRuta().getCodigoRuta());
            jTiquete.put(FECHA, tiquete.getVuelo().getFecha());
            jTiquete.put(TARIFA, tiquete.getTarifa());
            jTiquete.put(USADO, tiquete.esUsado());
            jTiquete.put(CLIENTE, tiquete.getCliente().getIdentificador());
            jTiquetes.put(jTiquete);
        }
        jobject.put("tiquetes", jTiquetes);
    }
}

