package lorenzofeldens.calendarfc;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dao.GameDAO;
import entidades.Game;

public class MyNotifications
        extends Activity
{
    ArrayList<Game> agora;
    ArrayList<Game> antes;
    String competicoes;
    ArrayList<Game> depois;
    ExpandableListView expListView;
    LinearLayout linearLayout;
    ExpandableListAdapter listAdapter;
    HashMap<String, List<String>> listDataChild;
    ArrayList<String> listDataHeader;
    ArrayList<Boolean> listGroups;
    InterstitialAd mInterstitialAd;
    String timesPrincipais;
    String timesSecundarios;

    private void fillExpandableList()
    {
        this.expListView = ((ExpandableListView)findViewById(2131492960));
        this.listDataHeader = new ArrayList();
        this.listDataChild = new HashMap();
        this.listGroups.removeAll(this.listGroups);
        setList(this.antes, "Jogos Finalizados");
        setList(this.agora, "Jogos em Andamento");
        setList(this.depois, "Pr��ximos Jogos");
        this.listAdapter = new ExpandableListAdapter(this, this.listDataHeader, this.listDataChild, 1);
        this.expListView.setAdapter(this.listAdapter);
        this.expListView.setOnGroupExpandListener(new OnGroupExpandListener()
        {
            public void onGroupExpand(int paramAnonymousInt)
            {
                MyNotifications.this.listGroups.set(paramAnonymousInt, Boolean.valueOf(true));
            }
        });
        this.expListView.setOnGroupCollapseListener(new OnGroupCollapseListener()
        {
            public void onGroupCollapse(int paramAnonymousInt)
            {
                MyNotifications.this.listGroups.set(paramAnonymousInt, Boolean.valueOf(false));
            }
        });
        int i = 0;
        while (i < this.listGroups.size())
        {
            if (((Boolean)this.listGroups.get(i)).booleanValue()) {
                this.expListView.expandGroup(i);
            }
            i += 1;
        }
    }

    private void getDados()
    {
        SharedPreferences localSharedPreferences = getSharedPreferences(getString(2131099705), 0);
        this.timesPrincipais = localSharedPreferences.getString("Times Principais", "");
        this.timesSecundarios = localSharedPreferences.getString("Times Secundarios", "");
        this.competicoes = localSharedPreferences.getString("Competicoes", "");
        this.antes = new GameDAO(this).getGamesAntes(this.timesPrincipais, this.timesSecundarios, this.competicoes);
        this.agora = new GameDAO(this).getGamesAgora(this.timesPrincipais, this.timesSecundarios, this.competicoes);
        this.depois = new GameDAO(this).getGamesDepois(this.timesPrincipais, this.timesSecundarios, this.competicoes);
    }

    private void setLayout()
    {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(-16777216);
        }
        this.listGroups = new ArrayList();
    }

    private void setList(ArrayList<Game> paramArrayList, String paramString)
    {
        if (paramArrayList.size() == 0) {
            return;
        }
        this.listGroups.add(Boolean.valueOf(false));
        this.listDataHeader.add(paramString);
        ArrayList localArrayList = new ArrayList();
        int i = 0;
        while (i < paramArrayList.size())
        {
            localArrayList.add(((Game)paramArrayList.get(i)).getTitulo() + "#" + ((Game)paramArrayList.get(i)).getData());
            i += 1;
        }
        this.listDataChild.put(paramString, localArrayList);
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(2130968603);
        setLayout();
        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId("ca-app-pub-8580494290111528/2080868098");
        this.mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
        this.mInterstitialAd.setAdListener(new AdListener()
        {
            public void onAdLoaded()
            {
                MyNotifications.this.mInterstitialAd.show();
            }
        });
        getDados();
        fillExpandableList();
    }
}
