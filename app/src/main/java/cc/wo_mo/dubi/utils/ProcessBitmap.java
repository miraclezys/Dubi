package cc.wo_mo.dubi.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.Nullable;

import com.squareup.picasso.Transformation;

/**
 * Created by womo on 2016/12/21.
 */

public class ProcessBitmap implements Transformation {
    public static final int MODE_SQUARE = 0;
    public static final int MODE_NORMAL = 1;
    public static final int MODE_FIX_SIZE = 2;
    private int targetWidth, targetHeight;
    private int mode;

    public ProcessBitmap(int mode, @Nullable Integer width, @Nullable Integer height) {
        this.mode = mode;
        this.targetWidth = width == null ? 0 : width;
        this.targetHeight = height == null ? 0 : height;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap result = null;
        float scale = 0;
        Matrix matrix = null;
        switch (mode) {
            case MODE_SQUARE:
                int size = Math.min(source.getWidth(), source.getHeight());
                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;
                result = Bitmap.createBitmap(source, x, y, size, size);
                if (result != source) {
                    source.recycle();
                }
                source = result;
            case MODE_NORMAL:
                scale = 1.0f*targetWidth/source.getWidth();
                if (scale < 1.0) {
                    matrix = new Matrix();
                    matrix.postScale(scale, scale); //长和宽放大缩小的比例
                    result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
                    if (source != result) {
                        source.recycle();
                    }
                    source = result;
                }
                break;
            case MODE_FIX_SIZE:
                scale = 1.0f*targetWidth/source.getWidth();
                matrix = new Matrix();
                matrix.postScale(scale, scale); //长和宽放大缩小的比例
                result = Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,true);
                if (source !=  result) {
                    source.recycle();
                }
                source = result;
                if (source.getHeight() > targetHeight) {
                    result = Bitmap.createBitmap(source, 0, (source.getHeight()-targetHeight)/2,
                            source.getWidth(), targetHeight);
                    if (result != source) {
                        source.recycle();
                    }
                    source = result;
                }
        }
        return source;
    }

    @Override
    public String key() {
        return "ProcessBitmap()";
    }
}
