package com.saltechdigital.iaischedule.database.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.saltechdigital.iaischedule.database.DAOBase;

/**
 * Created by Jeanpaul Tossou on 14/01/2017.
 */

public class TAdminDAO extends DAOBase {

    private Context context;
    private SQLiteDatabase database = getDb();

    private static final String IDADMIN = "IDADMIN";
    private static final String IDPERSONNE = "IDPERSONNE";
    private static final String NOMADMIN = "NOMADMIN";
    private static final String TABLE_NAME = "ADMIN";

    public TAdminDAO(Context context) {
        super(context);
        this.context = context;
    }

    public void ajouter(TAdmin tAdmin) {
        database = open();
        ContentValues valeur = new ContentValues();
        valeur.put(NOMADMIN, tAdmin.getNOMADMIN());
        valeur.put(IDPERSONNE, tAdmin.getIDPERSONNE());
        database.insert(TABLE_NAME, null, valeur);
    }

    public int supprimer(int id) {
        SQLiteDatabase database = open();
        return database.delete(TABLE_NAME, IDADMIN + " = ?", new String[]{String.valueOf(id)});
    }

    public int supprimerString(String nom) {
        SQLiteDatabase database = open();
        return database.delete(TABLE_NAME, NOMADMIN + " = ?", new String[]{nom});
    }

    public void modifier(TAdmin tAdmin) {

        database = open();
        ContentValues valeur = new ContentValues();
        valeur.put(NOMADMIN, tAdmin.getNOMADMIN());
        database.update(TABLE_NAME, valeur, IDADMIN + " = ?", new String[]{String.valueOf(tAdmin.getIDADMIN())});
    }

    public TAdmin selectionner(int id) {

        database = open();
        TAdmin tAdmin = null;

        Cursor c = database.rawQuery("select * from " + TABLE_NAME + "where " + IDADMIN + " ?", new String[]{String.valueOf(id)});
        if (c.getCount() != 0) {
            c.moveToFirst();
            int idAdmin = c.getInt(0);
            String nomAdmin = c.getString(1);
            int idpersonne = c.getInt(2);
            tAdmin = new TAdmin(idAdmin, idpersonne, nomAdmin);
        } else {
            Toast.makeText(context, "L'admin avec cet identifiant  n'existe pas dans la base", Toast.LENGTH_SHORT).show();
        }
        c.close();

        return tAdmin;
    }

    public int selectionnerString(String string) {
        database = open();
        int resultat = 0;
        Cursor c = database.rawQuery("select " + IDADMIN + " from " + TABLE_NAME + " where " + NOMADMIN + " = ?", new String[]{string});
        if (c.getCount() != 0) {
            resultat = c.getInt(0);
            return resultat;
        }
        c.close();
        return resultat;
    }

    public boolean existe(String nom) {
        return this.selectionnerString(nom) != 0;
    }
}
