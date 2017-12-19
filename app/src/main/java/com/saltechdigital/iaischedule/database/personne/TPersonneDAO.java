package com.saltechdigital.iaischedule.database.personne;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saltechdigital.iaischedule.constantandenum.Constantes;
import com.saltechdigital.iaischedule.database.DAOBase;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jeanpaul Tossou on 30/12/2016.
 */

public class TPersonneDAO extends DAOBase {

    private static final String TABLE_NAME = "PERSONNE";
    public static final String KEY = "IDPERSONNE";

    public static final String NOM_PERSONNE = "NOMPERSONNE";
    public static final String PRENOM_PERSONNE = "PRENOMPERSONNE";
    public static final String TELEPHONE_PERSONNE = "TELEPHONEPERSONNE";
    public static final String JID_PERSONNE = "JIDACCOUNTPERSONNE";
    public static final String SEXE_PERSONNE = "SEXEPERSONNE";

    private Context context;

    private SQLiteDatabase database = getDb();

    public TPersonneDAO(Context pContext) {
        super(pContext);
        this.context = pContext;
    }

    public void ajouter(TPersonne m) {

        database = open();

        ContentValues valeur = new ContentValues();

        if (m.getNOMPERSONNE() != null) {
            valeur.put(NOM_PERSONNE, m.getNOMPERSONNE());
        }
        if (m.getPRENOMPERSONNE() != null) {
            valeur.put(PRENOM_PERSONNE, m.getPRENOMPERSONNE());
        }
        if (m.getSEXEPERSONNE() != null) {
            valeur.put(SEXE_PERSONNE, m.getSEXEPERSONNE());
        }
        if (m.getTELEPHONEPERSONNE() != null) {
            valeur.put(TELEPHONE_PERSONNE, m.getTELEPHONEPERSONNE());
        }

        if (m.getIDPERSONNE() == 0) {
            valeur.put(KEY, taille() + 1);
        } else {
            valeur.put(KEY, m.getIDPERSONNE());
        }
        database.insert(TABLE_NAME, null, valeur);
        database.close();
    }

    public int getId(String jid) {
        int retourne = 0;
        Cursor cursor = database.rawQuery("SELECT " + KEY + " FROM " + TABLE_NAME + " WHERE " + JID_PERSONNE + " = " + jid, null);
        if (cursor.moveToFirst()) {
            retourne = cursor.getInt(0);
        }
        cursor.close();
        return retourne;
    }

    /**
     * @param personneList liste de personne à ajouter
     */

    public void ajouterListe(List<TPersonne> personneList) {

        for (TPersonne personne : personneList) {
            ajouter(personne);
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
     * @param m la personne modifiée
     */

    public void modifierStatut(TPersonne m) {

        database = getDb();
        ContentValues valeur = new ContentValues();
        //valeur.put(STATUT_PERSONNE, m.getSTATUTPERSONNE());
        database.update(TABLE_NAME, valeur, KEY + " = ?", new String[]{String.valueOf(m.getIDPERSONNE())});
        // CODE
    }

    /**
     * @param id l'identifiant du client à récupérer
     */
    public TPersonne selectionner(long id) {

        database = open();
        TPersonne tPersonne = new TPersonne();

        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY + " = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            tPersonne.setIDPERSONNE(c.getInt(0));
            tPersonne.setNOMPERSONNE(c.getString(1));
            tPersonne.setPRENOMPERSONNE(c.getString(2));
            tPersonne.setTELEPHONEPERSONNE(String.valueOf(c.getInt(3)));
            tPersonne.setSEXEPERSONNE(c.getString(4));
            return tPersonne;
        }
        c.close();
        database.close();
        return tPersonne;
    }

    public TPersonne getByJIDAccount(String jidAccount) {
        database = open();
        TPersonne personne = new TPersonne();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + JID_PERSONNE + " = ? ", new String[]{jidAccount});
        if (cursor.moveToFirst()) {
            personne.setIDPERSONNE(cursor.getInt(0));
            personne.setNOMPERSONNE(cursor.getString(1));
            personne.setPRENOMPERSONNE(cursor.getString(2));
            personne.setTELEPHONEPERSONNE(cursor.getString(3));
            personne.setSEXEPERSONNE(cursor.getString(8));
        }
        cursor.close();
        return personne;
    }

    public TPersonne selectionnerResponsable() {
        database = open();

        List<TPersonne> list = new ArrayList<>();
        String requete = "SELECT p.*\n" +
                "FROM personne p, eleve e\n" +
                "WHERE p.`IDPERSONNE` = e.`IDPERSONNE`\n" +
                "AND e.`RESPONSABLE` = ?";

        Cursor cursor = database.rawQuery(requete, new String[]{"1"});

        TPersonne personne = new TPersonne();
        if (cursor.moveToFirst()) {
            personne.setIDPERSONNE(cursor.getInt(0));
            personne.setNOMPERSONNE(cursor.getString(1));
            personne.setPRENOMPERSONNE(cursor.getString(2));
            personne.setTELEPHONEPERSONNE(cursor.getString(3));
            personne.setSEXEPERSONNE(cursor.getString(4));
            list.add(personne);
        }
        cursor.close();
        database.close();
        return personne;

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

    public List<TPersonne> selectionnerList() {

        database = open();

        List<TPersonne> liste = new ArrayList<>();

        String requete = "SELECT * FROM " + TABLE_NAME/* + " WHERE " + KEY + " <> ?"*/;

        Cursor cursor = database.rawQuery(requete, /*new String[]{String.valueOf(1)}*/null);

        if (cursor.moveToFirst()) {
            do {
                TPersonne personne = new TPersonne();
                personne.setIDPERSONNE(cursor.getInt(0));
                personne.setNOMPERSONNE(cursor.getString(1));
                personne.setPRENOMPERSONNE(cursor.getString(2));
                personne.setTELEPHONEPERSONNE(cursor.getString(3));
                personne.setSEXEPERSONNE(cursor.getString(4));
                liste.add(personne);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return liste;
    }
}
