package com.saltechdigital.iaischedule.database.cours;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Jeanpaul Tossou on 30/12/2016.
 */

public class TCours implements Parcelable {

    private int IDCOURS;
    private int IDCLASSE;
    private int IDPROF;
    private int IDPERSONNE;
    private String NOMCOURS;

    public TCours(int IDCOURS, int IDCLASSE, int IDPROF, int IDPERSONNE, String NOMCOURS) {
        this.IDCOURS = IDCOURS;
        this.IDCLASSE = IDCLASSE;
        this.IDPROF = IDPROF;
        this.IDPERSONNE = IDPERSONNE;
        this.NOMCOURS = NOMCOURS;
    }

    public TCours() {
    }

    protected TCours(Parcel in) {
        IDCOURS = in.readInt();
        IDCLASSE = in.readInt();
        IDPROF = in.readInt();
        IDPERSONNE = in.readInt();
        NOMCOURS = in.readString();
    }

    public static final Creator<TCours> CREATOR = new Creator<TCours>() {
        @Override
        public TCours createFromParcel(Parcel in) {
            return new TCours(in);
        }

        @Override
        public TCours[] newArray(int size) {
            return new TCours[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(IDCOURS);
        parcel.writeInt(IDCLASSE);
        parcel.writeInt(IDPROF);
        parcel.writeInt(IDPERSONNE);
        parcel.writeString(NOMCOURS);
    }

    public int getIDCOURS() {
        return IDCOURS;
    }

    public void setIDCOURS(int IDCOURS) {
        this.IDCOURS = IDCOURS;
    }

    public int getIDCLASSE() {
        return IDCLASSE;
    }

    public void setIDCLASSE(int IDCLASSE) {
        this.IDCLASSE = IDCLASSE;
    }

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

    public String getNOMCOURS() {
        return NOMCOURS;
    }

    public void setNOMCOURS(String NOMCOURS) {
        this.NOMCOURS = NOMCOURS;
    }

    @Override
    public String toString() {
        return "TCours{" +
                "IDCOURS=" + IDCOURS +
                ", IDCLASSE=" + IDCLASSE +
                ", IDPROF=" + IDPROF +
                ", IDPERSONNE=" + IDPERSONNE +
                ", NOMCOURS='" + NOMCOURS + '\'' +
                '}';
    }
}
