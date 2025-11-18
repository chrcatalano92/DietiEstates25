package com.demo.dietiestates25.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Classe Utils (WRAPPER) per la libreria GLIDE.
 * SRP: Caricare immagini
 */
public final class ImageLoaderUtils {

    private ImageLoaderUtils() { throw new UnsupportedOperationException("Questa è una classe utils e non può essere istanziata."); }

    /**
     * Carica un'immagine da un Uri locale.
     *
     * @param context Il Context.
     * @param imgUri  L'Uri dell'immagine.
     * @param imgId   L'ImageView di destinazione.
     */
    public static void loadImgFromUri(Context context, Uri imgUri, ImageView imgId) {
        Glide.with(context)
                .load(imgUri)
                .into(imgId);
    }

    /**
     * Carica un'immagine da un URL remoto.
     * @param imageView L'ImageView di destinazione (usata per il Context).
     * @param imgUrl L'URL dell'immagine.
     * @param defaultImgId L'ID della risorsa da usare come placeholder/errore.
     */
    public static void loadImgFromUrl(ImageView imageView, String imgUrl, int defaultImgId) {
        if(imgUrl != null && !imgUrl.isEmpty()) {
            Glide.with(imageView)
                    .load(imgUrl)
                    .placeholder(defaultImgId)
                    .error(defaultImgId)
                    .into(imageView);
        }
        else {
            imageView.setImageResource(defaultImgId);
        }
    }
}
