package db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.PrintStream;

public class Adapter
{
    private Context context;
    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase mDb;

    public Adapter(Context paramContext)
    {
        this.context = paramContext;
    }

    public void close()
    {
        this.dataBaseHelper.close();
    }

    public Cursor executeQuery(String paramString)
    {
        try
        {
            open();
            paramString = this.mDb.rawQuery(paramString, null);
            if (paramString != null) {
                paramString.moveToFirst();
            }
            close();
            return paramString;
        }
        catch (android.database.SQLException paramString)
        {
            System.out.println(paramString.toString());
            throw paramString;
        }
    }

    public Adapter open()
            throws android.database.SQLException
    {
        try
        {
            this.dataBaseHelper = new DataBaseHelper(this.context);
            this.dataBaseHelper.openDataBase();
            this.mDb = this.dataBaseHelper.getReadableDatabase();
            return this;
        }
        catch (android.database.SQLException localSQLException)
        {
            System.out.println(localSQLException.toString());
            throw localSQLException;
        }
        catch (java.sql.SQLException localSQLException1)
        {
            System.out.println("ERRO: " + localSQLException1.getMessage());
        }
        return this;
    }
}
