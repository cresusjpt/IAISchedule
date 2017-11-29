package com.saltechdigital.iaischedule;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saltechdigital.iaischedule.adapter.MyAdapterCoursPresence;
import com.saltechdigital.iaischedule.database.cours.TCours;
import com.saltechdigital.iaischedule.database.cours.TCoursDAO;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Presence extends Fragment {

    private TCoursDAO coursDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.presence, container, false);
        return v;
    }

    @Override
    public void onResume() {
        DataBind();
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        coursDAO = new TCoursDAO(getContext());
        DataBind();
    }

    @Override
    public void onDestroy() {
        coursDAO.close();
        super.onDestroy();
    }

    private void DataBind() {
        coursDAO = new TCoursDAO(getContext());

        List<TCours> cours;
        cours = coursDAO.selectionnerList();

        RecyclerView rv = getActivity().findViewById(R.id.rv_presence);
        assert rv != null;
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new MyAdapterCoursPresence(getContext(), cours));
        rv.setItemAnimator(new DefaultItemAnimator());
    }
}
