package com.saltechdigital.iaischedule;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.saltechdigital.iaischedule.adapter.MyAdapterCours;
import com.saltechdigital.iaischedule.database.cours.TCours;
import com.saltechdigital.iaischedule.database.cours.TCoursDAO;
import com.saltechdigital.iaischedule.database.horaire.THoraire;
import com.saltechdigital.iaischedule.database.horaire.THoraireDAO;
import com.saltechdigital.iaischedule.database.journee.TJournee;
import com.saltechdigital.iaischedule.database.journee.TJourneeDAO;
import com.saltechdigital.iaischedule.database.personne.TPersonne;
import com.saltechdigital.iaischedule.database.personne.TPersonneDAO;
import com.saltechdigital.iaischedule.database.schedule.TSchedule;
import com.saltechdigital.iaischedule.database.schedule.TScheduleDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cours extends Fragment {

    private RecyclerView rv;

    private TJourneeDAO journeeDAO;
    private THoraireDAO horaireDAO;
    private TCoursDAO coursDAO;
    private TPersonneDAO personneDAO;

    private RadioGroup radioGroup;

    @Override
    public void onDestroy() {
        journeeDAO.close();
        horaireDAO.close();
        coursDAO.close();
        personneDAO.close();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.cours, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        rv = getActivity().findViewById(R.id.rv_cours);

        radioGroup = getActivity().findViewById(R.id.rg_journee);

        journeeDAO = new TJourneeDAO(getContext());
        horaireDAO = new THoraireDAO(getContext());
        coursDAO = new TCoursDAO(getContext());
        personneDAO = new TPersonneDAO(getContext());

        DataBind();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.lundi:
                        DataBindBy("1");
                        break;
                    case R.id.mardi:
                        DataBindBy("2");
                        break;
                    case R.id.merc:
                        DataBindBy("3");
                        break;
                    case R.id.jeudi:
                        DataBindBy("4");
                        break;
                    case R.id.vend:
                        DataBindBy("5");
                        break;
                    case R.id.same:
                        DataBindBy("6");
                        break;
                    case R.id.all:
                        DataBind();
                        break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        DataBind();
        super.onResume();
    }

    private void DataBindBy(String order) {
        TScheduleDAO scheduleDAO = new TScheduleDAO(getContext());

        List<TSchedule> scheduleList;
        List<TCours> coursList = new ArrayList<>();
        List<TJournee> journeeList = new ArrayList<>();
        List<THoraire> horaireList = new ArrayList<>();
        List<TPersonne> personneList = new ArrayList<>();

        scheduleList = scheduleDAO.selectionnerListBy(order);

        for (TSchedule schedule : scheduleList) {
            journeeList.add(journeeDAO.selectionner(schedule.getIDJOURNEE()));
            horaireList.add(horaireDAO.selectionner(schedule.getIDHEURE()));
            coursList.add(coursDAO.selectionner(schedule.getIDCOURS()));
        }

        for (TCours cours : coursList) {
            personneList.add(personneDAO.selectionner(cours.getIDPERSONNE()));
        }

        assert rv != null;
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new MyAdapterCours(getContext(), scheduleList, journeeList, horaireList, coursList, personneList));
        rv.setItemAnimator(new DefaultItemAnimator());

    }

    private void DataBind() {
        TScheduleDAO scheduleDAO = new TScheduleDAO(getContext());

        List<TSchedule> scheduleList;
        List<TCours> coursList = new ArrayList<>();
        List<TJournee> journeeList = new ArrayList<>();
        List<THoraire> horaireList = new ArrayList<>();
        List<TPersonne> personneList = new ArrayList<>();

        scheduleList = scheduleDAO.selectionnerList();

        for (TSchedule schedule : scheduleList) {
            journeeList.add(journeeDAO.selectionner(schedule.getIDJOURNEE()));
            horaireList.add(horaireDAO.selectionner(schedule.getIDHEURE()));
            coursList.add(coursDAO.selectionner(schedule.getIDCOURS()));
        }

        for (TCours cours : coursList) {
            personneList.add(personneDAO.selectionner(cours.getIDPERSONNE()));
        }

        assert rv != null;
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new MyAdapterCours(getContext(), scheduleList, journeeList, horaireList, coursList, personneList));
        rv.setItemAnimator(new DefaultItemAnimator());
    }
}
