package com.example.cibushub.DAL;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.example.cibushub.GlideApp;
import com.example.cibushub.Interfaces.IDataImage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;


public class DataImage implements IDataImage {

    FirebaseStorage storage = FirebaseStorage.getInstance();


    @Override
    public void setImageFromPostPicId(final Context context, final ImageView view, String picId) {
        storage.getReference("post-pictures/" + picId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri uri) {
                GlideApp.with(context).load(uri).into(view);
            }
        });

    }

}
