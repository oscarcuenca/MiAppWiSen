package com.amg_eservices.miappwisen.ConfiguraSensores.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.amg_eservices.miappwisen.ConfiguraSensores.provider.Contrato.Objetos;
import com.amg_eservices.miappwisen.ConfiguraSensores.provider.HelperObjetos.Tablas;

/**
 * {@link ContentProvider} que encapsula el acceso a la base de datos de objetos
 */
public class ProviderObjetos extends ContentProvider {

    // Comparador de URIs de contenido
    public static final UriMatcher uriMatcher;

    // Identificadores de tipos
    /**
     * Código para URIs de multiples registros
     */
    public static final int OBJETOS = 100;
    /**
     * Código para URIS de un solo registro
     */
    public static final int OBJETOS_ID = 101;

    static {
        // Asignación de URIs

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contrato.AUTORIDAD, "objetos", OBJETOS);
        uriMatcher.addURI(Contrato.AUTORIDAD, "objetos/*", OBJETOS_ID);
    }
    /**
     * Instancia del administrador de BD
     */
    private HelperObjetos manejadorBD;
    /**
     * Instancia global del Content Resolver
     */
    private ContentResolver resolver;


    @Override
    public boolean onCreate() {
        // Inicializando gestor BD
        manejadorBD = new HelperObjetos(getContext());
        resolver = getContext().getContentResolver();
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case OBJETOS:
                return Contrato.Objetos.MIME_COLECCION;
            case OBJETOS_ID:
                return Contrato.Objetos.MIME_RECURSO;
            default:
                throw new IllegalArgumentException("Tipo desconocido: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Obtener base de datos
        SQLiteDatabase db = manejadorBD.getWritableDatabase();
        // Comparar Uri
        int match = uriMatcher.match(uri);

        Cursor c;

        switch (match) {
            case OBJETOS:
                // Consultando todos los registros
                c = db.query(Tablas.OBJETO, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(resolver, Contrato.Objetos.URI_CONTENIDO);
                break;
            case OBJETOS_ID:
                // Consultando un solo registro basado en el Id del Uri
                String idObjeto = Contrato.Objetos.obtenerIdObjeto(uri);
                c = db.query(Tablas.OBJETO, projection,
                        Contrato.Objetos.ID_OBJETO + "=" + "\'" + idObjeto + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver, uri);
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = manejadorBD.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int filasAfectadas;

        switch (match) {
            case OBJETOS:

                filasAfectadas = db.delete(Tablas.OBJETO,
                        selection,
                        selectionArgs);

                resolver.notifyChange(uri, null, false);

                break;
            case OBJETOS_ID:

                String idObjeto = Objetos.obtenerIdObjeto(uri);

                filasAfectadas = db.delete(Tablas.OBJETO,
                        Objetos.ID_OBJETO + "=" + "\'" + idObjeto + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);

                resolver.notifyChange(uri, null, false);
                break;
            default:
                throw new IllegalArgumentException("Objeto desconocido: " +
                        uri);
        }
        return filasAfectadas;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Validar la uri
        if (uriMatcher.match(uri) != OBJETOS) {
            throw new IllegalArgumentException("URI desconocida : " + uri);
        }

        ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }

        // Inserción de nueva fila
        SQLiteDatabase db = manejadorBD.getWritableDatabase();

        long _id = db.insert(Tablas.OBJETO, null, contentValues);

        if (_id > 0) {

            resolver.notifyChange(uri, null, false);

            String idObjeto = contentValues.getAsString(Objetos.ID_OBJETO);

            return Objetos.construirUriObjeto(idObjeto);
        }
        throw new SQLException("Falla al insertar fila en : " + uri);
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = manejadorBD.getWritableDatabase();

        int filasAfectadas;

        switch (uriMatcher.match(uri)) {
            case OBJETOS:

                filasAfectadas = db.update(Tablas.OBJETO, values,
                        selection, selectionArgs);

                resolver.notifyChange(uri, null, false);

                break;
            case OBJETOS_ID:

                String idObjeto = Objetos.obtenerIdObjeto(uri);

                filasAfectadas = db.update(Tablas.OBJETO, values,
                        Objetos.ID_OBJETO + "=" + "\'" + idObjeto + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);

                resolver.notifyChange(uri, null, false);

                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }



        return filasAfectadas;
    }
}
