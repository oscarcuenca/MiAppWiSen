package com.amg_eservices.miappwisen.GeneradorCharts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Propietario on 09/09/2016.
 *
 * http://stackoverflow.com/questions/32371236/how-to-remove-duplicate-and-sort-objects-from-jsonarray-using-java
 *
 */
public class JSONObjectSort {

    @SuppressWarnings("unchecked")

    public static void sortASCE( JSONObject object, Object key) {

        Object[] keys = { key };
        Collections.sort((List<JSONObject>) object, new JSONObjectComparator(false, keys));
    }

    @SuppressWarnings("unchecked")
    public static void sortDESC(JSONObject object, Object key) {
        Object[] keys = {key};
        Collections.sort((List<JSONObject>) object, new JSONObjectComparator(true, keys));
    }

    @SuppressWarnings("unchecked")
    public static void sortASCE(JSONObject object, Object[] key) {
        Collections.sort((List<JSONObject>) object, new JSONObjectComparator(false, key));
    }
    @SuppressWarnings("unchecked")
    public static void sortDESC(JSONObject object, Object[] key) {
        Collections.sort((List<JSONObject>) object, new JSONObjectComparator(true, key));
    }
    private static class JSONObjectComparator implements Comparator<JSONObject> {
        private final Object[] KEYS;
        private final boolean DESC;

        public JSONObjectComparator(boolean DESC, Object[] KEYS) {
            this.KEYS = KEYS;
            this.DESC = DESC;
        }

        @Override
        public int compare(JSONObject object1, JSONObject object2) {
            int length = KEYS.length;
            for(int i = 0 ; i < length ; i++){
                String KEY = KEYS[i].toString();
                Object one = null;
                try {
                    one = object1.get(KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Object two = null;
                try {
                    two = object2.get(KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(Number.class.isAssignableFrom(one.getClass()) && Number.class.isAssignableFrom(two.getClass())){
                    Double numOne = Number.class.cast(one).doubleValue();
                    Double numTwo = Number.class.cast(two).doubleValue();
                    int compared = 0;
                    if(DESC){
                        compared = numTwo.compareTo(numOne);
                    }else{
                        compared = numOne.compareTo(numTwo);
                    }
                    if(i == KEYS.length - 1 || compared != 0){
                        return compared;
                    }
                }else{
                    int compared = 0;
                    if(DESC){
                        compared = two.toString().compareTo(one.toString());
                    }else{
                        compared = one.toString().compareTo(two.toString());
                    }
                    if(i == KEYS.length - 1 || compared != 0){
                        return compared;
                    }
                }
            }
            // this shouldn't happen.
            return 0;
        }
    }


}
