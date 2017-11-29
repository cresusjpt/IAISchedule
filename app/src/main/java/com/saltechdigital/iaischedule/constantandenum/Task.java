package com.saltechdigital.iaischedule.constantandenum;


/**
 * Created by Jeanpaul Tossou on 10/07/2017.
 */

public enum Task {
    // Il faut appeler l'un des constructeurs déclarés plus bas :
    URL("http://iaidiscuss.alwaysdata.net/iaidiscuss", false),

    CREATIONLOGINFILE("inscription.php", false),
    ENREGFILE("enregistrer.php", false),
    LOGINFILE("verif.php", false, "tropical"),
    RECUPSTATIONFILE("recupStation.php", false),
    RECUPTYPECUVEFILE("recupTypeCuve.php"),
    MAKEDEPOTFILE("makeDepot.php", false); // <- NB: le point-virgule pour mettre fin à la liste des constantes !

    // Membres :
    private final String environnement;
    private final String nom;
    private final boolean domestique;

    Task(String nom) {
        this(nom, false);
    }

    Task(String nom, boolean domestique) {
        this(nom, domestique, null);
    }

    Task(String nom, boolean domestique, String environnement) {
        this.nom = nom;
        this.domestique = domestique;
        this.environnement = environnement;
    }

    public String getNom() {
        return this.nom;
    }

    public String getEnvironnement() {
        return this.environnement;
    }

    public boolean isDomestique() {
        return this.domestique;
    }
}