package lorenzofeldens.calendarfc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dao.GameDAO;

public class MainActivity extends Activity {
    private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLayout();
        setNotifications();
    }

    private void setLayout() {
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        //setAds();

        verifyFirstUse();
        verifyUpdate();
    }

    /*private void setAds() {
        MobileAds.initialize(getApplicationContext(), getString(R.string.ads_app_id));
        ((AdView)findViewById(R.id.adView_banner_main)).loadAd(new AdRequest.Builder().build());
    }*/

    private void verifyFirstUse() {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.shared_pref_file_key), Context.MODE_PRIVATE);

        boolean firstUse = sharedPref.getBoolean(getString
                (R.string.shared_pref_first_use_main),true);

        if(firstUse){
            showTutorial();
            update_Main();
        }

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean(getString(R.string.shared_pref_first_use_main),false);
        editor.apply();
    }

    private void verifyUpdate() {
        Date nextUpdate = null;
        Date now = new Date();

        try {
            nextUpdate = DAY_FORMAT.parse(new GameDAO(this).getNextUpdate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(nextUpdate != null) {
            if (now.after(nextUpdate)) {
                update_Main();
            }
        }
    }

    private void showTutorial() {
        //WAITING IMPLEMENTATION
    }

    private void setNotifications() {
        Scheduling scheduling = new Scheduling(this);
        scheduling.removeNotifications();
        scheduling.setNotifications();
    }

    private void update_Main() {
        startActivity(new Intent(this, Update.class));
    }

    public void myTeams_Main(View view) {
        startActivity(new Intent(this, MyTeams.class));
    }

    public void nextGames_Main(View view) {
        startActivity(new Intent(this, MyGames.class));
    }

    public void options_Main(View view) {
        startActivity(new Intent(this, Options.class));
    }
}
