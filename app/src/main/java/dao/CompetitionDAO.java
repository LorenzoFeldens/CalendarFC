package dao;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import db.Adapter;
import entidades.Competition;
import entidades.Item;

public class CompetitionDAO {
    private final Adapter adapter;

    private static final String TABLE_NAME = "Competition";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COUNTRY = "country";

    public CompetitionDAO(Context context) {
        adapter = new Adapter(context);
    }

    public ArrayList<Item> getById(String idCompetitions) {
        String query = "SELECT "+KEY_ID+", "+KEY_NAME+" FROM "+TABLE_NAME+" WHERE "
                +KEY_ID+" IN ("+idCompetitions+") ORDER BY "+KEY_NAME;

        return getArrayFromQuery(query);
    }

    public ArrayList<Item> getFromCountry(int idCountry) {
        String query = "SELECT "+KEY_ID+", "+KEY_NAME+" FROM "+TABLE_NAME+" WHERE "
                +KEY_COUNTRY+" = "+idCountry+" ORDER BY "+KEY_NAME;

        return getArrayFromQuery(query);
    }

    private ArrayList<Item> getArrayFromQuery(String query){
        ArrayList<Item> list = new ArrayList<>();

        Cursor cursor = adapter.executeQuery(query);

        while (!cursor.isAfterLast()) {
            Competition competition = new Competition();
            competition.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            competition.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));

            list.add(competition);
            cursor.moveToNext();
        }

        return list;
    }
}
