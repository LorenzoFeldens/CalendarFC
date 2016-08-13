package lorenzofeldens.calendarfc;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";

    @Override
    public void onReceive(Context context, Intent intent) {
        Scheduling scheduling = new Scheduling(context);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int id = intent.getIntExtra(NOTIFICATION_ID, -1);

        Notification notification = null;
        if(id == 1){
            notification = scheduling.getNotificationUpdate();
        }
        if(id == 0){
            notification = scheduling.getNotificationGames();
        }

        if(notification == null){
            return;
        }

        notificationManager.notify(id,notification);
        scheduling.setNotifications();
    }
}
