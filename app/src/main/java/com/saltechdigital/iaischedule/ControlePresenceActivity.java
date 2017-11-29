package com.saltechdigital.iaischedule;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.saltechdigital.iaischedule.adapter.MyAdapterControlePresence;
import com.saltechdigital.iaischedule.database.eleve.TEleve;
import com.saltechdigital.iaischedule.database.eleve.TEleveDAO;

import java.util.List;

public class ControlePresenceActivity extends AppCompatActivity {

    private Context context;
    private TEleveDAO eleveDAO;

    @Override
    protected void onDestroy() {
        eleveDAO.close();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void DataBind() {
        eleveDAO = new TEleveDAO(context);

        List<TEleve> eleveList;
        eleveList = eleveDAO.selectionnerList();

        RecyclerView rv = this.findViewById(R.id.rv_controlePresence);
        assert rv != null;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(new MyAdapterControlePresence(context, eleveList));
        rv.setItemAnimator(new DefaultItemAnimator());
    }

}
