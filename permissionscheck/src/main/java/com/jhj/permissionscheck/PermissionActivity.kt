package com.jhj.permissionscheck

import android.app.Activity
import android.os.Build
import android.os.Bundle

/**
 * 请求权限。
 * Created by jianhaojie on 2017/5/24.
 */

class PermissionActivity : Activity() {
    private var mRequestCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissions = intent.getStringArrayExtra(REQUEST_PERMISSIONS)
        mRequestCode = intent.getIntExtra(REQUEST_CODE, -1)
        if (permissions == null) {
            finish()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, mRequestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (mRequestCode == requestCode)
            mPermissionListener?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        finish()
    }

    internal interface PermissionListener {
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    }

    companion object {

        internal const val REQUEST_PERMISSIONS = "requestPermissions"
        internal const val REQUEST_CODE = "requestCode"

        private var mPermissionListener: PermissionListener? = null

        internal fun setPermissionListener(permissionListener: PermissionListener) {
            mPermissionListener = permissionListener
        }
    }
}
