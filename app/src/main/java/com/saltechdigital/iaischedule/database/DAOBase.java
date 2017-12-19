package com.saltechdigital.iaischedule.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Jeanpaul Tossou on 29/12/2016.
 */

public abstract class DAOBase extends SQLiteOpenHelper {

    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour après que l'app soit en utilisation, il faudra changer cet attribut
    private static int VERSION = 1;
    // Le nom du fichier qui représente ma base
    private final static String NOM = "iaischeduler.db";

    public static int getVERSION() {
        return VERSION;
    }

    public static void setVERSION(int VERSION) {
        DAOBase.VERSION = VERSION;
    }

    private static final String TABLE_CREATE_PERSONNE = "CREATE TABLE PERSONNE(\n" +
            "\tIDPERSONNE         INTEGER NOT NULL ,\n" +
            "\tNOMPERSONNE        TEXT NOT NULL ,\n" +
            "\tPRENOMPERSONNE     TEXT NOT NULL ,\n" +
            "\tTELEPHONEPERSONNE  TEXT ,\n" +
            "\tSEXEPERSONNE       TEXT  NOT NULL  ,\n" +
            "\tPRIMARY KEY (IDPERSONNE) ,\n" +
            "\tCHECK (SEXEPERSONNE IN (\"M\",\"F\"))\n" +
            ");";

    private static final String TABLE_CREATE_PROFESSEUR = "CREATE TABLE PROFESSEUR(\n" +
            "\tIDPROF      INTEGER NOT NULL ,\n" +
            "\tIDPERSONNE  INTEGER NOT NULL ,\n" +
            "\tPRIMARY KEY (IDPROF,IDPERSONNE) ,\n" +
            "\t\n" +
            "\tFOREIGN KEY (IDPERSONNE) REFERENCES PERSONNE(IDPERSONNE)\n" +
            ");";

    private static final String TABLE_CREATE_ELEVE = "CREATE TABLE ELEVE(\n" +
            "\tIDELEVE     INTEGER NOT NULL ,\n" +
            "\tIDPERSONNE  INTEGER NOT NULL ,\n" +
            "\tRESPONSABLE INTEGER NULL  ,\n" +
            "\tIDCLASSE    INTEGER ,\n" +
            "\tPRIMARY KEY (IDELEVE,IDPERSONNE) ,\n" +
            "\t\n" +
            "\tFOREIGN KEY (IDPERSONNE) REFERENCES PERSONNE(IDPERSONNE),\n" +
            "\tFOREIGN KEY (IDCLASSE) REFERENCES CLASSE(IDCLASSE)\n" +
            ");";

    private static final String TABLE_CREATE_CLASSE = "CREATE TABLE CLASSE(\n" +
            "\tIDCLASSE   INTEGER NOT NULL ,\n" +
            "\tNOMCLASSE  TEXT NOT NULL , \n" +
            "\tPRIMARY KEY (IDCLASSE) \n" +
            ");";

    private static final String TABLE_CREATE_COURS = "CREATE TABLE COURS(\n" +
            "\tIDCOURS     INTEGER NOT NULL ,\n" +
            "\tNOMCOURS    TEXT NOT NULL ,\n" +
            "\tIDCLASSE    INTEGER ,\n" +
            "\tIDPROF      INTEGER NOT NULL ,\n" +
            "\tIDPERSONNE  INTEGER NOT NULL ,\n" +
            "\tPRIMARY KEY (IDCOURS),\n" +
            "\t\n" +
            "\tFOREIGN KEY (IDCLASSE) REFERENCES CLASSE(IDCLASSE),\n" +
            "\tFOREIGN KEY (IDPROF) REFERENCES PROFESSEUR(IDPROF),\n" +
            "\tFOREIGN KEY (IDPERSONNE) REFERENCES PERSONNE(IDPERSONNE)\n" +
            ");";

    private static final String TABLE_CREATE_HORAIRE = "CREATE TABLE HORAIRE(\n" +
            "\tIDHEURE  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,\n" +
            "\tHEURE    TEXT NOT NULL \n" +
            ");";

    private static final String TABLE_CREATE_PRESENCE = "CREATE TABLE PRESENCE(\n" +
            "\tTYPEPRESENCE  TEXT  NOT NULL  ,\n" +
            "\tIDELEVE       INTEGER NOT NULL ,\n" +
            "\tIDPERSONNE    INTEGER NOT NULL ,\n" +
            "\tIDCOURS       INTEGER NOT NULL ,\n" +
            "\tIDHEURE       INTEGER NOT NULL ,\n" +
            "\tDATEPRESENCE  TEXT  NOT NULL  ,\n" +
            "\tPRIMARY KEY (IDELEVE,IDPERSONNE,IDCOURS,IDHEURE,DATEPRESENCE) ,\n" +
            "\tCHECK (TYPEPRESENCE IN (\"P\",\"R\",\"A\")) ,\n" +
            "\t\n" +
            "\tFOREIGN KEY (IDELEVE) REFERENCES ELEVE(IDELEVE),\n" +
            "\tFOREIGN KEY (IDPERSONNE) REFERENCES PERSONNE(IDPERSONNE),\n" +
            "\tFOREIGN KEY (IDCOURS) REFERENCES COURS(IDCOURS),\n" +
            "\tFOREIGN KEY (IDHEURE) REFERENCES HORAIRE(IDHEURE)\n" +
            ");";

    private static final String TABLE_CREATE_JOURNEE = "CREATE TABLE JOURNEE(\n" +
            "\tIDJOURNEE  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,\n" +
            "\tNOMJOUR    TEXT  NOT NULL  ,\n" +
            "\tCHECK (NOMJOUR IN (\"LUNDI\",\"MARDI\",\"MERCREDI\",\"JEUDI\",\"VENDREDI\",\"SAMEDI\"))\n" +
            ");";

    private static final String TABLE__CREATE_SCHEDULE = "CREATE TABLE SCHEDULE(\n" +
            "\tIDJOURNEE  INTEGER NOT NULL ,\n" +
            "\tIDHEURE    INTEGER NOT NULL ,\n" +
            "\tIDCOURS    INTEGER NOT NULL ,\n" +
            "\tPRIMARY KEY (IDJOURNEE,IDHEURE,IDCOURS) ,\n" +
            "\t\n" +
            "\tFOREIGN KEY (IDJOURNEE) REFERENCES JOURNEE(IDJOURNEE),\n" +
            "\tFOREIGN KEY (IDHEURE) REFERENCES HORAIRE(IDHEURE),\n" +
            "\tFOREIGN KEY (IDCOURS) REFERENCES COURS(IDCOURS)\n" +
            ");";

    private static final String TABLE_CREATE_NOTES = "CREATE TABLE NOTES(\n" +
            "\tIDNOTE     INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,\n" +
            "\tIDCOURS    INTEGER NOT NULL ,\n" +
            "\tIDHEURE    INTEGER NOT NULL ,\n" +
            "\tIDJOURNEE  INTEGER NOT NULL ,\n" +
            "\tNOTES      TEXT NOT NULL ,\n" +
            "\tDATENOTES  TEXT NOT NULL \n" +
            ");";

    private static final String TABLE_DROP_PERSONNE = "DROP TABLE IF EXISTS PERSONNE;";
    private static final String TABLE_DROP_PROFESSEUR = "DROP TABLE IF EXISTS PROFESSEUR;";
    private static final String TABLE_DROP_ELEVE = "DROP TABLE IF EXISTS ELEVE;";
    private static final String TABLE_DROP_CLASSE = "DROP TABLE IF EXISTS ELEVE;";
    private static final String TABLE_DROP_COURS = "DROP TABLE IF EXISTS COURS;";
    private static final String TABLE_DROP_HORAIRE = "DROP TABLE IF EXISTS HORAIRE;";
    private static final String TABLE_DROP_PRESENCE = "DROP TABLE IF EXISTS PRESENCE";
    private static final String TABLE_DROP_JOURNEE = "DROP TABLE IF EXISTS JOURNEE";
    private static final String TABLE_DROP_SCHEDULE = "DROP TABLE IF EXISTS SCHEDULE";
    private static final String TABLE_DROP_NOTES = "DROP TABLE IF EXISTS NOTES";

    private SQLiteDatabase mDb = null;

    public DAOBase(Context pContext) {
        super(pContext, NOM, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE_PERSONNE);
        sqLiteDatabase.execSQL(TABLE_CREATE_PROFESSEUR);
        sqLiteDatabase.execSQL(TABLE_CREATE_ELEVE);
        sqLiteDatabase.execSQL(TABLE_CREATE_CLASSE);
        sqLiteDatabase.execSQL(TABLE_CREATE_COURS);
        sqLiteDatabase.execSQL(TABLE_CREATE_HORAIRE);
        sqLiteDatabase.execSQL(TABLE_CREATE_JOURNEE);
        sqLiteDatabase.execSQL(TABLE_CREATE_NOTES);
        sqLiteDatabase.execSQL(TABLE_CREATE_PRESENCE);
        sqLiteDatabase.execSQL(TABLE__CREATE_SCHEDULE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newversion) {
        sqLiteDatabase.execSQL(TABLE_DROP_PERSONNE);
        sqLiteDatabase.execSQL(TABLE_DROP_PROFESSEUR);
        sqLiteDatabase.execSQL(TABLE_DROP_ELEVE);
        sqLiteDatabase.execSQL(TABLE_DROP_CLASSE);
        sqLiteDatabase.execSQL(TABLE_DROP_COURS);
        sqLiteDatabase.execSQL(TABLE_DROP_HORAIRE);
        sqLiteDatabase.execSQL(TABLE_DROP_JOURNEE);
        sqLiteDatabase.execSQL(TABLE_DROP_NOTES);
        sqLiteDatabase.execSQL(TABLE_DROP_PRESENCE);
        sqLiteDatabase.execSQL(TABLE_DROP_SCHEDULE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
        super.onDowngrade(db, oldVersion, newVersion);
    }

    protected SQLiteDatabase open() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = getWritableDatabase();
        return mDb;
    }

    private SQLiteDatabase read() {
        mDb = getReadableDatabase();
        return mDb;
    }

    public void close() {
        //quelques soit l'etat de la base ouverte ou pas on s'assure de pouvoir le fermer
        read();
        mDb.close();
    }

    protected SQLiteDatabase getDb() {
        return mDb;
    }
}
