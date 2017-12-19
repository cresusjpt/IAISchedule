package com.saltechdigital.iaischedule;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.saltechdigital.iaischedule.asyncTask.CheckPresenceTask;
import com.saltechdigital.iaischedule.constantandenum.CommonMethods;
import com.saltechdigital.iaischedule.constantandenum.Helper;
import com.saltechdigital.iaischedule.constantandenum.NotificationManage;
import com.saltechdigital.iaischedule.database.DAOBase;
import com.saltechdigital.iaischedule.database.cours.TCoursDAO;
import com.saltechdigital.iaischedule.database.horaire.THoraire;
import com.saltechdigital.iaischedule.database.horaire.THoraireDAO;
import com.saltechdigital.iaischedule.database.journee.TJournee;
import com.saltechdigital.iaischedule.database.journee.TJourneeDAO;
import com.saltechdigital.iaischedule.database.personne.TPersonne;
import com.saltechdigital.iaischedule.database.personne.TPersonneDAO;
import com.saltechdigital.iaischedule.database.presence.TPresence;
import com.saltechdigital.iaischedule.slidingTab.SlidingTabLayout;
import com.saltechdigital.iaischedule.slidingTab.ViewPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IAISchedule extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager pager;
    private Context context;

    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"COURS", "SCHEDULE"};
    private List<Integer> listIdCours;
    int Numboftabs = 2;

    private String idCC;

    private THoraireDAO horaireDAO;
    private TJourneeDAO journeeDAO;
    private TPersonneDAO personneDAO;
    private TCoursDAO coursDAO;

    private String repertoire, methode;
    private File dir;

    public static final String SAUVEGARDE_FILE_PERSONNE = "sauvegardePersonne.schedule";
   /* public static final String SAUVEGARDE_FILE_PROFESSEUR = "sauvegardeProfesseur.schedule";
    public static final String SAUVEGARDE_FILE_ELEVE = "sauvegardeEleve.schedule";
    public static final String SAUVEGARDE_FILE_CLASSE = "sauvegardeClasse.schedule";
    public static final String SAUVEGARDE_FILE_COURS = "sauvegardeCours.schedule";
    public static final String SAUVEGARDE_FILE_HORAIRE = "sauvegardeHoraire.schedule";
    public static final String SAUVEGARDE_FILE_JOURNEE = "sauvegardeJournee.schedule";
    public static final String SAUVEGARDE_FILE_NOTES = "sauvegardeNotes.schedule";
    public static final String SAUVEGARDE_FILE_PRESENCE = "sauvegardePresence.schedule";
    public static final String SAUVEGARDE_FILE_SCHEDULE = "sauvegardeSchedule.schedule";*/

    @Override
    protected void onDestroy() {
        horaireDAO.close();
        journeeDAO.close();
        personneDAO.close();
        coursDAO.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iaischedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        initializeViews();
        listIdCours = new ArrayList<>();

        repertoire = "Iaischeduler";
        dir = getDir(repertoire, MODE_APPEND);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        methode = preferences.getString("METHODE", "ELEVE");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = findViewById(R.id.tabs);

        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                                       @TargetApi(Build.VERSION_CODES.M)
                                       @Override
                                       public int getIndicatorColor(int position) {
                                           return getResources().getColor(R.color.colorPrimaryDark);
                                       }
                                   }
        );

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        if (horaireDAO.taille() == 0 && journeeDAO.taille() == 0) {
            verifHoraireAndJournee();
        }

        if (methode.equals("PROFESSEUR")) {
            listIdCours = coursDAO.getAllId();
            int hasard = Hasard(listIdCours.size());
            Log.d("JEANPAUL", "Hasard " + hasard);
            checkControlePresence(context, "ONCREATE", "" + listIdCours.get(hasard));
        }

    }

    static int Hasard(int n) {
        return (int) (Math.random() * n);
    }

    public void checkControlePresence(final Context context, final String message, @Nullable final String idCours) {
        idCC = idCours;

        CheckPresenceTask checkPresenceTask = new CheckPresenceTask(context, new CheckPresenceTask.CheckPresenceTaskCallback() {
            @Override
            public void onFind(JSONArray jsonArray) {
                List<TPresence> presenceList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        TPresence presence = new TPresence(jsonObject);
                        presenceList.add(presence);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                new NotificationManage().notificateValid(context, presenceList);
            }

            @Override
            public void notFind() {
                if (!message.equals("ONCREATE")) {
                    Toast.makeText(context, "Pas de nouveau contrôle de présence disponible pour ce cours", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError() {
                //Toast.makeText(context,"Une erreur s'est produite. Réssayez ulterieurement",Toast.LENGTH_SHORT).show();
            }
        });

        assert idCC != null;
        Log.d("JEANPAUL", idCC + " " + CommonMethods.getCurrentWebDate());
        checkPresenceTask.execute(idCC, CommonMethods.getCurrentWebDate());

        if (message.equals("ONCREATE")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int hasarde = Hasard(listIdCours.size());
                    Log.d("JEANPAUL", "Hasard " + hasarde);
                    checkControlePresence(context, "ONCREATE", "" + listIdCours.get(hasarde));
                }
            }, 60000);
        }
    }

    private void initializeViews() {
        horaireDAO = new THoraireDAO(context);
        journeeDAO = new TJourneeDAO(context);
        personneDAO = new TPersonneDAO(context);
        coursDAO = new TCoursDAO(context);
    }

    private void verifHoraireAndJournee() {
        List<THoraire> horaireList = new ArrayList<>();
        List<TJournee> journeeList = new ArrayList<>();

        horaireList.add(new THoraire(1, "07h30 - 09h30"));
        horaireList.add(new THoraire(2, "10h00 - 12h00"));
        horaireList.add(new THoraire(3, "13h00 - 15h00"));
        horaireList.add(new THoraire(4, "15h15 - 17h15"));

        journeeList.add(new TJournee(1, "LUNDI"));
        journeeList.add(new TJournee(2, "MARDI"));
        journeeList.add(new TJournee(3, "MERCREDI"));
        journeeList.add(new TJournee(4, "JEUDI"));
        journeeList.add(new TJournee(5, "VENDREDI"));
        journeeList.add(new TJournee(6, "SAMEDI"));

        horaireDAO.ajouterListe(horaireList);
        journeeDAO.ajouterListe(journeeList);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cours, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_backup_restore) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Sauvegarde et restauration");
            builder.setCancelable(true);
            final ProgressDialog bb = new ProgressDialog(context);
            builder.setItems(new CharSequence[]{"Sauvegarder", "Restaurer"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case 0:
                            bb.setIndeterminate(true);
                            bb.setTitle("Sauvegarde de la base de données en cours");
                            bb.setMessage("patientez quelques secondes...");
                            bb.setIcon(R.drawable.ic_action_backup);
                            bb.setCancelable(false);
                            bb.show();

                            JSONObject jsonObject = new JSONObject();

                            //Sauvegarde de personne
                            for (TPersonne personne : new TPersonneDAO(context).selectionnerList()) {
                                try {
                                    jsonObject.put(TPersonneDAO.KEY, personne.getIDPERSONNE());
                                    jsonObject.put(TPersonneDAO.NOM_PERSONNE, personne.getNOMPERSONNE());
                                    jsonObject.put(TPersonneDAO.PRENOM_PERSONNE, personne.getPRENOMPERSONNE());
                                    jsonObject.put(TPersonneDAO.TELEPHONE_PERSONNE, personne.getTELEPHONEPERSONNE());
                                    jsonObject.put(TPersonneDAO.SEXE_PERSONNE, personne.getSEXEPERSONNE());

                                    Helper.ecrireFichierI(dir, SAUVEGARDE_FILE_PERSONNE, jsonObject.toString());
                                    Helper.ecrireFichierE(repertoire, SAUVEGARDE_FILE_PERSONNE, jsonObject.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    bb.dismiss();
                                }
                            }, 5000);

                            bb.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    Toast.makeText(IAISchedule.this, "Sauvegarde effectuée avec succès", Toast.LENGTH_SHORT).show();
                                }
                            });

                            break;
                        case 1:
                            //List<TPersonne> personneList = new ArrayList<>();

                            bb.setIndeterminate(true);
                            bb.setTitle("Restauration de la base de donnée en cours");
                            bb.setMessage("patientez quelques secondes...");
                            bb.setIcon(R.drawable.ic_action_backup);
                            bb.setCancelable(false);
                            bb.show();

                            try {
                                JSONObject restore_json = new JSONObject(Helper.lireFichierI(dir, SAUVEGARDE_FILE_PERSONNE));

                                /*for (int j=0;i<restore_json.length();j++){
                                    personneList.add(new TPersonne(restore_json.getInt("IDPERSONNE")));
                                }*/

                                //restauration de la base de données

                                //DAOBase.setVERSION(DAOBase.getVERSION()+1); //incrementation de la base pour drop la base
                                //personneDAO.ajouterListe(personneList);

                            } catch (JSONException e) {
                                Log.d("JEANPAUL", "stackTrace : " + e.getMessage());
                                e.printStackTrace();
                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    bb.dismiss();
                                }
                            }, 5000);

                            bb.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    Toast.makeText(IAISchedule.this, "Restauration effectuée avec succès", Toast.LENGTH_SHORT).show();
                                }
                            });

                            break;
                    }
                }
            });
            builder.create().show();
            return true;
        } else if (id == R.id.action_actualiser) {
            checkControlePresence(context, "ONCREATE", null);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if (methode.equals("PROFESSEUR")) {
            listIdCours = coursDAO.getAllId();
            int hasard = Hasard(listIdCours.size());
            Log.d("JEANPAUL", "Hasard " + hasard);
            checkControlePresence(context, "ONCREATE", "" + listIdCours.get(hasard));
        }
        super.onResume();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_setting:
                startActivity(new Intent(IAISchedule.this, SettingsActivity.class));
                finish();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
