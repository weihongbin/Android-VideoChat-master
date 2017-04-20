package com.nercms;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by Administrator on 2015/12/4.
 */
public class ImageUtils {

    /**
     * 读取图片旋转的角度
     *
     * @param path 图片的绝对路径
     * @return 旋转的角度
     */
    public static int readImageDegree(String path) {
        int result = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int angle = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (angle) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    result = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    result = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    result = 270;
                    break;
                default:
                    result = 0;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 将图片旋转
     * @param angle 需要旋转的角度
     * @param bm 需要旋转的图片
     * @return 旋转后的图片
     */
    public static Bitmap rotateImage(int angle, Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotateBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        return rotateBitmap;
    }
    /**
     * 将图片旋转为镜像
     * @param bm 需要旋转的图片
     * @return 旋转后的图片
     */
    public static Bitmap convertImage(Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1);
        Bitmap rotateBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        return rotateBitmap;
    }

}
