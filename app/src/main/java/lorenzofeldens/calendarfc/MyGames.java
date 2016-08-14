package lorenzofeldens.calendarfc;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dao.GameDAO;
import entidades.Game;

public class MyGames extends Activity {
    private ArrayList<Game> gamesBefore;
    private ArrayList<Game> gamesNow;
    private ArrayList<Game> gamesAfter;

    private HashMap<String, List<String>> listDataChild;
    private ArrayList<String> listDataHeader;

    private static final String DEFAULT_PRIMARY_TEAMS = "";
    private static final String DEFAULT_SECONDARY_TEAMS = "";
    private static final String DEFAULT_COMPETITIONS = "";

    private static final String GAMES_BEFORE_GROUP_NAME = "Jogos Finalizados";
    private static final String GAMES_NOW_GROUP_NAME = "Jogos em Andamento";
    private static final String GAMES_AFTER_GROUP_NAME = "Próximos Jogos";

    private static final int EXPANDABLE_LIST_ADAPTER_TYPE = 1;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_games);

        setLayout();
        getData();
        fillExpandableList();
    }

    private void setLayout() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        //setAds();
    }

    /*private void setAds(){
        final InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.ads_interstitial_my_games));
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(
                AdRequest.DEVICE_ID_EMULATOR).build());
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                interstitialAd.show();
            }
        });
    }*/

    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(
                R.string.shared_pref_file_key), Context.MODE_PRIVATE);

        String primaryTeams = sharedPreferences.getString(getString(
                R.string.shared_pref_primary_teams), DEFAULT_PRIMARY_TEAMS);
        String secondaryTeams = sharedPreferences.getString(getString(
                R.string.shared_pref_secondary_teams), DEFAULT_SECONDARY_TEAMS);
        String competitions = sharedPreferences.getString(getString(
                R.string.shared_pref_competitions), DEFAULT_COMPETITIONS);

        gamesBefore = new GameDAO(this).getGamesBefore(primaryTeams, secondaryTeams, competitions);
        gamesNow = new GameDAO(this).getGamesNow(primaryTeams, secondaryTeams, competitions);
        gamesAfter = new GameDAO(this).getGamesAfter(primaryTeams, secondaryTeams, competitions);
    }

    private void fillExpandableList() {
        ExpandableListView expandableListView = ((ExpandableListView)findViewById(
                R.id.expandableListView_MyGames));

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        setGroup(gamesBefore, GAMES_BEFORE_GROUP_NAME);
        setGroup(gamesNow, GAMES_NOW_GROUP_NAME);
        setGroup(gamesAfter, GAMES_AFTER_GROUP_NAME);

        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this,
                listDataHeader, listDataChild, EXPANDABLE_LIST_ADAPTER_TYPE);
        expandableListView.setAdapter(expandableListAdapter);
    }

    private void setGroup(ArrayList<Game> games, String group) {
        if (games.size() == 0) {
            return;
        }

        listDataHeader.add(group);

        ArrayList<String> list = new ArrayList<>();

        for(int i=0; i<games.size(); i++){
            Game game = games.get(i);
            list.add(game.getTitle()+"#"+game.getDate());
        }

        listDataChild.put(group, list);
    }
}
