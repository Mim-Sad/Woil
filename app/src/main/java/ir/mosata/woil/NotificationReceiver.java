package ir.mosata.woil;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static ir.mosata.woil.MainActivity.CHANNEL_ID;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Come Back Notifications
        Intent repeating_game_intent = new Intent(context,MainActivity.class);
        repeating_game_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingGameIntent = PendingIntent.getActivity(context,88, repeating_game_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingGameIntent)
                .setSmallIcon(R.drawable.ic_woillogo_white)
                .setContentTitle(context.getString(R.string.notif_me_title))
                .setContentText(context.getString(R.string.notif_me_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setShowWhen(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(88, builder.build());
    }
}
