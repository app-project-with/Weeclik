package com.grace.weeclik;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.grace.weeclik.model.Commerce;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ShareReceiver extends BroadcastReceiver {

    public static final String SMS_EXTRA_NAME = "pdus";
    private ArrayList<Commerce> commerces;
    private String name_trade;
    private int nbshare_trade;

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

                if (messageBody.contains("Bonjour, je te conseille ce commerçant ")) {
                    Toast.makeText(context, "Vous avez recu un partage", Toast.LENGTH_LONG).show();
                    searchCommerce(messages[0], "address_commerce");
                    createNotification(context, "Nouveau partage", "");
                    addCommerce(context, messages[0]);
                }
            }
        }
    }

    private void addCommerce(Context context, SmsMessage message) {
        // TODO : Add commerce dans les commerces a essayer
    }

    // TODO : cherche commerce by name add address
    private void searchCommerce(SmsMessage smsMessage, String address_commerce) {
        String parts[] = smsMessage.getMessageBody().split("\"", 3);
        String name_commerce = parts[1].substring(1);
        Log.e("AAAAAAAAAAAAAAAAAA", name_commerce);
        commerces = new ArrayList<>();
        // va chercher dans la bd , la table "commerce"
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Commerce");
        // requete qui me cherche toutes les valeurs avec "nomCommerce" et "nombrePartages"
        query.selectKeys(Arrays.asList("nomCommerce", "nombrePartages", "typeCommerce")).whereEqualTo("nomCommerce", name_commerce);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    for (int i = 0; i < objects.size(); i++) {
                        //ParseObject comm = object.getParseObject("nomCommerce");
                        String img_id = objects.get(i).getParseObject("thumbnailPrincipal").getObjectId();
                        List<ParseObject> image = null;
                        name_trade = objects.get(i).getString("nomCommerce");
                        nbshare_trade = objects.get(i).getInt("nombrePartages");
                        commerces.add(new Commerce(name_trade, nbshare_trade, image.get(0).getParseFile("photo").getUrl()));
                        Log.e("COMMERCE", "Commerce " + commerces.get(i) + " ---- " + img_id);
                    }
                } else {
                    Log.e("COMMERCE", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void createNotification(Context context, String msgAlert, String msgText) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("NAME_TRADE", name_trade);
        intent.putExtra("NBSHARE_TRADE", nbshare_trade);
        //context.startActivity(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent/*new Intent(context, DetailsActivity.class)*/, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "grace_bk_id")
                .setSmallIcon(R.drawable.ic_location)
                .setContentTitle("Weeclik")
                .setTicker(msgAlert)
                .setContentText(msgText);
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(manager).notify(1, builder.build());
    }
}
