package com.saltechdigital.iaischedule.database.eleve;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saltechdigital.iaischedule.database.DAOBase;
import com.saltechdigital.iaischedule.database.cours.TCours;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeanpaul Tossou on 28/11/2017.
 */

public class TEleveDAO extends DAOBase {

    private static final String TABLE_NAME = "ELEVE";
    private static final String KEY = "IDELEVE";

    private static final String IDCLASSE = "IDCLASSE";
    private static final String IDPERSONNE = "IDPERSONNE";
    private static final String RESPONSABLE = "RESPONSABLE";

    private Context context;

    private SQLiteDatabase database = getDb();

    public TEleveDAO(Context pContext) {
        super(pContext);
        this.context = pContext;
    }

    public void ajouter(TEleve m) {

        database = open();

        ContentValues valeur = new ContentValues();

        if (m.getIDCLASSE() != 0) {
            valeur.put(IDCLASSE, m.getIDCLASSE());
        }
        if (m.getIDPERSONNE() != 0) {
            valeur.put(IDPERSONNE, m.getIDPERSONNE());
        }
        if (m.getRESPONSABLE() != 0) {
            valeur.put(RESPONSABLE, m.getRESPONSABLE());
        }

        if (m.getIDELEVE() != 0) {
            valeur.put(KEY, m.getIDELEVE());
        } else {
            valeur.put(KEY, taille() + 1);
        }

        database.insert(TABLE_NAME, null, valeur);
        database.close();
    }

    public int getId(String responsable) {
        int retourne = 0;
        database = open();
        Cursor cursor = database.rawQuery("SELECT " + KEY + " FROM " + TABLE_NAME + " WHERE " + RESPONSABLE + " = ?", new String[]{responsable});
        if (cursor.moveToFirst()) {
            retourne = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return retourne;
    }

    public void ajouterListe(List<TEleve> eleveList) {

        for (TEleve eleve : eleveList) {
            ajouter(eleve);
        }
    }

    public void supprimer(long id) {
        database = open();
        database.delete(TABLE_NAME, KEY + " = ?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void modifierResponsable(TEleve m) {

        database = open();
        ContentValues valeur = new ContentValues();
        valeur.put(RESPONSABLE, m.getRESPONSABLE());
        database.update(TABLE_NAME, valeur, KEY + " = ?", new String[]{String.valueOf(m.getIDPERSONNE())});
        database.close();
    }

    public TEleve selectionner(long id) {

        database = open();
        TEleve eleve = new TEleve();

        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY + " = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            eleve.setIDELEVE(c.getInt(0));
            eleve.setIDPERSONNE(c.getInt(1));
            eleve.setRESPONSABLE(c.getInt(2));
            eleve.setIDCLASSE(c.getInt(3));
            return eleve;
        }
        c.close();
        database.close();
        return eleve;
    }

    public int taille() {
        database = open();
        int retourne = 0;
        Cursor cursor = database.rawQuery("SELECT COUNT(" + KEY + ") FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            retourne = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return retourne;
    }

    public List<TEleve> selectionnerList() {

        database = open();

        List<TEleve> liste = new ArrayList<>();

        String requete = "SELECT * FROM " + TABLE_NAME/* + " WHERE " + KEY + " <> ?"*/;

        Cursor cursor = database.rawQuery(requete, /*new String[]{String.valueOf(1)}*/null);

        if (cursor.moveToFirst()) {
            do {
                TEleve eleve = new TEleve();
                eleve.setIDELEVE(cursor.getInt(0));
                eleve.setIDPERSONNE(cursor.getInt(1));
                eleve.setRESPONSABLE(cursor.getInt(2));
                eleve.setIDCLASSE(cursor.getInt(3));
                liste.add(eleve);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return liste;
    }
}