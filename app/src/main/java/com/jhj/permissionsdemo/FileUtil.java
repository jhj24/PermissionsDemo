package com.jhj.permissionsdemo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

/**
 * 文件管理工具类
 * Created by jhj on 17-7-27.
 */

public class FileUtil {

    /**
     * 新建文件夹
     *
     * @param subDir 　文件夹名称
     * @return　文件夹路径
     */
    public static String getSDPath(String subDir) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            if (!path.endsWith("/"))
                path += "/";

            if (subDir != null && subDir.trim().length() > 0)
                path += (subDir + "/");

            File f = new File(path);

            if (!f.exists()) {
                if (f.mkdirs())
                    return path;
                else
                    return null;
            } else {
                if (f.isFile()) {
                    if (f.delete()) {
                        if (f.mkdir())
                            return path;
                        else
                            return null;
                    } else
                        return null;
                } else
                    return path;
            }
        }
        return null;
    }

    /**
     * 判断路径是否存在
     *
     * @param path 路径
     * @return　path
     */
    public static boolean isExist(String path) {
        if (path == null || path.trim().equals(""))
            return false;
        File f = new File(path);
        return f.exists();
    }

    /**
     * 获取指定文件大小
     *
     * @param file 文件
     * @return 文件的大小 （字节）
     */
    public static String getFileSize(File file) {
        String fileSize = null;
        if (file.exists()) {
            long size = file.length();
            if (size / 1024f / 1024f > 1) {
                float length = size / 1024f / 1024f;
                fileSize = String.format(Locale.CHINA, "%.2f", length) + "MB";
            } else if (size / 1024f > 1 && size / 1024f / 1024f < 1) {
                float length = size / 1024f;
                fileSize = String.format(Locale.CHINA, "%.2f", length) + "KB";
            } else {
                fileSize = size + "B";
            }

        }
        return fileSize;
    }

    /**
     * 删除
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file != null && file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete();
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (File file1 : files) { // 遍历目录下所有的文件
                    deleteFile(file1); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }

    /**
     * 获取文件的名称
     *
     * @param path 路径
     * @return 文件名
     */
    public static String getFileName(String path) {
        int start = path.lastIndexOf("/");
        int end = path.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return path.substring(start + 1);
        } else {
            return null;
        }
    }

    /**
     * 根据文件类型，打开文件
     *
     * @param context  context
     * @param filePath 文件路径
     */
    public static void openFile(Context context, String filePath) {
        try {

            if (TextUtils.isEmpty(filePath)) {
                return;
            }

            File file = new File(filePath);

            if (!file.exists()) {
                return;
            }

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            // 获取文件file的MIME类型
            String type = getMIMEType(file);
            // 设置intent的data和Type属性。
            intent.setDataAndType(Uri.fromFile(file), type);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "附件打开失败,请下载相关软件！", Toast.LENGTH_SHORT).show();
        }
    }

    private static String getMIMEType(File file) {

        String type = "*/*";

        if (file == null)
            return type;

        if (TextUtils.isEmpty(file.getName())) {
            return type;
        }

        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

        String ext = fName.substring(dotIndex, fName.length()).toLowerCase();
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (String[] aMIMEMapTable : MIMEMapTable) {
            if (ext.equals(aMIMEMapTable[0]))
                type = aMIMEMapTable[1];
        }
        return type;
    }

    private static final String[][] MIMEMapTable = {
            // {文件扩展名,MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"}, {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"},
            {".sh", "text/plain"}, {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"}, {".txt", "text/plain"},
            {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"}, {"", "*/*"}
    };

}
