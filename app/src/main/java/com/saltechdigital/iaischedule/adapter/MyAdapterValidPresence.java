package com.saltechdigital.iaischedule.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saltechdigital.iaischedule.R;
import com.saltechdigital.iaischedule.database.eleve.TEleve;
import com.saltechdigital.iaischedule.database.personne.TPersonne;
import com.saltechdigital.iaischedule.database.personne.TPersonneDAO;
import com.saltechdigital.iaischedule.database.presence.TPresence;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.List;


/**
 * Created by Jeanpaul Tossou on 27/11/2017.
 */

public class MyAdapterValidPresence extends RecyclerView.Adapter<MyAdapterValidPresence.MyViewHolder> {
    private Context context;
    private final List<TEleve> mal;
    private TPersonneDAO personneDAO;
    private List<TPersonne> personneList;
    private List<TPresence> presenceList;

    public MyAdapterValidPresence(Context context, List<TEleve> liste, List<TPersonne> personneList, List<TPresence> presenceList) {
        this.context = context;
        this.mal = liste;
        personneDAO = new TPersonneDAO(context);
        this.personneList = personneList;
        this.presenceList = presenceList;
    }

    @Override
    public int getItemCount() {
        return mal.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_presence, parent, false);
        return new MyViewHolder(view);
    }

    private void setAnimation(View toAnimate) {
        Animation animation = AnimationUtils.loadAnimation(toAnimate.getContext(), android.R.anim.fade_in);
        animation.setDuration(1000);
        toAnimate.startAnimation(animation);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TEleve actuel = mal.get(position);
        TPersonne actuelPersonne = personneList.get(position);
        TPresence actuelPresence = presenceList.get(position);

        holder.card.setBackgroundResource(R.drawable.state_list);
        holder.iv_image.setBackgroundResource(R.drawable.state_button);
        holder.display(actuel, actuelPersonne, actuelPresence);

        setAnimation(holder.card);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nomEleve;
        private TEleve current;
        private TPersonne currentPersonne;
        private TPresence currentPresence;
        private ImageView iv_image;
        private RelativeLayout card;
        private RadioGroup group;

        MyViewHolder(final View itemView) {
            super(itemView);
            nomEleve = itemView.findViewById(R.id.tv_nomEleve);
            card = itemView.findViewById(R.id.card_viewRelative);
            iv_image = itemView.findViewById(R.id.iv_convers);
            group = itemView.findViewById(R.id.rg_presence);

            //presence par défaut

            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.rb_present:
                            currentPresence.setTYPEPRESENCE("P");
                            break;

                        case R.id.rb_retard:
                            currentPresence.setTYPEPRESENCE("R");
                            break;

                        case R.id.rb_abscent:
                            currentPresence.setTYPEPRESENCE("A");
                            break;
                    }
                }
            });

            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View view = LayoutInflater.from(context).inflate(R.layout.image_profil, null);
                    ImageView profil = view.findViewById(R.id.profil_icon);

                    Picasso.with(context).load(currentPersonne.getPHOTOPERSONNE()).placeholder(R.mipmap.ic_custom_profil_foreground).into(profil);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(view);
                    builder.setTitle(currentPersonne.getNOMPERSONNE() + " " + currentPersonne.getPRENOMPERSONNE());

                    builder.setCancelable(true);
                    builder.create().show();

                }
            });

        }

        void display(TEleve news, TPersonne newsPersonne, TPresence newsPresence) {
            current = news;
            currentPersonne = newsPersonne;
            currentPresence = newsPresence;

            switch (currentPresence.getTYPEPRESENCE()) {
                case "P":
                    group.check(R.id.rb_present);
                    break;
                case "R":
                    group.check(R.id.rb_retard);
                    break;
                case "A":
                    group.check(R.id.rb_abscent);
                    break;
            }

            nomEleve.setText(MessageFormat.format("{0}{1}{2}", currentPersonne.getNOMPERSONNE(), " ", currentPersonne.getPRENOMPERSONNE()));
        }

    }

    public List<TPresence> getPresenceList() {
        return presenceList;
    }
}
