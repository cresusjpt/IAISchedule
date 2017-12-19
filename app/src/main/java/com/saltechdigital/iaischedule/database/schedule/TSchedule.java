package com.saltechdigital.iaischedule.database.schedule;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Jeanpaul Tossou on 28/11/2017.
 */

public class TSchedule implements Parcelable {

    private int IDJOURNEE;
    private int IDHEURE;
    private int IDCOURS;

    public TSchedule() {
    }

    protected TSchedule(Parcel in) {
        IDJOURNEE = in.readInt();
        IDHEURE = in.readInt();
        IDCOURS = in.readInt();
    }

    public TSchedule(JSONObject jsonObject) {
        this.IDJOURNEE = jsonObject.optInt("IDJOURNEE");
        this.IDHEURE = jsonObject.optInt("IDHEURE");
        this.IDCOURS = jsonObject.optInt("IDCOURS");
    }

    public static final Creator<TSchedule> CREATOR = new Creator<TSchedule>() {
        @Override
        public TSchedule createFromParcel(Parcel in) {
            return new TSchedule(in);
        }

        @Override
        public TSchedule[] newArray(int size) {
            return new TSchedule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(IDJOURNEE);
        parcel.writeInt(IDHEURE);
        parcel.writeInt(IDCOURS);
    }


    public int getIDJOURNEE() {
        return IDJOURNEE;
    }

    public void setIDJOURNEE(int IDJOURNEE) {
        this.IDJOURNEE = IDJOURNEE;
    }

    public int getIDHEURE() {
        return IDHEURE;
    }

    public void setIDHEURE(int IDHEURE) {
        this.IDHEURE = IDHEURE;
    }

    public int getIDCOURS() {
        return IDCOURS;
    }

    public void setIDCOURS(int IDCOURS) {
        this.IDCOURS = IDCOURS;
    }

    @Override
    public String toString() {
        return "TSchedule{" +
                ", IDJOURNEE=" + IDJOURNEE +
                ", IDHEURE=" + IDHEURE +
                ", IDCOURS=" + IDCOURS +
                '}';
    }
}
