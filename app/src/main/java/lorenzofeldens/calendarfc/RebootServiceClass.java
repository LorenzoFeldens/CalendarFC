package lorenzofeldens.calendarfc;

import android.app.IntentService;
import android.content.Intent;

public class RebootServiceClass extends IntentService {
    public RebootServiceClass() {
        super("RebootServiceClass");
    }

    protected void onHandleIntent(Intent intent) {
        String intentType = intent.getExtras().getString("caller");
        if(intentType == null) {
            return;
        }
        if(intentType.equals("RebootReceiver")) {
            new Scheduling(this).setNotifications();
        }
    }
}
