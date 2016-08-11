package lorenzofeldens.calendarfc;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationPublisher
        extends BroadcastReceiver
{
    public static String NOTIFICATION = "notification";
    public static String NOTIFICATION_ID = "notification-id";

    public void onReceive(Context paramContext, Intent paramIntent)
    {
        Scheduling localScheduling = new Scheduling(paramContext);
        NotificationManager localNotificationManager = (NotificationManager)paramContext.getSystemService("notification");
        int i = paramIntent.getIntExtra(NOTIFICATION_ID, 0);
        if (i == 1) {}
        for (paramContext = localScheduling.getNotificationGames();; paramContext = localScheduling.getNotificationUpdate())
        {
            if ((i != 0) && (paramContext != null)) {
                localNotificationManager.notify(i, paramContext);
            }
            localScheduling.setNotifications();
            return;
        }
    }
}
