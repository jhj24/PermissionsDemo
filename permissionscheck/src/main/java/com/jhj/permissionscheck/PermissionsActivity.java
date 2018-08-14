package com.jhj.permissionscheck;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * 请求权限。
 * Created by jianhaojie on 2017/5/24.
 */

public final class PermissionsActivity extends Activity {

    static final String REQUEST_PERMISSIONS = "requestPermissions";
    static final String REQUEST_CODE = "requestCode";

    private static PermissionListener mPermissionListener;
    private int mRequestCode;

    static void setPermissionListener(PermissionListener permissionListener) {
        mPermissionListener = permissionListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissions = getIntent().getStringArrayExtra(REQUEST_PERMISSIONS);
        mRequestCode = getIntent().getIntExtra(REQUEST_CODE, -1);
        if (permissions == null) {
            finish();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, mRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mRequestCode == requestCode)
            mPermissionListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
        finish();
    }

    interface PermissionListener {
        void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
    }
}
