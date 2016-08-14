package db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Adapter {

    private final Context context;
    private DataBaseHelper dataBaseHelper;

    public Adapter(Context context){
        this.context = context;
    }

    private SQLiteDatabase mDb;

    private void open() throws SQLException{
        try{
            dataBaseHelper = new DataBaseHelper(context);
            dataBaseHelper.openDataBase();
            mDb = dataBaseHelper.getReadableDatabase();
        } catch (SQLException mSQLException){
            System.out.println(mSQLException.toString());
            throw mSQLException;
        } catch (java.sql.SQLException s){
            System.out.println("ERROR: "+s.getMessage());
        }
    }

    private void close(){
        dataBaseHelper.close();
    }

    public Cursor executeQuery(String query){
        try{
            open();
            Cursor mCur = mDb.rawQuery(query, null);
            if (mCur!=null){
                mCur.moveToFirst();
            }
            close();
            return mCur;
        }catch (SQLException mSQLException){
            System.out.println(mSQLException.toString());
            throw mSQLException;
        }
    }

}
