package com.jhj.permissionscheck;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 权限请求工具类
 * Created by jhj on 17-8-9.
 */

public class PermissionsUtil {

    public static DefaultRequest with(Context context) {
        return new DefaultRequest(context);
    }

    /**
     * 对于6.0以下以及个别6.0类型手机禁止拍照权限后。能拍照但不能二维码扫描
     *
     * @return boolean false-权限不允许
     */
    public static boolean isCamera() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }

        }
        return isCanUse;
    }


    public static class DefaultRequest implements PermissionsActivity.PermissionListener {

        private Context context;
        private String[] mPermissions;
        private int mRequestCode = 1;
        private PermissionsListener mCallback;

        DefaultRequest(Context context) {
            this.context = context;
        }

        public DefaultRequest requestPermissions(String... permissions) {
            this.mPermissions = permissions;
            return this;
        }

        public DefaultRequest callback(PermissionsListener callback) {
            this.mCallback = callback;
            return this;
        }

        public void prepare() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { //Android 6.0之前不用检测
                mCallback.onPermissionsAllowed(mRequestCode, Arrays.asList(mPermissions));
            } else if (isPermissionsDenied(mPermissions)) { //如果有权限被禁止，进行权限请求
                requestPermissions();
            } else { // 所有权限都被允许，使用原生权限管理检验
                List<String> permissionList = getDeniedPermissions(context, mPermissions);
                if (!permissionList.isEmpty() && permissionList.size() > 0) {
                    mCallback.onPermissionDenied(mRequestCode, permissionList, Arrays.asList(mPermissions));
                } else {
                    mCallback.onPermissionsAllowed(mRequestCode, Arrays.asList(mPermissions));
                }
            }
        }


        private boolean hasCamera() {
            for (String mPermission : mPermissions) {
                if (Manifest.permission.CAMERA.equals(mPermission)) {
                    return true;
                }
            }
            return false;
        }


        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            List<String> permissionList = getDeniedPermissions(context, permissions);
            if (!permissionList.isEmpty() && permissionList.size() > 0) {
                mCallback.onPermissionDenied(requestCode, permissionList, Arrays.asList(permissions));
            } else {
                mCallback.onPermissionsAllowed(requestCode, Arrays.asList(permissions));
            }
        }

        /**
         * 检测权限是否被禁止
         *
         * @param permissions permissions
         * @return 被禁止的权限
         */
        private boolean isPermissionsDenied(String... permissions) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 权限被禁，进行权限请求
         */
        private void requestPermissions() {
            PermissionsActivity.setPermissionListener(this);
            Intent intent = new Intent(context, PermissionsActivity.class);
            intent.putExtra(PermissionsActivity.REQUEST_PERMISSIONS, mPermissions);
            intent.putExtra(PermissionsActivity.REQUEST_CODE, mRequestCode);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        /**
         * 原生权限管理检验
         *
         * @param context     context
         * @param permissions permissions
         * @return 被禁权限
         */
        private List<String> getDeniedPermissions(Context context, String... permissions) {
            List<String> list = new ArrayList<>();
            for (String permission : permissions) {
                String op = AppOpsManagerCompat.permissionToOp(permission);
                int result = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
                if (result == AppOpsManagerCompat.MODE_IGNORED) {
                    list.add(permission);
                } else if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    list.add(permission);
                }
            }
            return list;
        }
    }


}
