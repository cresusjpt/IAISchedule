package com.saltechdigital.iaischedule.database.journee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.saltechdigital.iaischedule.database.DAOBase;
import com.saltechdigital.iaischedule.database.horaire.THoraire;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jeanpaul Tossou on 30/12/2016.
 */

public class TJourneeDAO extends DAOBase {

    private static final String TABLE_NAME = "JOURNEE";
    private static final String KEY = "IDJOURNEE";

    private static final String NOMJOUR = "NOMJOUR";

    private Context context;

    private SQLiteDatabase database = getDb();

    public TJourneeDAO(Context pContext) {
        super(pContext);
        this.context = pContext;
    }

    public void ajouter(TJournee m) {

        database = open();

        ContentValues valeur = new ContentValues();

        if (m.getNOMJOUR() != null) {
            valeur.put(NOMJOUR, m.getNOMJOUR());
        }
        database.insert(TABLE_NAME, null, valeur);
        database.close();
    }

    public int getId(String jour) {
        int retourne = 0;
        database = open();
        Cursor cursor = database.rawQuery("SELECT " + KEY + " FROM " + TABLE_NAME + " WHERE " + NOMJOUR + " = ?", new String[]{jour});
        if (cursor.moveToFirst()) {
            retourne = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return retourne;
    }

    /**
     * @param journeeList liste de personne à ajouter
     */

    public void ajouterListe(List<TJournee> journeeList) {

        for (TJournee journee : journeeList) {
            ajouter(journee);
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

    public void modifierHeure(TJournee m) {

        database = open();
        ContentValues valeur = new ContentValues();
        valeur.put(NOMJOUR, m.getNOMJOUR());
        database.update(TABLE_NAME, valeur, KEY + " = ?", new String[]{String.valueOf(m.getIDJOURNEE())});
        database.close();
    }

    /**
     * @param id l'identifiant du client à récupérer
     */
    public TJournee selectionner(long id) {

        database = open();
        TJournee tJournee = new TJournee();

        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY + " = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            tJournee.setIDJOURNEE(c.getInt(0));
            tJournee.setNOMJOUR(c.getString(1));
            return tJournee;
        }
        c.close();
        database.close();
        return tJournee;
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

    public List<TJournee> selectionnerList() {

        database = open();

        List<TJournee> liste = new ArrayList<>();

        String requete = "SELECT * FROM " + TABLE_NAME/* + " WHERE " + KEY + " <> ?"*/;

        Cursor cursor = database.rawQuery(requete, /*new String[]{String.valueOf(1)}*/null);

        if (cursor.moveToFirst()) {
            do {
                TJournee tJournee = new TJournee();
                tJournee.setIDJOURNEE(cursor.getInt(0));
                tJournee.setNOMJOUR(cursor.getString(1));
                liste.add(tJournee);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return liste;
    }
}
