package com.juns.wechat.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.juns.wechat.activity.CropImageActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 王者 on 2016/7/16.
 */
public class PhotoUtil {
    public static final String PHOTO_PATH = Environment.getExternalStorageDirectory().getPath() + "/wechat/img";

    static {
        File file = new File(PHOTO_PATH);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public static void takePhoto(Activity activity, int requestCode, String outputImgName){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(getFile(outputImgName)));
        activity.startActivityForResult(intent, requestCode);
    }

    public static void openAlbum(Activity activity, int requestCode){
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void cropView(Uri uri, int size, Activity activity, int requestCode, String outputImgName){
        Intent intent = new Intent(activity, CropImageActivity.class);
        intent.putExtra("uri", uri);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void saveBitmap(Bitmap bitmap, String fileName) {
        File f = new File(PHOTO_PATH, fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getFile(String imgName){
        File file = new File(PHOTO_PATH, imgName);
        if(file.getParent().isEmpty()){
            file.getParentFile().mkdirs();
        }
        return file;
    }
}
