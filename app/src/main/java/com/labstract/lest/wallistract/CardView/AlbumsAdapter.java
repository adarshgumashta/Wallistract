package com.labstract.lest.wallistract.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.labstract.lest.wallistract.GridActivities.SecondActivity;
import com.labstract.lest.wallistract.R;

import java.util.List;

/**
 * Created by Adi on 15-01-2017.
 */


public class AlbumsAdapter extends RecyclerView.Adapter < AlbumsAdapter.MyViewHolder > {
    private Context mContext;
    private List < Album > albumList;
    Intent intent;
    Bundle bundle;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail, overflow;
        public TextView title, noofimages;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            noofimages = (TextView) itemView.findViewById(R.id.noofimages);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            overflow = (ImageView) itemView.findViewById(R.id.overflow);
        }
    }
    public AlbumsAdapter(Context mContext, List < Album > albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }
    public AlbumsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Album album = albumList.get(position);
        holder.title.setText(album.getTitle());
        holder.noofimages.setText(album.getNoOfImages() + " Images");
        //loading album cover using glide library
        holder.thumbnail.setTag(album.getThumbnail(), position);
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopupMenu(holder.overflow);
                Toast.makeText(mContext, "Clicked", Toast.LENGTH_LONG).show();
            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
       intent= new Intent(view.getContext(), SecondActivity.class);
       bundle=new Bundle();
       bundle.putInt("position", (holder.getAdapterPosition()));
       intent.putExtras(bundle);
       view.getContext().startActivity(intent);
                //Toast.makeText(mContext,"Image Clicked",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return albumList.size();
    }

}