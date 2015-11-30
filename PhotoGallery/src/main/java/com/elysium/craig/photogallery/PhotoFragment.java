package com.elysium.craig.photogallery;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PhotoFragment extends Fragment {

    public static final String EXTRA_PHOTO = "photo";

    private Bitmap mBitmap;

    public static PhotoFragment newInstance(Bitmap bitmap) {

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PHOTO, bitmap);

        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mBitmap = getArguments().getParcelable(EXTRA_PHOTO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.full_screen_image_view);
        if (mBitmap != null) {
            imageView.setImageBitmap(mBitmap);
        }
        return view;
    }
}
