package com.saltechdigital.iaischedule.database.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.net.TrafficStatsCompat;

import com.saltechdigital.iaischedule.constantandenum.Constantes;
import com.saltechdigital.iaischedule.database.DAOBase;
import com.saltechdigital.iaischedule.database.journee.TJournee;
import com.saltechdigital.iaischedule.database.personne.TPersonne;
import com.saltechdigital.iaischedule.database.presence.TPresence;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jeanpaul Tossou on 30/12/2016.
 */

public class TScheduleDAO extends DAOBase {

    private static final String TABLE_NAME = "SCHEDULE";
    private static final String KEY = "IDJOURNEE,IDHEURE,IDCOURS";

    private static final String IDJOURNEE = "IDJOURNEE";
    private static final String IDHEURE = "IDHEURE";
    private static final String IDCOURS = "IDCOURS";

    private Context context;

    private SQLiteDatabase database = getDb();

    public TScheduleDAO(Context pContext) {
        super(pContext);
        this.context = pContext;
    }

    public void ajouter(TSchedule m) {

        database = open();

        ContentValues valeur = new ContentValues();

        if (m.getIDJOURNEE() != 0) {
            valeur.put(IDJOURNEE, m.getIDJOURNEE());
        }

        if (m.getIDCOURS() != 0) {
            valeur.put(IDCOURS, m.getIDCOURS());
        }

        if (m.getIDHEURE() != 0) {
            valeur.put(IDHEURE, m.getIDHEURE());
        }
        database.insert(TABLE_NAME, null, valeur);
        database.close();
    }

    public void ajouterListe(List<TSchedule> journeeList) {

        for (TSchedule schedule : journeeList) {
            ajouter(schedule);
        }
    }

    public void supprimer(String id) {
        database = open();
        database.delete(TABLE_NAME, KEY + " = ?", new String[]{id});
        super.close();
        // CODE
    }

    /*public void modifierCours(TPresence m) {

        database = open();
        ContentValues valeur = new ContentValues();
        valeur.put(IDCOURS,m.getIDHEURE());

        database.update(TABLE_NAME, valeur, KEY + " = ?", new String[]{String.valueOf(m.getIDELEVE()+","+m.getIDPERSONNE()+","+m.getIDCOURS()+","+m.getIDHEURE())});
        // CODE
    }*/

    public TSchedule selectionner(long idCours) {

        database = open();
        TSchedule tSchedule = new TSchedule();

        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + IDCOURS + " = ?", new String[]{String.valueOf(idCours)});
        if (c.moveToFirst()) {

            return tSchedule;
        }
        c.close();
        return tSchedule;
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

    public List<TSchedule> selectionnerList() {

        database = open();

        List<TSchedule> liste = new ArrayList<>();

        String requete = "SELECT * FROM " + TABLE_NAME + " ORDER BY IDJOURNEE ASC"/* + " WHERE " + KEY + " <> ?"*/;

        Cursor c = database.rawQuery(requete, /*new String[]{String.valueOf(1)}*/null);

        if (c.moveToFirst()) {
            do {
                TSchedule tschedule = new TSchedule();
                tschedule.setIDJOURNEE(c.getInt(0));
                tschedule.setIDHEURE(c.getInt(1));
                tschedule.setIDCOURS(c.getInt(2));
                liste.add(tschedule);
            } while (c.moveToNext());
        }
        c.close();
        return liste;
    }

    public List<TSchedule> selectionnerListBy(String order) {

        database = open();

        List<TSchedule> liste = new ArrayList<>();

        String requete = "SELECT * FROM " + TABLE_NAME + " WHERE " + IDJOURNEE + " = ?";

        Cursor c = database.rawQuery(requete, new String[]{String.valueOf(order)});

        if (c.moveToFirst()) {
            do {
                TSchedule tschedule = new TSchedule();
                tschedule.setIDJOURNEE(c.getInt(0));
                tschedule.setIDHEURE(c.getInt(1));
                tschedule.setIDCOURS(c.getInt(2));
                liste.add(tschedule);
            } while (c.moveToNext());
        }
        c.close();
        return liste;
    }

}
