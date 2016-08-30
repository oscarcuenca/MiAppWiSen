package com.amg_eservices.miappwisen.ConfiguraSensores.provider;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.UUID;

/**
 * Contrato con la estructura de la base de datos y forma de las URIs
 */
public class Contrato {

    interface ColumnasSincronizacion {
        String MODIFICADO = "modificado";
        String ELIMINADO = "eliminado";
        String INSERTADO = "insertado";
    }

    interface ColumnasObjeto {
        String ID_OBJETO = "idObjeto"; // Pk
        String DESCRIPCION_NOMBRE = "descripcionNombre";
        String MARCA_MARCA = "marca";
        String MODELO = "modelo";
        String CORREO = "correo";
        String MODIFICATO = "udpateStatus";
        String VERSION = "version";
    }


    // Autoridad del Content Provider
    public final static String AUTORIDAD = "com.amg_eservices.miappwisen";

    // Uri base
    public final static Uri URI_CONTENIDO_BASE = Uri.parse("content://" + AUTORIDAD);



    /**
     * Controlador de la tabla "objetos"
     */

    /**
     * Representación de la tabla a consultar
     */
    public static class Objetos
            implements BaseColumns, ColumnasObjeto, ColumnasSincronizacion {


        public static final Uri URI_CONTENIDO =
                URI_CONTENIDO_BASE.buildUpon().appendPath(RECURSO_OBJETO).build();

        /**
         * Tipo MIME que retorna la consulta de una sola fila
         */

        public final static String MIME_RECURSO =
                "vnd.android.cursor.item/vnd." + AUTORIDAD + "/" + RECURSO_OBJETO;

        /**
          Tipo MIME que retorna la consulta de { URI_CONTENIDO_BASE}
         */

        public final static String MIME_COLECCION =
                "vnd.android.cursor.dir/vnd." + AUTORIDAD + "/" + RECURSO_OBJETO;



        /**
         * Construye una {@link Uri} para el {@link #ID_OBJETO} solicitado.
         */
        public static Uri construirUriObjeto(String idObjeto) {
            return URI_CONTENIDO.buildUpon().appendPath(idObjeto).build();
        }

        public static String generarIdObjeto() {
            return "C-" + UUID.randomUUID();
        }

        public static String obtenerIdObjeto(Uri uri) {
            return uri.getLastPathSegment();
        }



    }
    // Recursos-nombre de la tabla
    public final static String RECURSO_OBJETO = "objetos";
}

