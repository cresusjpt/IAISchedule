package com.saltechdigital.iaischedule.database.classe;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Jeanpaul Tossou on 30/12/2016.
 */

public class TClasse implements Parcelable {

    private int IDCLASSE;
    private String NOMCLASSE;

    //on recupere l'idconvers ici pour eviter de faire d'autres requetes --pas conforme au regles mais bon...


    public TClasse() {
    }

    public TClasse(int IDCLASSE, String NOMCLASSE) {
        this.IDCLASSE = IDCLASSE;
        this.NOMCLASSE = NOMCLASSE;
    }

    public TClasse(String NOMCLASSE) {
        this.NOMCLASSE = NOMCLASSE;
    }

    public int getIDCLASSE() {
        return IDCLASSE;
    }

    public void setIDCLASSE(int IDCLASSE) {
        this.IDCLASSE = IDCLASSE;
    }

    public String getNOMCLASSE() {
        return NOMCLASSE;
    }

    public void setNOMCLASSE(String NOMCLASSE) {
        this.NOMCLASSE = NOMCLASSE;
    }

    protected TClasse(Parcel in) {
        IDCLASSE = in.readInt();
        NOMCLASSE = in.readString();
    }

    public TClasse(JSONObject jsonObject) {
        this.IDCLASSE = jsonObject.optInt("IDCLASSE");
        this.NOMCLASSE = jsonObject.optString("NOMCLASSE");
    }

    public static final Creator<TClasse> CREATOR = new Creator<TClasse>() {
        @Override
        public TClasse createFromParcel(Parcel in) {
            return new TClasse(in);
        }

        @Override
        public TClasse[] newArray(int size) {
            return new TClasse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(IDCLASSE);
        parcel.writeString(NOMCLASSE);
    }
}
