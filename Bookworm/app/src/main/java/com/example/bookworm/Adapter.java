package com.example.bookworm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<Book> booklist;

    Adapter(Context context, ArrayList<Book> booklist){
        this.layoutInflater = LayoutInflater.from(context);
        this.booklist = booklist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.content_borrower_lists,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind texview with data received
        //        holder.ownerPhoto.setImageResource(R.drawable.ic_launcher_foreground);
//        holder.ownerName.setText(null);
        Book book = booklist.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.isbn.setText(book.getIsbn());
        holder.status.setText(book.getStatus());
        holder.ownerName.setText("Owner: " + book.getOwner());
    }

    @Override
    public int getItemCount() {
        return booklist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //TextView bookTitle,bookName;
        TextView ownerName;
        TextView title;
        TextView author;
        TextView isbn;
        TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ownerName = itemView.findViewById(R.id.book_owner);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            isbn = itemView.findViewById(R.id.book_isbn);
            status = itemView.findViewById(R.id.book_status);
        }
    }

}