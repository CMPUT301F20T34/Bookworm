package com.example.bookworm;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BooklistAdapter extends RecyclerView.Adapter<BooklistAdapter.MyViewHolder> {
    private ArrayList<Book> booklist;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ownerPhoto;
        public TextView ownerName;
        public TextView title;
        public TextView author;
        public TextView isbn;
        public TextView status;
        public TextView currentBurrower;

        public MyViewHolder(@NonNull View itemView, ImageView ownerPhoto, TextView ownerName,
                            TextView title, TextView author, TextView isbn, TextView status,
                            TextView currentBurrower) {
            super(itemView);
            this.ownerPhoto = ownerPhoto;
            this.ownerName = ownerName;
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.status = status;
            this.currentBurrower = currentBurrower;
        }


    }

    public BooklistAdapter(ArrayList<Book> booklist) {
        this.booklist = booklist;
    }

    @Override
    public BooklistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.owner_booklist_content, parent, false);
        ImageView ownerPhoto = view.findViewById(R.id.imageView);
        TextView ownerName = view.findViewById(R.id.textView);
        TextView title = view.findViewById(R.id.textView2);
        TextView author = view.findViewById(R.id.textView3);
        TextView isbn = view.findViewById(R.id.textView4);
        TextView status = view.findViewById(R.id.textView5);
        TextView currentBurrower = view.findViewById(R.id.textView6);

        MyViewHolder vh = new MyViewHolder(view, ownerPhoto, ownerName,
                title, author, isbn, status, currentBurrower);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.ownerPhoto.setImageResource(R.drawable.ic_launcher_foreground);
//        holder.ownerName.setText(null);
        holder.title.setText(booklist.get(position).getTitle());
        holder.author.setText(booklist.get(position).getAuthor());
        holder.isbn.setText(booklist.get(position).getIsbn());
        holder.status.setText(booklist.get(position).getStatus());
//        holder.currentBurrower.setText(null);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.booklist.size();
    }
}
