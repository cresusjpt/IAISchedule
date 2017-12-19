package com.saltechdigital.iaischedule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.saltechdigital.iaischedule.adapter.MyAdapterControlePresence;
import com.saltechdigital.iaischedule.asyncTask.PresenceTask;
import com.saltechdigital.iaischedule.constantandenum.CommonMethods;
import com.saltechdigital.iaischedule.database.cours.TCours;
import com.saltechdigital.iaischedule.database.eleve.TEleve;
import com.saltechdigital.iaischedule.database.eleve.TEleveDAO;
import com.saltechdigital.iaischedule.database.personne.TPersonne;
import com.saltechdigital.iaischedule.database.personne.TPersonneDAO;
import com.saltechdigital.iaischedule.database.presence.TPresence;
import com.saltechdigital.iaischedule.database.presence.TPresenceDAO;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControlePresenceActivity extends AppCompatActivity {

    private Context context;
    private TEleveDAO eleveDAO;
    private TPersonneDAO personneDAO;
    private TPresenceDAO presenceDAO;

    private TCours coursActu;
    private MyAdapterControlePresence myAdapterControlePresence;
    private PresenceTask presenceTask;

    @Override
    protected void onDestroy() {
        eleveDAO.close();
        personneDAO.close();
        presenceDAO.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        DataBind();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_presence);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        initializeViews();
        coursActu = getIntent().getParcelableExtra("cour");

        DataBind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.iaischedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            alertDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choisir l'heure du cours");
        builder.setCancelable(true);
        builder.setIcon(R.mipmap.ic_launcher_round);

        builder.setItems(new CharSequence[]{"Premiere heure 07h30 - 09h30", "Deuxieme heure 10h - 12h", "Troisieme heure 13h - 15h", "Quatrieme heure 15h15 - 17h15"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                List<TPresence> presenceListe = myAdapterControlePresence.getPresenceList();
                for (TPresence pre : presenceListe) {
                    pre.setIDHEURE(i + 1);
                    pre.setDATEPRESENCE(CommonMethods.getCurrentWebDate());
                }

                if (!presenceDAO.existeDateHeure(CommonMethods.getCurrentWebDate(), i + 1)) {
                    presenceDAO.ajouterListe(presenceListe);

                    for (TPresence presence : presenceListe) {

                        presenceTask = new PresenceTask(context, new PresenceTask.IdentificationCallback() {
                            @Override
                            public void onSuccess(String message) {
                                //Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(context, "Une erreur s'est produite. Réssayer plus tard.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        presenceTask.execute(presence.getTYPEPRESENCE(), String.valueOf(presence.getIDELEVE())
                                , String.valueOf(presence.getIDPERSONNE())
                                , String.valueOf(presence.getIDCOURS())
                                , String.valueOf(presence.getIDHEURE())
                                , presence.getDATEPRESENCE());

                        presenceTask = null;
                    }

                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("patienter quelques secondes...");
                    progressDialog.setIcon(R.mipmap.logo_round);
                    progressDialog.show();


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.setMessage("Enregistrement effectué avec succès");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(context, IAISchedule.class));
                                    finish();
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                        }
                    }, 4000);
                } else {
                    Toast.makeText(context, "Vous avez déja effectué le contrôle de présence de cours pour cette heure de la journée", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.create().show();
    }

    private void initializeViews() {
        eleveDAO = new TEleveDAO(context);
        personneDAO = new TPersonneDAO(context);
        presenceDAO = new TPresenceDAO(context);
    }

    private void DataBind() {

        List<TEleve> eleveList;
        List<TPersonne> personneList = new ArrayList<>();
        List<TPresence> presenceList = new ArrayList<>();

        eleveList = eleveDAO.selectionnerList();
        for (TEleve eleve : eleveList) {
            personneList.add(personneDAO.selectionner(eleve.getIDPERSONNE()));
        }

        for (TEleve eleve : eleveList) {
            presenceList.add(new TPresence("P", eleve.getIDELEVE(), eleve.getIDPERSONNE(), coursActu.getIDCOURS()));
        }

        RecyclerView rv = this.findViewById(R.id.rv_controlePresence);
        assert rv != null;
        rv.setLayoutManager(new LinearLayoutManager(context));
        myAdapterControlePresence = new MyAdapterControlePresence(context, eleveList, personneList, presenceList);

        rv.setAdapter(myAdapterControlePresence);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

}
