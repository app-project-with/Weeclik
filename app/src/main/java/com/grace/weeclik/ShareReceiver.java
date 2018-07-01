package com.grace.weeclik;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Objects;

public class ShareReceiver extends BroadcastReceiver {

    public static final String SMS_EXTRA_NAME = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        String ACTION_RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";
        if (Objects.equals(intent.getAction(), ACTION_RECEIVE_SMS)) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                Object[] pdus = ((Object[]) bundle.get(SMS_EXTRA_NAME));

                final SmsMessage[] messages = new SmsMessage[Objects.requireNonNull(pdus).length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                //if (messages[0].getMessageBody().charAt(0) == 'i') {
                final String messageBody = messages[0].getMessageBody();
                final String phoneNumber = messages[0].getDisplayOriginatingAddress();

                if (messageBody.contains("@weeclik ")) {
                    Toast.makeText(context, "Vous avez recu un partage", Toast.LENGTH_LONG).show();
                    createNotification(context, "Nouveau partage", "");
                    addCommerce(context, messages[0]);
                }
            }
        }
    }

    private void addCommerce(Context context, SmsMessage message) {
        // TODO : Add commerce dans les commerces a essayer
    }

    public void createNotification(Context context, String msgAlert, String msgText) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, DetailsActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "grace_bk_id")
                .setSmallIcon(R.drawable.ic_location)
                .setContentTitle("Nouveau SMS dechiffre")
                .setTicker(msgAlert)
                .setContentText(msgText);
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(manager).notify(1, builder.build());
    }
}
