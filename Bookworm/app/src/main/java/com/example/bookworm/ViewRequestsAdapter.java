package com.example.bookworm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewRequestsAdapter extends RecyclerView.Adapter<ViewRequestsAdapter.MyViewHolder> {
    private final ArrayList<Request> requestList;

    public ViewRequestsAdapter(ArrayList<Request> requestList) {
        this.requestList = requestList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameView;
        public TextView timestampView;

        public MyViewHolder(@NonNull View itemView, TextView usernameView, TextView timestampView) {
            super(itemView);
            this.usernameView = usernameView;
            this.timestampView = timestampView;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.view_request_result, parent, false);
        TextView usernameView = view.findViewById(R.id.view_request_result_username);
        TextView timestampView = view.findViewById(R.id.view_request_result_timestamp);

        MyViewHolder vh = new MyViewHolder(view, usernameView, timestampView);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.ownerPhoto.setImageResource(R.drawable.ic_launcher_foreground);
        Request req = requestList.get(position);

        holder.usernameView.setText(req.getCreator().getUsername());
        holder.timestampView.setText(req.getTimestamp().toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.requestList.size();
    }
}
