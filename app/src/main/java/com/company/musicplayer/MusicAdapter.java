package com.company.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder>{

    private final ArrayList<String> songsList;
    private final Context mContext;

    private MediaPlayer mediaPlayer;



    public MusicAdapter(ArrayList<String> list, Context mContext) {
        this.songsList = list;
        this.mContext = mContext;
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewFilename;
        private final CardView cardView;
        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);


            textViewFilename = itemView.findViewById(R.id.filename);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }


    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, @SuppressLint("RecyclerView") final int position) {
       String path = songsList.get(position);
        Log.e("filepath", path );
        String title = path.substring(path.lastIndexOf("/") + 1);
        holder.textViewFilename.setText(title);
        holder.cardView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                Intent intent = new Intent(mContext,Musicactivity.class);
                intent.putExtra("title",title);
                intent.putExtra("filepath",path);
                intent.putExtra("position", position);
                intent.putExtra("list",songsList);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }





}
