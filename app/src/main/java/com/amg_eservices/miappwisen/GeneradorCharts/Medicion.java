package com.amg_eservices.miappwisen.GeneradorCharts;

/**
 * Created by pedrogonzalezferrandez on 13/09/16.
 */
public class Medicion {

    private String temperatura, presion, fecha, id;

    public Medicion(String temperatura, String presion, String fecha, String id) {
        this.temperatura = temperatura;
        this.presion = presion;
        this.fecha = fecha;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Medicion medicion = (Medicion) o;

//        if (!temperatura.equals(medicion.temperatura)) return false;
//        if (!presion.equals(medicion.presion)) return false;
//        if (!fecha.equals(medicion.fecha)) return false;
        return id.equals(medicion.id);

    }

    @Override
    public int hashCode() {
        int result = temperatura.hashCode();
        result = 31 * result + presion.hashCode();
        result = 31 * result + fecha.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
