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
    private static PermissionsListener mPermissionsListener;
    private int mRequestCode = 0x10000000;

    static void setPermissionListener(PermissionsListener permissionsListener) {
        mPermissionsListener = permissionsListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissions = getIntent().getStringArrayExtra(REQUEST_PERMISSIONS);
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
        if (mRequestCode == requestCode) {
            mPermissionsListener.onRequestPermissionsResult(permissions, grantResults);
        }
        finish();
    }

    interface PermissionsListener {
        void onRequestPermissionsResult(String[] permissions, int[] grantResults);
    }
}
