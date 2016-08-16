package lorenzofeldens.calendarfc;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Collections;

public class Options extends Activity {
    private int condition;

    private LinearLayout linearLayout;
    private Button button;

    private ArrayList<Integer> notifications;
    private ArrayList<Boolean> notificationsSelected;

    private boolean notificationsEnabled;

    private static final String DEFAULT_NOTIFICATION_VALUE = "10";
    private static final boolean DEFAULT_NOTIFICATION_ENABLED_VALUE = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        getData();
        setLayout();
        fillLayout();
    }

    private void getData() {
        notifications = new ArrayList<>();
        notificationsSelected = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences(getString(
                R.string.shared_pref_file_key), Context.MODE_PRIVATE);

        String[] array = sharedPreferences.getString(getString(
                R.string.shared_pref_notifications), DEFAULT_NOTIFICATION_VALUE).split("#");

        for (String anArray : array) {
            notifications.add(Integer.valueOf(anArray));
            notificationsSelected.add(false);
        }

        notificationsEnabled = sharedPreferences.getBoolean(getString(
                R.string.shared_pref_notifications_enabled), DEFAULT_NOTIFICATION_ENABLED_VALUE);
    }

    private void setLayout() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        ((CheckBox) findViewById(R.id.checkBox_Options)).setChecked(notificationsEnabled);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout_Options);
        button = (Button) findViewById(R.id.button_Options_editar);
        condition = 0;
    }

    private void fillLayout() {
        linearLayout.removeAllViews();

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i=0; i<notifications.size(); i++){
            View view = layoutInflater.inflate(R.layout.notifications_layout_item, null);

            TextView textView = (TextView) view.findViewById(
                    R.id.textView_notification_layout_item);
            textView.setText(getNotificationText(notifications.get(i)));

            if(condition == 1){
                final int i2 = i;
                final CheckBox checkBox = (CheckBox) view.findViewById(
                        R.id.checkBox_notification_layout_item);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setEnabled(true);
                checkBox.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checkBox.isChecked()){
                            notificationsSelected.set(i2,false);
                        }else{
                            notificationsSelected.set(i2,true);
                        }
                    }
                });
            }

            linearLayout.addView(view);
        }
    }

    private String getNotificationText(int time) {
        int hours = time/60;
        int minutes = time-hours*60;

        String str = "";

        if(hours > 0){
            if(hours == 1){
                str += "1 Hora";
            }else{
                str += hours+" Horas";
            }
        }
        if(minutes > 0){
            if(!str.equalsIgnoreCase("")){
                str += " e ";
            }
            if(minutes == 1){
                str += "1 Minuto";
            }else{
                str += minutes+" Minutos";
            }
        }

        if(str.equalsIgnoreCase("")){
            str = "No Horário";
        }else{
            str += " Antes";
        }

        return str;
    }

    private void addNotification(int hour, int minute) {
        int time = hour*60 + minute;

        if(!notifications.contains(time)){
            notifications.add(time);
            notificationsSelected.add(false);
        }

        fillLayout();
    }

    private void doneEditing() {
        for(int i=notificationsSelected.size()-1; i>=0; i--){
            if(notificationsSelected.get(i)){
                notifications.remove(i);
                notificationsSelected.remove(i);
            }
        }

        Collections.sort(notifications);

        String str = "";
        for(int i=0; i<notifications.size(); i++){
            notificationsSelected.set(i,false);

            str += notifications.get(i);
            if(i != notifications.size()-1){
                str += "#";
            }
        }

        SharedPreferences.Editor editor = getSharedPreferences(getString(
                R.string.shared_pref_file_key), Context.MODE_PRIVATE).edit();

        editor.putString(getString(R.string.shared_pref_notifications),str);
        editor.apply();

        new Scheduling(this).setNotifications();
    }

    public void onCheckBoxClickOptions(View view){
        boolean status = true;
        if(!((CheckBox)view).isChecked()){
            status = false;
            linearLayout.setAlpha(.5f);
            button.setAlpha(.5f);
            button.setEnabled(false);
        }else{
            linearLayout.setAlpha(1);
            button.setAlpha(1);
            button.setEnabled(true);
        }

        SharedPreferences.Editor editor = getSharedPreferences(getString(
                R.string.shared_pref_file_key), Context.MODE_PRIVATE).edit();

        editor.putBoolean(getString(R.string.shared_pref_notifications_enabled),status);
        editor.apply();
    }

    public void showTimePicker_Options(View view) {
        TimePickerDialog tpd = new TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        addNotification(hourOfDay,minute);
                    }
                }, 0, 0, true);
        tpd.show();
    }

    public void editar_Options(View view){
        String buttonText = (String) button.getText();

        if(buttonText.equalsIgnoreCase(getString(R.string.button_Options_editar_text))){
            condition = 1;

            button.setText(getString(R.string.button_Options_ok_text));
            findViewById(R.id.imageButton_Options).setVisibility(View.VISIBLE);
        }else{
            condition = 0;

            doneEditing();

            button.setText(getString(R.string.button_Options_editar_text));
            findViewById(R.id.imageButton_Options).setVisibility(View.INVISIBLE);
        }

        fillLayout();
    }

    public void update_Options(View view) {
        startActivity(new Intent(this, Update.class));
    }

    @Override
    public void onBackPressed() {
        doneEditing();
        super.onBackPressed();
    }
}

