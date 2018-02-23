package com.purdue.a407.cryptodisco.CacheData;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.util.Log;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NetworkBoundResource<Local, Remote> {
    private final MediatorLiveData<CDResource<Local>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundResource() {
        result.setValue(CDResource.loading(null));
        LiveData<Local> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> result.setValue(CDResource.success(newData)));
            }
        });
    }

    private void fetchFromNetwork(final LiveData<Local> dbSource) {
        result.addSource(dbSource, newData -> result.setValue(CDResource.loading(newData)));
        createCall().enqueue(new Callback<Remote>() {
            @Override
            public void onResponse(Call<Remote> call, Response<Remote> response) {
                if(response.code() != 200) {
                    Log.d("ERROR CODE", String.valueOf(response.code()));
                    onFetchFailed();
                    result.removeSource(dbSource);
                    result.addSource(dbSource, newData -> result.setValue(CDResource.error(newData, null)));
                    return;
                }
                result.removeSource(dbSource);
                saveResultAndReInit(response.body());
            }

            @Override
            public void onFailure(Call<Remote> call, Throwable t) {
                onFetchFailed();
                result.removeSource(dbSource);
                result.addSource(dbSource, newData -> result.setValue(CDResource.error(newData, t)));
            }
        });
    }

    @MainThread
    private void saveResultAndReInit(Remote response) {

        if (response == null) {
            Log.d("Query Response", "It's null, well shiet");
        }
        Log.d("Query Response", response.toString());
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                saveCallResult(response);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                result.addSource(loadFromDb(), newData -> result.setValue(CDResource.success(newData)));
            }
        }.execute();
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull Remote item);

    @MainThread
    protected boolean shouldFetch(@Nullable Local data) {
        return true;
    }

    @NonNull
    @MainThread
    protected abstract LiveData<Local> loadFromDb();

    @NonNull
    @MainThread
    protected abstract Call<Remote> createCall();

    @MainThread
    protected void onFetchFailed() {
    }

    public final LiveData<CDResource<Local>> getAsLiveData() {
        return result;
    }
}
