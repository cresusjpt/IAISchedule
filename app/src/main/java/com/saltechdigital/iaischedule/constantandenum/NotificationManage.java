package com.saltechdigital.iaischedule.constantandenum;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;

import com.saltechdigital.iaischedule.R;
import com.saltechdigital.iaischedule.ValidPresenceActivity;
import com.saltechdigital.iaischedule.database.presence.TPresence;

import java.util.List;

/**
 * Created by Jeanpaul Tossou on 21/11/2017.
 */

public class NotificationManage {

    public NotificationManage() {
    }

    public void notificateValid(Context context, List<TPresence> presenceList) {

        Intent intent = new Intent(context, ValidPresenceActivity.class);

        for (int i = 0; i < presenceList.size(); i++) {
            intent.putExtra("presence" + i, presenceList.get(i));
        }
        intent.putExtra("nombre", presenceList.size());

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, toString());

        builder.setTicker("Nouveau controle de presence reçu");
        builder.setContentText("Vous avez des controles de presence pas encore validé");
        builder.setContentTitle("Validation de controle de presence");
        builder.setSmallIcon(R.mipmap.logo_round);
        builder.setAutoCancel(true);

        builder.setStyle(new NotificationCompat.BigTextStyle(builder));
        builder.setContentIntent(pendingIntent);

        Notification notification1 = builder.build();

        NotificationManagerCompat.from(context).notify(0, notification1);
    }
}
