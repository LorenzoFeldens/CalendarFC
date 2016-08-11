package lorenzofeldens.calendarfc;


import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dao.GameDAO;
import entidades.Country;
import entidades.Team;
import entidades.Tournament;

public class AddTeam
        extends Activity
{
    private static int prev = -1;
    ExpandableListView expListView;
    ExpandableListAdapter listAdapter;
    ArrayList listChecked;
    ArrayList<Team> listChecked2;
    HashMap<String, List<String>> listDataChild;
    ArrayList<String> listDataHeader;
    ArrayList paisAtual;
    ArrayList<Country> paises;
    int tipo;

    private void adicionar()
    {
        ArrayList localArrayList = ((ExpandableListAdapter)this.expListView.getExpandableListAdapter()).getListAdd2();
        int j = 0;
        while (j < localArrayList.size())
        {
            int k;
            if (this.tipo == 2)
            {
                this.paisAtual = new GameDAO(this).getCompetitionsCountry(((Country)this.paises.get(j)).getId());
                k = 0;
            }
            for (;;)
            {
                if (k >= ((ArrayList)localArrayList.get(j)).size()) {
                    break label236;
                }
                int i = 0;
                label83:
                if (i < this.paisAtual.size())
                {
                    if (this.tipo == 2) {}
                    for (String str = ((Tournament)this.paisAtual.get(i)).getNome();; str = ((Team)this.paisAtual.get(i)).getNome())
                    {
                        int m = i;
                        if (((String)((ArrayList)localArrayList.get(j)).get(k)).equalsIgnoreCase(str))
                        {
                            this.listChecked.add(this.paisAtual.get(i));
                            m = this.paisAtual.size();
                        }
                        i = m + 1;
                        break label83;
                        this.paisAtual = new GameDAO(this).getTeamsCountry(((Country)this.paises.get(j)).getId());
                        break;
                    }
                }
                k += 1;
            }
            label236:
            j += 1;
        }
    }

    private void fillExpandableList()
    {
        this.paises = new GameDAO(this).getCountries();
        this.expListView = ((ExpandableListView)findViewById(2131492952));
        this.listDataHeader = new ArrayList();
        this.listDataChild = new HashMap();
        int i = 0;
        if (i < this.paises.size())
        {
            if (this.tipo == 2) {}
            for (this.paisAtual = new GameDAO(this).getCompetitionsCountry(((Country)this.paises.get(i)).getId());; this.paisAtual = new GameDAO(this).getTeamsCountry(((Country)this.paises.get(i)).getId()))
            {
                removeDuplicated();
                setList(this.paisAtual, ((Country)this.paises.get(i)).getNome());
                i += 1;
                break;
            }
        }
        this.listAdapter = new ExpandableListAdapter(this, this.listDataHeader, this.listDataChild, 0);
        this.listAdapter.setCondicao(2);
        this.expListView.setAdapter(this.listAdapter);
        this.expListView.setOnGroupExpandListener(new OnGroupExpandListener()
        {
            public void onGroupExpand(int paramAnonymousInt)
            {
                if ((AddTeam.prev != -1) && (paramAnonymousInt != AddTeam.prev)) {
                    AddTeam.this.expListView.collapseGroup(AddTeam.prev);
                }
                AddTeam.access$002(paramAnonymousInt);
            }
        });
    }

    private void removeDuplicated()
    {
        int k = 0;
        int i;
        while (k < this.listChecked.size())
        {
            i = 0;
            if (i < this.paisAtual.size())
            {
                if (this.tipo == 2)
                {
                    j = i;
                    if (((Tournament)this.listChecked.get(k)).getNome().equalsIgnoreCase(((Tournament)this.paisAtual.get(i)).getNome()))
                    {
                        this.paisAtual.remove(i);
                        j = this.paisAtual.size() + 1;
                    }
                }
                for (;;)
                {
                    i = j + 1;
                    break;
                    j = i;
                    if (((Team)this.listChecked.get(k)).getNome().equalsIgnoreCase(((Team)this.paisAtual.get(i)).getNome()))
                    {
                        this.paisAtual.remove(i);
                        j = this.paisAtual.size() + 1;
                    }
                }
            }
            k += 1;
        }
        int j = 0;
        while (j < this.listChecked2.size())
        {
            for (i = 0; i < this.paisAtual.size(); i = k + 1)
            {
                k = i;
                if (((Team)this.listChecked2.get(j)).getNome().equalsIgnoreCase(((Team)this.paisAtual.get(i)).getNome()))
                {
                    this.paisAtual.remove(i);
                    k = this.paisAtual.size() + 1;
                }
            }
            j += 1;
        }
    }

    private void savePreferences()
    {
        String str = "";
        int i = 0;
        if (i < this.listChecked.size())
        {
            localObject = str;
            if (!str.equalsIgnoreCase("")) {
                localObject = str + ", ";
            }
            if (this.tipo == 2) {}
            for (str = (String)localObject + ((Tournament)this.listChecked.get(i)).getId();; str = (String)localObject + ((Team)this.listChecked.get(i)).getId())
            {
                i += 1;
                break;
            }
        }
        Object localObject = getSharedPreferences(getString(2131099705), 0).edit();
        switch (this.tipo)
        {
        }
        for (;;)
        {
            ((Editor)localObject).commit();
            new Scheduling(this).setNotifications();
            return;
            ((Editor)localObject).putString("Times Principais", str);
            continue;
            ((Editor)localObject).putString("Times Secundarios", str);
            continue;
            ((Editor)localObject).putString("Competicoes", str);
        }
    }

    private void setList(ArrayList paramArrayList, String paramString)
    {
        this.listDataHeader.add(paramString);
        ArrayList localArrayList = new ArrayList();
        int i = 0;
        if (i < paramArrayList.size())
        {
            if (this.tipo == 2) {
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
            localArrayList.add("Nenhum item selecionado");
        }
        this.listDataChild.put(paramString, localArrayList);
    }

    public void ok(View paramView)
    {
        adicionar();
        savePreferences();
        super.onBackPressed();
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(2130968601);
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(-16777216);
        }
        paramBundle = getIntent().getExtras();
        this.tipo = paramBundle.getInt("TIPO");
        this.listChecked = ((ArrayList)paramBundle.getSerializable("LIST"));
        this.listChecked2 = ((ArrayList)paramBundle.getSerializable("LIST2"));
        fillExpandableList();
    }
}
