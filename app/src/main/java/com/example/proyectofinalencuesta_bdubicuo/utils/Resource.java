package com.example.proyectofinalencuesta_bdubicuo.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * @param <T>
 * @author Google samples
 */
public class Resource<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    private Resource(@NonNull Status status, @Nullable T data,
                     @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    /**
     * Creates [Resource] object with `SUCCESS` status and [data].
     */
    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    /**
     * Creates [Resource] object with `ERROR` status and [message].
     */
    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    /**
     * Creates [Resource] object with `LOADING` status to notify
     * the UI to show loading.
     */
    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resource)) return false;
        Resource<?> resource = (Resource<?>) o;
        return status == status &&
                Objects.equals(data, data) &&
                Objects.equals(message, message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, data, message);
    }
}
