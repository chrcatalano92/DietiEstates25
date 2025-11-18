package com.demo.dietiestates25.support_classes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

/**
 * Classe Base Abstract per un'implementazione del Principio Open/Closed (rispetta la O di SOLID). Fornisce una base per tutti i DiffUtil.ItemCallback dell'app. Gestisce la logica comune, lasciando la logica variabile (i metodi abstract) ai figli.
 * ALTA COESIONE (SRP): La sua Sola Responsabilità è gestire la logica di base del DiffUtil payload.
 * SOLID (Open/Closed): Perfetto. La classe è "chiusa" alle modifiche (non devi più toccare la logica dei payload) ma "aperta" all'estensione (puoi creare N classi figlie per TrackedOfferData, RealEstateAd, ecc.).
 */
public abstract class GenericDiffRecyclerViewCallback<T> extends DiffUtil.ItemCallback<T> {

    @Override
    public abstract boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem);

    @Override
    public abstract boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem);

    @Nullable
    @Override
    public Object getChangePayload(@NonNull T oldItem, @NonNull T newItem) {
        Bundle diffBundle = new Bundle();

        populateChangePayload(diffBundle, oldItem, newItem);

        return diffBundle.isEmpty() ? null : diffBundle;
    }

    protected abstract void populateChangePayload(@NonNull Bundle diffBundle, @NonNull T oldItem, @NonNull T newItem);
}