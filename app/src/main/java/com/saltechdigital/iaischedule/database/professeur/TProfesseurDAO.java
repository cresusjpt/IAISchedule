package com.saltechdigital.iaischedule.database.professeur;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saltechdigital.iaischedule.constantandenum.Constantes;
import com.saltechdigital.iaischedule.database.DAOBase;
import com.saltechdigital.iaischedule.database.personne.TPersonne;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jeanpaul Tossou on 30/12/2016.
 */

public class TProfesseurDAO extends DAOBase {

    private static final String TABLE_NAME = "PROFESSEUR";
    private static final String KEY = "IDPROF";

    private static final String IDPERSONNE = "IDPERSONNE";

    private Context context;

    private SQLiteDatabase database = getDb();

    public TProfesseurDAO(Context pContext) {
        super(pContext);
        this.context = pContext;
    }

    public void ajouter(TProfesseur m) {

        database = open();

        ContentValues valeur = new ContentValues();

        if (m.getIDPERSONNE() != 0) {
            valeur.put(IDPERSONNE, m.getIDPERSONNE());
        }

        database.insert(TABLE_NAME, null, valeur);
        Log.d("JEANPAUL", "ENREGISTREMENT REUSSI");
    }

    public int getId(int idPersonne) {
        int retourne = 0;
        Cursor cursor = database.rawQuery("SELECT " + KEY + " FROM " + TABLE_NAME + " WHERE " + IDPERSONNE + " = " + idPersonne, null);
        if (cursor.moveToFirst()) {
            retourne = cursor.getInt(0);
        }
        cursor.close();
        return retourne;
    }

    /**
     * @param professeurList liste de personne à ajouter
     */

    public void ajouterListe(List<TProfesseur> professeurList) {

        for (TProfesseur professeur : professeurList) {
            ajouter(professeur);
        }
    }

    /**
     * @param id l'identifiant de la personne à supprimer
     */

    public void supprimer(long id) {
        database = open();
        database.delete(TABLE_NAME, KEY + " = ?", new String[]{String.valueOf(id)});
        super.close();
        // CODE
    }

    /**
     * @param id l'identifiant du professeur à récupérer
     */
    public TProfesseur selectionner(long id) {

        database = open();
        TProfesseur tProfesseur = new TProfesseur();

        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY + " = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            tProfesseur.setIDPROF(c.getInt(0));
            tProfesseur.setIDPERSONNE(c.getInt(1));
            return tProfesseur;
        }
        c.close();
        return tProfesseur;
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

    public List<TProfesseur> selectionnerList() {

        database = open();

        List<TProfesseur> liste = new ArrayList<>();

        String requete = "SELECT * FROM " + TABLE_NAME/* + " WHERE " + KEY + " <> ?"*/;

        Cursor cursor = database.rawQuery(requete, /*new String[]{String.valueOf(1)}*/null);

        if (cursor.moveToFirst()) {
            do {
                TProfesseur professeur = new TProfesseur();
                professeur.setIDPROF(cursor.getInt(0));
                professeur.setIDPERSONNE(cursor.getInt(1));
                liste.add(professeur);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return liste;
    }
}
