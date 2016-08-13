package dao;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import db.Adapter;
import entidades.Country;

public class CountryDAO {
    private Adapter adapter;

    private static final String TABLE_NAME = "Country";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public CountryDAO(Context context) {
        adapter = new Adapter(context);
    }

    public ArrayList<Country> getAll() {
        String query = "SELECT "+KEY_ID+", "+KEY_NAME+" FROM "+TABLE_NAME+" ORDER BY "+KEY_NAME;

        return getArrayFromQuery(query);
    }

    private ArrayList<Country> getArrayFromQuery(String query){
        ArrayList<Country> list = new ArrayList<>();

        Cursor cursor = adapter.executeQuery(query);

        while (!cursor.isAfterLast()) {
            Country country = new Country();
            country.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            country.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));

            list.add(country);
            cursor.moveToNext();
        }

        return list;
    }
}
