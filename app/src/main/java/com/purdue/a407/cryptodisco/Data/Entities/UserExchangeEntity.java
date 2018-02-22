package com.purdue.a407.cryptodisco.Data.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.kyleohanian.databinding.modelbindingforms.Annotations.BindingAttributes;
import com.kyleohanian.databinding.modelbindingforms.Annotations.BindingPositioning;
import com.kyleohanian.databinding.modelbindingforms.Annotations.ModelBindingAttributes;

@Entity
@ModelBindingAttributes(padding = 10, createTitle = "Add Exchange")
public class UserExchangeEntity {

    @NonNull
    @PrimaryKey
    String name;

    @BindingAttributes(viewName = "API Key")
    @BindingPositioning(row = 1)
    String apiKey;
    @BindingAttributes(viewName = "Secret Key")
    @BindingPositioning(row = 2)
    String secretKey;

    public UserExchangeEntity(String name, String apiKey, String secretKey) {
        this.name = name;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }


    public String getName() {
        return name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
