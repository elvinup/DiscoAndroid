package com.purdue.a407.cryptodisco.CDson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.purdue.a407.cryptodisco.HttpModels.Responses.ExchangeResponse;

import java.lang.reflect.Type;

public class ResponseWrapperAdapter implements JsonDeserializer<ExchangeResponse> {

    private static Gson sGson = new Gson();

    public ResponseWrapperAdapter() {}

    @Override
    public ExchangeResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {



        if(json.isJsonArray()) {
            JsonArray array = json.getAsJsonArray();
            JsonObject object = new JsonObject();
            object.add("data", array);
            return sGson.fromJson(object, typeOfT);
        }
        else {
            return sGson.fromJson(json.getAsJsonObject(),typeOfT);
        }
    }
}