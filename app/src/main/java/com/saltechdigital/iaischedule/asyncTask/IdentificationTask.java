package com.saltechdigital.iaischedule.asyncTask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.saltechdigital.iaischedule.constantandenum.Task;

import org.json.JSONException;
import org.json.JSONObject;

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

public class IdentificationTask extends AsyncTask<String, Integer, Boolean> {

    @SuppressLint("StaticFieldLeak")
    private Context context;

    private String json;
    private final IdentificationCallback callback;

    public IdentificationTask(Context context, IdentificationCallback callback) {
        this.callback = callback;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        new ProgressDialog(context);
    }

    @Override
    protected Boolean doInBackground(String... params) {

        String enreg_url = Task.URL.getNom() + "/" + Task.LOGINFILE.getNom();

        System.setProperty("http.keepAlive", "false");

        final String mNom;
        final String mPrenom;
        final String mTelephone;
        final String mSexe;
        final String mPassword;

        mNom = params[0];
        mPrenom = params[1];
        mSexe = params[2];
        mTelephone = params[3];
        mPassword = params[4];

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

            String donnee = URLEncoder.encode("nom", "UTF-8") + "=" + URLEncoder.encode(mNom, "UTF-8") + "&" +
                    URLEncoder.encode("prenom", "UTF-8") + "=" + URLEncoder.encode(mPrenom, "UTF-8") + "&" +
                    URLEncoder.encode("telephone", "UTF-8") + "=" + URLEncoder.encode(mTelephone, "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(mPassword, "UTF-8") + "&" +
                    URLEncoder.encode("sexe", "UTF-8") + "=" + URLEncoder.encode(mSexe, "UTF-8");

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
                }

                if (!reponse.toString().isEmpty()) {
                    if (!reponse.toString().equals("Cette personne n'existe pas")) {
                        json = reponse.toString();
                        return true;
                    } else {
                        callback.onError();
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
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {

            try {
                JSONObject jsonArray = new JSONObject(json);
                callback.onSuccess(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        void onSuccess(JSONObject jsonArray);

        void onError();
    }
}
