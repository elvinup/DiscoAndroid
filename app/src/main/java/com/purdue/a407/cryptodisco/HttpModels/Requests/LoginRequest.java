package com.purdue.a407.cryptodisco.HttpModels.Requests;

public class LoginRequest {
    String uuid;

    public LoginRequest() {}

    public LoginRequest(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
