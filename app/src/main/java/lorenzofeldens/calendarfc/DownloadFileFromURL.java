package lorenzofeldens.calendarfc;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import dao.GameDAO;

public class DownloadFileFromURL extends AsyncTask<String, String, String> {
    private String result;
    private Context context;
    private static final String TOAST_TEXT = "Jogos Atualizados!";

    public DownloadFileFromURL(Context context) {
        this.context = context;
    }

    /**
     * Before starting background thread Show Progress Bar Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        //int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            // this will be useful so that you can show a typical 0-100%
            // progress bar
            //int lengthOfFile = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream
            /*OutputStream output = new FileOutputStream(Environment
                    .getExternalStorageDirectory().toString()
                    + "/file.txt");*/

            //byte data[] = new byte[1024];

            //long total = 0;

            BufferedReader r = new BufferedReader(new InputStreamReader(input));

            String x = r.readLine();
            String totalS = "";

            while(x!= null){
                totalS += x+"\n";
                x = r.readLine();
            }

            result = totalS;

            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        executeUpdate();

        Toast toast = Toast.makeText(context, TOAST_TEXT, Toast.LENGTH_SHORT);
        toast.show();

        Scheduling scheduling = new Scheduling(context);
        scheduling.removeNotifications();
        scheduling.setNotifications();
    }

    private void executeUpdate(){
        String[] array = result.split("\n");

        GameDAO gameDAO = new GameDAO(context);

        for (String anArray : array) {
            if (!anArray.equalsIgnoreCase("")) {
                gameDAO.executeUpdate(anArray);
            }
        }
    }
}
