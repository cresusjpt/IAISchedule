package com.saltechdigital.iaischedule;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.saltechdigital.iaischedule.adapter.MyAdapterMemo;
import com.saltechdigital.iaischedule.constantandenum.CommonMethods;
import com.saltechdigital.iaischedule.database.cours.TCours;
import com.saltechdigital.iaischedule.database.horaire.THoraire;
import com.saltechdigital.iaischedule.database.journee.TJournee;
import com.saltechdigital.iaischedule.database.note.TNotes;
import com.saltechdigital.iaischedule.database.note.TNotesDAO;
import com.saltechdigital.iaischedule.database.personne.TPersonne;
import com.saltechdigital.iaischedule.database.personne.TPersonneDAO;
import com.saltechdigital.iaischedule.database.schedule.TSchedule;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MemoScheduleActivity extends AppCompatActivity {
    private Context context;

    private TSchedule schedule;
    private TCours cours;
    private TJournee journee;
    private THoraire horaire;
    private TPersonne personne;

    private TextView numMemo;

    private RecyclerView recyclerView;
    private TNotesDAO notesDAO;
    private TPersonneDAO personneDAO;

    private MyAdapterMemo myAdapterMemo;

    private TextView noteText;

    @Override
    protected void onResume() {
        DataBind();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_schedule);
        context = this;
        recyclerView = findViewById(R.id.rv_memo);
        notesDAO = new TNotesDAO(context);
        personneDAO = new TPersonneDAO(context);

        schedule = getIntent().getParcelableExtra("SCHEDULE");
        cours = getIntent().getParcelableExtra("COURS");
        journee = getIntent().getParcelableExtra("JOURNEE");
        horaire = getIntent().getParcelableExtra("HORAIRE");
        personne = getIntent().getParcelableExtra("PERSONNE");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(cours.getNOMCOURS() + " " + horaire.getHEURE());

        numMemo = findViewById(R.id.tv_memoNumero);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String methode = preferences.getString("METHODE", "ELEVE");

        if (methode.equals("PROFESSEUR")) {
            numMemo.setText(MessageFormat.format("{0}\nMobile", personneDAO.selectionnerResponsable().getTELEPHONEPERSONNE()));
        } else {
            numMemo.setText(MessageFormat.format("{0}\nMobile", personne.getTELEPHONEPERSONNE()));
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewInflate = getLayoutInflater().inflate(R.layout.content_controle_presence, null);
                noteText = viewInflate.findViewById(R.id.add_note);

                AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(context);
                alertdialogBuilder.setView(viewInflate);
                alertdialogBuilder.setTitle("Ajout de notes");

                alertdialogBuilder.setPositiveButton("Enreg", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (noteText.getText().toString().trim().length() > 0) {
                            TNotes notes = new TNotes(1, cours.getIDCOURS(), horaire.getIDHEURE(), journee.getIDJOURNEE(), noteText.getEditableText().toString(), CommonMethods.getCurrentDate());
                            notesDAO.ajouter(notes);
                            DataBind();
                        } else {
                            Toast.makeText(context, "Veuillez entrer une note à enregistrer", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alertdialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertdialogBuilder.setCancelable(true);
                alertdialogBuilder.create().show();
            }
        });

        ImageButton ib_call = findViewById(R.id.call);
        ib_call.setBackgroundResource(R.drawable.state_button);
        ib_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri telephone = Uri.parse("tel:" + personne.getTELEPHONEPERSONNE());
                Intent telephoner = new Intent(Intent.ACTION_DIAL, telephone);
                startActivity(telephoner);
            }
        });

        ImageButton ib_chat = findViewById(R.id.chat);
        ib_chat.setBackgroundResource(R.drawable.state_button);
        ib_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri sms = Uri.parse("sms:" + personne.getTELEPHONEPERSONNE());
                Intent chat = new Intent(Intent.ACTION_VIEW, sms);
                startActivity(chat);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DataBind();
    }

    public void DataBind() {

        List<TNotes> notesList;
        notesDAO = new TNotesDAO(context);

        notesList = notesDAO.selectionnerList(cours.getIDCOURS(), horaire.getIDHEURE(), journee.getIDJOURNEE());

        recyclerView = this.findViewById(R.id.rv_memo);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        myAdapterMemo = new MyAdapterMemo(context, notesList);
        myAdapterMemo.setCours(cours);
        myAdapterMemo.setHoraire(horaire);
        myAdapterMemo.setJournee(journee);
        myAdapterMemo.setAdapterMemo(myAdapterMemo);

        recyclerView.setAdapter(myAdapterMemo);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onDestroy() {
        notesDAO.close();
        personneDAO.close();
        super.onDestroy();
    }
}
