package dao;

import android.content.Context;
import android.database.Cursor;
import db.Adapter;
import entidades.Country;
import entidades.Game;
import entidades.Item;
import entidades.Team;
import entidades.Competition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GameDAO {
    private Adapter adapter;

    public GameDAO(Context context) {
        adapter = new Adapter(context);
    }

    public void executeUpdate(String query) {
        adapter.executeQuery(query);
    }

    public ArrayList<Item> getCompetitionById(String idCompetitions) {
        String query = "SELECT id, name FROM Competition WHERE id IN ("+idCompetitions+") ORDER BY name";

        return getArrayCompetitionFromQuery(query);
    }

    private ArrayList<Item> getArrayCompetitionFromQuery(String query){
        ArrayList list = new ArrayList();

        Cursor cursor = adapter.executeQuery(query);

        while (!cursor.isAfterLast()) {
            list.add(new Competition(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name"))));
            cursor.moveToNext();
        }

        return list;
    }

    public ArrayList<Item> getCompetitionsCountry(int idCountry) {
        String query = "SELECT id, name FROM Competition WHERE country = "+idCountry
                +" ORDER BY name";

        return getArrayCompetitionFromQuery(query);
    }

    public ArrayList<Country> getCountries() {
        ArrayList list = new ArrayList();

        Cursor cursor = adapter.executeQuery("SELECT id, name FROM Country ORDER BY name");

        while (!cursor.isAfterLast()) {
            list.add(new Country(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("id"))));
            cursor.moveToNext();
        }

        return list;
    }

    /*public ArrayList<Game> getGames(String paramString1, String paramString2, String paramString3)
    {
        Object localObject = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String str = (String)localObject + " 00:00";
        localObject = new ArrayList();
        paramString1 = "SELECT titulo, date FROM Game WHERE date > '" + str + "' AND (" + "(t_home IN (" + paramString1 + ") OR t_away IN (" + paramString1 + ")) OR (t_away IN (" + paramString2 + ") AND t_home IN (" + paramString2 + ")) OR " + "competition IN (" + paramString3 + ")) ORDER BY date";
        paramString1 = this.adapter.executeQuery(paramString1);
        while (!paramString1.isAfterLast())
        {
            ((ArrayList)localObject).add(new Game(paramString1.getString(paramString1.getColumnIndex("titulo")), paramString1.getString(paramString1.getColumnIndex("date"))));
            paramString1.moveToNext();
        }
        return (ArrayList<Game>)localObject;
    }*/

    public ArrayList<Game> getGamesNow(String idPrimaryTeams,
                                         String idSecondaryTeams, String idCompetitions) {
        Date date = new Date();
        SimpleDateFormat full = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String end = full.format(date);

        date.setTime(date.getTime() - TimeUnit.HOURS.toMillis(2));
        String begin = full.format(date);

        String query = "SELECT title, date FROM Game WHERE date >= '"+begin+"' AND date < '"+end
                +"' AND (" + "(t_home IN (" +idPrimaryTeams+ ") OR t_away IN ("+idPrimaryTeams
                +")) OR (t_away IN ("+idSecondaryTeams+") AND t_home IN ("+idSecondaryTeams+")) OR "
                + "competition IN ("+idCompetitions+")) ORDER BY date";

        return getArrayGameFromQuery(query);
    }

    private ArrayList<Game> getArrayGameFromQuery(String query){
        ArrayList list = new ArrayList();

        Cursor cursor = adapter.executeQuery(query);

        while (!cursor.isAfterLast()) {
            list.add(new Game(cursor.getString(cursor.getColumnIndex("title")),cursor.getString(cursor.getColumnIndex("date"))));
            cursor.moveToNext();
        }

        return list;
    }

    public ArrayList<Game> getGamesBefore(String idPrimaryTeams,
                                         String idSecondaryTeams, String idCompetitions) {
        Date date = new Date();

        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat full = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String begin = day.format(today);
        begin+=" 00:00";

        date.setTime(date.getTime() - TimeUnit.HOURS.toMillis(2));
        String end = full.format(date);

        String query = "SELECT title, date FROM Game WHERE date >= '"+begin+"' AND date < '"+end
                +"' AND (" + "(t_home IN (" +idPrimaryTeams+ ") OR t_away IN ("+idPrimaryTeams
                +")) OR (t_away IN ("+idSecondaryTeams+") AND t_home IN ("+idSecondaryTeams+")) OR "
                + "competition IN ("+idCompetitions+")) ORDER BY date";

        return getArrayGameFromQuery(query);
    }

    public ArrayList<Game> getGamesAfter(String idPrimaryTeams,
                                          String idSecondaryTeams, String idCompetitions) {
        Date date = new Date();
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat full = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String begin = full.format(date);
        String end = day.format(date);
        end+=" 00:00";

        String query = "SELECT title, date FROM Game WHERE date >= '"+begin+"' AND date < '"+end
                +"' AND (" + "(t_home IN (" +idPrimaryTeams+ ") OR t_away IN ("+idPrimaryTeams
                +")) OR (t_away IN ("+idSecondaryTeams+") AND t_home IN ("+idSecondaryTeams+")) OR "
                + "competition IN ("+idCompetitions+")) ORDER BY date";

        return getArrayGameFromQuery(query);
    }

    public String getNextNotificationGames(String paramString1, String paramString2, String paramString3, Date paramDate) {
        Object localObject = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        new Date();
        localObject = ((SimpleDateFormat)localObject).format(paramDate);
        paramDate = "";
        paramString1 = "SELECT date FROM Game WHERE date > '" + (String)localObject + "' AND (" + "(t_home IN (" + paramString1 + ") OR t_away IN (" + paramString1 + ")) OR (t_away IN (" + paramString2 + ") AND t_home IN (" + paramString2 + ")) OR " + "competition IN (" + paramString3 + ")) ORDER BY date LIMIT 1";
        paramString2 = this.adapter.executeQuery(paramString1);
        paramString1 = paramDate;
        if (paramString2 != null)
        {
            paramString1 = paramDate;
            if (paramString2.moveToFirst()) {
                paramString1 = paramString2.getString(paramString2.getColumnIndex("date"));
            }
        }
        return paramString1;
    }

    public String getNextUpdate()
    {
        Cursor localCursor = this.adapter.executeQuery("SELECT date FROM Game WHERE id == 0");
        String str = "2016-01-01";
        if (localCursor != null) {
            str = localCursor.getString(localCursor.getColumnIndex("date"));
        }
        return str;
    }

    public ArrayList<Game> getNotificationGames(String paramString1, String paramString2, String paramString3, ArrayList<String> paramArrayList)
    {
        new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        ArrayList localArrayList = new ArrayList();
        Object localObject = "SELECT titulo, date FROM Game WHERE date IN (";
        int i = 0;
        while (i < paramArrayList.size())
        {
            String str = (String)localObject + "'" + (String)paramArrayList.get(i) + "'";
            localObject = str;
            if (i != paramArrayList.size() - 1) {
                localObject = str + ", ";
            }
            i += 1;
        }
        paramString1 = (String)localObject + ") AND ((t_home IN (" + paramString1 + ") OR t_away IN (" + paramString1 + ")) OR (t_away IN (" + paramString2 + ") AND " + "t_home IN (" + paramString2 + ")) OR competition IN (" + paramString3 + ")) ORDER BY date";
        paramString1 = this.adapter.executeQuery(paramString1);
        while (!paramString1.isAfterLast())
        {
            localArrayList.add(new Game(paramString1.getString(paramString1.getColumnIndex("titulo")), paramString1.getString(paramString1.getColumnIndex("date"))));
            paramString1.moveToNext();
        }
        return localArrayList;
    }

    public ArrayList<String> getNotificationsLimit(String paramString1, String paramString2, String paramString3, Date paramDate)
    {
        Object localObject = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str = ((SimpleDateFormat)localObject).format(new Date());
        localObject = ((SimpleDateFormat)localObject).format(paramDate);
        paramDate = new ArrayList();
        paramString1 = "SELECT DISTINCT date FROM Game WHERE date > '" + str + "' AND date < '" + (String)localObject + "' AND (" + "(t_home IN (" + paramString1 + ") OR t_away IN (" + paramString1 + ")) OR (t_away IN (" + paramString2 + ") AND t_home IN (" + paramString2 + ")) OR " + "competition IN (" + paramString3 + ")) ORDER BY date";
        paramString1 = this.adapter.executeQuery(paramString1);
        while (!paramString1.isAfterLast())
        {
            paramDate.add(paramString1.getString(paramString1.getColumnIndex("date")));
            paramString1.moveToNext();
        }
        return paramDate;
    }

    public ArrayList<Team> getTeamsById(String paramString)
    {
        ArrayList localArrayList = new ArrayList();
        paramString = "SELECT name, id FROM Team WHERE id IN (" + paramString + ") ORDER BY name";
        paramString = this.adapter.executeQuery(paramString);
        while (!paramString.isAfterLast())
        {
            localArrayList.add(new Team(paramString.getString(paramString.getColumnIndex("name")), paramString.getInt(paramString.getColumnIndex("id"))));
            paramString.moveToNext();
        }
        return localArrayList;
    }

    public ArrayList<Item> getTeamsCountry(int idCountry) {
        ArrayList<Item> list = new ArrayList();

        String query = "SELECT id, name FROM Team WHERE country = "+idCountry+" ORDER BY name";

        Cursor cursor = adapter.executeQuery(query);

        while (!cursor.isAfterLast()){
            list.add(new Team(cursor.getInt(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("name"))));
            cursor.moveToNext();
        }

        return list;
    }
}

