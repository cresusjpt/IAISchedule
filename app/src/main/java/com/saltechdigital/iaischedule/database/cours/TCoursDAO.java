package com.saltechdigital.iaischedule.database.cours;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saltechdigital.iaischedule.database.DAOBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeanpaul Tossou on 28/11/2017.
 */

public class TCoursDAO extends DAOBase {

    private static final String TABLE_NAME = "COURS";
    private static final String KEY = "IDCOURS";

    private static final String IDCLASSE = "IDCLASSE";
    private static final String IDPROF = "IDPROF";
    private static final String IDPERSONNE = "IDPERSONNE";
    private static final String NOMCOURS = "NOMCOURS";

    private Context context;

    private SQLiteDatabase database = getDb();

    public TCoursDAO(Context pContext) {
        super(pContext);
        this.context = pContext;
    }

    public void ajouter(TCours m) {

        database = open();

        ContentValues valeur = new ContentValues();

        if (m.getIDCLASSE() != 0) {
            valeur.put(IDCLASSE, m.getIDCLASSE());
        }
        if (m.getIDPERSONNE() != 0) {
            valeur.put(IDPERSONNE, m.getIDPERSONNE());
        }
        if (m.getIDPROF() != 0) {
            valeur.put(IDPROF, m.getIDPROF());
        }
        if (m.getNOMCOURS() != null) {
            valeur.put(NOMCOURS, m.getNOMCOURS());
        }

        database.insert(TABLE_NAME, null, valeur);
        Log.d("JEANPAUL", "ENREGISTREMENT REUSSI");
    }

    public int getId(String nomCours) {
        int retourne = 0;
        Cursor cursor = database.rawQuery("SELECT " + KEY + " FROM " + TABLE_NAME + " WHERE " + NOMCOURS + " = ?", new String[]{nomCours});
        if (cursor.moveToFirst()) {
            retourne = cursor.getInt(0);
        }
        cursor.close();
        return retourne;
    }

    public void ajouterListe(List<TCours> coursList) {

        for (TCours cours : coursList) {
            ajouter(cours);
        }
    }

    public void supprimer(long id) {
        database = open();
        database.delete(TABLE_NAME, KEY + " = ?", new String[]{String.valueOf(id)});
        super.close();
        // CODE
    }

    public void modifierNom(TCours m) {

        database = open();
        ContentValues valeur = new ContentValues();
        valeur.put(NOMCOURS, m.getNOMCOURS());
        database.update(TABLE_NAME, valeur, KEY + " = ?", new String[]{String.valueOf(m.getIDPERSONNE())});
        // CODE
    }

    public TCours selectionner(long id) {

        database = open();
        TCours tCours = new TCours();

        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY + " = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            tCours.setIDCOURS(c.getInt(0));
            tCours.setNOMCOURS(c.getString(1));
            tCours.setIDCLASSE(c.getInt(2));
            tCours.setIDPROF(c.getInt(3));
            tCours.setIDPERSONNE(c.getInt(4));
            return tCours;
        }
        c.close();
        return tCours;
    }

    public int taille() {
        database = open();
        int retourne = 0;
        Cursor cursor = database.rawQuery("SELECT COUNT(" + KEY + ") FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            retourne = cursor.getInt(0);
        }
        cursor.close();
        return retourne;
    }

    public List<TCours> selectionnerList() {

        database = open();

        List<TCours> liste = new ArrayList<>();

        String requete = "SELECT * FROM " + TABLE_NAME/* + " WHERE " + KEY + " <> ?"*/;

        Cursor cursor = database.rawQuery(requete, /*new String[]{String.valueOf(1)}*/null);

        if (cursor.moveToFirst()) {
            do {
                TCours cours = new TCours();
                cours.setIDCOURS(cursor.getInt(0));
                cours.setNOMCOURS(cursor.getString(1));
                cours.setIDCLASSE(cursor.getInt(2));
                cours.setIDPROF(cursor.getInt(3));
                cours.setIDPERSONNE(cursor.getInt(4));
                liste.add(cours);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return liste;
    }
}