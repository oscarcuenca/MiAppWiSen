package com.amg_eservices.miappwisen.ConfiguraSensores.modelo;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.amg_eservices.miappwisen.ConfiguraSensores.provider.Contrato;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class ProcesadorLocal {

    private static final String TAG = ProcesadorLocal.class.getSimpleName();

    private interface ConsultaObjetos {

        // Proyección para consulta de objetos
        String[] PROYECCION = {
                Contrato.Objetos.ID_OBJETO,
                Contrato.Objetos.DESCRIPCION_NOMBRE,
                Contrato.Objetos.MARCA_MARCA,
                Contrato.Objetos.MODELO,
                Contrato.Objetos.CORREO,
                Contrato.Objetos.VERSION,
                Contrato.Objetos.MODIFICADO
        };

        // Indices de columnas
        int ID_OBJETO = 0;
        int DESCRIPCION_NOMBRE = 1;
        int MARCA_MARCA = 2;
        int MODELO = 3;
        int CORREO = 4;
        int VERSION = 5;
        int MODIFICADO = 6;

    }

    // Mapa para filtrar solo los elementos a sincronizar
    private HashMap<String, Objeto> objetosRemotos = new HashMap<>();

    // Conversor JSON
    private Gson gson = new Gson();

    public ProcesadorLocal() {
    }

    public void procesar(JSONArray arrayJsonObjetos) {
        // Añadir elementos convertidos a los objetos remotos
        for (Objeto objetoActual : gson
                .fromJson(arrayJsonObjetos.toString(), Objeto[].class)) {
            objetoActual.aplicarSanidad();
            objetosRemotos.put(objetoActual.idObjeto, objetoActual);
        }
    }

    public void procesarOperaciones(ArrayList<ContentProviderOperation> ops, ContentResolver resolver) {

        // Consultar objetos locales
        Cursor c = resolver.query(Contrato.Objetos.URI_CONTENIDO,
                ConsultaObjetos.PROYECCION,
                Contrato.Objetos.INSERTADO + "=?",
                new String[]{"0"}, null);

        if (c != null) {

            while (c.moveToNext()) {

                // Convertir fila del cursor en objeto Objeto
                Objeto filaActual = deCursorAObjeto(c);

                // Buscar si el objeto actual se encuentra en el mapa de mapaobjetos
                Objeto match = objetosRemotos.get(filaActual.idObjeto);

                if (match != null) {
                    // Esta entrada existe, por lo que se remueve del mapeado
                    objetosRemotos.remove(filaActual.idObjeto);

                    // Crear uri de este objeto
                    Uri updateUri = Contrato.Objetos.construirUriObjeto(filaActual.idObjeto);

                    /*
                    Aquí se aplica la resolución de conflictos de modificaciones de un mismo recurso
                    tanto en el servidro como en la app. Quién tenga la versión más actual, será tomado
                    como preponderante
                     */
                    if (!match.compararCon(filaActual)) {
                        int flag = match.esMasReciente(filaActual);
                        if (flag > 0) {
                            Log.d(TAG, "Programar actualización  del objeto " + updateUri);

                            // Verificación: ¿Existe conflicto de modificación?
                            if (filaActual.modificado == 1) {
                                match.modificado = 0;
                            }
                            ops.add(construirOperacionUpdate(match, updateUri));

                        }

                    }

                } else {
                    /*
                    Se deduce que aquellos elementos que no coincidieron, ya no existen en el servidor,
                    por lo que se eliminarán
                     */
                    Uri deleteUri = Contrato.Objetos.construirUriObjeto(filaActual.idObjeto);
                    Log.i(TAG, "Programar Eliminación del objeto " + deleteUri);
                    ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                }
            }
            c.close();
        }

        // Insertar los items resultantes ya que se asume que no existen en el objeto
        for (Objeto objeto : objetosRemotos.values()) {
            Log.d(TAG, "Programar Inserción de un nuevo objeto con ID = " + objeto.idObjeto);
            ops.add(construirOperacionInsert(objeto));
        }
    }

    private ContentProviderOperation construirOperacionInsert(Objeto objeto) {
        return ContentProviderOperation.newInsert(Contrato.Objetos.URI_CONTENIDO)
                .withValue(Contrato.Objetos.ID_OBJETO, objeto.idObjeto)
                .withValue(Contrato.Objetos.DESCRIPCION_NOMBRE, objeto.descripcionNombre)
                .withValue(Contrato.Objetos.MARCA_MARCA, objeto.marca)
                .withValue(Contrato.Objetos.MODELO, objeto.modelo)
                .withValue(Contrato.Objetos.CORREO, objeto.correo)
                .withValue(Contrato.Objetos.VERSION, objeto.version)
                .withValue(Contrato.Objetos.INSERTADO, 0)
                .build();
    }

    private ContentProviderOperation construirOperacionUpdate(Objeto match, Uri updateUri) {
        return ContentProviderOperation.newUpdate(updateUri)
                .withValue(Contrato.Objetos.ID_OBJETO, match.idObjeto)
                .withValue(Contrato.Objetos.DESCRIPCION_NOMBRE, match.descripcionNombre)
                .withValue(Contrato.Objetos.MARCA_MARCA, match.marca)
                .withValue(Contrato.Objetos.MODELO, match.modelo)
                .withValue(Contrato.Objetos.CORREO, match.correo)
                .withValue(Contrato.Objetos.VERSION, match.version)
                .withValue(Contrato.Objetos.MODIFICADO, match.modificado)
                .build();
    }

    /**
     * Convierte una fila de un Cursor en un nuevo Objeto
     *
     * @param c cursor
     * @return objeto objeto
     */
    private Objeto deCursorAObjeto(Cursor c) {
        return new Objeto(
                c.getString(ConsultaObjetos.ID_OBJETO),
                c.getString(ConsultaObjetos.DESCRIPCION_NOMBRE),
                c.getString(ConsultaObjetos.MARCA_MARCA),
                c.getString(ConsultaObjetos.MODELO),
                c.getString(ConsultaObjetos.CORREO),
                c.getString(ConsultaObjetos.VERSION),
                c.getInt(ConsultaObjetos.MODIFICADO)
        );
    }
}
