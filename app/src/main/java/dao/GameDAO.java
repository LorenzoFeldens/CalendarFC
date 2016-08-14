package dao;

import android.content.Context;
import android.database.Cursor;
import db.Adapter;
import entidades.Game;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GameDAO {
    private Adapter adapter;

    private static final String TABLE_NAME = "Game";
    private static final String KEY_ID = "id";
    private static final String KEY_T_HOME = "t_home";
    private static final String KEY_T_AWAY = "t_away";
    private static final String KEY_COMPETITION = "competition";
    private static final String KEY_DATE = "date";
    private static final String KEY_TITLE = "title";

    private static final int UPDATE_ID = 0;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final String DEFAULT_DATE = "2016-01-01";

    public GameDAO(Context context) {
        adapter = new Adapter(context);
    }

    public void executeUpdate(String query) {
        adapter.executeQuery(query);
    }

    public String getNextUpdate() {
        String query = "SELECT "+KEY_DATE+" FROM "+TABLE_NAME+" WHERE "+KEY_ID+" == "+UPDATE_ID;

        String str = getDateFromQuery(query);

        if(str.equalsIgnoreCase("")){
            return DEFAULT_DATE;
        }else{
            return str;
        }
    }

    public String getNextNotificationGames(String idPrimaryTeams, String idSecondaryTeams,
                                           String idCompetitions, Date date) {
        String begin = DATE_FORMAT.format(date);

        String query = "SELECT "+KEY_DATE+" FROM "+TABLE_NAME+" WHERE "+KEY_DATE+" > '"+begin
                +"' AND (("+KEY_T_HOME+" IN ("+idPrimaryTeams+") OR "+KEY_T_AWAY+" IN ("
                +idPrimaryTeams+")) OR ("+KEY_T_HOME+" IN ("+idSecondaryTeams+") AND "+KEY_T_AWAY
                +" IN ("+idSecondaryTeams+")) OR "+KEY_COMPETITION+" IN ("+idCompetitions
                +")) ORDER BY "+KEY_DATE+" LIMIT 1";

        return getDateFromQuery(query);
    }

    private String getDateFromQuery(String query){
        Cursor cursor = adapter.executeQuery(query);

        String str = "";
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                str = cursor.getString(cursor.getColumnIndex(KEY_DATE));
            }
        }

        return str;
    }

    public ArrayList<Game> getGamesBefore(String idPrimaryTeams,
                                          String idSecondaryTeams, String idCompetitions) {
        Date date = new Date();

        String begin = DAY_FORMAT.format(date);
        begin+=" 00:00";

        date.setTime(date.getTime() - TimeUnit.HOURS.toMillis(2));
        String end = DATE_FORMAT.format(date);

        String query = "SELECT "+KEY_TITLE+", "+KEY_DATE+" FROM "+TABLE_NAME+" WHERE "+KEY_DATE
                +" >= '"+begin+"' AND "+KEY_DATE+" < '"+end+"' AND (("+KEY_T_HOME+" IN ("
                +idPrimaryTeams+") OR "+KEY_T_AWAY+" IN ("+idPrimaryTeams+")) OR ("+KEY_T_HOME
                +" IN ("+idSecondaryTeams+") AND "+KEY_T_AWAY+" IN ("+idSecondaryTeams+")) OR "
                +KEY_COMPETITION+" IN ("+idCompetitions+")) ORDER BY "+KEY_DATE;

        return getArrayFromQuery(query);
    }

    public ArrayList<Game> getGamesNow(String idPrimaryTeams,
                                         String idSecondaryTeams, String idCompetitions) {
        Date date = new Date();
        String end = DATE_FORMAT.format(date);

        date.setTime(date.getTime() - TimeUnit.HOURS.toMillis(2));
        String begin = DATE_FORMAT.format(date);

        String query = "SELECT "+KEY_TITLE+", "+KEY_DATE+" FROM "+TABLE_NAME+" WHERE "+KEY_DATE
                +" >= '"+begin+"' AND "+KEY_DATE+" < '"+end+"' AND (("+KEY_T_HOME+" IN ("
                +idPrimaryTeams+") OR "+KEY_T_AWAY+" IN ("+idPrimaryTeams+")) OR ("+KEY_T_HOME
                +" IN ("+idSecondaryTeams+") AND "+KEY_T_AWAY+" IN ("+idSecondaryTeams+")) OR "
                +KEY_COMPETITION+" IN ("+idCompetitions+")) ORDER BY "+KEY_DATE;

        return getArrayFromQuery(query);
    }

    public ArrayList<Game> getGamesAfter(String idPrimaryTeams,
                                          String idSecondaryTeams, String idCompetitions) {
        Date date = new Date();

        String begin = DATE_FORMAT.format(date);
        String end = DAY_FORMAT.format(date);
        end+=" 00:00";

        String query = "SELECT "+KEY_TITLE+", "+KEY_DATE+" FROM "+TABLE_NAME+" WHERE "+KEY_DATE
                +" >= '"+begin+"' AND "+KEY_DATE+" < '"+end+"' AND (" + "("+KEY_T_HOME+" IN ("
                +idPrimaryTeams+ ") OR "+KEY_T_AWAY+" IN ("+idPrimaryTeams+")) OR ("+KEY_T_HOME
                +" IN ("+idSecondaryTeams+") AND "+KEY_T_AWAY+" IN ("+idSecondaryTeams+")) OR "
                +KEY_COMPETITION+" IN ("+idCompetitions+")) ORDER BY "+KEY_DATE;

        return getArrayFromQuery(query);
    }

    public ArrayList<Game> getNotificationGames(String idPrimaryTeams, String idSecondaryTeams,
                                                String idCompetitions, ArrayList<String> dates) {
        String query = "SELECT "+KEY_TITLE+", "+KEY_DATE+" FROM "+TABLE_NAME+" WHERE "
                +KEY_DATE+" IN (";

        for(int i=0; i<dates.size(); i++){
            if(i > 0){
                query += ", ";
            }
            query += "'"+dates.get(i)+"'";
        }

        query += ") AND (("+KEY_T_HOME+" IN ("+idPrimaryTeams+") OR "+KEY_T_AWAY+" IN ("
                +idPrimaryTeams+")) OR ("+KEY_T_HOME+" IN ("+idSecondaryTeams+") AND "+KEY_T_AWAY
                +" IN ("+idSecondaryTeams+")) OR "+KEY_COMPETITION+" IN ("+idCompetitions
                +")) ORDER BY "+KEY_DATE;

        return getArrayFromQuery(query);
    }

    private ArrayList<Game> getArrayFromQuery(String query){
        ArrayList<Game> list = new ArrayList<>();

        Cursor cursor = adapter.executeQuery(query);

        while (!cursor.isAfterLast()) {
            Game game = new Game();
            game.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            game.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));

            list.add(game);
            cursor.moveToNext();
        }

        return list;
    }
}

