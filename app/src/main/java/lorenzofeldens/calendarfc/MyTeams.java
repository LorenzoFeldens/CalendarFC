package lorenzofeldens.calendarfc;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dao.GameDAO;
import entidades.Team;
import entidades.Tournament;

public class MyTeams
        extends Activity
{
    ArrayList<Tournament> competicoes;
    int condicao;
    ExpandableListView expListView;
    ExpandableListAdapter listAdapter;
    HashMap<String, List<String>> listDataChild;
    ArrayList<String> listDataHeader;
    ArrayList<Boolean> listGroups;
    ArrayList<String> listRemove;
    ArrayList<Team> timesPrincipais;
    ArrayList<Team> timesSecundarios;

    private void fillExpandableList()
    {
        this.expListView = ((ExpandableListView)findViewById(2131492961));
        this.listDataHeader = new ArrayList();
        this.listDataChild = new HashMap();
        setList(this.timesPrincipais, "Times Principais");
        setList(this.timesSecundarios, "Times Secund��rios");
        setList(this.competicoes, "Competi����es");
        this.listAdapter = new ExpandableListAdapter(this, this.listDataHeader, this.listDataChild, 0);
        if (this.condicao == 1) {
            this.listAdapter.setCondition(this.condicao);
        }
        this.expListView.setAdapter(this.listAdapter);
        this.expListView.setOnGroupExpandListener(new OnGroupExpandListener()
        {
            public void onGroupExpand(int paramAnonymousInt)
            {
                MyTeams.this.listGroups.set(paramAnonymousInt, Boolean.valueOf(true));
            }
        });
        this.expListView.setOnGroupCollapseListener(new OnGroupCollapseListener()
        {
            public void onGroupCollapse(int paramAnonymousInt)
            {
                MyTeams.this.listGroups.set(paramAnonymousInt, Boolean.valueOf(false));
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
        GameDAO localGameDAO = new GameDAO(this);
        this.timesPrincipais = localGameDAO.getTeamsById(localSharedPreferences.getString("Times Principais", ""));
        this.timesSecundarios = localGameDAO.getTeamsById(localSharedPreferences.getString("Times Secundarios", ""));
        this.competicoes = localGameDAO.getCompetitionById(localSharedPreferences.getString("Competicoes", ""));
    }

    private void remove(ArrayList paramArrayList, int paramInt, String paramString)
    {
        int i = 0;
        for (;;)
        {
            if (i < paramArrayList.size()) {
                if (paramInt != 2) {
                    break label48;
                }
            }
            label48:
            for (String str = ((Tournament)paramArrayList.get(i)).getNome(); str.equalsIgnoreCase(paramString); str = ((Team)paramArrayList.get(i)).getNome())
            {
                paramArrayList.remove(i);
                return;
            }
            i += 1;
        }
    }

    private void remover()
    {
        ArrayList localArrayList = ((ExpandableListAdapter)this.expListView.getExpandableListAdapter()).getListChecked();
        int i = 0;
        while (i < ((ArrayList)localArrayList.get(0)).size())
        {
            remove(this.timesPrincipais, 0, (String)((ArrayList)localArrayList.get(0)).get(i));
            i += 1;
        }
        i = 0;
        while (i < ((ArrayList)localArrayList.get(1)).size())
        {
            remove(this.timesSecundarios, 1, (String)((ArrayList)localArrayList.get(1)).get(i));
            i += 1;
        }
        i = 0;
        while (i < ((ArrayList)localArrayList.get(2)).size())
        {
            remove(this.competicoes, 2, (String)((ArrayList)localArrayList.get(2)).get(i));
            i += 1;
        }
    }

    private void savePreferences()
    {
        String str1 = "";
        int i = 0;
        while (i < this.timesPrincipais.size())
        {
            str2 = str1;
            if (!str1.equalsIgnoreCase("")) {
                str2 = str1 + ", ";
            }
            str1 = str2 + ((Team)this.timesPrincipais.get(i)).getId();
            i += 1;
        }
        String str2 = "";
        i = 0;
        while (i < this.timesSecundarios.size())
        {
            str3 = str2;
            if (!str2.equalsIgnoreCase("")) {
                str3 = str2 + ", ";
            }
            str2 = str3 + ((Team)this.timesSecundarios.get(i)).getId();
            i += 1;
        }
        String str3 = "";
        i = 0;
        while (i < this.competicoes.size())
        {
            localObject = str3;
            if (!str3.equalsIgnoreCase("")) {
                localObject = str3 + ", ";
            }
            str3 = (String)localObject + ((Tournament)this.competicoes.get(i)).getId();
            i += 1;
        }
        Object localObject = getSharedPreferences(getString(2131099705), 0).edit();
        ((Editor)localObject).putString("Times Principais", str1);
        ((Editor)localObject).putString("Times Secundarios", str2);
        ((Editor)localObject).putString("Competicoes", str3);
        ((Editor)localObject).commit();
        new Scheduling(this).setNotifications();
    }

    private void setList(ArrayList paramArrayList, String paramString)
    {
        this.listDataHeader.add(paramString);
        ArrayList localArrayList = new ArrayList();
        int i = 0;
        if (i < paramArrayList.size())
        {
            if (paramString.equalsIgnoreCase("Competi����es")) {
                localArrayList.add(((Tournament)paramArrayList.get(i)).getNome());
            }
            for (;;)
            {
                i += 1;
                break;
                localArrayList.add(((Team)paramArrayList.get(i)).getNome());
            }
        }
        if (paramArrayList.size() == 0) {
            localArrayList.add("Nenhum item dispon��vel");
        }
        this.listDataChild.put(paramString, localArrayList);
    }

    public void adicionar(View paramView)
    {
        paramView = (String)((TextView)((RelativeLayout)((ImageButton)paramView).getParent()).findViewById(2131492971)).getText();
        ArrayList localArrayList = new ArrayList();
        Bundle localBundle = new Bundle();
        int i;
        if (paramView.equalsIgnoreCase("Competi����es"))
        {
            paramView = this.competicoes;
            i = 2;
        }
        for (;;)
        {
            localBundle.putSerializable("LIST", paramView);
            localBundle.putSerializable("LIST2", localArrayList);
            localBundle.putInt("TIPO", i);
            savePreferences();
            ((Button)findViewById(2131492962)).setText("EDITAR");
            paramView = new Intent(this, AddTeam.class);
            paramView.putExtras(localBundle);
            startActivity(paramView);
            return;
            if (paramView.equalsIgnoreCase("Times Principais"))
            {
                paramView = this.timesPrincipais;
                localArrayList = this.timesSecundarios;
                i = 0;
            }
            else
            {
                paramView = this.timesSecundarios;
                localArrayList = this.timesPrincipais;
                i = 1;
            }
        }
    }

    public void editar(View paramView)
    {
        paramView = (Button)findViewById(2131492962);
        if (this.condicao == 0)
        {
            this.condicao = 1;
            paramView.setText("OK");
        }
        for (;;)
        {
            this.listRemove.removeAll(this.listRemove);
            fillExpandableList();
            return;
            this.condicao = 0;
            paramView.setText("EDITAR");
            remover();
            savePreferences();
        }
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(2130968604);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(-16777216);
        }
        this.listGroups = new ArrayList();
        int i = 0;
        while (i < 3)
        {
            this.listGroups.add(Boolean.valueOf(false));
            i += 1;
        }
    }

    protected void onStart()
    {
        super.onStart();
        this.listRemove = new ArrayList();
        this.condicao = 0;
        getDados();
        fillExpandableList();
    }
}
