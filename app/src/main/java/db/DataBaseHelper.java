package db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

class DataBaseHelper extends SQLiteOpenHelper{

    private static String DB_PATH = "";
    private static final String DB_NAME = "calendar.db";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase mDataBase;
    private final Context mContext;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);// 1? its Database Version
        this.mContext = context;
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else {
            DB_PATH = context.getFilesDir() + context.getPackageName() + "/databases/";
        }
        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDataBase() {
        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist){
            this.getReadableDatabase();
            this.close();
            try{
                copyDataBase();     //Copy the database from assets
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("BDV", DB_VERSION);
                editor.apply();
                System.out.println(">>>>DB Copied. Version: " + DB_VERSION);
            }
            catch (IOException mIOException){
                throw new Error("ErrorCopyingDataBase");
            }
        }else{
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            int dbVersion = prefs.getInt("BDV", 1);
            if (DB_VERSION != dbVersion) {
                File dbFile = mContext.getDatabasePath(DB_PATH + DB_NAME);
                System.out.println(">>>>Erasing old DB.");
                if (!dbFile.delete()) {
                    System.out.println(">>>>ERROR ERASING DB!");
                }else{
                    createDataBase();
                }
            }
        }
    }

    private boolean checkDataBase(){     ///data/data/your package/databases/Da Name
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException{     //Copy the database from assets
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0){
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public void openDataBase() {      //Open the database, so we can query it
        String mPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public synchronized void close(){
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

}