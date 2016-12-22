package cc.wo_mo.dubi.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.picasso.Transformation;

import static android.R.attr.bitmap;

/**
 * Created by womo on 2016/12/21.
 */

public class ProcessBitmap implements Transformation {
    public static final int MODE_SQUARE = 0;
    public static final int MODE_NORMAL = 1;
    public static final int MODE_CIRCLE = 2;
    public static final int MODE_FIX_SIZE = 3;
    private int targetWidth, targetHeight;
    private int mode;

    public ProcessBitmap(int mode, @Nullable Integer width, @Nullable Integer height) {
        this.mode = mode;
        this.targetWidth = width == null ? 0 : width;
        this.targetHeight = height == null ? 0 : height;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        switch (mode) {
            case MODE_SQUARE:
                source = fitWidth(source);
                Log.d("cut", "fuck");
                source = square(source);
                break;
            case MODE_NORMAL:
                source = fitWidth(source);
                break;
            case MODE_FIX_SIZE:
                source = fitWidth(source);
                source = cropCenterHeight(source);
                break;
            case MODE_CIRCLE:
                source = fitWidth(source);
                Log.d("cut", "fuck");
                source = square(source);
                source = circle(source);
        }
        return source;
    }

    private Bitmap circle(Bitmap source) {
        int size = source.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP,
                BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);
        float r = size/2f;
        canvas.drawCircle(r, r, r, paint);
        source.recycle();
        return bitmap;
    }

    private Bitmap square(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap result = Bitmap.createBitmap(source, x, y, size, size, null, false);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    private Bitmap fitWidth(Bitmap source) {
        float scale = 1.0f*targetWidth/source.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        Bitmap result = Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,true);
        if (source !=  result) {
            source.recycle();
        }
        return result;
    }

    private Bitmap cropCenterHeight(Bitmap source) {
        if (source.getHeight() > targetHeight) {
            Bitmap result = Bitmap.createBitmap(source, 0, (source.getHeight()-targetHeight)/2,
                    source.getWidth(), targetHeight);
            if (result != source) {
                source.recycle();
            }
            source = result;
        }
        return source;
    }


    @Override
    public String key() {
        return "ProcessBitmap()";
    }
}
