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
import android.widget.TextView;

import com.saltechdigital.iaischedule.ControlePresenceActivity;
import com.saltechdigital.iaischedule.R;
import com.saltechdigital.iaischedule.database.cours.TCours;

import java.util.List;


/**
 * Created by Jeanpaul Tossou on 27/11/2017.
 */

public class MyAdapterCoursPresence extends RecyclerView.Adapter<MyAdapterCoursPresence.MyViewHolder> {
    private Context context;
    private final List<TCours> mal;

    public MyAdapterCoursPresence(Context context, List<TCours> liste) {
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
        View view = inflater.inflate(R.layout.list_cours, parent, false);
        return new MyViewHolder(view);
    }

    private void setAnimation(View toAnimate) {
        Animation animation = AnimationUtils.loadAnimation(toAnimate.getContext(), android.R.anim.fade_in);
        animation.setDuration(1000);
        toAnimate.startAnimation(animation);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TCours actuel = mal.get(position);
        holder.linearLayout.setBackgroundResource(R.drawable.state_list);
        holder.iv_image.setBackgroundResource(R.drawable.state_button);
        holder.display(actuel);
        setAnimation(holder.linearLayout);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nomCours;
        private TCours current;
        private ImageView iv_image;
        private LinearLayout linearLayout;

        MyViewHolder(final View itemView) {
            super(itemView);
            nomCours = itemView.findViewById(R.id.tv_nomCours);
            linearLayout = itemView.findViewById(R.id.lin_cours);
            iv_image = itemView.findViewById(R.id.iv_cours);

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

        void display(TCours news) {
            current = news;
            nomCours.setText(news.getNOMCOURS());
        }

    }
}
