package com.saltechdigital.iaischedule.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.saltechdigital.iaischedule.database.personne.TPersonne;

import java.util.List;


/**
 * Created by Jeanpaul Tossou on 27/11/2017.
 */

public class MyAdapterCours extends RecyclerView.Adapter<MyAdapterCours.MyViewHolder> {
    private Context context;
    private final List<TPersonne> mal;

    public MyAdapterCours(Context context, List<TPersonne> liste) {
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
        //View view = inflater.inflate(R.layout.list_convers, parent, false);
        //return new MyViewHolder(view);
        return null;
    }

    private void setAnimation(View toAnimate) {
        Animation animation = AnimationUtils.loadAnimation(toAnimate.getContext(), android.R.anim.fade_in);
        animation.setDuration(1000);
        toAnimate.startAnimation(animation);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TPersonne actuel = mal.get(position);
        //holder.card_view.setBackgroundResource(R.drawable.state_list);
        //holder.iv_image.setBackgroundResource(R.drawable.state_button);
        //holder.display(actuel);
        //setAnimation(holder.card_view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView utilisateur, classe;
        private TPersonne current;
        private ImageView iv_image;
        //private CardView card_view;

        MyViewHolder(final View itemView) {
            super(itemView);
            // utilisateur = ((TextView) itemView.findViewById(R.id.tv_nomUtilisateur));
            //1 classe = ((TextView) itemView.findViewById(R.id.tv_classe));
            // card_view = (CardView) itemView.findViewById(R.id.card_view14);
            //iv_image = (ImageView) itemView.findViewById(R.id.iv_convers);

            /*iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("JEANPAUL", "IMAGE CLICK");
                }
            });*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayoutClick(v);
                }
            });

        }

        private void linearLayoutClick(View v) {
            int idConversation;
            //    TPersonneConversDAO personneConversDAO = new TPersonneConversDAO(context);

            //  int conversCreated = personneConversDAO.conversChatCreated(current.getIDPERSONNE());

            //       Intent intent = new Intent(v.getContext(), com.saltechdigital.iaidiscuss.ChatWriting.class);
//            //on a pas une conversation avec la personne
//            if (conversCreated == 0) {
//                idConversation = createConversation();
//            } else {
//                idConversation = conversCreated;
//            }
//
//            personneConversDAO.close();
//            intent.putExtra("personne", current);
//            intent.putExtra("idConversation", idConversation);
//            v.getContext().startActivity(intent);
        }

        /*private int createConversation() {
            TConversationDAO tConversationDAO = new TConversationDAO(context);
            TPersonneConversDAO tPersonneConversDAO = new TPersonneConversDAO(context);

            TConversation newConversation = new TConversation(Constantes.REGLE.CONVERSATION + current.getNOMPERSONNE().concat(current.getPRENOMPERSONNE()), 1);
            tConversationDAO.ajouter(newConversation);


            TConversation persConvers = tConversationDAO.lastInsert();
            TPersonneConvers personneConvers = new TPersonneConvers(current, persConvers);

            tPersonneConversDAO.ajouter(personneConvers);

            TConversation lastInsertConverstaion = tConversationDAO.lastInsert();

            tConversationDAO.close();
            tPersonneConversDAO.close();

            return lastInsertConverstaion.getIDCONVERSATION();

        }

        void display(TPersonne news) {
            current = news;
            utilisateur.setText(MessageFormat.format("{0}{1}{2}", news.getNOMPERSONNE()," ", news.getPRENOMPERSONNE()));
            classe.setText(news.getCLASSEPERSONNE());
        }

        */
    }
}
