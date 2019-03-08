package hu.reformatus.csillagpont.model.programs;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import hu.reformatus.csillagpont.R;
import hu.reformatus.csillagpont.model.programs.databases.DatabaseQuery;

import static android.content.ContentValues.TAG;


public class RemoteDatabase {
    private ProgressDialog pd;
    private Context context;
    private static long tStart = 0;

    public RemoteDatabase(Context context) {
        this.context = context;
    }

    public void checkAndDownloadUpdates() {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        double elapsedMinute = elapsedSeconds / 60;
        if (elapsedMinute >= 5.0f) {
            tStart = System.currentTimeMillis();
            Log.d(TAG, "elapsed 5 Minutes");
            if (isUpdateAvailable())
                new JsonTask().execute("http://lokodonc.hu/CSPdatabases/getEvents.php");
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isUpdateAvailable() {
        //TODO: implement
        return true;
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(context);
            pd.setMessage(context.getString(R.string.please_wait));
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    //Log.d("Response: ", "> " + line);

                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            try {
                JSONArray json = new JSONArray(result);
                DatabaseQuery dQuery = new DatabaseQuery(context);
                dQuery.updateDatabase(json);
                Log.d(TAG, "length: " + String.valueOf(json.length()));

                //Log.d(TAG, "row: "+row);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
