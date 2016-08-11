package lorenzofeldens.calendarfc;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Preferences
        extends Activity
{
    Button button;
    Button button2;
    CheckBox checkBox;
    int condicao;
    boolean enableNot;
    LinearLayout linearLayout;
    ArrayList<Integer> notifications;

    private void addNotification(int paramInt1, int paramInt2)
    {
        this.notifications.add(Integer.valueOf(paramInt1 * 60 + paramInt2));
        HashSet localHashSet = new HashSet();
        localHashSet.addAll(this.notifications);
        this.notifications.clear();
        this.notifications.addAll(localHashSet);
        Collections.sort(this.notifications);
        preenche();
    }

    private void geraAlert()
    {
        new TimePickerDialog(this, 16973939, new OnTimeSetListener()
        {
            public void onTimeSet(TimePicker paramAnonymousTimePicker, int paramAnonymousInt1, int paramAnonymousInt2)
            {
                Preferences.this.addNotification(paramAnonymousInt1, paramAnonymousInt2);
            }
        }, 0, 0, true).show();
    }

    private void getDados()
    {
        SharedPreferences localSharedPreferences = getSharedPreferences(getString(2131099705), 0);
        String[] arrayOfString = localSharedPreferences.getString("Notificacoes", "10").split("#");
        int i = 0;
        while (i < arrayOfString.length)
        {
            this.notifications.add(Integer.valueOf(arrayOfString[i]));
            i += 1;
        }
        if (localSharedPreferences.getInt("Enabled", 1) == 1)
        {
            this.enableNot = true;
            return;
        }
        this.enableNot = false;
    }

    private String getNotificationText(String paramString)
    {
        int k = Integer.valueOf(paramString).intValue();
        int i = 0;
        int j = k;
        if (k == 0) {
            return "No Hor��rio";
        }
        while (j >= 60)
        {
            i += 1;
            j -= 60;
        }
        paramString = "";
        if (i > 0) {
            paramString = "" + i + " Horas ";
        }
        if (j <= 0)
        {
            str = paramString;
            if (i >= 0) {}
        }
        else
        {
            str = paramString;
            if (i > 0) {
                str = paramString + "e ";
            }
            if (j != 1) {
                break label163;
            }
        }
        label163:
        for (String str = str + j + " Minuto ";; str = str + j + " Minutos ") {
            return str + "Antes";
        }
    }

    private void ok()
    {
        this.condicao = 0;
        this.linearLayout.setOnClickListener(null);
        String str = "";
        int i = 0;
        while (i < this.notifications.size())
        {
            localObject = str;
            if (!str.equalsIgnoreCase("")) {
                localObject = str + "#";
            }
            str = (String)localObject + this.notifications.get(i);
            i += 1;
        }
        Object localObject = getSharedPreferences(getString(2131099705), 0).edit();
        ((Editor)localObject).putString("Notificacoes", str);
        ((Editor)localObject).commit();
        preenche();
        new Scheduling(this).setNotifications();
    }

    private void preenche()
    {
        this.linearLayout.removeAllViews();
        Object localObject1 = new LinearLayout.LayoutParams(-1, -1);
        ScrollView localScrollView = new ScrollView(this);
        localScrollView.setLayoutParams((ViewGroup.LayoutParams)localObject1);
        LinearLayout localLinearLayout = new LinearLayout(this);
        localLinearLayout.setLayoutParams((ViewGroup.LayoutParams)localObject1);
        localLinearLayout.setOrientation(1);
        final int i = 0;
        while (i < this.notifications.size())
        {
            localObject1 = new LinearLayout(this);
            Object localObject2 = new LinearLayout.LayoutParams(-1, -2);
            ((LinearLayout.LayoutParams)localObject2).setMargins(5, 5, 5, 5);
            ((LinearLayout)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
            ((LinearLayout)localObject1).setOrientation(0);
            localObject2 = new LinearLayout(this);
            ((LinearLayout)localObject2).setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            Object localObject3 = new TextView(this);
            ((TextView)localObject3).setTextColor(-1);
            ((TextView)localObject3).setTextSize(20.0F);
            ((TextView)localObject3).setText(getNotificationText(String.valueOf(this.notifications.get(i))));
            ((LinearLayout)localObject2).addView((View)localObject3);
            ((LinearLayout)localObject1).addView((View)localObject2);
            if (this.condicao == 2)
            {
                localObject2 = new LinearLayout(this);
                localObject3 = new LinearLayout.LayoutParams(-1, -2);
                ((LinearLayout.LayoutParams)localObject3).setMargins(10, 0, 10, 0);
                ((LinearLayout)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject3);
                ((LinearLayout)localObject2).setGravity(5);
                localObject3 = new TextView(this);
                ((TextView)localObject3).setTextColor(-65536);
                ((TextView)localObject3).setTextSize(20.0F);
                ((TextView)localObject3).setText("X");
                ((LinearLayout)localObject2).addView((View)localObject3);
                ((LinearLayout)localObject1).addView((View)localObject2);
                ((LinearLayout)localObject1).setOnClickListener(new OnClickListener()
                {
                    public void onClick(View paramAnonymousView)
                    {
                        Preferences.this.notifications.remove(i);
                        Preferences.this.preenche();
                    }
                });
            }
            localLinearLayout.addView((View)localObject1);
            i += 1;
        }
        if (this.condicao == 1)
        {
            localScrollView.setOnTouchListener(new OnTouchListener()
            {
                public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
                {
                    if (paramAnonymousMotionEvent.getActionMasked() == 0) {
                        Preferences.this.geraAlert();
                    }
                    return false;
                }
            });
            if (!this.enableNot) {
                break label404;
            }
            this.checkBox.setChecked(true);
            this.linearLayout.setEnabled(true);
            this.button.setEnabled(true);
            this.button2.setEnabled(true);
        }
        for (;;)
        {
            localScrollView.addView(localLinearLayout);
            this.linearLayout.addView(localScrollView);
            return;
            localScrollView.setOnTouchListener(null);
            break;
            label404:
            this.checkBox.setChecked(false);
            this.linearLayout.setEnabled(false);
            this.button.setEnabled(false);
            this.button2.setEnabled(false);
        }
    }

    private void remover()
    {
        this.condicao = 2;
        preenche();
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
        localLayoutParams.setMargins(5, 5, 5, 5);
        this.button.setLayoutParams(localLayoutParams);
        this.button.setText("OK");
        this.button.setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                Preferences.this.condicao = 0;
                paramAnonymousView = new LinearLayout.LayoutParams(-1, -2, 1.0F);
                paramAnonymousView.setMargins(5, 5, 5, 5);
                Preferences.this.button.setLayoutParams(paramAnonymousView);
                Preferences.this.button.setText("REMOVER");
                Preferences.this.button.setOnClickListener(new OnClickListener()
                {
                    public void onClick(View paramAnonymous2View)
                    {
                        Preferences.this.remover();
                    }
                });
                Preferences.this.preenche();
                Preferences.this.ok();
            }
        });
    }

    private void setEnabledFalse()
    {
        Editor localEditor = getSharedPreferences(getString(2131099705), 0).edit();
        localEditor.putInt("Enabled", 0);
        localEditor.commit();
        new Scheduling(this).removeNotifications();
        this.enableNot = false;
    }

    private void setEnabledTrue()
    {
        Editor localEditor = getSharedPreferences(getString(2131099705), 0).edit();
        localEditor.putInt("Enabled", 1);
        localEditor.commit();
        this.enableNot = true;
    }

    private void setLayout()
    {
        this.linearLayout = ((LinearLayout)findViewById(2131492965));
        this.button = ((Button)findViewById(2131492966));
        this.button2 = ((Button)findViewById(2131492967));
        this.checkBox = ((CheckBox)findViewById(2131492963));
        this.checkBox.setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                if (Preferences.this.checkBox.isChecked())
                {
                    Preferences.this.setEnabledTrue();
                    Preferences.this.preenche();
                    return;
                }
                Preferences.this.setEnabledFalse();
                Preferences.this.preenche();
            }
        });
        this.notifications = new ArrayList();
        this.condicao = 0;
    }

    public void add(View paramView)
    {
        geraAlert();
        this.condicao = 1;
        this.linearLayout.setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                Preferences.this.geraAlert();
            }
        });
        paramView = new LinearLayout.LayoutParams(-1, -2);
        paramView.setMargins(5, 5, 5, 5);
        this.button.setLayoutParams(paramView);
        this.button.setText("OK");
        this.button.setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                paramAnonymousView = new LinearLayout.LayoutParams(-1, -2, 1.0F);
                paramAnonymousView.setMargins(5, 5, 5, 5);
                Preferences.this.button.setLayoutParams(paramAnonymousView);
                Preferences.this.button.setText("REMOVER");
                Preferences.this.button.setOnClickListener(new OnClickListener()
                {
                    public void onClick(View paramAnonymous2View)
                    {
                        Preferences.this.remover();
                    }
                });
                Preferences.this.ok();
            }
        });
        preenche();
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(2130968605);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(-16777216);
        }
        setLayout();
        getDados();
        preenche();
    }

    public void remove(View paramView)
    {
        remover();
    }

    public void update(View paramView)
    {
        startActivity(new Intent(this, Update.class));
    }
}

