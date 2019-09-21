package com.example.a21__void.afroturf.libIX.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.Salon;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgCommon.AfroTextView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

/**
 * Created by ASANDA on 2019/09/13.
 * for Pandaphic
 */
public class PagesAdapter extends RecyclerView.Adapter<PagesAdapter.PagesViewHolder> {
    private Context context;
    private List<SalonAfroObject> salons;
    private InteractionListener interactionListener;
    private boolean isLoading = false;

    public PagesAdapter(Context pContext, List<SalonAfroObject> pSalons){
        this.context = pContext;
        this.salons = pSalons;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        this.notifyDataSetChanged();
    }

    public void setSalons(List<SalonAfroObject> salons) {
        this.salons = salons;
    }

    public void setInteractionListener(InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    public void removeInteractionListener(){
        this.interactionListener = null;
    }

    @NonNull
    @Override
    public PagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);

        View page = inflater.inflate(R.layout.salon_layout, parent, false);

        PagesViewHolder viewHolder = new PagesViewHolder(page);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PagesViewHolder holder, final int position) {
        holder.setIsLoading(this.isLoading);
        if(!isLoading){
            final SalonAfroObject salon = this.salons.get(position);



            holder.ratingBar.setRating(salon.getRating());
            holder.txtName.setText(salon.getName());
            holder.txtLocation.setText(salon.location.toString());

            if(!holder.itemView.hasOnClickListeners()){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(PagesAdapter.this.interactionListener != null)
                            PagesAdapter.this.interactionListener.onItemClick(salon, position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(isLoading)
            return 2;
        else
            return salons != null ? salons.size() : 0;
    }

    public int indexOf(SalonAfroObject selectedSalon) {
        if(!isLoading){
            if(this.salons != null){

                return this.salons.indexOf(selectedSalon);
            }else{
                return -1;
            }
        }else{
            return 0;
        }
    }

    public class PagesViewHolder extends RecyclerView.ViewHolder{
        public final ImageView imgBanner;
        public final RatingBar ratingBar;
        public final TextView txtName, txtLocation;
        public final AVLoadingIndicatorView avLoading;

        public PagesViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imgBanner = itemView.findViewById(R.id.img_banner);
            this.ratingBar = itemView.findViewById(R.id.rat_rating);
            this.txtName = itemView.findViewById(R.id.txt_name);
            this.txtLocation = itemView.findViewById(R.id.txt_location);
            this.avLoading = itemView.findViewById(R.id.av_progress);
        }

        public void setIsLoading(boolean isLoading){
            if(isLoading){
                this.imgBanner.setVisibility(View.INVISIBLE);
                this.ratingBar.setVisibility(View.INVISIBLE);
                this.txtName.setVisibility(View.INVISIBLE);
                this.txtLocation.setVisibility(View.INVISIBLE);
                this.avLoading.setVisibility(View.VISIBLE);
            }else{
                this.imgBanner.setVisibility(View.VISIBLE);
                this.ratingBar.setVisibility(View.VISIBLE);
                this.txtName.setVisibility(View.VISIBLE);
                this.txtLocation.setVisibility(View.VISIBLE);
                this.avLoading.setVisibility(View.INVISIBLE);
            }
        }
    }

    public interface InteractionListener{
        void onItemClick(SalonAfroObject salon, int position);
    }
}
