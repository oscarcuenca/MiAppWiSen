package com.amg_eservices.miappwisen.ConfiguraSensores.sync;


        import android.accounts.Account;
        import android.content.AbstractThreadedSyncAdapter;
        import android.content.ContentProviderClient;
        import android.content.ContentProviderOperation;
        import android.content.ContentResolver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.OperationApplicationException;
        import android.content.SyncResult;
        import android.os.Bundle;
        import android.os.RemoteException;
        import android.support.v4.content.LocalBroadcastManager;
        import android.util.Log;

        import com.android.volley.NetworkError;
        import com.android.volley.NoConnectionError;
        import com.android.volley.ParseError;
        import com.android.volley.Response;
        import com.android.volley.ServerError;
        import com.android.volley.TimeoutError;
        import com.android.volley.VolleyError;
        import com.google.gson.Gson;
        import com.google.gson.JsonSyntaxException;
        import com.amg_eservices.miappwisen.ConfiguraSensores.modelo.ProcesadorLocal;
        import com.amg_eservices.miappwisen.ConfiguraSensores.modelo.ProcesadorRemoto;
        import com.amg_eservices.miappwisen.ConfiguraSensores.provider.Contrato;
        import com.amg_eservices.miappwisen.ConfiguraSensores.provider.Contrato.Objetos;
        import com.amg_eservices.miappwisen.ConfiguraSensores.utilidades.UPreferencias;
        import com.amg_eservices.miappwisen.ConfiguraSensores.web.RESTService;
        import com.amg_eservices.miappwisen.ConfiguraSensores.web.RespuestaApi;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;

/**
 * Sincronizador cliente-servidor
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();

    // Extras para intent local
    public static final String EXTRA_RESULTADO = "extra.resultado";
    private static final String EXTRA_MENSAJE = "extra.mensaje";

    // Recurso sync
    public static final String URL_SYNC_BATCH = "http://wi-sen.esy.es/wisen/Sensores/v1/sync";

    private static final int ESTADO_PETICION_FALLIDA = 107;
    private static final int ESTADO_TIEMPO_ESPERA = 108;
    private static final int ESTADO_ERROR_PARSING = 109;
    private static final int ESTADO_ERROR_SERVIDOR = 110;


    private ContentResolver cr;
    private Gson gson = new Gson();
    private ProcesadorRemoto procRemoto = new ProcesadorRemoto();

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        cr = context.getContentResolver();
    }

    /**
     * Constructor para mantener compatibilidad en versiones inferiores a 3.0
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        cr = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              final SyncResult syncResult) {

        Log.i(TAG, "Comenzando a sincronizar:" + account);

        // Sincronización local
        syncLocal();

    }

    private void syncLocal() {
        // ConstrucciÃ³n de cabeceras
        HashMap<String, String> cabeceras = new HashMap<>();
        cabeceras.put("Authorization", UPreferencias.obtenerClaveApi(getContext()));

        // Petición GET
        new RESTService(getContext()).get(URL_SYNC_BATCH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar GET
                        tratarGet(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Procesar Error
                        tratarErrores(error);
                    }
                }, cabeceras);
    }

    private void tratarGet(JSONObject respuesta) {

        try {
            // Crear referencia de lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // Convertir array JSON de descuentos a modelo
            ProcesadorLocal manejadorObjetos = new ProcesadorLocal();
            manejadorObjetos.procesar(respuesta.getJSONArray("objetos"));
            manejadorObjetos.procesarOperaciones(ops, cr);


            // Â¿ Hay operaciones por realizar ?
            if (ops.size() > 0) {
                Log.d(TAG, "# Cambios en \'objeto\': " + ops.size() + " ops.");
                // Aplicar batch de operaciones
                cr.applyBatch(Contrato.AUTORIDAD, ops);
                // Notificar cambio al content provider
                cr.notifyChange(Objetos.URI_CONTENIDO, null, false);
            } else {
                Log.d(TAG, "Sin cambios remotos");
            }

            // Sincronización remota
            syncRemota();

        } catch (RemoteException | OperationApplicationException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void syncRemota() {
        procRemoto = new ProcesadorRemoto();

        // Construir payload con todas las operaciones remotas pendientes
        String datos = procRemoto.crearPayload(cr);

        if (datos != null) {
            Log.d(TAG, "Payload de objetos:" + datos);

            HashMap<String, String> cabeceras = new HashMap<>();
            cabeceras.put("Authorization", UPreferencias.obtenerClaveApi(getContext()));

            new RESTService(getContext()).post(URL_SYNC_BATCH, datos,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            tratarPost();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            tratarErrores(error);
                        }
                    }
                    , cabeceras);
        } else {
            Log.d(TAG, "Sin cambios locales");
            enviarBroadcast(true, "Sincronización completa");
        }
    }


    private void enviarBroadcast(boolean estado, String mensaje) {
        Intent intentLocal = new Intent(Intent.ACTION_SYNC);
        intentLocal.putExtra(EXTRA_RESULTADO, estado);
        intentLocal.putExtra(EXTRA_MENSAJE, mensaje);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intentLocal);
    }

    private void tratarPost() {
        // Desmarcar inserciones locales
        procRemoto.desmarcarObjetos(cr);
        enviarBroadcast(true, "Sincronizacion completa");
    }




    private void tratarErrores(VolleyError error) {




        // Crear respuesta de error por defecto
        RespuestaApi respuesta = new RespuestaApi(ESTADO_PETICION_FALLIDA,
                "Peticion incorrecta");


        // VerificaciÃ³n: Â¿La respuesta tiene contenido interpretable?
        if (error.networkResponse != null) {

            String s = new String(error.networkResponse.data);
            try {
                respuesta = gson.fromJson(s, RespuestaApi.class);
            } catch (JsonSyntaxException e) {
                Log.d(TAG, "Error de parsing: " + s);
            }

        }

        if (error instanceof NetworkError) {
            respuesta = new RespuestaApi(ESTADO_TIEMPO_ESPERA
                    , "Error en la conexiÃ³n. Intentalo de nuevo");
        }

        // Error de espera al servidor
        if (error instanceof TimeoutError) {
            respuesta = new RespuestaApi(ESTADO_TIEMPO_ESPERA, "Error de espera del servidor");
        }

        // Error de parsing
        if (error instanceof ParseError) {
            respuesta = new RespuestaApi(ESTADO_ERROR_PARSING, "La respuesta no es formato JSON");
        }

        // Error conexiÃ³n servidor
        if (error instanceof ServerError) {
            respuesta = new RespuestaApi(ESTADO_ERROR_SERVIDOR, "Error en el servidor");
        }

        if (error instanceof NoConnectionError) {
            respuesta = new RespuestaApi(ESTADO_ERROR_SERVIDOR
                    , "Servidor no disponible, prueba mas tarde");
        }

        Log.d(TAG, "Error Respuesta:" + (respuesta != null ? respuesta.toString() : "()")
                + "\nDetalles:" + error.getMessage());

        enviarBroadcast(false, respuesta.getMensaje());

    }
}

