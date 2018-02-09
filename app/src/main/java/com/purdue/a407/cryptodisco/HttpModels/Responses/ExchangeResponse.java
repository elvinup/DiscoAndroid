package com.purdue.a407.cryptodisco.HttpModels.Responses;

import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;

import java.util.List;

public class ExchangeResponse {

    List<ExchangeEntity> data;

    public ExchangeResponse(List<ExchangeEntity> data) {
        this.data = data;
    }

    public List<ExchangeEntity> getData() {
        return data;
    }

    public void setData(List<ExchangeEntity> data) {
        this.data = data;
    }
}
