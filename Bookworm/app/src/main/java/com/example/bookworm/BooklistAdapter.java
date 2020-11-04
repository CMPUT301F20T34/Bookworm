package com.example.bookworm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
                            TextView currentBorrower) {
            super(itemView);
            this.ownerPhoto = ownerPhoto;
            this.ownerName = ownerName;
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.status = status;
            this.currentBurrower = currentBorrower;
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
        TextView isbn = view.findViewById(R.id.phone_profile);
        TextView status = view.findViewById(R.id.email_profile);
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
        Book book = booklist.get(position);
        if (book.getPhotograph() != null) {
            holder.ownerPhoto.setImageBitmap(StringToBitMap(book.getPhotograph()));
        }

        holder.ownerName.setText(book.getOwner());
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.isbn.setText(book.getIsbn());
        holder.status.setText(book.getStatus());
        holder.currentBurrower.setText(book.getBorrower());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.booklist.size();
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
