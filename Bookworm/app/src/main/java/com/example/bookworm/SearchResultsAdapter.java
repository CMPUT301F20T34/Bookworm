package com.example.bookworm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        holder.getImage().setImageBitmap(books.get(position).getDrawablePhotograph());
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
        private final ImageView image;
        OnBookListener onBookListener;

        public SRViewHolder(@NonNull View itemView, OnBookListener onBookListener) {
            super(itemView);
            this.title = itemView.findViewById(R.id.search_result_title);
            this.author = itemView.findViewById(R.id.search_result_author);
            this.username = itemView.findViewById(R.id.search_result_owner);
            this.status = itemView.findViewById(R.id.search_result_status);
            this.image = itemView.findViewById(R.id.search_result_image);
            this.onBookListener = onBookListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.onBookListener.onBookClick(getAdapterPosition());
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

        public ImageView getImage() {
            return image;
        }
    }

    public interface OnBookListener {
        void onBookClick(int position);
    }

}
