package com.saltechdigital.iaischedule.database.presence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.saltechdigital.iaischedule.constantandenum.Constantes;
import com.saltechdigital.iaischedule.database.DAOBase;
import com.saltechdigital.iaischedule.database.personne.TPersonne;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jeanpaul Tossou on 30/12/2016.
 */

public class TPresenceDAO extends DAOBase {

    private static final String TABLE_NAME = "PRESENCE";
    private static final String KEY = "IDELEVE,IDPERSONNE,IDCOURS,IDHEURE";

    private static final String TYPEPRESENCE = "TYPEPRESENCE";
    private static final String IDELEVE = "IDELEVE";
    private static final String IDPERSONNE = "IDPERSONNE";
    private static final String IDCOURS = "IDCOURS";
    private static final String IDHEURE = "IDHEURE";
    private static final String DATEPRESENCE = "DATEPRESENCE";

    private Context context;

    private SQLiteDatabase database = getDb();

    public TPresenceDAO(Context pContext) {
        super(pContext);
        this.context = pContext;
    }

    public void ajouter(TPresence m) {

        database = open();

        ContentValues valeur = new ContentValues();

        if (m.getTYPEPRESENCE() != null) {
            valeur.put(TYPEPRESENCE, m.getTYPEPRESENCE());
        }

        if (m.getDATEPRESENCE() != null) {
            valeur.put(DATEPRESENCE, m.getDATEPRESENCE());
        }

        if (m.getIDELEVE() != 0) {
            valeur.put(IDELEVE, m.getIDELEVE());
        }

        if (m.getIDPERSONNE() != 0) {
            valeur.put(IDPERSONNE, m.getIDPERSONNE());
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

    public int getId(String presence) {
        database = open();
        int retourne = 0;
        Cursor cursor = database.rawQuery("SELECT " + KEY + " FROM " + TABLE_NAME + " WHERE " + TYPEPRESENCE + " = ?", new String[]{presence});
        if (cursor.moveToFirst()) {
            retourne = cursor.getInt(0);
        }
        cursor.close();
        return retourne;
    }

    public void ajouterListe(List<TPresence> presenceList) {

        for (TPresence presence : presenceList) {
            ajouter(presence);
        }
    }

    public void supprimer(String id) {
        database = open();
        database.delete(TABLE_NAME, KEY + " = ?", new String[]{id});
        super.close();
        // CODE
    }

    public boolean existeDateHeure(String date, int idHeure) {
        database = open();
        boolean retourne = false;

        String requete = "SELECT COUNT(DATEPRESENCE AND IDHEURE)\n" +
                "FROM PRESENCE\n" +
                "WHERE DATEPRESENCE = ?\n" +
                "AND IDHEURE = ?";

        Cursor cursor = database.rawQuery(requete, new String[]{date, String.valueOf(idHeure)});
        if (cursor.moveToFirst()) {
            int existe = cursor.getInt(0);
            if (existe >= 1) {
                retourne = true;
            }
        }

        cursor.close();
        database.close();
        return retourne;
    }

    public void modifierPresence(TPresence m) {

        database = open();
        ContentValues valeur = new ContentValues();
        valeur.put(TYPEPRESENCE, m.getIDHEURE());

        database.update(TABLE_NAME, valeur, KEY + " = ?", new String[]{String.valueOf(m.getIDELEVE() + "," + m.getIDPERSONNE() + "," + m.getIDCOURS() + "," + m.getIDHEURE())});
        // CODE
    }

    public TPresence selectionner(long idEleve) {

        database = open();
        TPresence tPresence = new TPresence();

        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + IDELEVE + " = ?", new String[]{String.valueOf(idEleve)});
        if (c.moveToFirst()) {
            tPresence.setTYPEPRESENCE(c.getString(0));
            tPresence.setIDELEVE(c.getInt(1));
            tPresence.setIDPERSONNE(c.getInt(2));
            tPresence.setIDCOURS(c.getInt(3));
            tPresence.setIDHEURE(c.getInt(4));
            return tPresence;
        }
        c.close();
        return tPresence;
    }

    public List<TPersonne> selectionnerPersonneByConvers() {
        database = open();

        List<TPersonne> list = new ArrayList<>();
        String requete = "SELECT p.*,pc.*,c.*\n" +
                "FROM personne p,personneconvers pc,conversation c,typeconversation t\n" +
                "WHERE p.`IDPERSONNE` = pc.`IDPERSONNE`\n" +
                "AND pc.`IDCONVERS` = c.`IDCONVERS`\n" +
                "AND c.`IDTYPEMESSAGE` = t.`IDTYPEMESSAGE`\n" +
                "AND t.`LIBELLETYPEMESSAGE` = ?";

        Cursor cursor = database.rawQuery(requete, new String[]{Constantes.REGLE.CHAT});

        if (cursor.moveToFirst()) {
            do {
                TPersonne personne = new TPersonne();
                personne.setIDPERSONNE(cursor.getInt(0));
                personne.setNOMPERSONNE(cursor.getString(1));
                personne.setPRENOMPERSONNE(cursor.getString(2));
                personne.setTELEPHONEPERSONNE(cursor.getString(3));
                personne.setSEXEPERSONNE(cursor.getString(8));
                list.add(personne);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;

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

    public List<TPresence> selectionnerList() {

        database = open();

        List<TPresence> liste = new ArrayList<>();

        String requete = "SELECT * FROM " + TABLE_NAME/* + " WHERE " + KEY + " <> ?"*/;

        Cursor c = database.rawQuery(requete, /*new String[]{String.valueOf(1)}*/null);

        if (c.moveToFirst()) {
            do {
                TPresence tPresence = new TPresence();
                tPresence.setTYPEPRESENCE(c.getString(0));
                tPresence.setIDELEVE(c.getInt(1));
                tPresence.setIDPERSONNE(c.getInt(2));
                tPresence.setIDCOURS(c.getInt(3));
                tPresence.setIDHEURE(c.getInt(4));
                liste.add(tPresence);
            } while (c.moveToNext());
        }
        c.close();
        return liste;
    }
}
