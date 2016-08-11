package lorenzofeldens.calendarfc;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import dao.GameDAO;

public class DownloadFileFromURL
        extends AsyncTask<String, String, String>
{
    Context context;
    String result;

    public DownloadFileFromURL(Context paramContext)
    {
        this.context = paramContext;
    }

    private void executeUpdate()
    {
        String[] arrayOfString = this.result.split("\n");
        GameDAO localGameDAO = new GameDAO(this.context);
        int i = 0;
        while (i < arrayOfString.length)
        {
            if (!arrayOfString[i].equalsIgnoreCase("")) {
                localGameDAO.executeUpdate(arrayOfString[i]);
            }
            i += 1;
        }
    }

    protected String doInBackground(String... paramVarArgs)
    {
        try
        {
            paramVarArgs = new URL(paramVarArgs[0]);
            Object localObject = paramVarArgs.openConnection();
            ((URLConnection)localObject).connect();
            ((URLConnection)localObject).getContentLength();
            BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramVarArgs.openStream(), 8192);
            paramVarArgs = new byte['��'];
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localBufferedInputStream));
            localObject = localBufferedReader.readLine();
            paramVarArgs = "";
            while (localObject != null)
            {
                paramVarArgs = paramVarArgs + (String)localObject + "\n";
                localObject = localBufferedReader.readLine();
            }
            this.result = paramVarArgs;
            localBufferedInputStream.close();
        }
        catch (Exception paramVarArgs)
        {
            for (;;)
            {
                Log.e("Error: ", paramVarArgs.getMessage());
            }
        }
        return null;
    }

    public String getResult()
    {
        return this.result;
    }

    protected void onPostExecute(String paramString)
    {
        executeUpdate();
        Toast.makeText(this.context, "Jogos Atualizados", 0).show();
        new Scheduling(this.context).setNotifications();
    }

    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    protected void onProgressUpdate(String... paramVarArgs) {}
}

