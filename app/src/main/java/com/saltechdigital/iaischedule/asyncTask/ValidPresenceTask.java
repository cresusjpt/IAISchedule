package com.saltechdigital.iaischedule.asyncTask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.saltechdigital.iaischedule.constantandenum.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Jeanpaul Tossou on 19/11/2017.
 */

public class ValidPresenceTask extends AsyncTask<String, Integer, Boolean> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String json;
    private final IdentificationCallback callback;

    public ValidPresenceTask(Context context, IdentificationCallback callback) {
        this.callback = callback;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        String enreg_url = Task.URL.getNom() + "/" + Task.MAKEVALIDFILE.getNom();

        System.setProperty("http.keepAlive", "false");

        String typepresence = params[0];
        String idEleve = params[1];
        String idPersonne = params[2];
        String idCours = params[3];
        String idHeure = params[4];
        String datePresence = params[5];

        HttpURLConnection urlConnection = null;
        BufferedWriter bufferedWriter = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            URL url = new URL(enreg_url);

            //Parametrage de la connection Http

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setReadTimeout(20000);
            urlConnection.setConnectTimeout(30000);

            // Envoie des parametres

            outputStream = urlConnection.getOutputStream();

            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String donnee = URLEncoder.encode("typepresence", "UTF-8") + "=" + URLEncoder.encode(typepresence, "UTF-8") + "&" +
                    URLEncoder.encode("idEleve", "UTF-8") + "=" + URLEncoder.encode(idEleve, "UTF-8") + "&" +
                    URLEncoder.encode("idPersonne", "UTF-8") + "=" + URLEncoder.encode(idPersonne, "UTF-8") + "&" +
                    URLEncoder.encode("idCours", "UTF-8") + "=" + URLEncoder.encode(idCours, "UTF-8") + "&" +
                    URLEncoder.encode("idHeure", "UTF-8") + "=" + URLEncoder.encode(idHeure, "UTF-8") + "&" +
                    URLEncoder.encode("datePresence", "UTF-8") + "=" + URLEncoder.encode(datePresence, "UTF-8");


            Log.d("JEANPAUL", "doInBackground: " + donnee);

            bufferedWriter.write(donnee);
            bufferedWriter.flush();

            // Recuperation de la reponse

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder reponse = new StringBuilder();
                String ligne;

                //calcul cu pourcentage d'avancement

                while ((ligne = reader.readLine()) != null) {
                    reponse.append(ligne).append("\n");
                    Log.d("JEANPAUL", "reader: " + reponse.toString());
                }

                if (!reponse.toString().isEmpty()) {
                    if (!reponse.toString().equals("ERROR")) {
                        json = reponse.toString();
                        Log.d("JEANPAUL", "readerFini: " + json);
                        return true;
                    } else {
                        callback.onError();
                        return false;
                    }
                } else {
                    callback.onError();
                    return false;
                }
            } else {
                callback.onError();
                return false;
            }

        } catch (IOException e) {
            callback.onError();
            e.printStackTrace();
        } finally {
            try {
                assert bufferedWriter != null;
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                assert inputStream != null;
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        new ProgressDialog(context);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            callback.onSuccess(json);
        } else {
            callback.onError();
        }
    }

    @Override
    protected void onCancelled() {
        callback.onError();
        super.onCancelled();
    }

    public interface IdentificationCallback {
        void onSuccess(String message);

        void onError();
    }
}
