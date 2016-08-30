package com.amg_eservices.miappwisen.ConfiguraSensores.modelo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.amg_eservices.miappwisen.ConfiguraSensores.provider.Contrato;
import com.amg_eservices.miappwisen.ConfiguraSensores.utilidades.UConsultas;
import com.amg_eservices.miappwisen.ConfiguraSensores.utilidades.UDatos;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Actua como un transformador desde SQLite a JSON para enviar objetos al servidor
 */
public class ProcesadorRemoto {
    private static final String TAG = ProcesadorRemoto.class.getSimpleName();

    // Campos JSON
    private static final String INSERCIONES = "inserciones";
    private static final String MODIFICACIONES = "modificaciones";
    private static final String ELIMINACIONES = "eliminaciones";

    private Gson gson = new Gson();

    private interface ConsultaObjetos {

        // Proyección para consulta de objetos
        String[] PROYECCION = {
                Contrato.Objetos.ID_OBJETO,
                Contrato.Objetos.DESCRIPCION_NOMBRE,
                Contrato.Objetos.MARCA_MARCA,
                Contrato.Objetos.MODELO,
                Contrato.Objetos.CORREO,
                Contrato.Objetos.VERSION

        };
    }


    public String crearPayload(ContentResolver cr) {
        HashMap<String, Object> payload = new HashMap<>();

        List<Map<String, Object>> inserciones = obtenerInserciones(cr);
        List<Map<String, Object>> modificaciones = obtenerModificaciones(cr);
        List<String> eliminaciones = obtenerEliminaciones(cr);

        // Verificación: ¿Hay cambios locales?
        if (inserciones == null && modificaciones == null && eliminaciones == null) {
            return null;
        }

        payload.put(INSERCIONES, inserciones);
        payload.put(MODIFICACIONES, modificaciones);
        payload.put(ELIMINACIONES, eliminaciones);

        return gson.toJson(payload);
    }

    public List<Map<String, Object>> obtenerInserciones(ContentResolver cr) {
        List<Map<String, Object>> ops = new ArrayList<>();

        // Obtener objetos donde 'insertado' = 1
        Cursor c = cr.query(Contrato.Objetos.URI_CONTENIDO,
                ConsultaObjetos.PROYECCION,
                Contrato.Objetos.INSERTADO + "=?",
                new String[]{"1"}, null);

        // Comprobar si hay trabajo que realizar
        if (c != null && c.getCount() > 0) {

            Log.d(TAG, "Inserciones remotas: " + c.getCount());

            // Procesar inserciones
            while (c.moveToNext()) {
                ops.add(mapearInsercion(c));
            }

            return ops;

        } else {
            return null;
        }

    }

    public List<Map<String, Object>> obtenerModificaciones(ContentResolver cr) {

        List<Map<String, Object>> ops = new ArrayList<>();

        // Obtener Objetos donde 'modificado' = 1
        Cursor c = cr.query(Contrato.Objetos.URI_CONTENIDO,
                ConsultaObjetos.PROYECCION,
                Contrato.Objetos.MODIFICADO + "=?",
                new String[]{"1"}, null);

        // Comprobar si hay trabajo que realizar
        if (c != null && c.getCount() > 0) {

            Log.d(TAG, "Existen " + c.getCount() + " modificaciones de objetos");

            // Procesar operaciones
            while (c.moveToNext()) {
                ops.add(mapearActualizacion(c));
            }

            return ops;

        } else {
            return null;
        }

    }

    public List<String> obtenerEliminaciones(ContentResolver cr) {

        List<String> ops = new ArrayList<>();

        // Obtener objetos donde 'eliminado' = 1
        Cursor c = cr.query(Contrato.Objetos.URI_CONTENIDO,
                ConsultaObjetos.PROYECCION,
                Contrato.Objetos.ELIMINADO + "=?",
                new String[]{"1"}, null);

        // Comprobar si hay trabajo que realizar
        if (c != null && c.getCount() > 0) {

            Log.d(TAG, "Existen " + c.getCount() + " eliminaciones de objetos");

            // Procesar operaciones
            while (c.moveToNext()) {
                ops.add(UConsultas.obtenerString(c, Contrato.Objetos.ID_OBJETO));
            }

            return ops;

        } else {
            return null;
        }

    }


    /**
     * Desmarca los OBJETOS locales que ya han sido sincronizados
     *
     * @param cr content resolver
     */
    public void desmarcarObjetos(ContentResolver cr) {
        // Establecer valores de la actualización
        ContentValues valores = new ContentValues();
        valores.put(Contrato.Objetos.INSERTADO, 0);
        valores.put(Contrato.Objetos.MODIFICADO, 0);

        String seleccion = Contrato.Objetos.INSERTADO + " = ? OR " +
                Contrato.Objetos.MODIFICADO + "= ?";
        String[] argumentos = {"1", "1"};

        // Modificar banderas de insertados y modificados
        cr.update(Contrato.Objetos.URI_CONTENIDO, valores, seleccion, argumentos);

        seleccion = Contrato.Objetos.ELIMINADO + "=?";
        // Eliminar definitivamente
        cr.delete(Contrato.Objetos.URI_CONTENIDO, seleccion, new String[]{"1"});

    }

    private Map<String, Object> mapearInsercion(Cursor c) {
        // Nuevo mapa para reflejarlo en JSON
        Map<String, Object> mapaObjeto = new HashMap<String, Object>();

        // Añadir valores de columnas como atributos
        UDatos.agregarStringAMapa(mapaObjeto, Contrato.Objetos.ID_OBJETO, c);
        UDatos.agregarStringAMapa(mapaObjeto, Contrato.Objetos.DESCRIPCION_NOMBRE, c);
        UDatos.agregarStringAMapa(mapaObjeto, Contrato.Objetos.MARCA_MARCA, c);
        UDatos.agregarStringAMapa(mapaObjeto, Contrato.Objetos.MODELO, c);
        UDatos.agregarStringAMapa(mapaObjeto, Contrato.Objetos.CORREO, c);
        UDatos.agregarStringAMapa(mapaObjeto, Contrato.Objetos.VERSION, c);

        return mapaObjeto;
    }

    private Map<String, Object> mapearActualizacion(Cursor c) {
        // Nuevo mapa para reflejarlo en JSON
        Map<String, Object> mapaObjeto = new HashMap<String, Object>();


        // Añadir valores de columnas como atributos
        UDatos.agregarStringAMapa(mapaObjeto, Contrato.Objetos.ID_OBJETO, c);
        UDatos.agregarStringAMapa(mapaObjeto, Contrato.Objetos.DESCRIPCION_NOMBRE, c);
        UDatos.agregarStringAMapa(mapaObjeto, Contrato.Objetos.MARCA_MARCA, c);
        UDatos.agregarStringAMapa(mapaObjeto, Contrato.Objetos.MODELO, c);
        UDatos.agregarStringAMapa(mapaObjeto, Contrato.Objetos.CORREO, c);
        UDatos.agregarStringAMapa(mapaObjeto, Contrato.Objetos.VERSION, c);

        return mapaObjeto;
    }
}

