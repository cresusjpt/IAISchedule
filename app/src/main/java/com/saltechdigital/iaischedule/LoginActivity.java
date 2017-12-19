package com.saltechdigital.iaischedule;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.saltechdigital.iaischedule.asyncTask.IdentificationTask;
import com.saltechdigital.iaischedule.constantandenum.Constantes;
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
import com.saltechdigital.iaischedule.database.schedule.TSchedule;
import com.saltechdigital.iaischedule.database.schedule.TScheduleDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private IdentificationTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView nom, prenom;
    private EditText telephone, mdp;
    private RadioGroup radioGroup;
    private View mProgressView;
    private View mLoginFormView;

    private TPersonneDAO personneDAO;
    private TCoursDAO coursDAO;
    private TEleveDAO eleveDAO;
    private TClasseDAO classeDAO;
    private TProfesseurDAO professeurDAO;
    private TScheduleDAO scheduleDAO;
    private View focusView;

    private Context context;
    Button mConnexionButton;

    @Override
    protected void onDestroy() {
        personneDAO.close();
        coursDAO.close();
        eleveDAO.close();
        classeDAO.close();
        professeurDAO.close();
        scheduleDAO.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getString("isFirstLaunch", "NO").equals("YES")) {
            startActivity(new Intent(this, IAISchedule.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        initializeViews();

        mConnexionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void initializeViews() {
        // Set up the login form.
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        telephone = findViewById(R.id.telephone);
        mdp = findViewById(R.id.mdp);
        radioGroup = findViewById(R.id.rg_login);
        mConnexionButton = findViewById(R.id.connexion_bt);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        personneDAO = new TPersonneDAO(context);
        coursDAO = new TCoursDAO(context);
        eleveDAO = new TEleveDAO(context);
        classeDAO = new TClasseDAO(context);
        professeurDAO = new TProfesseurDAO(context);
        scheduleDAO = new TScheduleDAO(context);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        nom.setError(null);
        prenom.setError(null);
        telephone.setError(null);
        mdp.setError(null);

        // Store values at the time of the login attempt.
        String nomStr = nom.getText().toString();
        String prenomStr = prenom.getText().toString();
        String telephoneStr = telephone.getText().toString();
        String passwordStr = mdp.getText().toString();

        //valeur par defaut checked in xml file
        String sexe = "M";

        int check = radioGroup.getCheckedRadioButtonId();
        if (check == R.id.rb_login_mas) {
            sexe = "M";
        } else if (check == R.id.rb_login_fem) {
            sexe = "F";
        }

        boolean cancel = false;
        focusView = null;

        // Check for a valid phone, if the user entered one.

        if (!TextUtils.isEmpty(telephoneStr) && !isTelephoneValid(telephoneStr)) {
            telephone.setError(getString(R.string.error_invalid_password));
            focusView = telephone;
            cancel = true;
        }

        // Check for a password, if the user entered one.
        if (TextUtils.isEmpty(passwordStr)) {
            mdp.setError(getString(R.string.error_field_required));
            focusView = mdp;
            cancel = true;
        } else if (!isPasswordValid(passwordStr)) {
            mdp.setError(getString(R.string.error_incorrect_password));
            focusView = mdp;
            cancel = true;
        }

        // Check for a valid name address.
        if (TextUtils.isEmpty(nomStr)) {
            nom.setError(getString(R.string.error_field_required));
            focusView = nom;
            cancel = true;
        } else if (!isEmailValid(nomStr)) {
            nom.setError(getString(R.string.error_invalid_email));
            focusView = nom;
            cancel = true;
        }

        if (TextUtils.isEmpty(prenomStr)) {
            prenom.setError(getString(R.string.error_field_required));
            focusView = prenom;
            cancel = true;
        } else if (!isEmailValid(nomStr)) {
            prenom.setError(getString(R.string.error_invalid_email));
            focusView = prenom;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            /*
             * Represents an asynchronous login/registration task used to authenticate
             * the user.
             */
            mAuthTask = new IdentificationTask(context, new IdentificationTask.IdentificationCallback() {

                @Override
                public void onSuccess(final JSONObject jsonArray) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<TCours> coursList = new ArrayList<>();
                            List<TClasse> classeList = new ArrayList<>();
                            List<TEleve> eleveList = new ArrayList<>();
                            List<TPersonne> personneList = new ArrayList<>();
                            List<TProfesseur> professeurList = new ArrayList<>();
                            List<TSchedule> scheduleList = new ArrayList<>();

                            try {
                                JSONArray cours = jsonArray.getJSONArray("COURS");
                                JSONArray classes = jsonArray.getJSONArray("CLASSES");
                                JSONArray eleves = jsonArray.getJSONArray("ELEVES");
                                JSONArray professeur = jsonArray.getJSONArray("PROFESSEURS");
                                JSONArray schedule = jsonArray.getJSONArray("SCHEDULE");

                                String methode = jsonArray.getString("METHODE");

                                for (int i = 0; i < cours.length(); i++) {
                                    JSONObject oneCours = cours.getJSONObject(i);
                                    TCours tCours = new TCours(oneCours);
                                    coursList.add(tCours);
                                }

                                for (int j = 0; j < classes.length(); j++) {
                                    JSONObject oneClasse = classes.getJSONObject(j);
                                    TClasse tClasse = new TClasse(oneClasse);
                                    classeList.add(tClasse);
                                }

                                for (int j = 0; j < eleves.length(); j++) {
                                    JSONObject oneEleve = eleves.getJSONObject(j);
                                    TPersonne tPersonne = new TPersonne(oneEleve);
                                    TEleve tEleve = new TEleve(oneEleve);
                                    personneList.add(tPersonne);
                                    eleveList.add(tEleve);
                                }

                                for (int j = 0; j < professeur.length(); j++) {
                                    JSONObject oneProf = professeur.getJSONObject(j);
                                    TPersonne tPersonne = new TPersonne(oneProf);
                                    TProfesseur tProfesseur = new TProfesseur(oneProf);
                                    personneList.add(tPersonne);
                                    professeurList.add(tProfesseur);
                                }

                                for (int j = 0; j < schedule.length(); j++) {
                                    JSONObject oneSchedule = schedule.getJSONObject(j);
                                    TSchedule tSchedule = new TSchedule(oneSchedule);
                                    scheduleList.add(tSchedule);
                                }

                                classeDAO.ajouterListe(classeList);
                                personneDAO.ajouterListe(personneList);
                                Log.d("JEANPAUL", personneList.toString());
                                eleveDAO.ajouterListe(eleveList);
                                professeurDAO.ajouterListe(professeurList);
                                coursDAO.ajouterListe(coursList);
                                scheduleDAO.ajouterListe(scheduleList);

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                                preferences.edit()
                                        .putString("METHODE", methode)
                                        .putString("isFirstLaunch", "YES").apply();

                                startActivity(new Intent(LoginActivity.this, IAISchedule.class));
                                finish();
                            } catch (JSONException e) {
                                showProgress(false);
                                setFocus();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 2000);
                                e.printStackTrace();
                            }
                        }
                    }, Constantes.REGLE.SPLASH_TIME_OUT);

                }

                @Override
                public void onError() {
                    setTask();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showProgress(false);
                            setFocus();
                            Snackbar.make(mLoginFormView, "Une erreur s'est produite. Veuillez réessayer ultérieurement", Snackbar.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 3000);
                        }
                    }, Constantes.REGLE.SPLASH_TIME_OUT);
                }
            });
            mAuthTask.execute(nomStr, prenomStr, sexe, telephoneStr, passwordStr);
        }
    }

    private void setTask() {
        mAuthTask = null;
    }

    private void setFocus() {
        nom.setError(getString(R.string.action_incorrect_field));
        prenom.setError(getString(R.string.action_incorrect_field));
        focusView = nom;
        focusView = prenom;
        focusView.requestFocus();
    }


    private boolean isEmailValid(String nom) {
        return nom.length() > 1;
    }

    private boolean isTelephoneValid(String tel) {
        return tel.length() == 8;
    }

    private boolean isPasswordValid(String mdp) {
        return mdp.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}

