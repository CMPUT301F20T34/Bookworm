package com.example.bookworm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Activity class for the owner booklist activity
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<Request> requestList;

    RequestAdapter(Context context, ArrayList<Request> requestList){
        this.layoutInflater = LayoutInflater.from(context);
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.content_request_lists,parent,false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        //bind texview with data received
        //        holder.ownerPhoto.setImageResource(R.drawable.ic_launcher_foreground);
//        holder.ownerName.setText(null);
        Request request = requestList.get(position);
        holder.creatorName.setText(request.getCreator().getUsername());
        holder.timestamp.setText(request.getTimestamp().toString());
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder{

        //TextView bookTitle,bookName;
        TextView creatorName;
        TextView timestamp;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            creatorName = itemView.findViewById(R.id.creator_title);
            timestamp = itemView.findViewById(R.id.request_time);
        }
    }

}