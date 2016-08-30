package com.amg_eservices.miappwisen.ConfiguraSensores.ui;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amg_eservices.miappwisen.ConfiguraSensores.provider.Contrato.Objetos;
import com.amg_eservices.miappwisen.ConfiguraSensores.utilidades.UConsultas;
import com.amg_eservices.miappwisen.R;
//import com.amg_eservices.appiot.R;

/**
 * Adaptador para la lista de objetos
 */
public class AdaptadorObjetos extends RecyclerView.Adapter<AdaptadorObjetos.ViewHolder> {
    private Cursor items;

    // Instancia de escucha
    private OnItemClickListener escucha;

    /**
     * Interfaz para escuchar clicks del recycler
     */
    interface OnItemClickListener {
        /*
        void onReceive(Context context, Intent intent);
*/

        public void onClick(ViewHolder holder, String idObjeto);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // Campos respectivos de un item
        public TextView nombre;

        public ViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.nombre_objeto);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            escucha.onClick(this, obtenerIdObjeto(getAdapterPosition()));
        }
    }

    /**
     * Obtiene el valor de la columna 'idObjeto' basado en la posición actual del cursor
     * @param posicion Posición actual del cursor
     * @return Identificador del objeto
     */
    private String obtenerIdObjeto(int posicion) {
        if (items != null) {
            if (items.moveToPosition(posicion)) {
                return UConsultas.obtenerString(items, Objetos.ID_OBJETO);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public AdaptadorObjetos(OnItemClickListener escucha) {
        this.escucha = escucha;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_objeto, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        items.moveToPosition(position);

        String descripcionNombre;
        String marca;

        descripcionNombre = UConsultas.obtenerString(items,Objetos.DESCRIPCION_NOMBRE);
        marca = UConsultas.obtenerString(items,Objetos.MARCA_MARCA);

        holder.nombre.setText(String.format("%s %s", descripcionNombre, marca));


    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.getCount();
        return 0;
    }

    public void swapCursor(Cursor nuevoCursor) {
        if (nuevoCursor != null) {

            items = nuevoCursor;
            notifyDataSetChanged();
        }
    }

    public Cursor getCursor() {
        return items;
    }

}

