package com.example.bookworm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ViewPhotoFragment extends DialogFragment {
    private ImageView ViewPhoto;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_photo_fragment_layout, null);
        ViewPhoto = view.findViewById(R.id.view_photo);

        Bundle bundle = getArguments();
        assert bundle != null;
        Bitmap bitmap = bundle.getParcelable("bitmap");
        ViewPhoto.setImageBitmap(bitmap);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Book Photo Detail")
                .setNegativeButton("Back", null)
                .create();
    }

    static ViewPhotoFragment newInstance(Bitmap bitmap) {
        Bundle args = new Bundle();
        args.putParcelable("bitmap", bitmap);
        ViewPhotoFragment fragment = new ViewPhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
