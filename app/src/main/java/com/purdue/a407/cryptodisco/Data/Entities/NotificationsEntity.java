package com.purdue.a407.cryptodisco.Data.Entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class NotificationsEntity {

    @PrimaryKey(autoGenerate = true)
    int notificationId;

    String message;
    String timeStamp;
    boolean checked;

    public NotificationsEntity(String message, String timeStamp, boolean checked) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.checked = checked;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
