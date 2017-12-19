package com.saltechdigital.iaischedule.database.horaire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.saltechdigital.iaischedule.database.DAOBase;
import com.saltechdigital.iaischedule.database.personne.TPersonne;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jeanpaul Tossou on 30/12/2016.
 */

public class THoraireDAO extends DAOBase {

    private static final String TABLE_NAME = "HORAIRE";
    private static final String KEY = "IDHEURE";

    private static final String HEURE = "HEURE";

    private Context context;

    private SQLiteDatabase database = getDb();

    public THoraireDAO(Context pContext) {
        super(pContext);
        this.context = pContext;
    }

    public void ajouter(THoraire m) {

        database = open();

        ContentValues valeur = new ContentValues();

        if (m.getHEURE() != null) {
            valeur.put(HEURE, m.getHEURE());
        }
        database.insert(TABLE_NAME, null, valeur);
        database.close();
    }

    public int getId(String heure) {
        int retourne = 0;
        database = open();
        Cursor cursor = database.rawQuery("SELECT " + KEY + " FROM " + TABLE_NAME + " WHERE " + HEURE + " = ?", new String[]{heure});
        if (cursor.moveToFirst()) {
            retourne = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return retourne;
    }

    /**
     * @param horaireList liste de personne à ajouter
     */

    public void ajouterListe(List<THoraire> horaireList) {

        for (THoraire horaire : horaireList) {
            ajouter(horaire);
        }
    }

    /**
     * @param id l'identifiant de la personne à supprimer
     */

    public void supprimer(long id) {
        database = open();
        database.delete(TABLE_NAME, KEY + " = ?", new String[]{String.valueOf(id)});
        database.close();
    }

    /**
     * @param m la personne modifiée
     */

    public void modifierHeure(THoraire m) {

        database = open();
        ContentValues valeur = new ContentValues();
        valeur.put(HEURE, m.getHEURE());
        database.update(TABLE_NAME, valeur, KEY + " = ?", new String[]{String.valueOf(m.getIDHEURE())});
        database.close();
    }

    /**
     * @param id l'identifiant du client à récupérer
     */
    public THoraire selectionner(long id) {

        database = open();
        THoraire thoraire = new THoraire();

        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY + " = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            thoraire.setIDHEURE(c.getInt(0));
            thoraire.setHEURE(c.getString(1));
            return thoraire;
        }
        c.close();
        database.close();
        return thoraire;
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

    public List<THoraire> selectionnerList() {

        database = open();

        List<THoraire> liste = new ArrayList<>();

        String requete = "SELECT * FROM " + TABLE_NAME/* + " WHERE " + KEY + " <> ?"*/;

        Cursor cursor = database.rawQuery(requete, /*new String[]{String.valueOf(1)}*/null);

        if (cursor.moveToFirst()) {
            do {
                THoraire horaire = new THoraire();
                horaire.setIDHEURE(cursor.getInt(0));
                horaire.setHEURE(cursor.getString(1));
                liste.add(horaire);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return liste;
    }
}
