package lorenzofeldens.calendarfc;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

public class Update extends Activity {
    private static final String URL_UPDATE_FILE = "http://mymatches.co.nf/update.txt";
    private static final String TOAST_TEXT = "Conecte-se à Internet para Atualizar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        if(isOnline()) {
            DownloadFileFromURL d = new DownloadFileFromURL(this);
            d.execute(URL_UPDATE_FILE);
        }else{
            Toast toast = Toast.makeText(this,TOAST_TEXT, Toast.LENGTH_SHORT);
            toast.show();
        }

        mainActivity();
        this.finish();
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void mainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
