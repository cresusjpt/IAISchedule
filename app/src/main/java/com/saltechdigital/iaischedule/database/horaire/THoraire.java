package com.saltechdigital.iaischedule.database.horaire;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jeanpaul Tossou on 28/11/2017.
 */

public class THoraire implements Parcelable {

    private int IDHEURE;
    private String HEURE;

    public THoraire() {
    }

    public THoraire(int IDHEURE, String HEURE) {
        this.IDHEURE = IDHEURE;
        this.HEURE = HEURE;
    }

    public THoraire(String HEURE) {
        this.HEURE = HEURE;
    }

    protected THoraire(Parcel in) {
        IDHEURE = in.readInt();
        HEURE = in.readString();
    }

    public static final Creator<THoraire> CREATOR = new Creator<THoraire>() {
        @Override
        public THoraire createFromParcel(Parcel in) {
            return new THoraire(in);
        }

        @Override
        public THoraire[] newArray(int size) {
            return new THoraire[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(IDHEURE);
        parcel.writeString(HEURE);
    }

    public int getIDHEURE() {
        return IDHEURE;
    }

    public void setIDHEURE(int IDHEURE) {
        this.IDHEURE = IDHEURE;
    }

    public String getHEURE() {
        return HEURE;
    }

    public void setHEURE(String HEURE) {
        this.HEURE = HEURE;
    }

    @Override
    public String toString() {
        return "THoraire{" +
                "IDHEURE=" + IDHEURE +
                ", HEURE='" + HEURE + '\'' +
                '}';
    }
}
