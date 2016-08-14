package lorenzofeldens.calendarfc;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dao.CompetitionDAO;
import dao.TeamDAO;
import entidades.Item;

public class MyTeams extends Activity {
    private ArrayList<Item> competitions;
    private ArrayList<Item> primaryTeams;
    private ArrayList<Item> secondaryTeams;

    private ExpandableListView expListView;
    private HashMap<String, List<String>> listDataChild;
    private ArrayList<String> listDataHeader;

    private ArrayList<Boolean> listGroups;

    private int condition;

    private static final String PRIMARY_TEAMS_GROUP_NAME = "Times Principais";
    private static final String SECONDARY_TEAMS_GROUP_NAME = "Times Secundários";
    private static final String COMPETITIONS_GROUP_NAME = "Competições";

    private static final int EXPANDABLE_LIST_ADAPTER_TYPE = 0;

    private static final String DEFAULT_PRIMARY_TEAMS = "";
    private static final String DEFAULT_SECONDARY_TEAMS = "";
    private static final String DEFAULT_COMPETITIONS = "";

    private static final String EMPTY_GROUP_TEXT = "Nenhum item selecionado";

    private static final String LIST_TYPE_KEY = "LIST_TYPE";
    private static final String LIST_SELECTED_KEY = "LIST_SELECTED";
    private static final String LIST_SELECTED_KEY2 = "LIST_SELECTED2";

    private static final String BUTTON_TEXT_EDIT = "Editar";
    private static final String BUTTON_TEXT_OK = "OK";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teams);

        setLayout();
    }

    protected void onStart() {
        super.onStart();
        condition = 0;
        getData();
        fillExpandableList();
    }

    private void setLayout(){
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        listGroups = new ArrayList<>();
        for(int i=0; i<3; i++){
            listGroups.add(false);
        }
    }

    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(
                R.string.shared_pref_file_key), Context.MODE_PRIVATE);

        primaryTeams = new TeamDAO(this).getById(sharedPreferences.getString(getString(
                R.string.shared_pref_primary_teams), DEFAULT_PRIMARY_TEAMS));
        secondaryTeams = new TeamDAO(this).getById(sharedPreferences.getString(getString(
                R.string.shared_pref_secondary_teams), DEFAULT_SECONDARY_TEAMS));
        competitions = new CompetitionDAO(this).getById(sharedPreferences.getString(getString(
                R.string.shared_pref_competitions), DEFAULT_COMPETITIONS));
    }

    private void fillExpandableList() {
        expListView = (ExpandableListView) findViewById(R.id.expandableListView_MyTeams);

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        setGroup(primaryTeams, PRIMARY_TEAMS_GROUP_NAME);
        setGroup(secondaryTeams, SECONDARY_TEAMS_GROUP_NAME);
        setGroup(competitions, COMPETITIONS_GROUP_NAME);

        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this,
                listDataHeader, listDataChild, EXPANDABLE_LIST_ADAPTER_TYPE);

        if (condition == 1) {
            expandableListAdapter.setCondition(condition);
        }

        expListView.setAdapter(expandableListAdapter);
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            public void onGroupExpand(int group) {
                listGroups.set(group, true);
            }
        });
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            public void onGroupCollapse(int group) {
                listGroups.set(group, false);
            }
        });

        for(int i=0; i<listGroups.size(); i++){
            if(listGroups.get(i)){
                expListView.expandGroup(i);
            }
        }
    }

    private void setGroup(ArrayList<Item> listItems, String group) {
        listDataHeader.add(group);

        ArrayList<String> list = new ArrayList<>();

        for(int i=0; i<listItems.size(); i++){
            list.add(listItems.get(i).getName());
        }

        if (list.size() == 0) {
            list.add(EMPTY_GROUP_TEXT);
        }

        listDataChild.put(group, list);
    }

    private void removeSelected() {
        ArrayList<ArrayList<String>> list = ((ExpandableListAdapter)expListView
                .getExpandableListAdapter()).getListChecked();

        for(int i=0; i<list.get(0).size(); i++){
            removeFromArray(primaryTeams,list.get(0).get(i));
        }
        for(int i=0; i<list.get(1).size(); i++){
            removeFromArray(secondaryTeams,list.get(1).get(i));
        }
        for(int i=0; i<list.get(2).size(); i++){
            removeFromArray(competitions,list.get(2).get(i));
        }
    }

    private void removeFromArray (ArrayList<Item> list, String name) {
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getName().equalsIgnoreCase(name)){
                list.remove(i);
                return;
            }
        }
    }

    private void savePreferences() {
        String strPrimaryTeams = "";
        for(int i=0; i<primaryTeams.size(); i++){
            if(!strPrimaryTeams.equalsIgnoreCase("")){
                strPrimaryTeams += ", ";
            }
            strPrimaryTeams += primaryTeams.get(i).getName();
        }

        String strSecondaryTeams = "";
        for(int i=0; i<secondaryTeams.size(); i++){
            if(!strSecondaryTeams.equalsIgnoreCase("")){
                strSecondaryTeams += ", ";
            }
            strSecondaryTeams += secondaryTeams.get(i).getName();
        }

        String strCompetitions = "";
        for(int i=0; i<competitions.size(); i++){
            if(!strCompetitions.equalsIgnoreCase("")){
                strCompetitions += ", ";
            }
            strCompetitions += competitions.get(i).getName();
        }

        SharedPreferences.Editor editor = getSharedPreferences(getString(
                R.string.shared_pref_file_key), Context.MODE_PRIVATE).edit();
        editor.putString(getString(R.string.shared_pref_primary_teams), strPrimaryTeams);
        editor.putString(getString(R.string.shared_pref_secondary_teams), strSecondaryTeams);
        editor.putString(getString(R.string.shared_pref_competitions), strCompetitions);
        editor.apply();

        new Scheduling(this).setNotifications();
    }

    public void add_MyTeams(View view) {
        String group = (String) ((TextView) ((RelativeLayout) view.getParent())
                .findViewById(R.id.textView_list_group)).getText();

        ArrayList<Item> list1 = new ArrayList<>();
        ArrayList<Item> list2 = new ArrayList<>();
        int type = 0;

        switch (group){
            case PRIMARY_TEAMS_GROUP_NAME:
                list1 = primaryTeams;
                list2 = secondaryTeams;
                type = 0;
                break;
            case SECONDARY_TEAMS_GROUP_NAME:
                list1 = secondaryTeams;
                list2 = primaryTeams;
                type = 1;
                break;
            case COMPETITIONS_GROUP_NAME:
                list1 = competitions;
                type = 2;
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putInt(LIST_TYPE_KEY,type);
        bundle.putSerializable(LIST_SELECTED_KEY,list1);
        bundle.putSerializable(LIST_SELECTED_KEY2,list2);

        Intent intent = new Intent(this, AddTeam.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void editar_MyTeams(View view) {
        if(condition == 0){
            ((Button) view).setText(BUTTON_TEXT_OK);
            condition = 1;
        }else{
            ((Button) view).setText(BUTTON_TEXT_EDIT);
            condition = 0;

            removeSelected();
            savePreferences();
        }

        fillExpandableList();
    }

    @Override
    public void onBackPressed() {
        removeSelected();
        savePreferences();
        super.onBackPressed();
    }
}
