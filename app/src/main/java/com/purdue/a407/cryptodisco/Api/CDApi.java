package com.purdue.a407.cryptodisco.Api;

import com.purdue.a407.cryptodisco.Data.Entities.ChatJoin;
import com.purdue.a407.cryptodisco.Data.Entities.CoinEntity;
import com.purdue.a407.cryptodisco.Data.Entities.CoinPairingEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Data.Entities.SettingsEntity;
import com.purdue.a407.cryptodisco.Data.Entities.TweetId;
import com.purdue.a407.cryptodisco.Data.Entities.TweetQueryEntity;
import com.purdue.a407.cryptodisco.Data.Entities.WatchListEntity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CDApi {

    @GET("/user")
    Call<Void> userExists(@Query("uuid") String uid,
                          @Query("fcm_token") String fcm_token);

    @GET("/exchanges")
    Call<List<ExchangeEntity>> getExchanges();

    @GET("/exchange/{exchange}/allPairs")
    Call<List<CoinPairingEntity>> getCoinPairingsFromExchange(
            @Path("exchange") String exchange);

    @GET("/chatrooms")
    Call<List<ChatRoomEntity>> getChatRooms();

    @GET("/chatmessages")
    Call<List<ChatMessageEntity>> getChatMessages(@Query("room") String room);

    @GET("/chatmessages")
    Call<ChatMessageEntity> getChatMessagesTest(@Query("room") String room);

    @POST("/sendmessage")
    Call<Void> sendMessage(@Body ChatMessageEntity message);

    @POST("/insertbackup")
    Call<Void> insertBackup(@Body SettingsEntity settings);

    @GET("/removebackup")
    Call<Void> removeBackup(@Query("uuid") String uuid);

    @GET("/coins")
    Call<List<CoinEntity>> getCoins();

    @POST("/insertlikedcoin")
    Call<Void> insertLikedCoin(@Body WatchListEntity watchlist);

    @GET("/userlikedcoin")
    Call<List<WatchListEntity>> getWatchListEntities();

    @POST("/removewatchlist")
    Call<Void> removeWatchList(@Body WatchListEntity watchListEntity);

    @POST("/numberoflikedcoins")
    Call<List<SqlCount>> numberOfLikedCoins(@Body WatchListEntity watchListEntity);

    @POST("/subchat")
    Call<Void> joinChat(@Body ChatJoin chatJoin);

    @GET("/twitter")
    Call<List<TweetId>> getTweets(@Query("query") String query, @Query("count") int count);

}
