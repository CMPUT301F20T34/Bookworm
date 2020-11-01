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

    public SearchResultsAdapter(Context context, ArrayList<Book> books) {
           this.context = context;
           this.books = books;
    }

    @NonNull
    @Override
    public SRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.search_result_content, parent, false);
        return new SRViewHolder(view);
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

    public class SRViewHolder extends RecyclerView.ViewHolder{
        private final TextView title;
        private final TextView author;
        private final TextView username;
        private final TextView status;

        public SRViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.search_result_title);
            author = itemView.findViewById(R.id.search_result_author);
            username = itemView.findViewById(R.id.search_result_owner);
            status = itemView.findViewById(R.id.search_result_status);
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
    }
}
