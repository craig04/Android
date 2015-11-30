package com.elysium.craig.photogallery;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class PhotoActivity extends SingleFragmentActivity {

    private Bitmap mBitmap;

    @Override
    protected Fragment createFragment() {
        return PhotoFragment.newInstance(mBitmap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mBitmap = getIntent().getParcelableExtra(PhotoFragment.EXTRA_PHOTO);
        super.onCreate(savedInstanceState);
    }
}
