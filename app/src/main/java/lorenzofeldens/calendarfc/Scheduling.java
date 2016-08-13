package lorenzofeldens.calendarfc;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import dao.GameDAO;
import entidades.Game;

public class Scheduling
{
    static final int ID_GAMES_NOTIFICATION = 1;
    static final int ID_UPDATE_NOTIFICATION = 2;
    static final String NOTIFICATION_EXTRAS = "notification_content";
    String competicoes;
    Context context;
    ArrayList<Integer> delay;
    ArrayList<Game> jogos;
    NotificationManager notificationManager;
    String timesPrincipais;
    String timesSecundarios;

    public Scheduling(Context paramContext)
    {
        this.context = paramContext;
        this.notificationManager = ((NotificationManager)paramContext.getSystemService("notification"));
    }

    private void fillJogos(int paramInt)
    {
        if (this.delay == null) {
            getData();
        }
        ArrayList localArrayList1 = new ArrayList();
        int i = 0;
        while (i < this.delay.size())
        {
            localObject1 = new Date();
            ((Date)localObject1).setTime(((Date)localObject1).getTime() + TimeUnit.MINUTES.toMillis(((Integer)this.delay.get(i)).intValue()));
            if (paramInt == 0) {
                ((Date)localObject1).setTime(((Date)localObject1).getTime() - TimeUnit.MINUTES.toMillis(1L));
            }
            localObject1 = new GameDAO(this.context).getNextNotificationGames(this.timesPrincipais, this.timesSecundarios, this.competicoes, (Date)localObject1);
            if (!((String)localObject1).equalsIgnoreCase("")) {
                localArrayList1.add(localObject1);
            }
            i += 1;
        }
        if (localArrayList1.size() == 0) {
            return;
        }
        ArrayList localArrayList2 = new ArrayList();
        paramInt = 0;
        while (paramInt < localArrayList1.size())
        {
            i = 0;
            for (;;)
            {
                if (i >= this.delay.size()) {
                    break label338;
                }
                localObject1 = null;
                localObject3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try
                {
                    localDate = ((SimpleDateFormat)localObject3).parse((String)localArrayList1.get(paramInt));
                    localObject1 = localDate;
                }
                catch (ParseException localParseException)
                {
                    for (;;)
                    {
                        Date localDate;
                        localParseException.printStackTrace();
                    }
                }
                if (localObject1 != null)
                {
                    ((Date)localObject1).setTime(((Date)localObject1).getTime() - TimeUnit.MINUTES.toMillis(((Integer)this.delay.get(i)).intValue()));
                    localDate = new Date();
                    localDate.setTime(localDate.getTime() - TimeUnit.MINUTES.toMillis(1L));
                    if (((Date)localObject1).after(localDate)) {
                        localArrayList2.add(((SimpleDateFormat)localObject3).format((Date)localObject1) + "#" + paramInt);
                    }
                }
                i += 1;
            }
            label338:
            paramInt += 1;
        }
        Collections.sort(localArrayList2);
        Object localObject1 = new ArrayList();
        Object localObject3 = ((String)localArrayList2.get(0)).split("#");
        Object localObject2 = localObject3[0];
        ((ArrayList)localObject1).add(Integer.valueOf(localObject3[1]));
        paramInt = 1;
        if (paramInt < localArrayList2.size())
        {
            localObject3 = ((String)localArrayList2.get(paramInt)).split("#");
            if (localObject3[0].equalsIgnoreCase((String)localObject2)) {
                ((ArrayList)localObject1).add(Integer.valueOf(localObject3[1]));
            }
            for (;;)
            {
                paramInt += 1;
                break;
                paramInt = localArrayList2.size();
            }
        }
        localObject2 = new ArrayList();
        paramInt = 0;
        while (paramInt < ((ArrayList)localObject1).size())
        {
            ((ArrayList)localObject2).add(localArrayList1.get(((Integer)((ArrayList)localObject1).get(paramInt)).intValue()));
            paramInt += 1;
        }
        this.jogos = new GameDAO(this.context).getNotificationGames(this.timesPrincipais, this.timesSecundarios, this.competicoes, (ArrayList)localObject2);
    }

    private boolean getData()
    {
        int i = 0;
        Object localObject = this.context.getSharedPreferences(this.context.getString(2131099705), 0);
        if (((SharedPreferences)localObject).getInt(this.context.getString(2131099703), 1) == 0) {
            return false;
        }
        this.delay = new ArrayList();
        this.timesPrincipais = ((SharedPreferences)localObject).getString(this.context.getString(2131099707), "");
        this.timesSecundarios = ((SharedPreferences)localObject).getString(this.context.getString(2131099708), "");
        this.competicoes = ((SharedPreferences)localObject).getString(this.context.getString(2131099702), "");
        localObject = ((SharedPreferences)localObject).getString(this.context.getString(2131099706), "10").trim().split("#");
        int j = localObject.length;
        while (i < j)
        {
            String str = localObject[i];
            this.delay.add(Integer.valueOf(str));
            i += 1;
        }
        Collections.sort(this.delay, Collections.reverseOrder());
        return true;
    }

    private Notification getNotification1Game(ArrayList<String> paramArrayList)
    {
        String[] arrayOfString = ((String)paramArrayList.get(0)).split("#");
        Object localObject1 = arrayOfString[1].split(" - ");
        arrayOfString = arrayOfString[0].split(" ");
        Object localObject2 = new Intent(this.context, MyGames.class);
        Object localObject3 = TaskStackBuilder.create(this.context);
        ((TaskStackBuilder)localObject3).addParentStack(MyGames.class);
        ((TaskStackBuilder)localObject3).addNextIntent((Intent)localObject2);
        localObject3 = ((TaskStackBuilder)localObject3).getPendingIntent(0, 134217728);
        localObject2 = new Notification.Builder(this.context);
        ((Notification.Builder)localObject2).setContentTitle(localObject1[0]);
        ((Notification.Builder)localObject2).setContentText(arrayOfString[1] + " - " + localObject1[1]);
        ((Notification.Builder)localObject2).setSmallIcon(2130837627);
        ((Notification.Builder)localObject2).setContentIntent((PendingIntent)localObject3);
        ((Notification.Builder)localObject2).setPriority(1);
        if (Build.VERSION.SDK_INT >= 20)
        {
            localObject1 = new Bundle();
            ((Bundle)localObject1).putStringArrayList("notification_content", paramArrayList);
            ((Notification.Builder)localObject2).setGroup(this.context.getString(2131099701));
            ((Notification.Builder)localObject2).addExtras((Bundle)localObject1);
        }
        ((Notification.Builder)localObject2).setWhen(new Date().getTime());
        ((Notification.Builder)localObject2).setVibrate(new long[] { 1000L, 1000L, 1000L });
        paramArrayList = ((Notification.Builder)localObject2).build();
        paramArrayList.flags = 16;
        return paramArrayList;
    }

    private Notification getNotificationMoreGames(ArrayList<String> paramArrayList)
    {
        Object localObject1 = new Intent(this.context, MyGames.class);
        Object localObject2 = TaskStackBuilder.create(this.context);
        ((TaskStackBuilder)localObject2).addParentStack(MyGames.class);
        ((TaskStackBuilder)localObject2).addNextIntent((Intent)localObject1);
        localObject2 = ((TaskStackBuilder)localObject2).getPendingIntent(0, 134217728);
        localObject1 = new NotificationCompat.Builder(this.context);
        ((NotificationCompat.Builder)localObject1).setContentTitle(String.valueOf(paramArrayList.size()) + " Jogos");
        ((NotificationCompat.Builder)localObject1).setSmallIcon(2130837627);
        ((NotificationCompat.Builder)localObject1).setContentIntent((PendingIntent)localObject2);
        ((NotificationCompat.Builder)localObject1).setPriority(1);
        if (Build.VERSION.SDK_INT >= 20)
        {
            localObject2 = new Bundle();
            ((Bundle)localObject2).putStringArrayList("notification_content", paramArrayList);
            ((NotificationCompat.Builder)localObject1).setGroup(this.context.getString(2131099701));
            ((NotificationCompat.Builder)localObject1).addExtras((Bundle)localObject2);
        }
        ((NotificationCompat.Builder)localObject1).setWhen(new Date().getTime());
        ((NotificationCompat.Builder)localObject1).setVibrate(new long[] { 1000L, 1000L, 1000L });
        localObject2 = new NotificationCompat.InboxStyle();
        ((NotificationCompat.InboxStyle)localObject2).setBigContentTitle(String.valueOf(paramArrayList.size()) + " Jogos");
        int i = 0;
        while ((i < paramArrayList.size()) && (i < 5))
        {
            String[] arrayOfString2 = ((String)paramArrayList.get(i)).split("#");
            String[] arrayOfString1 = arrayOfString2[1].split(" - ");
            arrayOfString2 = arrayOfString2[0].split(" ");
            ((NotificationCompat.InboxStyle)localObject2).addLine(arrayOfString1[0] + " - " + arrayOfString2[1]);
            i += 1;
        }
        if (paramArrayList.size() > 5) {
            ((NotificationCompat.InboxStyle)localObject2).setSummaryText(String.valueOf(paramArrayList.size() - 5) + " Mais..");
        }
        for (;;)
        {
            ((NotificationCompat.Builder)localObject1).setStyle((NotificationCompat.Style)localObject2);
            ((NotificationCompat.Builder)localObject1).setGroupSummary(true);
            paramArrayList = ((NotificationCompat.Builder)localObject1).build();
            paramArrayList.flags = 16;
            return paramArrayList;
            ((NotificationCompat.InboxStyle)localObject2).setSummaryText("Calendar FC");
        }
    }

    private void scheduleNotification(long paramLong, int paramInt)
    {
        Object localObject = new Intent(this.context, NotificationPublisher.class);
        ((Intent)localObject).putExtra(NotificationPublisher.NOTIFICATION_ID, paramInt);
        localObject = PendingIntent.getBroadcast(this.context, paramInt, (Intent)localObject, 134217728);
        long l = System.currentTimeMillis() + paramLong;
        new SimpleDateFormat("yyyy-MM-dd HH:mm");
        new Date().setTime(l);
        if (paramLong < 0L) {
            return;
        }
        ((AlarmManager)this.context.getSystemService("alarm")).set(0, l, (PendingIntent)localObject);
    }

    private void setNotificationUpdate()
    {
        Object localObject2 = new GameDAO(this.context).getNextUpdate();
        Object localObject1;
        SimpleDateFormat localSimpleDateFormat;
        if (!((String)localObject2).equalsIgnoreCase(""))
        {
            localObject1 = null;
            localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
        try
        {
            localObject2 = localSimpleDateFormat.parse((String)localObject2);
            localObject1 = localObject2;
        }
        catch (ParseException localParseException)
        {
            for (;;)
            {
                long l;
                localParseException.printStackTrace();
            }
        }
        l = 0L;
        if (localObject1 != null) {
            l = ((Date)localObject1).getTime();
        }
        scheduleNotification(l - new Date().getTime(), 2);
    }

    private void setNotificationsGames()
    {
        fillJogos(1);
        if ((this.jogos == null) || (this.jogos.size() == 0)) {
            return;
        }
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date localDate2 = new Date();
        Object localObject = null;
        for (int i = 0;; i = j + 1)
        {
            if (i >= this.delay.size()) {
                break label168;
            }
            localObject = null;
            try
            {
                Date localDate1 = localSimpleDateFormat.parse(((Game)this.jogos.get(0)).getData());
                localObject = localDate1;
            }
            catch (ParseException localParseException)
            {
                for (;;)
                {
                    int j;
                    localParseException.printStackTrace();
                }
            }
            if (localObject != null) {
                ((Date)localObject).setTime(((Date)localObject).getTime() - TimeUnit.MINUTES.toMillis(((Integer)this.delay.get(i)).intValue()));
            }
            j = i;
            if (localObject != null)
            {
                j = i;
                if (((Date)localObject).after(localDate2)) {
                    j = this.delay.size();
                }
            }
        }
        label168:
        long l1 = 0L;
        if (localObject != null) {
            l1 = ((Date)localObject).getTime();
        }
        long l2 = new Date().getTime();
        ((Date)localObject).setTime(l1);
        scheduleNotification(l1 - l2, 1);
    }

    public Notification getNotificationGames()
    {
        fillJogos(0);
        Object localObject2 = new ArrayList();
        Object localObject1 = localObject2;
        if (Build.VERSION.SDK_INT >= 23)
        {
            StatusBarNotification[] arrayOfStatusBarNotification = this.notificationManager.getActiveNotifications();
            localObject1 = localObject2;
            if (arrayOfStatusBarNotification.length != 0) {
                localObject1 = NotificationCompat.getExtras(arrayOfStatusBarNotification[0].getNotification()).getStringArrayList("notification_content");
            }
        }
        int i = 0;
        while (i < this.jogos.size())
        {
            ((ArrayList)localObject1).add(((Game)this.jogos.get(i)).getData() + "#" + ((Game)this.jogos.get(i)).getTitulo());
            i += 1;
        }
        localObject2 = new HashSet();
        ((Set)localObject2).addAll((Collection)localObject1);
        ((ArrayList)localObject1).clear();
        ((ArrayList)localObject1).addAll((Collection)localObject2);
        Collections.sort((List)localObject1);
        if ((localObject1 != null) && (((ArrayList)localObject1).size() == 0)) {
            return null;
        }
        if ((localObject1 != null) && (((ArrayList)localObject1).size() == 1)) {
            return getNotification1Game((ArrayList)localObject1);
        }
        return getNotificationMoreGames((ArrayList)localObject1);
    }

    public Notification getNotificationUpdate()
    {
        Object localObject1 = new Intent(this.context, Update.class);
        Object localObject2 = TaskStackBuilder.create(this.context);
        ((TaskStackBuilder)localObject2).addParentStack(Update.class);
        ((TaskStackBuilder)localObject2).addNextIntent((Intent)localObject1);
        long l = new Date().getTime();
        localObject1 = ((TaskStackBuilder)localObject2).getPendingIntent(0, 134217728);
        localObject2 = new Notification.Builder(this.context);
        ((Notification.Builder)localObject2).setContentTitle("Atualiza����o Necess��ria");
        ((Notification.Builder)localObject2).setContentText("Cique para Atualizar");
        ((Notification.Builder)localObject2).setSmallIcon(2130837627);
        ((Notification.Builder)localObject2).setContentIntent((PendingIntent)localObject1);
        ((Notification.Builder)localObject2).setPriority(1);
        ((Notification.Builder)localObject2).setWhen(l);
        ((Notification.Builder)localObject2).setVibrate(new long[] { 1000L, 1000L, 1000L });
        ((Notification.Builder)localObject2).build().flags = 16;
        return ((Notification.Builder)localObject2).build();
    }

    public void removeNotifications()
    {
        this.notificationManager.cancelAll();
    }

    public void setNotifications()
    {
        if (getData())
        {
            setNotificationsGames();
            setNotificationUpdate();
        }
    }
}
