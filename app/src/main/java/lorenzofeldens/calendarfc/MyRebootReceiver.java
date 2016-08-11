package lorenzofeldens.calendarfc;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyRebootReceiver
        extends BroadcastReceiver
{
    public void onReceive(Context paramContext, Intent paramIntent)
    {
        paramIntent = new Intent(paramContext, RebootServiceClass.class);
        paramIntent.putExtra("caller", "RebootReceiver");
        paramContext.startService(paramIntent);
    }
}