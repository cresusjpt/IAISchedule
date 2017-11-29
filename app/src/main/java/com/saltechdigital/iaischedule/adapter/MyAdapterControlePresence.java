package com.saltechdigital.iaischedule.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saltechdigital.iaischedule.ControlePresenceActivity;
import com.saltechdigital.iaischedule.R;
import com.saltechdigital.iaischedule.database.cours.TCours;
import com.saltechdigital.iaischedule.database.eleve.TEleve;

import java.text.MessageFormat;
import java.util.List;


/**
 * Created by Jeanpaul Tossou on 27/11/2017.
 */

public class MyAdapterControlePresence extends RecyclerView.Adapter<MyAdapterControlePresence.MyViewHolder> {
    private Context context;
    private final List<TEleve> mal;

    public MyAdapterControlePresence(Context context, List<TEleve> liste) {
        this.context = context;
        this.mal = liste;
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
        holder.card.setBackgroundResource(R.drawable.state_list);
        holder.iv_image.setBackgroundResource(R.drawable.state_button);
        holder.display(actuel);
        setAnimation(holder.card);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nomEleve;
        private TEleve current;
        private ImageView iv_image;
        private RelativeLayout card;
        private RadioGroup group;

        MyViewHolder(final View itemView) {
            super(itemView);
            nomEleve = (TextView) itemView.findViewById(R.id.tv_nomEleve);
            card = (RelativeLayout) itemView.findViewById(R.id.card_viewRelative);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_convers);
            group = (RadioGroup) itemView.findViewById(R.id.rg_presence);

            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("JEANPAUL", "IMAGE CLICK");
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ControlePresenceActivity.class);
                    intent.putExtra("cour", current);
                    context.startActivity(intent);
                }
            });

        }

        void display(TEleve news) {
            current = news;
            nomEleve.setText(MessageFormat.format("{0}", "Eleve con et taré"));
        }

    }
}
