package com.elysium.craig.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ThumbnailDownloader<Token> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler mHandler;
    Map<ImageView, String> mRequestMap = Collections
        .synchronizedMap(new HashMap<ImageView, String>());

    private Handler mResponseHandler;
    private Listener<Token> mListener;

    private LruCache<String, Bitmap> mLRUCache;

    public interface Listener<Token> {
        void onThumbnailDownloaded(Token token, Bitmap thumbnail);
    }

    public void setListener(Listener<Token> listener) {
        mListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler) {

        super(TAG);
        mResponseHandler = responseHandler;

        int memory = (int) Runtime.getRuntime().maxMemory();
        mLRUCache = new LruCache<>(memory / 15);
        Log.d(TAG, "LRUCache memory usage: " + memory);
    }

    @Override
    protected void onLooperPrepared() {

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == MESSAGE_DOWNLOAD) {
                    @SuppressWarnings("unchecked")
                    Token token = (Token) msg.obj;
                    Log.i(TAG, "Got a request for url: " + mRequestMap.get(token));
                    handleRequest(token);
                }
            }
        };
    }

    public void queueThumbnail(Token token, String url) {

        Log.i(TAG, "Got an URL: " + url);
        mRequestMap.put((ImageView) token, url);

        mHandler.obtainMessage(MESSAGE_DOWNLOAD, token)
            .sendToTarget();
    }

    private void handleRequest(final Token token) {

        try {

            final String url = mRequestMap.get(token);
            if (url == null) {
                return;
            }

            Bitmap bitmap = mLRUCache.get(url);
            if (bitmap == null) {
                byte bitmapBytes[] = FlickrFetchr.getUrlBytes(url);
                bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                mLRUCache.put(url, bitmap);
            }
            Log.i(TAG, "Bitmap created");

            final Bitmap finalBitmap = bitmap;
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mRequestMap.get(token) != url) {
                        return;
                    }
                    mRequestMap.remove(token);
                    mListener.onThumbnailDownloaded(token, finalBitmap);
                }
            });

        } catch (IOException e) {
            Log.e(TAG, "Error downloading image", e);
        }
    }

    public void clearQueue() {
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        mRequestMap.clear();
    }
}
