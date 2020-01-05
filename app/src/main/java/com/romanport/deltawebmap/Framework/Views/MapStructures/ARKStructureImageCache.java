package com.romanport.deltawebmap.Framework.Views.MapStructures;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.util.HashMap;

public class ARKStructureImageCache {

    private Context ctx;
    private HashMap<String, Bitmap> structureImageCache;
    private Boolean isBitmapLoadComplete;
    private Bitmap unknownBitmap;

    public ARKStructureImageCache(Context ctx) {
        this.ctx = ctx;
        this.isBitmapLoadComplete = false;
        this.structureImageCache = new HashMap<>();
    }

    public void RunBackgroundThread() {
        try {
            //Quickly load the unknown bitmap
            unknownBitmap = LoadBitmap("structure_unknown.png");
        } catch (Exception ex) {
            Log.d("ARKStructureImageCache", "Failed to load the unknown bitmap! We probably won't be able to load others.");
        }

        //Run background thread
        ARKStructureImageCacheLoader loader = new ARKStructureImageCacheLoader();
        loader.start();
    }

    public Bitmap GetBitmap(String name) {
        //If we haven't loaded it yet, but will shortly. Hang and wait
        //(this WILL block the UI thread, but it would anyways if we attempted to load this)
        while(!structureImageCache.containsKey(name) && !isBitmapLoadComplete);

        //If we still haven't found it, default to the unknown image. This might be used if we add new structure images and the user doesn't update the app
        if(!structureImageCache.containsKey(name))
            return unknownBitmap;

        //Return this from the cache
        return structureImageCache.get(name);
    }

    private Bitmap LoadBitmap(String name) throws Exception {
        InputStream bitmap = ctx.getAssets().open(name);
        Bitmap bp = BitmapFactory.decodeStream(bitmap);
        return bp;
    }

    class ARKStructureImageCacheLoader extends Thread {

        public void run() {
            try {
                //Load list of structures
                String[] assets = ctx.getAssets().list("structures/");

                //Load each asset
                for(String a : assets) {
                    String name = a.substring(0, a.length() - 4);
                    Bitmap bp = LoadBitmap("structures/"+name+".png");
                    structureImageCache.put(name, bp);
                }

                //Log
                Log.d("ARKStructureCache", "Cache loader thread finished!");
            } catch (Exception ex) {
                Log.d("ARKStructureCache", "Cache loader thread crashed!");
            }
        }

    }

}
