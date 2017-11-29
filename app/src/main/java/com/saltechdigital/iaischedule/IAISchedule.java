package com.saltechdigital.iaischedule;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.saltechdigital.iaischedule.database.classe.TClasse;
import com.saltechdigital.iaischedule.database.classe.TClasseDAO;
import com.saltechdigital.iaischedule.database.cours.TCours;
import com.saltechdigital.iaischedule.database.cours.TCoursDAO;
import com.saltechdigital.iaischedule.database.eleve.TEleve;
import com.saltechdigital.iaischedule.database.eleve.TEleveDAO;
import com.saltechdigital.iaischedule.database.personne.TPersonne;
import com.saltechdigital.iaischedule.database.personne.TPersonneDAO;
import com.saltechdigital.iaischedule.database.professeur.TProfesseur;
import com.saltechdigital.iaischedule.database.professeur.TProfesseurDAO;
import com.saltechdigital.iaischedule.slidingTab.SlidingTabLayout;
import com.saltechdigital.iaischedule.slidingTab.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class IAISchedule extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager pager;
    private Context context;

    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"COURS", "SCHEDULE"};
    int Numboftabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iaischedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
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
        tryInsert();

    }

    private void tryInsert() {
        /*TClasse classe = new TClasse("GL3");
        new TClasseDAO(context).ajouter(classe);

        TPersonne personne = new TPersonne(1,"TOSSOU",
                "JEANPAUL","92109283","M");
        new TPersonneDAO(context).ajouter(personne);

        TProfesseur professeur = new TProfesseur(1,1);
        new TProfesseurDAO(context).ajouter(professeur);*/

        List<TEleve> eleveList = new ArrayList<>();

        for (int i = 0; i < 22; i++) {
            TEleve eleve = new TEleve(i, 2, 1);
            eleveList.add(eleve);
        }
        //new TEleveDAO(context).ajouterListe(eleveList);
        Toast.makeText(context, "" + new TEleveDAO(context).taille(), Toast.LENGTH_SHORT).show();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cours, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
