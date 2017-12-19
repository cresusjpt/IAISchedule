package com.saltechdigital.iaischedule.database.journee;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jeanpaul Tossou on 28/11/2017.
 */

public class TJournee implements Parcelable {

    private int IDJOURNEE;
    private String NOMJOUR;

    public TJournee() {
    }

    public TJournee(int IDJOURNEE, String NOMJOUR) {
        this.IDJOURNEE = IDJOURNEE;
        this.NOMJOUR = NOMJOUR;
    }

    public TJournee(String NOMJOUR) {
        this.NOMJOUR = NOMJOUR;
    }

    protected TJournee(Parcel in) {
        IDJOURNEE = in.readInt();
        NOMJOUR = in.readString();
    }

    public static final Creator<TJournee> CREATOR = new Creator<TJournee>() {
        @Override
        public TJournee createFromParcel(Parcel in) {
            return new TJournee(in);
        }

        @Override
        public TJournee[] newArray(int size) {
            return new TJournee[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(IDJOURNEE);
        parcel.writeString(NOMJOUR);
    }

    @Override
    public String toString() {
        return "TJournee{" +
                "IDJOURNEE=" + IDJOURNEE +
                ", NOMJOUR='" + NOMJOUR + '\'' +
                '}';
    }

    public int getIDJOURNEE() {
        return IDJOURNEE;
    }

    public void setIDJOURNEE(int IDJOURNEE) {
        this.IDJOURNEE = IDJOURNEE;
    }

    public String getNOMJOUR() {
        return NOMJOUR;
    }

    public void setNOMJOUR(String NOMJOUR) {
        this.NOMJOUR = NOMJOUR;
    }
}
