package com.purdue.a407.cryptodisco.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.NotificationsEntity;

import java.util.List;

import javax.inject.Inject;

public class NotificationsViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;

    public NotificationsViewModel(@NonNull Application application,
                                  AppDatabase appDatabase) {
        super(application);
        this.appDatabase = appDatabase;

    }

    public LiveData<List<NotificationsEntity>> getNotifications() {
        return appDatabase.notificationsDao().getNotifications();
    }
}
