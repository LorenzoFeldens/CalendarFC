package lorenzofeldens.calendarfc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dao.GameDAO;

public class MainActivity extends Activity {
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);

        setAds();
        setLayout();
        setNotifications();
        verifyFirstUse();
        verifyUpdate();
    }

    private void setAds(){
        MobileAds.initialize(getApplicationContext(), getString(R.string.ads_app_id));
        ((AdView)findViewById(R.id.adView_banner_main)).loadAd(new AdRequest.Builder().build());
    }

    private void setLayout() {
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
    }

    private void setNotifications(){
        Scheduling scheduling = new Scheduling(this);
        scheduling.removeNotifications();
        scheduling.setNotifications();
    }

    private void verifyFirstUse(){
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.shared_pref_file_key), Context.MODE_PRIVATE);

        boolean firstUse = sharedPref.getBoolean(getString(R.string.shared_pref_first_use_main),true);

        if(firstUse){
            showTutorial();
            update();
        }

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean(getString(R.string.shared_pref_first_use_main),false);
        editor.apply();
    }

    public void update() {
        startActivity(new Intent(this, Update.class));
    }

    private void showTutorial(){
        //WAITING IMPLEMENTATION
    }

    private void verifyUpdate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date nextUpdate = null;
        Date now = new Date();

        try {
            nextUpdate = dateFormat.parse(new GameDAO(this).getNextUpdate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(nextUpdate != null) {
            if (now.after(nextUpdate)) {
                update();
            }
        }
    }

    public void myTeams(View view) {
        startActivity(new Intent(this, MyTeams.class));
    }

    public void nextGames(View view) {
        startActivity(new Intent(this, MyNotifications.class));
    }

    public void options(View view) {
        startActivity(new Intent(this, Preferences.class));
    }
}
