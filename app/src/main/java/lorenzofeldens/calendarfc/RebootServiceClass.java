package lorenzofeldens.calendarfc;

import android.app.IntentService;
import android.content.Intent;

public class RebootServiceClass
        extends IntentService
{
    public RebootServiceClass()
    {
        super("RebootServiceClass");
    }

    protected void onHandleIntent(Intent paramIntent)
    {
        paramIntent = paramIntent.getExtras().getString("caller");
        if (paramIntent == null) {}
        while (!paramIntent.equals("RebootReceiver")) {
            return;
        }
        new Scheduling(this).setNotifications();
    }
}
