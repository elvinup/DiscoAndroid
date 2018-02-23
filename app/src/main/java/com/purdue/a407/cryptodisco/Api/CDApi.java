package com.purdue.a407.cryptodisco.Api;

import com.purdue.a407.cryptodisco.Data.Entities.CoinPairingEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CDApi {

    @GET("/user/")
    Call<Void> userExists(@Query("uuid") String uid);

    @GET("/exchanges")
    Call<List<ExchangeEntity>> getExchanges();

    @GET("/exchange/{exchange}/allPairs")
    Call<List<CoinPairingEntity>> getCoinPairingsFromExchange(
            @Path("exchange") String exchange);
    @GET("/chatrooms")
    Call<List<ChatRoomEntity>> getChatRooms();

    @GET("/chatmessages")
    Call<List<ChatMessageEntity>> getChatMessages();

    @POST("/sendmessage")
    Call<Void> sendMessage(@Body ChatMessageEntity message);
}
