package lorenzofeldens.calendarfc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

public class AddTeam extends Activity {
    private ArrayList listSelected;
    private ArrayList listSelected2;
    private int listType;

    private ArrayList<Country> countries;
    private ArrayList currentCountry;

    private HashMap<String,List<String>> listDataChild;
    private ArrayList<String> listDataHeader;
    private ExpandableListView expListView;
    private static int prevGroup;

    private static final String LIST_TYPE_KEY = "LIST_TYPE";
    private static final String LIST_SELECTED_KEY = "LIST_SELECTED";
    private static final String LIST_SELECTED_KEY2 = "LIST_SELECTED2";

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_add_team);

        paramBundle = getIntent().getExtras();
        listType = paramBundle.getInt(LIST_TYPE_KEY);
        listSelected = ((ArrayList)paramBundle.getSerializable(LIST_SELECTED_KEY));
        listSelected2 = ((ArrayList)paramBundle.getSerializable(LIST_SELECTED_KEY2));

        setLayout();
        fillExpandableList();
    }

    private void setLayout() {
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        prevGroup = -1;

        expListView = ((ExpandableListView)findViewById(R.id.expandableListView_AddTeam));
    }

    private void fillExpandableList() {
        countries = new GameDAO(this).getCountries();

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        for(int i = 0; i< countries.size(); i++){
            if(listType == 2){
                currentCountry = new GameDAO(this).getCompetitionsCountry(countries.get(i).getId());
            }else{
                currentCountry = new GameDAO(this).getTeamsCountry(countries.get(i).getId());
            }
            removeSelected();
            setGroup(countries.get(i).getNome());
        }

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, 0);
        listAdapter.setCondicao(2);
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            public void onGroupExpand(int group) {
                if (prevGroup != -1 && group != prevGroup) {
                    expListView.collapseGroup(prevGroup);
                }
                prevGroup = group;
            }
        });
    }

    private void removeSelected() {
        ArrayList list = listSelected;

        if(listType != 2){
            list.addAll(listSelected2);
        }

        for(int i=0; i<list.size(); i++){
            for(int j = 0; j< currentCountry.size(); j++){
                String nameChecked;
                String nameCurrent;

                if(listType == 2){
                    nameChecked = ((Tournament) list.get(i)).getNome();
                    nameCurrent = ((Tournament) currentCountry.get(j)).getNome();
                }else{
                    nameChecked = ((Team) list.get(i)).getNome();
                    nameCurrent = ((Team) currentCountry.get(j)).getNome();
                }

                if(nameChecked.equalsIgnoreCase(nameCurrent)){
                    currentCountry.remove(j);
                    j = currentCountry.size()+1;
                }
            }
        }
    }

    private void setGroup(String group) {
        listDataHeader.add(group);

        ArrayList<String> list = new ArrayList<>();

        for(int i = 0; i< currentCountry.size(); i++){
            if(listType == 2)
                list.add(((Tournament) currentCountry.get(i)).getNome());
            else
                list.add(((Team) currentCountry.get(i)).getNome());
        }

        if (list.size() == 0) {
            list.add("Nenhum item disponível");
        }

        listDataChild.put(group, list);
    }

    private void insert() {
        ArrayList<ArrayList<String>> list = ((ExpandableListAdapter)this.expListView.getExpandableListAdapter()).getListAdd2();

        for(int i=0; i<list.size(); i++){
            if(listType == 2)
                currentCountry = new GameDAO(this).getCompetitionsCountry(countries.get(i).getId());
            else
                currentCountry = new GameDAO(this).getTeamsCountry(countries.get(i).getId());

            for(int j=0; j<list.get(i).size(); j++){
                for(int k = 0; k< currentCountry.size(); k++){
                    String nameCurrent;

                    if(listType == 2){
                        nameCurrent = ((Tournament) currentCountry.get(k)).getNome();
                    }else{
                        nameCurrent = ((Team) currentCountry.get(k)).getNome();
                    }

                    if(list.get(i).get(j).equalsIgnoreCase(nameCurrent)){
                        listSelected.add(currentCountry.get(k));
                        k = currentCountry.size();
                    }
                }
            }
        }
    }

    private void savePreferences() {
        String str = "";

        for(int i = 0; i< listSelected.size(); i++){
            if(!str.equalsIgnoreCase(""))
                str += ", ";

            if(listType == 2){
                str += ((Tournament) listSelected.get(i)).getId();
            }else{
                str += ((Team) listSelected.get(i)).getId();
            }
        }


        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.shared_pref_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        switch (listType){
            case 0:
                editor.putString(getString(R.string.shared_pref_primary_teams),str);
                break;
            case 1:
                editor.putString(getString(R.string.shared_pref_secondary_teams),str);
                break;
            case 2:
                editor.putString(getString(R.string.shared_pref_competitions),str);
                break;
        }

        editor.apply();
    }

    public void ok(View view) {
        insert();
        savePreferences();
        super.onBackPressed();
    }

}
