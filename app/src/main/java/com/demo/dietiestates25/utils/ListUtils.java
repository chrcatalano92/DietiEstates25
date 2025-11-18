package com.demo.dietiestates25.utils;

import java.util.List;

/**
 * Classe Utils (statica e final) per operazioni comuni sulle Liste.
 * SRP: Fornisce funzioni helper per la manipolazione di collezioni.
 */
public final class ListUtils {

    private ListUtils() { }

    /**
     * Trova la prima posizione di una stringa in una lista.
     * @return L'indice, o -1 se non trovato o in caso di input non valido.
     */
    public static int getStringPosition(String searchString, List<String> stringList) {
        if(searchString == null || stringList == null || stringList.isEmpty()) {
            return  -1;
        }

        for(int i=0; i < stringList.size(); i++) {
            String currStr = stringList.get(i);

            if(searchString.equals(currStr)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Svuota una lista e restituisce il primo elemento (il "placeholder").
     */
    public static String resetItemsList(List<String> oldlist) {
        if(oldlist != null && !oldlist.isEmpty()) {
            String placeholder = oldlist.get(0);
            oldlist.clear();
            return placeholder;
        }
        return null;
    }
}