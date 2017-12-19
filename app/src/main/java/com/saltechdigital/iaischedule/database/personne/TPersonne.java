package com.saltechdigital.iaischedule.database.personne;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Jeanpaul Tossou on 30/12/2016.
 */

public class TPersonne implements Parcelable {

    private int IDPERSONNE;
    private String NOMPERSONNE;
    private String PRENOMPERSONNE;
    private String TELEPHONEPERSONNE;
    private String SEXEPERSONNE;
    private String PHOTOPERSONNE;

    public TPersonne(int IDPERSONNE) {
        this.IDPERSONNE = IDPERSONNE;
    }

    public TPersonne(int IDPERSONNE, String NOMPERSONNE, String PRENOMPERSONNE, String TELEPHONEPERSONNE, String SEXEPERSONNE) {
        this.IDPERSONNE = IDPERSONNE;
        this.NOMPERSONNE = NOMPERSONNE;
        this.PRENOMPERSONNE = PRENOMPERSONNE;
        this.TELEPHONEPERSONNE = TELEPHONEPERSONNE;
        this.SEXEPERSONNE = SEXEPERSONNE;
    }

    public TPersonne(String NOMPERSONNE, String PRENOMPERSONNE, String TELEPHONEPERSONNE, String SEXEPERSONNE) {
        this.NOMPERSONNE = NOMPERSONNE;
        this.PRENOMPERSONNE = PRENOMPERSONNE;
        this.TELEPHONEPERSONNE = TELEPHONEPERSONNE;
        this.SEXEPERSONNE = SEXEPERSONNE;
    }

    protected TPersonne(Parcel in) {
        IDPERSONNE = in.readInt();
        NOMPERSONNE = in.readString();
        PRENOMPERSONNE = in.readString();
        TELEPHONEPERSONNE = in.readString();
        SEXEPERSONNE = in.readString();
    }

    public TPersonne() {
    }

    public TPersonne(JSONObject jsonObject) {

        this.IDPERSONNE = jsonObject.optInt("IDPERSONNE");
        this.NOMPERSONNE = jsonObject.optString("NOMPERSONNE");
        this.TELEPHONEPERSONNE = jsonObject.optString("TELEPHONEPERSONNE");
        this.PRENOMPERSONNE = jsonObject.optString("PRENOMPERSONNE");
        this.SEXEPERSONNE = jsonObject.optString("SEXEPERSONNE");
    }

    public static final Creator<TPersonne> CREATOR = new Creator<TPersonne>() {
        @Override
        public TPersonne createFromParcel(Parcel in) {
            return new TPersonne(in);
        }

        @Override
        public TPersonne[] newArray(int size) {
            return new TPersonne[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(IDPERSONNE);
        parcel.writeString(NOMPERSONNE);
        parcel.writeString(PRENOMPERSONNE);
        parcel.writeString(TELEPHONEPERSONNE);
        parcel.writeString(SEXEPERSONNE);
    }

    public int getIDPERSONNE() {
        return IDPERSONNE;
    }

    public void setIDPERSONNE(int IDPERSONNE) {
        this.IDPERSONNE = IDPERSONNE;
    }

    public String getNOMPERSONNE() {
        return NOMPERSONNE;
    }

    public void setNOMPERSONNE(String NOMPERSONNE) {
        this.NOMPERSONNE = NOMPERSONNE;
    }

    public String getPRENOMPERSONNE() {
        return PRENOMPERSONNE;
    }

    public void setPRENOMPERSONNE(String PRENOMPERSONNE) {
        this.PRENOMPERSONNE = PRENOMPERSONNE;
    }

    public String getTELEPHONEPERSONNE() {
        return TELEPHONEPERSONNE;
    }

    public void setTELEPHONEPERSONNE(String TELEPHONEPERSONNE) {
        this.TELEPHONEPERSONNE = TELEPHONEPERSONNE;
    }

    public String getSEXEPERSONNE() {
        return SEXEPERSONNE;
    }

    public String getPHOTOPERSONNE() {
        return PHOTOPERSONNE;
    }

    public void setPHOTOPERSONNE(String PHOTOPERSONNE) {
        this.PHOTOPERSONNE = PHOTOPERSONNE;
    }

    public void setSEXEPERSONNE(String SEXEPERSONNE) {
        this.SEXEPERSONNE = SEXEPERSONNE;
    }

    @Override
    public String toString() {
        return "TPersonne{" +
                "IDPERSONNE=" + IDPERSONNE +
                ", NOMPERSONNE='" + NOMPERSONNE + '\'' +
                ", PRENOMPERSONNE='" + PRENOMPERSONNE + '\'' +
                ", TELEPHONEPERSONNE='" + TELEPHONEPERSONNE + '\'' +
                ", SEXEPERSONNE='" + SEXEPERSONNE + '\'' +
                '}';
    }


}
