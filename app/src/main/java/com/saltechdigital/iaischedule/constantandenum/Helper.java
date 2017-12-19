package com.saltechdigital.iaischedule.constantandenum;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Jeanpaul Tossou on 05/12/2017.
 */

public class Helper {

    public static void ecrireFichierI(File dir, String nomFichier, String monText) {
        BufferedWriter writer = null;
        try {
            if (!dir.exists()) {
                dir.mkdir();// On crée le répertoire (s'il n'existe pas!!)
            }
            File newfile = new File(dir.getAbsolutePath() + File.separator + nomFichier); // Déclaration de l’objet fichier new file
            newfile.createNewFile();// Création du fichier

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newfile)));// Intégration du contenu dans un BufferedWriter

            writer.write(monText);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (writer != null) {
                try {
                    writer.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String lireFichierI(File dir, String nomFichier) {

        File newfile = new File(dir.getAbsolutePath() + File.separator + nomFichier); // Déclaration de l’objet fichier new file
        String monText = "";
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(new FileInputStream(newfile))); // Récupération du contenu du fichier dans un BufferdReader
            String line;
            StringBuffer buffer = new StringBuffer();

// Parcours du bufferReader et intégration dans un String

            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }
            monText = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return monText;
    }

    public static void ecrireFichierE(String repertoire, String nomFichier, String monText) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            File monFichier;
            Boolean success = true;
            File monRep = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/data" + File.separator + "com.saletechdigital.iaischedule/Files" + File.separator + repertoire);

            if (!monRep.exists()) {
                success = monRep.mkdir(); // On crée le répertoire (s'il n'existe pas!!)
            }
            monFichier = new File(monRep, nomFichier);

            if (!success) {
                Log.i("repertInterne", monFichier.getAbsolutePath());
            }
            BufferedWriter writer = null;
            try {
                FileWriter out = new FileWriter(monFichier);
                writer = new BufferedWriter(out);
                writer.write(monText);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static String lireFichierE(String repertoire, String nomFichier) {

        String monText = "";
        File sdLien = Environment.getExternalStorageDirectory();
        File monFichier = new File(sdLien + File.separator + "Android/data" + File.separator + "com.saletechdigital.iaischedule/Files" + File.separator + repertoire + File.separator + nomFichier);
        Log.d("JEANPAUL", "path de fichier:" + monFichier);

        if (!monFichier.exists()) {
            throw new RuntimeException("Fichier innéxistant sur la carte sd");
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(monFichier));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            monText = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return monText;
    }
}
