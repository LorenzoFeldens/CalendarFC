package dao;

import android.content.Context;
import android.database.Cursor;
import db.Adapter;
import entidades.Country;
import entidades.Game;
import entidades.Team;
import entidades.Tournament;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GameDAO
{
    private Adapter adapter;

    public GameDAO(Context paramContext)
    {
        this.adapter = new Adapter(paramContext);
    }

    public void executeUpdate(String paramString)
    {
        this.adapter.executeQuery(paramString);
    }

    public ArrayList<Game> get8Games(String paramString1, String paramString2, String paramString3)
    {
        Object localObject = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String str = (String)localObject + " 00:00";
        localObject = new ArrayList();
        paramString1 = "SELECT titulo, date FROM Game WHERE date > '" + str + "' AND (" + "(t_home IN (" + paramString1 + ") OR t_away IN (" + paramString1 + ")) OR (t_away IN (" + paramString2 + ") AND t_home IN (" + paramString2 + ")) OR " + "competition IN (" + paramString3 + ")) ORDER BY date LIMIT 8";
        paramString1 = this.adapter.executeQuery(paramString1);
        while (!paramString1.isAfterLast())
        {
            ((ArrayList)localObject).add(new Game(paramString1.getString(paramString1.getColumnIndex("titulo")), paramString1.getString(paramString1.getColumnIndex("date"))));
            paramString1.moveToNext();
        }
        return (ArrayList<Game>)localObject;
    }

    public ArrayList<Tournament> getCompetitionById(String paramString)
    {
        ArrayList localArrayList = new ArrayList();
        paramString = "SELECT name, id FROM Competition WHERE id IN (" + paramString + ") ORDER BY name";
        paramString = this.adapter.executeQuery(paramString);
        while (!paramString.isAfterLast())
        {
            localArrayList.add(new Tournament(paramString.getString(paramString.getColumnIndex("name")), paramString.getInt(paramString.getColumnIndex("id"))));
            paramString.moveToNext();
        }
        return localArrayList;
    }

    public ArrayList<Tournament> getCompetitionsCountry(int paramInt)
    {
        ArrayList localArrayList = new ArrayList();
        Object localObject = "SELECT name, id FROM Competition Where country = " + paramInt + " ORDER BY name";
        localObject = this.adapter.executeQuery((String)localObject);
        while (!((Cursor)localObject).isAfterLast())
        {
            localArrayList.add(new Tournament(((Cursor)localObject).getString(((Cursor)localObject).getColumnIndex("name")), ((Cursor)localObject).getInt(((Cursor)localObject).getColumnIndex("id"))));
            ((Cursor)localObject).moveToNext();
        }
        return localArrayList;
    }

    public ArrayList<Country> getCountries()
    {
        ArrayList localArrayList = new ArrayList();
        Cursor localCursor = this.adapter.executeQuery("SELECT name,id FROM Country ORDER BY name");
        while (!localCursor.isAfterLast())
        {
            localArrayList.add(new Country(localCursor.getString(localCursor.getColumnIndex("name")), localCursor.getInt(localCursor.getColumnIndex("id"))));
            localCursor.moveToNext();
        }
        return localArrayList;
    }

    public ArrayList<Game> getGames(String paramString1, String paramString2, String paramString3)
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
    }

    public ArrayList<Game> getGamesAgora(String paramString1, String paramString2, String paramString3)
    {
        ArrayList localArrayList = new ArrayList();
        Object localObject2 = new Date();
        Object localObject1 = new Date();
        ((Date)localObject2).setTime(((Date)localObject2).getTime() - TimeUnit.HOURS.toMillis(2L));
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        localObject2 = localSimpleDateFormat.format((Date)localObject2);
        localObject1 = localSimpleDateFormat.format((Date)localObject1);
        paramString1 = "SELECT titulo, date FROM Game WHERE date >= '" + (String)localObject2 + "' AND date < '" + (String)localObject1 + "' AND (" + "(t_home IN (" + paramString1 + ") OR t_away IN (" + paramString1 + ")) OR (t_away IN (" + paramString2 + ") AND t_home IN (" + paramString2 + ")) OR " + "competition IN (" + paramString3 + ")) ORDER BY date";
        paramString1 = this.adapter.executeQuery(paramString1);
        while (!paramString1.isAfterLast())
        {
            localArrayList.add(new Game(paramString1.getString(paramString1.getColumnIndex("titulo")), paramString1.getString(paramString1.getColumnIndex("date"))));
            paramString1.moveToNext();
        }
        return localArrayList;
    }

    public ArrayList<Game> getGamesAntes(String paramString1, String paramString2, String paramString3)
    {
        ArrayList localArrayList = new ArrayList();
        Object localObject1 = new SimpleDateFormat("HH");
        Object localObject2 = new Date();
        if (Integer.valueOf(((SimpleDateFormat)localObject1).format((Date)localObject2)).intValue() < 2) {}
        for (;;)
        {
            return localArrayList;
            localObject1 = new Date();
            ((Date)localObject1).setTime(((Date)localObject1).getTime() - TimeUnit.HOURS.toMillis(2L));
            SimpleDateFormat localSimpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat localSimpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            localObject2 = localSimpleDateFormat2.format((Date)localObject2);
            localObject2 = (String)localObject2 + " 00:00";
            localObject1 = localSimpleDateFormat1.format((Date)localObject1);
            paramString1 = "SELECT titulo, date FROM Game WHERE date >= '" + (String)localObject2 + "' AND date < '" + (String)localObject1 + "' AND (" + "(t_home IN (" + paramString1 + ") OR t_away IN (" + paramString1 + ")) OR (t_away IN (" + paramString2 + ") AND t_home IN (" + paramString2 + ")) OR " + "competition IN (" + paramString3 + ")) ORDER BY date";
            paramString1 = this.adapter.executeQuery(paramString1);
            while (!paramString1.isAfterLast())
            {
                localArrayList.add(new Game(paramString1.getString(paramString1.getColumnIndex("titulo")), paramString1.getString(paramString1.getColumnIndex("date"))));
                paramString1.moveToNext();
            }
        }
    }

    public ArrayList<Game> getGamesDepois(String paramString1, String paramString2, String paramString3)
    {
        ArrayList localArrayList = new ArrayList();
        Object localObject2 = new Date();
        Object localObject1 = new Date();
        ((Date)localObject1).setTime(((Date)localObject1).getTime() + TimeUnit.DAYS.toMillis(1L));
        SimpleDateFormat localSimpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat localSimpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        localObject2 = localSimpleDateFormat2.format((Date)localObject2);
        localObject1 = localSimpleDateFormat1.format((Date)localObject1);
        localObject1 = (String)localObject1 + " 00:00";
        paramString1 = "SELECT titulo, date FROM Game WHERE date >= '" + (String)localObject2 + "' AND date < '" + (String)localObject1 + "' AND (" + "(t_home IN (" + paramString1 + ") OR t_away IN (" + paramString1 + ")) OR (t_away IN (" + paramString2 + ") AND t_home IN (" + paramString2 + ")) OR " + "competition IN (" + paramString3 + ")) ORDER BY date";
        paramString1 = this.adapter.executeQuery(paramString1);
        while (!paramString1.isAfterLast())
        {
            localArrayList.add(new Game(paramString1.getString(paramString1.getColumnIndex("titulo")), paramString1.getString(paramString1.getColumnIndex("date"))));
            paramString1.moveToNext();
        }
        return localArrayList;
    }

    public String getNextNotificationGames(String paramString1, String paramString2, String paramString3, Date paramDate)
    {
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

    public ArrayList<Team> getTeamsCountry(int paramInt)
    {
        ArrayList localArrayList = new ArrayList();
        Object localObject = "SELECT name, id FROM Team Where country = " + paramInt + " ORDER BY name";
        localObject = this.adapter.executeQuery((String)localObject);
        while (!((Cursor)localObject).isAfterLast())
        {
            localArrayList.add(new Team(((Cursor)localObject).getString(((Cursor)localObject).getColumnIndex("name")), ((Cursor)localObject).getInt(((Cursor)localObject).getColumnIndex("id"))));
            ((Cursor)localObject).moveToNext();
        }
        return localArrayList;
    }
}

