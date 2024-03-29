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

import dao.CompetitionDAO;
import dao.CountryDAO;
import dao.TeamDAO;
import entidades.Country;
import entidades.Item;

public class AddTeam extends Activity {
    private ArrayList<Item> listSelected;
    private ArrayList<Item> listSelected2;
    private int listType;

    private ArrayList<Country> countries;
    private ArrayList<Item> currentCountry;

    private HashMap<String,List<String>> listDataChild;
    private ArrayList<String> listDataHeader;
    private ExpandableListView expListView;
    private static int prevGroup;

    public static final String LIST_TYPE_KEY = "LIST_TYPE";
    public static final String LIST_SELECTED_KEY = "LIST_SELECTED";
    public static final String LIST_SELECTED_KEY2 = "LIST_SELECTED2";

    private static final int EXPANDABLE_LIST_ADAPTER_TYPE = 0;
    private static final int EXPANDABLE_LIST_ADAPTER_CONDITION = 2;

    public static final String EMPTY_GROUP_TEXT = "Nenhum item disponível";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        Bundle bundle = getIntent().getExtras();
        listType = bundle.getInt(LIST_TYPE_KEY);

        Object list1 = bundle.getSerializable(LIST_SELECTED_KEY);
        Object list2 = bundle.getSerializable(LIST_SELECTED_KEY2);

        listSelected = verifySerializable(list1);
        listSelected2 = verifySerializable(list2);

        setLayout();
        fillExpandableList();
    }

    private ArrayList<Item> verifySerializable(Object object){
        ArrayList<Item> ret = new ArrayList<>();

        if(object instanceof ArrayList){
            for(int i=0; i<((ArrayList) object).size(); i++){
                if(((ArrayList) object).get(i) instanceof Item){
                    ret.add((Item)((ArrayList) object).get(i));
                }else{
                    return new ArrayList<>();
                }
            }
        }else{
            return new ArrayList<>();
        }

        return ret;
    }

    private void setLayout() {
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        prevGroup = -1;

        expListView = ((ExpandableListView)findViewById(R.id.expandableListView_AddTeam));
    }

    private void fillExpandableList() {
        countries = new CountryDAO(this).getAll();

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        for(int i = 0; i< countries.size(); i++){
            if(listType == 2){
                currentCountry = new CompetitionDAO(this).getFromCountry(countries.get(i).getId());
            }else{
                currentCountry = new TeamDAO(this).getFromCountry(countries.get(i).getId());
            }

            removeSelected();
            setGroup(countries.get(i).getName());
        }

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader,
                listDataChild, EXPANDABLE_LIST_ADAPTER_TYPE);
        listAdapter.setCondition(EXPANDABLE_LIST_ADAPTER_CONDITION);
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

    private void setGroup(String group) {
        listDataHeader.add(group);

        ArrayList<String> list = new ArrayList<>();

        for(int i = 0; i< currentCountry.size(); i++){
            list.add(currentCountry.get(i).getName());
        }

        if (list.size() == 0) {
            list.add(EMPTY_GROUP_TEXT);
        }

        listDataChild.put(group, list);
    }

    private void removeSelected() {
        ArrayList<Item> list = listSelected;

        list.addAll(listSelected2);

        for(int i=0; i<list.size(); i++){
            removeFromCurrentCountry(list.get(i).getName());
        }
    }

    private void removeFromCurrentCountry(String name){
        for(int i = 0; i< currentCountry.size(); i++){
            if(currentCountry.get(i).getName().equalsIgnoreCase(name)){
                currentCountry.remove(i);
                return;
            }
        }
    }

    private void insertChecked() {
        ArrayList<ArrayList<String>> list = ((ExpandableListAdapter)expListView
                .getExpandableListAdapter()).getListChecked();

        for(int i=0; i<list.size(); i++){
            if(listType == 2)
                currentCountry = new CompetitionDAO(this).getFromCountry(countries.get(i).getId());
            else
                currentCountry = new TeamDAO(this).getFromCountry(countries.get(i).getId());

            insertArray(list.get(i));
        }
    }

    private void insertArray (ArrayList<String> list){
        for(int i=0; i<list.size(); i++){
            insertItem(list.get(i));
        }
    }

    private void insertItem (String name){
        for(int i=0; i<currentCountry.size(); i++){
            String nameCurrent;

            nameCurrent = currentCountry.get(i).getName();

            if(name.equalsIgnoreCase(nameCurrent)){
                listSelected.add(currentCountry.get(i));
                return;
            }
        }
    }

    private void savePreferences() {
        String str = "";

        for(int i = 0; i< listSelected.size(); i++){
            if(!str.equalsIgnoreCase(""))
                str += ", ";

            str += listSelected.get(i).getId();
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

    public void ok_AddTeam(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        insertChecked();
        savePreferences();
        super.onBackPressed();
    }
}
