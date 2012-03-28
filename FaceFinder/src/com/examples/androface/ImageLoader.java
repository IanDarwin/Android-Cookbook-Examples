/**
 * 
 */
package com.examples.androface;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * @author wdavid01
 * 
 */
public class ImageLoader
  {
    public static final String tag = "ImageLoader";
    public static final String IMAGE_FILE_PATH = "file:///android_asset/";
    private Context _context = null;
    private static ImageLoader uniqueInstance = null;
    private final Bitmap imageBmp = null;

    public ImageLoader(Context context)
      {
        _context = context;
      }

    public static ImageLoader getInstance(Context context)
      {
        if (uniqueInstance == null)
          {
            uniqueInstance = new ImageLoader(context);
          }
        return uniqueInstance;
      }

    public Bitmap loadFromFile(String pathName)
      {
        Bitmap bitmap = null;
        try
          {
            String imgFilepath = pathName;
            Log.d(tag, "ImageFile: " + imgFilepath);
            bitmap = BitmapFactory.decodeStream(this._context.getResources().getAssets().open(imgFilepath));

          }
        catch (IOException e)
          {
            e.printStackTrace();
          }
        return bitmap;
      }

    public void loadFromURL(String url)
      {

      }

  }
