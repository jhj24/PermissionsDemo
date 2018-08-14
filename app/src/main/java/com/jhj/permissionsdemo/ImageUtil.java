package com.jhj.permissionsdemo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

/**
 * 照片
 * Created by jhj on 17-8-3.
 */

public class ImageUtil {
    /**
     * 系统拍照
     *
     * @param act         　Activity
     * @param filePath    图片保存文件夹
     * @param requestCode requestCode
     * @return 照片路径
     */
    public static String openCamera(Activity act, String filePath, int requestCode) {
        String path = FileUtil.getSDPath(filePath);
        if (path == null) {
            return null;
        }

        String imgPath = path + UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
        Log.v("image", imgPath);
        File out = new File(imgPath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, out.getAbsolutePath());
            uri = act.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            uri = Uri.fromFile(out);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        try {
            act.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            return null;
        }
        return imgPath;
    }

    /**
     * 本地选取图片
     *
     * @param activity    Activity
     * @param requestCode requestCode
     */
    public static void getLocalImage(Activity activity, int requestCode) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            }
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "你的手机不支持选取图片", Toast.LENGTH_SHORT).show();
        }
    }
}
