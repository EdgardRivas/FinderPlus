package com.apps.er.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader;

public class Singleton
{
    private static Context context;
    private static Singleton singleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private Singleton(Context context)
    {
        Singleton.context = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache()
        {
            private final LruCache<String, Bitmap>
            cache = new LruCache<String, Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url)
            {
                return cache.get(url);
            }
            @Override
            public void putBitmap(String url, Bitmap bitmap)
            {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized Singleton getInstance(Context context)
    {
        if(singleton == null)
            singleton = new Singleton(context);
        return singleton;
    }

    public RequestQueue getRequestQueue()
    {
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        return requestQueue;
    }

    public<T> void addToRequest(Request<T> request)
    {
        requestQueue.add(request);
    }

    public ImageLoader getImageLoader()
    {
        return imageLoader;
    }
}
