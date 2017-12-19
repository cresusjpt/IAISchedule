package com.saltechdigital.iaischedule.database.eleve;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Jeanpaul Tossou on 28/11/2017.
 */

public class TEleve implements Parcelable {

    private int IDELEVE;
    private int IDPERSONNE;
    private int IDCLASSE;
    private int RESPONSABLE;

    public int getRESPONSABLE() {
        return RESPONSABLE;
    }

    public void setRESPONSABLE(int RESPONSABLE) {
        this.RESPONSABLE = RESPONSABLE;
    }


    public TEleve(int IDELEVE, int IDPERSONNE, int IDCLASSE) {
        this.IDELEVE = IDELEVE;
        this.IDPERSONNE = IDPERSONNE;
        this.IDCLASSE = IDCLASSE;
    }

    public TEleve() {

    }

    public TEleve(int IDELEVE, int IDPERSONNE) {
        this.IDELEVE = IDELEVE;
        this.IDPERSONNE = IDPERSONNE;
    }

    protected TEleve(Parcel in) {
        IDELEVE = in.readInt();
        IDPERSONNE = in.readInt();
        IDCLASSE = in.readInt();
        RESPONSABLE = in.readInt();
    }

    public TEleve(JSONObject jsonObject) {

        this.IDELEVE = jsonObject.optInt("IDELEVE");
        this.IDPERSONNE = jsonObject.optInt("IDPERSONNE");
        this.IDCLASSE = jsonObject.optInt("IDCLASSE");
        this.RESPONSABLE = jsonObject.optInt("RESPONSABLE");
    }

    public static final Creator<TEleve> CREATOR = new Creator<TEleve>() {
        @Override
        public TEleve createFromParcel(Parcel in) {
            return new TEleve(in);
        }

        @Override
        public TEleve[] newArray(int size) {
            return new TEleve[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(IDELEVE);
        parcel.writeInt(IDPERSONNE);
        parcel.writeInt(IDCLASSE);
        parcel.writeInt(RESPONSABLE);
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

    public int getIDCLASSE() {
        return IDCLASSE;
    }

    public void setIDCLASSE(int IDCLASSE) {
        this.IDCLASSE = IDCLASSE;
    }

    @Override
    public String toString() {
        return "TEleve{" +
                "IDELEVE=" + IDELEVE +
                ", IDPERSONNE=" + IDPERSONNE +
                ", IDCLASSE=" + IDCLASSE +
                '}';
    }
}
