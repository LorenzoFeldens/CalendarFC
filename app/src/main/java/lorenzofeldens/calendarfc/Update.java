package lorenzofeldens.calendarfc;


import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

public class Update
        extends Activity
{
    private boolean isOnline()
    {
        NetworkInfo localNetworkInfo = ((ConnectivityManager)getSystemService("connectivity")).getActiveNetworkInfo();
        return (localNetworkInfo != null) && (localNetworkInfo.isConnectedOrConnecting());
    }

    public void mainActivity()
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(2130968606);
        if (isOnline()) {
            new DownloadFileFromURL(this).execute(new String[] { "http://mymatches.co.nf/update.txt" });
        }
        for (;;)
        {
            mainActivity();
            finish();
            return;
            Toast.makeText(this, "Conecte-se �� Internet para Atualizar", 0).show();
        }
    }
}
