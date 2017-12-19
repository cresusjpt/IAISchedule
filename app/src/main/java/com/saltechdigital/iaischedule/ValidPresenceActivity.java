package com.saltechdigital.iaischedule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.saltechdigital.iaischedule.adapter.MyAdapterValidPresence;
import com.saltechdigital.iaischedule.asyncTask.ValidPresenceTask;
import com.saltechdigital.iaischedule.constantandenum.CommonMethods;
import com.saltechdigital.iaischedule.database.eleve.TEleve;
import com.saltechdigital.iaischedule.database.eleve.TEleveDAO;
import com.saltechdigital.iaischedule.database.personne.TPersonne;
import com.saltechdigital.iaischedule.database.personne.TPersonneDAO;
import com.saltechdigital.iaischedule.database.presence.TPresence;
import com.saltechdigital.iaischedule.database.presence.TPresenceDAO;

import java.util.ArrayList;
import java.util.List;

public class ValidPresenceActivity extends AppCompatActivity {

    private Context context;

    private TEleveDAO eleveDAO;
    private TPersonneDAO personneDAO;
    private TPresenceDAO presenceDAO;
    private MyAdapterValidPresence myAdapterValidPresence;

    private List<TPresence> presenceList;
    private ValidPresenceTask validPresenceTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valid_presence);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenceList = new ArrayList<>();

        context = this;
        initializeViews();

        Intent receiveIntent = getIntent();

        int nombre = receiveIntent.getIntExtra("nombre", 0);

        if (nombre != 0) {
            for (int i = 0; i < nombre; i++) {
                TPresence presence = receiveIntent.getParcelableExtra("presence" + i);
                presenceList.add(presence);
            }
        }

        DataBind();

    }

    private void initializeViews() {
        eleveDAO = new TEleveDAO(context);
        personneDAO = new TPersonneDAO(context);
        presenceDAO = new TPresenceDAO(context);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_schedule_drawer2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_valid) {
            alertDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void alertDialog() {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Validation en cours");
        progressDialog.setMessage("patienter quelques secondes...");
        progressDialog.setIcon(R.mipmap.logo_round);
        progressDialog.show();

        final List<TPresence> presenceLister = myAdapterValidPresence.getPresenceList();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (TPresence presence : presenceLister) {

                    validPresenceTask = new ValidPresenceTask(context, new ValidPresenceTask.IdentificationCallback() {
                        @Override
                        public void onSuccess(String message) {
                            //Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(context, "Une erreur s'est produite. Réssayer plus tard.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    validPresenceTask.execute(presence.getTYPEPRESENCE(), String.valueOf(presence.getIDELEVE())
                            , String.valueOf(presence.getIDPERSONNE())
                            , String.valueOf(presence.getIDCOURS())
                            , String.valueOf(presence.getIDHEURE())
                            , CommonMethods.getCurrentWebDate());

                    validPresenceTask = null;
                }
            }
        }, 3000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage("Controle validé avec succès");

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
    }

    private void DataBind() {

        List<TEleve> eleveList;
        List<TPersonne> personneList = new ArrayList<>();

        assert presenceList != null;

        eleveList = eleveDAO.selectionnerList();

        for (TEleve eleve : eleveList) {
            personneList.add(personneDAO.selectionner(eleve.getIDPERSONNE()));
        }

        RecyclerView rv = this.findViewById(R.id.rv_validPresence);
        assert rv != null;
        rv.setLayoutManager(new LinearLayoutManager(context));
        myAdapterValidPresence = new MyAdapterValidPresence(context, eleveList, personneList, presenceList);

        rv.setAdapter(myAdapterValidPresence);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

}
