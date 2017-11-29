package com.saltechdigital.iaischedule.database.admin;

/**
 * Created by Jeanpaul Tossou on 14/01/2017.
 */

public class TAdmin {
    private int IDADMIN;
    private int IDPERSONNE;
    private String NOMADMIN;

    public TAdmin(int IDADMIN) {
        this.IDADMIN = IDADMIN;
    }

    public TAdmin(String NOMADMIN) {
        this.NOMADMIN = NOMADMIN;
    }

    public TAdmin(int IDADMIN, int IDPERSONNE, String NOMADMIN) {
        this.IDADMIN = IDADMIN;
        this.IDPERSONNE = IDPERSONNE;
        this.NOMADMIN = NOMADMIN;
    }

    public int getIDADMIN() {
        return IDADMIN;
    }

    public void setIDADMIN(int IDADMIN) {
        this.IDADMIN = IDADMIN;
    }

    public int getIDPERSONNE() {
        return IDPERSONNE;
    }

    public void setIDPERSONNE(int IDPERSONNE) {
        this.IDPERSONNE = IDPERSONNE;
    }

    public String getNOMADMIN() {
        return NOMADMIN;
    }

    public void setNOMADMIN(String NOMADMIN) {
        this.NOMADMIN = NOMADMIN;
    }
}
