package db;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

public class DataBaseHelper
        extends SQLiteOpenHelper
{
    private static String DB_NAME = "calendar.db";
    private static String DB_PATH = "";
    private static int DB_VERSION = 2;
    private final Context mContext;
    private SQLiteDatabase mDataBase;

    public DataBaseHelper(Context paramContext)
    {
        super(paramContext, DB_NAME, null, 1);
        this.mContext = paramContext;
        if (Build.VERSION.SDK_INT >= 17) {}
        for (DB_PATH = paramContext.getApplicationInfo().dataDir + "/databases/";; DB_PATH = "/data/data/" + paramContext.getPackageName() + "/databases/") {
            try
            {
                createDataBase();
                return;
            }
            catch (IOException paramContext)
            {
                paramContext.printStackTrace();
            }
        }
    }

    private boolean checkDataBase()
    {
        return new File(DB_PATH + DB_NAME).exists();
    }

    private void copyDataBase()
            throws IOException
    {
        InputStream localInputStream = this.mContext.getAssets().open(DB_NAME);
        FileOutputStream localFileOutputStream = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] arrayOfByte = new byte['��'];
        for (;;)
        {
            int i = localInputStream.read(arrayOfByte);
            if (i <= 0) {
                break;
            }
            localFileOutputStream.write(arrayOfByte, 0, i);
        }
        localFileOutputStream.flush();
        localFileOutputStream.close();
        localInputStream.close();
    }

    public void close()
    {
        try
        {
            if (this.mDataBase != null) {
                this.mDataBase.close();
            }
            super.close();
            return;
        }
        finally {}
    }

    public void createDataBase()
            throws IOException
    {
        if (!checkDataBase())
        {
            getReadableDatabase();
            close();
        }
        int i;
        do
        {
            try
            {
                copyDataBase();
                SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
                localEditor.putInt("BDV", DB_VERSION);
                localEditor.apply();
                System.out.println(">>>>BD copiado. Versao: " + DB_VERSION);
                return;
            }
            catch (IOException localIOException)
            {
                throw new Error("ErrorCopyingDataBase");
            }
            i = PreferenceManager.getDefaultSharedPreferences(this.mContext).getInt("BDV", 1);
        } while (DB_VERSION == i);
        File localFile = this.mContext.getDatabasePath(DB_PATH + DB_NAME);
        System.out.println(">>>>Apagando BD velho.");
        if (!localFile.delete())
        {
            System.out.println(">>>>ERRO AO APAGAR BD!");
            return;
        }
        createDataBase();
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {}

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {}

    public boolean openDataBase()
            throws SQLException
    {
        this.mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, 268435456);
        return this.mDataBase != null;
    }
}
