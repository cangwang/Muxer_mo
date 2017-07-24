package cangwang.com.base.utils;

import android.graphics.Bitmap;
import android.util.LruCache;


/**
 * Created by air on 16/10/14.
 */

public class ImageLoader {

    private int maxMemory = (int) Runtime.getRuntime().maxMemory();
    private int mCacheSize = maxMemory / 8;

    private LruCache<String,Bitmap> lruCache = new LruCache<String,Bitmap>(mCacheSize){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }
    };

    private volatile static ImageLoader instane;

    public static ImageLoader getInstane(){
        if(instane == null){
            synchronized (ImageLoader.class){
                if(instane == null)
                    instane = new ImageLoader();
            }
        }
        return instane;
    }

    public void addImage(String key,Bitmap bitmap){
        if(lruCache.get(key) == null & bitmap !=null){
            lruCache.put(key,bitmap);
        }
    }

    public Bitmap getImage(String key){
        return lruCache.get(key);
    }

    public boolean isBimapExist(String key){
        return lruCache.get(key) != null;
    }
}
