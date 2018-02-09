package com.purdue.a407.cryptodisco.CacheData;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CDResource<T> {

    @Nullable
    private final T body;

    @Nullable
    private final Throwable error;

    private Status status;

    private boolean loading;

    private CDResource(Status status, @Nullable T body, @Nullable Throwable error) {
        this.status = status;
        this.body = body;
        this.error = error;
    }


    @NonNull
    public static <T> CDResource<T> success(@NonNull T data) {
        return new CDResource<T>(Status.SUCCESS, data, null);
    }

    @NonNull
    public static <T> CDResource<T> error(@NonNull T data, Throwable error) {
        return new CDResource<T>(Status.ERROR, data, error);
    }

    @NonNull
    public static <T> CDResource<T> loading(@NonNull T data) {
        return new CDResource<T>(Status.LOADING, data, null);
    }

    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return body;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    public boolean isLoading() {
        return status == Status.LOADING;
    }
}