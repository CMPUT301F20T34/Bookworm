package com.example.bookworm;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
        View view =  inflater.inflate(R.layout.content_borrower_lists, parent, false);
        return new SRViewHolder(view, this.onBookListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull SRViewHolder holder, int position) {
        Book book = books.get(position);
        holder.getTitle().setText(book.getTitle());
        holder.getAuthor().setText(book.getAuthor());
        holder.getUsername().setText(book.getOwner());
        holder.getStatus().setText(book.getStatus());
        holder.getIsbn().setText(book.getIsbn());
        holder.getDescription().setText(book.descriptionAsString());
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
        private final TextView isbn;
        private final TextView description;
        OnBookListener onBookListener;

        public SRViewHolder(@NonNull View itemView, OnBookListener onBookListener) {
            super(itemView);
            this.title = itemView.findViewById(R.id.book_title);
            this.author = itemView.findViewById(R.id.book_author);
            this.username = itemView.findViewById(R.id.book_owner);
            this.status = itemView.findViewById(R.id.book_status);
            this.isbn = itemView.findViewById(R.id.book_isbn);
            this.description = itemView.findViewById(R.id.book_description);
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

        public TextView getIsbn() {
            return isbn;
        }

        public TextView getDescription() { return description; }
    }

    public interface OnBookListener {
        void onBookClick(int position);
    }

}
