package com.saltechdigital.iaischedule.database.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.saltechdigital.iaischedule.database.DAOBase;
import com.saltechdigital.iaischedule.database.cours.TCours;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeanpaul Tossou on 28/11/2017.
 */

public class TNotesDAO extends DAOBase {

    private static final String TABLE_NAME = "NOTES";
    private static final String KEY = "IDNOTE";

    private static final String IDCOURS = "IDCOURS";
    private static final String IDHEURE = "IDHEURE";
    private static final String IDJOURNEE = "IDJOURNEE";
    private static final String NOTES = "NOTES";
    private static final String DATENOTES = "DATENOTES";

    private Context context;

    private SQLiteDatabase database = getDb();

    public TNotesDAO(Context pContext) {
        super(pContext);
        this.context = pContext;
    }

    public void ajouter(TNotes m) {

        database = open();

        ContentValues valeur = new ContentValues();

        if (m.getIDCOURS() != 0) {
            valeur.put(IDCOURS, m.getIDCOURS());
        }

        if (m.getIDHEURE() != 0) {
            valeur.put(IDHEURE, m.getIDHEURE());
        }

        if (m.getIDJOURNEE() != 0) {
            valeur.put(IDJOURNEE, m.getIDJOURNEE());
        }

        if (m.getNOTES() != null) {
            valeur.put(NOTES, m.getNOTES());
        }

        if (m.getDATENOTES() != null) {
            valeur.put(DATENOTES, m.getDATENOTES());
        }

        database.insert(TABLE_NAME, null, valeur);
        database.close();
    }

    public int getId(String notes) {
        int retourne = 0;
        database = open();
        Cursor cursor = database.rawQuery("SELECT " + KEY + " FROM " + TABLE_NAME + " WHERE " + NOTES + " = ?", new String[]{notes});
        if (cursor.moveToFirst()) {
            retourne = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return retourne;
    }

    public void ajouterListe(List<TNotes> notesList) {

        for (TNotes notes : notesList) {
            ajouter(notes);
        }
    }

    public void supprimer(long id) {
        database = open();
        database.delete(TABLE_NAME, KEY + " = ?", new String[]{String.valueOf(id)});
        super.close();
    }

    public void modifierNote(TNotes m) {

        database = open();
        ContentValues valeur = new ContentValues();
        valeur.put(NOTES, m.getNOTES());
        database.update(TABLE_NAME, valeur, KEY + " = ?", new String[]{String.valueOf(m.getIDNOTE())});
        database.close();
    }

    public TNotes selectionner(long id) {

        database = open();
        TNotes tNotes = new TNotes();

        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY + " = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            tNotes = bind(tNotes, c);
        }
        c.close();
        return tNotes;
    }

    private TNotes bind(TNotes tNotes, Cursor c) {

        tNotes.setIDNOTE(c.getInt(0));
        tNotes.setIDCOURS(c.getInt(1));
        tNotes.setIDHEURE(c.getInt(2));
        tNotes.setIDJOURNEE(c.getInt(3));
        tNotes.setNOTES(c.getString(4));
        tNotes.setDATENOTES(c.getString(5));

        return tNotes;
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

    public List<TNotes> selectionnerList(int idCours, int idHeure, int idJournee) {

        database = open();

        List<TNotes> liste = new ArrayList<>();

        String requete = "SELECT * FROM " + TABLE_NAME + " WHERE " + IDCOURS + " = ?" + " AND " + IDHEURE + " = ?" + " AND " + IDJOURNEE + " = ? ORDER BY " + KEY + " DESC ";

        Cursor cursor = database.rawQuery(requete, new String[]{String.valueOf(idCours), String.valueOf(idHeure), String.valueOf(idJournee)});
        if (cursor.moveToFirst()) {
            do {
                TNotes notes = new TNotes();
                notes = bind(notes, cursor);
                liste.add(notes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return liste;
    }
}