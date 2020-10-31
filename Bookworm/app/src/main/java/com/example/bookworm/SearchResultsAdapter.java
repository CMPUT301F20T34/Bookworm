package com.example.bookworm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SRViewHolder>{

    private ArrayList<Book> books;
    private Context context;
    private OnBookListener onBookListener;

    public SearchResultsAdapter(Context context, ArrayList<Book> books, OnBookListener onBookListener) {
           this.context = context;
           this.books = books;
           this.onBookListener = onBookListener;
    }

    @NonNull
    @Override
    public SRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.search_result_content, parent, false);
        return new SRViewHolder(view, this.onBookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SRViewHolder holder, int position) {
        holder.getTitle().setText(books.get(position).getTitle());
        holder.getAuthor().setText(books.get(position).getAuthor());
        holder.getUsername().setText(books.get(position).getOwner());
        holder.getStatus().setText(books.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class SRViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title;
        private final TextView author;
        private final TextView username;
        private final TextView status;
        OnBookListener onBookListener;

        public SRViewHolder(@NonNull View itemView, OnBookListener onBookListener) {
            super(itemView);
            title = itemView.findViewById(R.id.search_result_title);
            author = itemView.findViewById(R.id.search_result_author);
            username = itemView.findViewById(R.id.search_result_owner);
            status = itemView.findViewById(R.id.search_result_status);
            this.onBookListener = onBookListener;

            itemView.setOnClickListener(this);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getAuthor() {
            return author;
        }

        public TextView getUsername() {
            return username;
        }

        public TextView getStatus() {
            return status;
        }

        @Override
        public void onClick(View v) {
            this.onBookListener.onBookClick(getAdapterPosition());
        }
    }

    public interface OnBookListener {
        void onBookClick(int position);
    }

}
