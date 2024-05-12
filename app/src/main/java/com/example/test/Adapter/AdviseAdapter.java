package com.example.test.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.test.Domain.AdviseDomain;
import com.example.test.R;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdviseAdapter extends RecyclerView.Adapter<AdviseAdapter.ViewHolder> {

    ArrayList<AdviseDomain> items;

    public AdviseAdapter(ArrayList<AdviseDomain> items) {
        this.items = items;
    }

    Context context;
    @NonNull
    @Override
    public AdviseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflactor = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_advise, parent, false);

        return new ViewHolder(inflactor);
    }

    @Override
    public void onBindViewHolder(@NonNull AdviseAdapter.ViewHolder holder, int position) {

        holder.title.setText(items.get(position).getTitle());
        holder.subtitle.setText(items.get(position).getSubTitle());

        int drawableRessourceId = holder.itemView.getResources().getIdentifier(items.get(position).getPicAddress()
                , "drawable", holder.itemView.getContext().getPackageName());


        Glide.with(holder.itemView.getContext())
                .load(drawableRessourceId)
                .transform(new GranularRoundedCorners(30,30,0,0))
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, subtitle;
        ImageView pic;
        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            subtitle = itemView.findViewById(R.id.subtitleTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
