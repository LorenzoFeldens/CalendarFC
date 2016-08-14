package lorenzofeldens.calendarfc;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import entidades.Game;

public class NotificationPublisher extends BroadcastReceiver {
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String ARRAY_GAMES = "ARRAY_GAMES";

    @Override
    public void onReceive(Context context, Intent intent) {
        Scheduling scheduling = new Scheduling(context);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        int id = intent.getIntExtra(NOTIFICATION_ID, -1);

        Notification notification = null;
        if(id == 1){
            notification = scheduling.getNotificationUpdate();
        }
        if(id == 0){
            Bundle bundle = intent.getExtras();
            ArrayList<Game> list = verifySerializable(bundle.getSerializable(ARRAY_GAMES));
            notification = scheduling.getNotificationGames(list);
        }

        if(notification == null){
            return;
        }

        notificationManager.notify(id,notification);
        scheduling.setNotifications();
    }

    private ArrayList<Game> verifySerializable(Object object){
        ArrayList<Game> ret = new ArrayList<>();

        if(object instanceof ArrayList){
            for(int i=0; i<((ArrayList) object).size(); i++){
                if(((ArrayList) object).get(i) instanceof Game){
                    ret.add((Game)((ArrayList) object).get(i));
                }else{
                    return new ArrayList<>();
                }
            }
        }else{
            return new ArrayList<>();
        }

        return ret;
    }
}
