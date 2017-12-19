package com.saltechdigital.iaischedule.database.presence;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Jeanpaul Tossou on 28/11/2017.
 */

public class TPresence implements Parcelable {

    private String TYPEPRESENCE;
    private int IDELEVE;
    private int IDPERSONNE;
    private int IDCOURS;
    private int IDHEURE;
    private String DATEPRESENCE;

    public TPresence(JSONObject jsonObject) {

        this.TYPEPRESENCE = jsonObject.optString("typepresence");
        this.IDELEVE = jsonObject.optInt("IDELEVE");
        this.IDPERSONNE = jsonObject.optInt("IDPERSONNE");
        this.IDCOURS = jsonObject.optInt("IDCOURS");
        this.IDHEURE = jsonObject.optInt("IDHEURE");
        this.DATEPRESENCE = jsonObject.optString("DATEPRESENCE");
    }


    public TPresence(int IDELEVE, int IDPERSONNE, int IDCOURS) {
        this.IDELEVE = IDELEVE;
        this.IDPERSONNE = IDPERSONNE;
        this.IDCOURS = IDCOURS;
    }

    public TPresence(int IDELEVE, int IDCOURS) {

        this.IDELEVE = IDELEVE;
        this.IDCOURS = IDCOURS;
    }

    public TPresence() {
    }

    public TPresence(String TYPEPRESENCE, int IDELEVE, int IDPERSONNE, int IDCOURS) {
        this.TYPEPRESENCE = TYPEPRESENCE;
        this.IDELEVE = IDELEVE;
        this.IDPERSONNE = IDPERSONNE;
        this.IDCOURS = IDCOURS;
    }

    public TPresence(String TYPEPRESENCE, int IDELEVE, int IDPERSONNE, int IDCOURS, int IDHEURE) {
        this.TYPEPRESENCE = TYPEPRESENCE;
        this.IDELEVE = IDELEVE;
        this.IDPERSONNE = IDPERSONNE;
        this.IDCOURS = IDCOURS;
        this.IDHEURE = IDHEURE;
    }

    public TPresence(int IDELEVE, int IDPERSONNE, int IDCOURS, int IDHEURE) {
        this.IDELEVE = IDELEVE;
        this.IDPERSONNE = IDPERSONNE;
        this.IDCOURS = IDCOURS;
        this.IDHEURE = IDHEURE;
    }

    public TPresence(String TYPEPRESENCE) {
        this.TYPEPRESENCE = TYPEPRESENCE;
    }


    protected TPresence(Parcel in) {
        TYPEPRESENCE = in.readString();
        IDELEVE = in.readInt();
        IDPERSONNE = in.readInt();
        IDCOURS = in.readInt();
        IDHEURE = in.readInt();
    }

    public static final Creator<TPresence> CREATOR = new Creator<TPresence>() {
        @Override
        public TPresence createFromParcel(Parcel in) {
            return new TPresence(in);
        }

        @Override
        public TPresence[] newArray(int size) {
            return new TPresence[size];
        }
    };

    public TPresence(int IDCOURS) {
        this.IDCOURS = IDCOURS;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(TYPEPRESENCE);
        parcel.writeInt(IDELEVE);
        parcel.writeInt(IDPERSONNE);
        parcel.writeInt(IDCOURS);
        parcel.writeInt(IDHEURE);
    }

    public String getTYPEPRESENCE() {
        return TYPEPRESENCE;
    }

    public void setTYPEPRESENCE(String TYPEPRESENCE) {
        this.TYPEPRESENCE = TYPEPRESENCE;
    }

    public int getIDELEVE() {
        return IDELEVE;
    }

    public void setIDELEVE(int IDELEVE) {
        this.IDELEVE = IDELEVE;
    }

    public int getIDPERSONNE() {
        return IDPERSONNE;
    }

    public void setIDPERSONNE(int IDPERSONNE) {
        this.IDPERSONNE = IDPERSONNE;
    }

    public int getIDCOURS() {
        return IDCOURS;
    }

    public void setIDCOURS(int IDCOURS) {
        this.IDCOURS = IDCOURS;
    }

    public int getIDHEURE() {
        return IDHEURE;
    }

    public void setIDHEURE(int IDHEURE) {
        this.IDHEURE = IDHEURE;
    }

    public String getDATEPRESENCE() {
        return DATEPRESENCE;
    }

    public void setDATEPRESENCE(String DATEPRESENCE) {
        this.DATEPRESENCE = DATEPRESENCE;
    }

    @Override
    public String toString() {
        return "TPresence{" +
                "TYPEPRESENCE='" + TYPEPRESENCE + '\'' +
                ", IDELEVE=" + IDELEVE +
                ", IDPERSONNE=" + IDPERSONNE +
                ", IDCOURS=" + IDCOURS +
                ", IDHEURE=" + IDHEURE +
                '}';
    }
}
