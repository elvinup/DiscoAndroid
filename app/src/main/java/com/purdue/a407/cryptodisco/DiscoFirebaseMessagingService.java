package com.purdue.a407.cryptodisco;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.purdue.a407.cryptodisco.Activities.HomeActivity;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.Arbitrage;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.Data.Entities.NotificationsEntity;
import com.purdue.a407.cryptodisco.Fragments.ApiHelpers;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.service.trade.TradeService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

public class DiscoFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "DiscoFirebaseMsgService";
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]

    @Inject
    AppDatabase appDatabase;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        ((App)getApplication()).getNetComponent().inject(this);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> mapping = remoteMessage.getData();
            if(mapping.get("type").equals("0")) {
                String uuid = mapping.get("uuid");
                String chatID = mapping.get("chatroom_id");
                String body = mapping.get("body");
                appDatabase.chatmsgDao().insert(new ChatMessageEntity(body, uuid, "",
                        Integer.parseInt(chatID)));
            } else if(mapping.get("type").equals("1")) {

                String currencyPairStr = mapping.get("coin_short").concat("/").concat(mapping.get("market_short"));
                try {
                    MarketOrder.Builder marketBuilder = new MarketOrder.Builder(Order.OrderType.BID, new CurrencyPair(currencyPairStr));
                    marketBuilder.originalAmount(new BigDecimal(Double.parseDouble(mapping.get("size"))));
                    marketBuilder.timestamp(new Date());
                    MarketOrder marketOrder = marketBuilder.build();
                    Exchange exchange = ApiHelpers.getExchange(getApplicationContext(), mapping.get("exchange"));
                    exchange.getTradeService().placeMarketOrder(marketOrder);

                    String msg = "Order: ";
                    msg += Boolean.parseBoolean(mapping.get("side")) ? "Buying " : "Selling ";
                    msg += mapping.get("size").concat(" ");
                    msg += mapping.get("coin_short").concat(" in ");
                    msg += mapping.get("market_short").concat(" market on ");
                    msg += mapping.get("exchange").concat(".");

                    sendNotification("Executing Transaction", msg);

                    appDatabase.notificationsDao().
                            insert(new NotificationsEntity(msg, String.valueOf(new Date().getTime()), false));
                } catch (Exception e) {
                    sendNotification("Order Failed", e.getLocalizedMessage().toString());
                    appDatabase.notificationsDao().
                            insert(new NotificationsEntity(e.getLocalizedMessage().toString(), String.valueOf(new Date().getTime()), false));
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "\n\n\nSO THE REG TOKEN IS: " + FirebaseInstanceId.getInstance().getToken() + "\n\n\n\n" );
            sendArbitrageNotification(remoteMessage.getNotification().getBody());
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendArbitrageNotification(String messageBody) {
        Arbitrage arb = new Gson().fromJson(messageBody, Arbitrage.class);
        String translate = String.format("There is a percent difference between exchanges" +
                        " %s and %s with the coin pairing: %s",
                arb.getFirst().getExchange(), arb.getSecond().getExchange(), arb.getFirst().getCoin_short());

        sendNotification("Arbitrage Alert!", translate);

        appDatabase.notificationsDao().
                insert(new NotificationsEntity(messageBody, String.valueOf(new Date().getTime()), false));

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.app_initials_blue)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "CD Note Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
