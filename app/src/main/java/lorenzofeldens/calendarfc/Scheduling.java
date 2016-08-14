package lorenzofeldens.calendarfc;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import dao.GameDAO;
import entidades.Game;

class Scheduling {
    private String competitions;
    private String primaryTeams;
    private String secondaryTeams;

    private final Context context;
    private final NotificationManager notificationManager;

    private ArrayList<Integer> notifications;
    private ArrayList<Game> games;

    private static final String NOTIFICATION_EXTRAS = "NOTIFICATION_EXTRAS";
    private static final String NOTIFICATIONS_GROUP = "CalendarFC_Notification_Group";
    private static final String NOTIFICATION_UPDATE_TITLE = "Atualização Necessária";
    private static final String NOTIFICATION_UPDATE_TEXT = "Clique para Atualizar";
    private static final int NOTIFICATION_GAMES_ID = 1;
    private static final int NOTIFICATION_UPDATE_ID = 2;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public Scheduling(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void setNotifications() {
        if (getData()) {
            setNotificationsGames();
            setNotificationUpdate();
        }
    }

    private boolean getData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(
                R.string.shared_pref_file_key), Context.MODE_PRIVATE);

        if(!sharedPreferences.getBoolean(context.getString(
                R.string.shared_pref_notifications_enabled),true))
            return false;

        notifications = new ArrayList<>();

        primaryTeams = sharedPreferences.getString(context.getString(
                R.string.shared_pref_primary_teams),"");
        secondaryTeams = sharedPreferences.getString(context.getString(
                R.string.shared_pref_secondary_teams),"");
        competitions = sharedPreferences.getString(context.getString(
                R.string.shared_pref_competitions),"");

        String[] array = sharedPreferences.getString(context.getString(
                R.string.shared_pref_notifications),"10").split("#");

        for (String anArray : array) {
            notifications.add(Integer.valueOf(anArray));
        }

        Collections.sort(notifications, Collections.reverseOrder());
        return true;
    }

    private void setNotificationUpdate() {
        String nextUpdate = new GameDAO(context).getNextUpdate();

        Date date = null;

        try{
            date = DATE_FORMAT.parse(nextUpdate);
        }catch (ParseException p){
            p.printStackTrace();
        }

        if(date != null){
            scheduleNotification(date.getTime(),NOTIFICATION_UPDATE_ID);
        }
    }

    private void setNotificationsGames() {
        if (notifications == null) {
            if (!getData()) {
                return;
            }
        }

        Date date = new Date();
        date.setTime(date.getTime() + TimeUnit.MINUTES.toMillis(notifications.get(0)));

        ArrayList<String> possibleGameTimes = new GameDAO(context).getPossibleGameTimes(
                primaryTeams, secondaryTeams, competitions, date);

        ArrayList<String> possibleNotificationTimes = new ArrayList<>();

        for (int i = 0; i < possibleGameTimes.size(); i++) {
            date = null;
            try {
                date = DATE_FORMAT.parse(possibleGameTimes.get(i));
            } catch (ParseException p) {
                p.printStackTrace();
            }
            if (date != null) {
                possibleNotificationTimes.addAll(getPossibleNotificationsTimesGame(date));
            }
        }

        Collections.sort(possibleNotificationTimes);

        String[] array = possibleNotificationTimes.get(0).split("#");
        String time = array[0];

        ArrayList<String> notificationsTimes = getNotificationsTimes(
                possibleNotificationTimes, time);

        games = new GameDAO(context).getGamesNotification(primaryTeams, secondaryTeams,
                competitions, notificationsTimes);

        Date now = new Date();
        date = null;

        try {
            date = DATE_FORMAT.parse(time);
        }catch (ParseException p){
            p.printStackTrace();
        }

        if(date != null) {
            long futureInMillis = date.getTime() - now.getTime();

            scheduleNotification(futureInMillis,NOTIFICATION_GAMES_ID);
        }
    }

    private ArrayList<String> getNotificationsTimes(ArrayList<String> possibleTimes, String time){
        ArrayList<String> list = new ArrayList<>();

        for(int i=1; i<list.size(); i++){
            String[] array = possibleTimes.get(i).split("#");

            if(!array[0].equalsIgnoreCase(time)){
                return list;
            }
            list.add(array[1]);
        }

        return list;
    }

    private ArrayList<String> getPossibleNotificationsTimesGame(Date game){
        ArrayList<String> list = new ArrayList<>();

        for(int i=0; i<notifications.size(); i++){
            Date date = new Date();
            date.setTime(game.getTime() - TimeUnit.MINUTES.toMillis(notifications.get(i)));

            if(date.after(new Date())){
                list.add(DATE_FORMAT.format(date)+"#"+DATE_FORMAT.format(game));
            }
        }

        return list;
    }

    private void scheduleNotification(long delay, int notificationId) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);

        if(notificationId == NOTIFICATION_GAMES_ID){
            Bundle bundle = new Bundle();
            bundle.putSerializable(NotificationPublisher.ARRAY_GAMES, games);
            notificationIntent.putExtras(bundle);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = System.currentTimeMillis() + delay;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }

    public Notification getNotificationGames(ArrayList<Game> games) {
        this.games = games;

        ArrayList<String> gamesShown = null;

        if (Build.VERSION.SDK_INT >= 23) {
            StatusBarNotification[] statusBarNotifications = notificationManager
                    .getActiveNotifications();
            if (statusBarNotifications.length != 0) {
                gamesShown = NotificationCompat.getExtras(statusBarNotifications[0]
                        .getNotification()).getStringArrayList(NOTIFICATION_EXTRAS);
            }
        }

        if(gamesShown == null){
            gamesShown = new ArrayList<>();
        }

        for(int i=0; i<games.size(); i++){
            gamesShown.add(games.get(i).getDate()+"#"+games.get(i).getTitle());
        }

        HashSet<String> hashSet = new HashSet<>();
        hashSet.addAll(gamesShown);
        gamesShown.clear();
        gamesShown.addAll(hashSet);

        Collections.sort(gamesShown);

        if(gamesShown.size() == 0){
            return null;
        }
        if(gamesShown.size() == 1){
            return getNotification1Game(gamesShown);
        }
        return getNotificationMoreGames(gamesShown);
    }

    private Notification getNotification1Game(ArrayList<String> list) {
        String[] array = list.get(0).split("#");
        String[] time = array[0].split(" ");
        String[] title = array[1].split(" - ");

        Intent intent = new Intent(context, MyGames.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MyGames.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(title[0]);
        builder.setContentText(time[1]+ " - "+title[1]);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentIntent(resultPendingIntent);
        builder.setPriority(Notification.PRIORITY_MAX);

        if(Build.VERSION.SDK_INT >= 20){
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(NOTIFICATION_EXTRAS, list);

            builder.setGroup(NOTIFICATIONS_GROUP);
            builder.addExtras(bundle);
        }

        builder.setWhen(new Date().getTime());
        builder.setVibrate(new long[]{ 1000, 1000});

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        return notification;
    }

    private Notification getNotificationMoreGames(ArrayList<String> list) {
        Intent intent = new Intent(context, MyGames.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MyGames.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(String.valueOf(list.size()) + "Jogos");
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentIntent(resultPendingIntent);
        builder.setPriority(Notification.PRIORITY_MAX);

        if(Build.VERSION.SDK_INT >= 20){
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(NOTIFICATION_EXTRAS, list);

            builder.setGroup(NOTIFICATIONS_GROUP);
            builder.addExtras(bundle);
        }

        builder.setWhen(new Date().getTime());
        builder.setVibrate(new long[]{ 1000, 1000});

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.setBigContentTitle(String.valueOf(list.size()) + " Jogos");

        for(int i=0; i<list.size() && i<5; i++){
            String[] array = list.get(i).split("#");
            String[] time = array[0].split(" ");
            String[] title = array[1].split(" - ");

            style.addLine(time[1]+" - "+title[0]);
        }

        if(list.size() > 5){
            style.setSummaryText(String.valueOf(list.size() - 5) + " Mais..");
        }

        builder.setStyle(style);
        builder.setGroupSummary(true);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        return notification;
    }

    public Notification getNotificationUpdate() {
        Intent intent = new Intent(context, Update.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Update.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(NOTIFICATION_UPDATE_TITLE);
        builder.setContentText(NOTIFICATION_UPDATE_TEXT);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentIntent(resultPendingIntent);
        builder.setPriority(Notification.PRIORITY_MAX);

        builder.setWhen(new Date().getTime());
        builder.setVibrate(new long[]{ 1000, 1000});

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        return notification;
    }

    public void removeNotifications() {
        notificationManager.cancelAll();
    }


}
