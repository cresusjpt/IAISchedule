package com.saltechdigital.iaischedule.database.note;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Jeanpaul Tossou on 30/12/2016.
 */

public class TNotes implements Parcelable {

    private int IDNOTE;
    private int IDCOURS;
    private int IDHEURE;
    private int IDJOURNEE;
    private String NOTES;
    private String DATENOTES;

    public TNotes(int IDNOTE, int IDCOURS, int IDHEURE, int IDJOURNEE, String NOTES, String DATENOTES) {
        this.IDNOTE = IDNOTE;
        this.IDCOURS = IDCOURS;
        this.IDHEURE = IDHEURE;
        this.IDJOURNEE = IDJOURNEE;
        this.NOTES = NOTES;
        this.DATENOTES = DATENOTES;
    }

    public TNotes() {
    }

    public TNotes(String NOTES) {
        this.NOTES = NOTES;
    }

    protected TNotes(Parcel in) {
        IDNOTE = in.readInt();
        IDCOURS = in.readInt();
        IDHEURE = in.readInt();
        IDJOURNEE = in.readInt();
        NOTES = in.readString();
        DATENOTES = in.readString();
    }

    public static final Creator<TNotes> CREATOR = new Creator<TNotes>() {
        @Override
        public TNotes createFromParcel(Parcel in) {
            return new TNotes(in);
        }

        @Override
        public TNotes[] newArray(int size) {
            return new TNotes[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(IDNOTE);
        parcel.writeInt(IDCOURS);
        parcel.writeInt(IDHEURE);
        parcel.writeInt(IDJOURNEE);
        parcel.writeString(NOTES);
        parcel.writeString(DATENOTES);
    }

    public int getIDNOTE() {
        return IDNOTE;
    }

    public void setIDNOTE(int IDNOTE) {
        this.IDNOTE = IDNOTE;
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

    public int getIDJOURNEE() {
        return IDJOURNEE;
    }

    public void setIDJOURNEE(int IDJOURNEE) {
        this.IDJOURNEE = IDJOURNEE;
    }

    public String getNOTES() {
        return NOTES;
    }

    public void setNOTES(String NOTES) {
        this.NOTES = NOTES;
    }

    public String getDATENOTES() {
        return DATENOTES;
    }

    public void setDATENOTES(String DATENOTES) {
        this.DATENOTES = DATENOTES;
    }

    @Override
    public String toString() {
        return "TNotes{" +
                "IDNOTE=" + IDNOTE +
                ", IDCOURS=" + IDCOURS +
                ", IDHEURE=" + IDHEURE +
                ", IDJOURNEE=" + IDJOURNEE +
                ", NOTES='" + NOTES + '\'' +
                '}';
    }
}
