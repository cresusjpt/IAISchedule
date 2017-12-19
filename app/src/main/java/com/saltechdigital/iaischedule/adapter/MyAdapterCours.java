package com.saltechdigital.iaischedule.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saltechdigital.iaischedule.MemoScheduleActivity;
import com.saltechdigital.iaischedule.R;
import com.saltechdigital.iaischedule.database.classe.TClasseDAO;
import com.saltechdigital.iaischedule.database.cours.TCours;
import com.saltechdigital.iaischedule.database.horaire.THoraire;
import com.saltechdigital.iaischedule.database.journee.TJournee;
import com.saltechdigital.iaischedule.database.personne.TPersonne;
import com.saltechdigital.iaischedule.database.schedule.TSchedule;

import java.text.MessageFormat;
import java.util.List;


/**
 * Created by Jeanpaul Tossou on 27/11/2017.
 */

public class MyAdapterCours extends RecyclerView.Adapter<MyAdapterCours.MyViewHolder> {
    private Context context;
    private final List<TSchedule> mal;
    private final List<TJournee> journeeList;
    private final List<THoraire> horaireList;
    private final List<TCours> coursList;
    private final List<TPersonne> personneList;
    private SharedPreferences preferences;
    private final String methode;
    private TClasseDAO classeDAO;

    public MyAdapterCours(Context context, List<TSchedule> liste, List<TJournee> journees, List<THoraire> horaireList, List<TCours> coursList, List<TPersonne> personneList) {
        this.context = context;
        this.mal = liste;
        this.journeeList = journees;
        this.horaireList = horaireList;
        this.coursList = coursList;
        this.personneList = personneList;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        methode = preferences.getString("METHODE", "ELEVE");
    }

    @Override
    public int getItemCount() {
        return mal.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_schedule, parent, false);
        return new MyViewHolder(view);
    }

    private void setAnimation(View toAnimate) {
        Animation animation = AnimationUtils.loadAnimation(toAnimate.getContext(), android.R.anim.fade_in);
        animation.setDuration(1000);
        toAnimate.startAnimation(animation);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TSchedule actuelSchedule = mal.get(position);
        TJournee actuelJournee = journeeList.get(position);
        THoraire actuelHoraire = horaireList.get(position);
        TCours actuelCours = coursList.get(position);
        TPersonne actuelPersonne = personneList.get(position);


        holder.linear_top.setBackgroundResource(R.drawable.state_schedule);
        holder.display(actuelSchedule, actuelJournee, actuelHoraire, actuelCours, actuelPersonne);
        setAnimation(holder.linear_top);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView journ, heure, classeProf;
        private LinearLayout linear_top;

        private TSchedule currentSchedule;
        private TJournee currentJournee;
        private THoraire currentHoraire;
        private TCours currentCours;
        private TPersonne currentPersonne;

        MyViewHolder(final View itemView) {
            super(itemView);
            journ = itemView.findViewById(R.id.tv_dateSchedule);
            heure = itemView.findViewById(R.id.tv_heure_shedule);
            classeProf = itemView.findViewById(R.id.tv_scheduleClasse);
            linear_top = itemView.findViewById(R.id.linear_top);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MemoScheduleActivity.class);
                    intent.putExtra("SCHEDULE", currentSchedule);
                    intent.putExtra("COURS", currentCours);
                    intent.putExtra("JOURNEE", currentJournee);
                    intent.putExtra("HORAIRE", currentHoraire);
                    intent.putExtra("PERSONNE", currentPersonne);
                    context.startActivity(intent);
                }
            });

        }

        void display(TSchedule newsSchedule, TJournee newsJournee, THoraire newsHoraire, TCours newsCours, TPersonne newsPersonne) {
            currentSchedule = newsSchedule;
            currentJournee = newsJournee;
            currentHoraire = newsHoraire;
            currentCours = newsCours;
            currentPersonne = newsPersonne;

            journ.setText(newsJournee.getNOMJOUR());
            heure.setText(newsHoraire.getHEURE());
            if (methode.equals("PROFESSEUR")) {
                classeDAO = new TClasseDAO(context);
                classeProf.setText(classeDAO.getNomclasse(currentCours.getIDCLASSE()));
                classeDAO.close();
            } else {
                classeProf.setText(MessageFormat.format("{0} {1}", currentPersonne.getNOMPERSONNE(), currentPersonne.getPRENOMPERSONNE()));
            }
        }

    }
}
