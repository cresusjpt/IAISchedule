package com.saltechdigital.iaischedule.database.professeur;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Jeanpaul Tossou on 14/01/2017.
 */

public class TProfesseur implements Parcelable {

    private int IDPROF;
    private int IDPERSONNE;

    public TProfesseur(int IDPROF, int IDPERSONNE) {
        this.IDPROF = IDPROF;
        this.IDPERSONNE = IDPERSONNE;
    }

    public TProfesseur() {
    }

    public TProfesseur(JSONObject jsonObject) {
        this.IDPROF = jsonObject.optInt("IDPROF");
        this.IDPERSONNE = jsonObject.optInt("IDPERSONNE");
    }

    protected TProfesseur(Parcel in) {
        IDPROF = in.readInt();
        IDPERSONNE = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(IDPROF);
        dest.writeInt(IDPERSONNE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TProfesseur> CREATOR = new Creator<TProfesseur>() {
        @Override
        public TProfesseur createFromParcel(Parcel in) {
            return new TProfesseur(in);
        }

        @Override
        public TProfesseur[] newArray(int size) {
            return new TProfesseur[size];
        }
    };

    public int getIDPROF() {
        return IDPROF;
    }

    public void setIDPROF(int IDPROF) {
        this.IDPROF = IDPROF;
    }

    public int getIDPERSONNE() {
        return IDPERSONNE;
    }

    public void setIDPERSONNE(int IDPERSONNE) {
        this.IDPERSONNE = IDPERSONNE;
    }

    @Override
    public String toString() {
        return "TProfesseur{" +
                "IDPROF=" + IDPROF +
                ", IDPERSONNE=" + IDPERSONNE +
                '}';
    }
}
