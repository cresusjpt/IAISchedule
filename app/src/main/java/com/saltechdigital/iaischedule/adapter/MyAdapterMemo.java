package com.saltechdigital.iaischedule.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saltechdigital.iaischedule.ControlePresenceActivity;
import com.saltechdigital.iaischedule.MemoScheduleActivity;
import com.saltechdigital.iaischedule.R;
import com.saltechdigital.iaischedule.constantandenum.CommonMethods;
import com.saltechdigital.iaischedule.database.cours.TCours;
import com.saltechdigital.iaischedule.database.horaire.THoraire;
import com.saltechdigital.iaischedule.database.journee.TJournee;
import com.saltechdigital.iaischedule.database.note.TNotes;
import com.saltechdigital.iaischedule.database.note.TNotesDAO;

import java.util.List;


/**
 * Created by Jeanpaul Tossou on 27/11/2017.
 */

public class MyAdapterMemo extends RecyclerView.Adapter<MyAdapterMemo.MyViewHolder> {
    private Context context;
    private final List<TNotes> mal;
    private TNotesDAO notesDAO;

    private TCours cours;
    private THoraire horaire;
    private TJournee journee;
    private MyAdapterMemo adapterMemo;

    public void setAdapterMemo(MyAdapterMemo adapterMemo) {
        this.adapterMemo = adapterMemo;
    }

    public void setCours(TCours cours) {
        this.cours = cours;
    }

    public void setHoraire(THoraire horaire) {
        this.horaire = horaire;
    }

    public void setJournee(TJournee journee) {
        this.journee = journee;
    }

    public MyAdapterMemo(Context context, List<TNotes> liste) {
        this.context = context;
        this.mal = liste;
        notesDAO = new TNotesDAO(context);
    }

    @Override
    public int getItemCount() {
        return mal.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_memo, parent, false);
        return new MyViewHolder(view);
    }

    private void setAnimation(View toAnimate) {
        Animation animation = AnimationUtils.loadAnimation(toAnimate.getContext(), android.R.anim.fade_in);
        animation.setDuration(1000);
        toAnimate.startAnimation(animation);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TNotes actuel = mal.get(position);
        holder.linearLayout.setBackgroundResource(R.drawable.state_memo);
        holder.display(actuel);
        setAnimation(holder.linearLayout);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMemo, tvMemoDate, noteText;
        private TNotes current;
        private LinearLayout linearLayout;

        MyViewHolder(final View itemView) {
            super(itemView);
            tvMemo = itemView.findViewById(R.id.tv_memo);
            tvMemoDate = itemView.findViewById(R.id.tv_memoDate);

            linearLayout = itemView.findViewById(R.id.lin);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Mémo");
                    builder.setMessage(current.getNOTES());
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setCancelable(true);
                    builder.create().show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Option");
                    builder.setItems(new CharSequence[]{"Editer", "Supprimer"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0:

                                    View viewInflate = LayoutInflater.from(context).inflate(R.layout.content_controle_presence, null);
                                    noteText = viewInflate.findViewById(R.id.add_note);
                                    noteText.setText(current.getNOTES());

                                    android.support.v7.app.AlertDialog.Builder alertdialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                                    alertdialogBuilder.setView(viewInflate);
                                    alertdialogBuilder.setTitle("Modification de mémo");

                                    alertdialogBuilder.setPositiveButton("Enreg", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            modifMemo();
                                            adapterMemo.notifyDataSetChanged();
                                            adapterMemo.notifyItemChanged(getLayoutPosition());
                                        }
                                    });

                                    alertdialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    alertdialogBuilder.setCancelable(true);
                                    alertdialogBuilder.create().show();
                                    break;
                                case 1:
                                    notesDAO.supprimer(current.getIDNOTE());
                                    break;
                            }
                        }
                    });
                    builder.create().show();
                    return true;
                }
            });

        }

        void display(TNotes news) {
            current = news;
            tvMemo.setText(news.getNOTES());
            tvMemoDate.setText(news.getDATENOTES());
        }

        void modifMemo() {
            if (noteText.getText().toString().trim().length() > 0) {
                TNotes notes = new TNotes(notesDAO.getId(current.getNOTES()), cours.getIDCOURS(), horaire.getIDHEURE(), journee.getIDJOURNEE(), noteText.getEditableText().toString(), CommonMethods.getCurrentDate());
                notesDAO.modifierNote(notes);
            } else {
                Toast.makeText(context, "Veuillez entrer une note à enregistrer", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
