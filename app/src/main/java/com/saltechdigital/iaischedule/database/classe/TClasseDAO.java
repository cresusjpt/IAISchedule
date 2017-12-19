package com.saltechdigital.iaischedule.database.classe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.saltechdigital.iaischedule.database.DAOBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeanpaul Tossou on 28/11/2017.
 */

public class TClasseDAO extends DAOBase {

    private static final String TABLE_NAME = "CLASSE";
    private static final String KEY = "IDCLASSE";

    private static final String NOMCLASSE = "NOMCLASSE";


    private Context context;

    private SQLiteDatabase database = getDb();

    public TClasseDAO(Context pContext) {
        super(pContext);
        this.context = pContext;
    }

    public void ajouter(TClasse m) {

        database = open();

        ContentValues valeur = new ContentValues();

        if (m.getNOMCLASSE() != null) {
            valeur.put(NOMCLASSE, m.getNOMCLASSE());
        }

        if (m.getIDCLASSE() != 0) {
            valeur.put(KEY, m.getIDCLASSE());
        } else {
            valeur.put(KEY, taille() + 1);
        }
        database.insert(TABLE_NAME, null, valeur);
        database.close();
    }

    public int getId(String nomClasse) {
        int retourne = 0;
        database = open();
        Cursor cursor = database.rawQuery("SELECT " + KEY + " FROM " + TABLE_NAME + " WHERE " + NOMCLASSE + " = ?", new String[]{nomClasse});
        if (cursor.moveToFirst()) {
            retourne = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return retourne;
    }

    public void ajouterListe(List<TClasse> classeList) {

        for (TClasse classe : classeList) {
            ajouter(classe);
        }
    }

    public void supprimer(long id) {
        database = open();
        database.delete(TABLE_NAME, KEY + " = ?", new String[]{String.valueOf(id)});
        database.close();
    }

    public String getNomclasse(int idClasse) {
        database = open();
        String retourne = "";
        Cursor cursor = database.rawQuery("SELECT " + NOMCLASSE + " FROM " + TABLE_NAME + " WHERE " + KEY + " = ?", new String[]{String.valueOf(idClasse)});
        if (cursor.moveToFirst()) {
            retourne = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return retourne;
    }

    public void modifierNomClasse(TClasse m) {

        database = open();
        ContentValues valeur = new ContentValues();
        valeur.put(NOMCLASSE, m.getNOMCLASSE());
        database.update(TABLE_NAME, valeur, KEY + " = ?", new String[]{String.valueOf(m.getNOMCLASSE())});
        // CODE
        database.close();
    }

    public TClasse selectionner(long id) {

        database = open();
        TClasse classe = new TClasse();

        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY + " = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            classe.setIDCLASSE(c.getInt(0));
            classe.setNOMCLASSE(c.getString(1));
            return classe;
        }

        c.close();
        database.close();
        return classe;
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

    public List<TClasse> selectionnerList() {

        database = open();

        List<TClasse> liste = new ArrayList<>();

        String requete = "SELECT * FROM " + TABLE_NAME/* + " WHERE " + KEY + " <> ?"*/;

        Cursor cursor = database.rawQuery(requete, /*new String[]{String.valueOf(1)}*/null);

        if (cursor.moveToFirst()) {
            do {
                TClasse classe = new TClasse();
                classe.setIDCLASSE(cursor.getInt(0));
                classe.setNOMCLASSE(cursor.getString(1));
                liste.add(classe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return liste;
    }
}