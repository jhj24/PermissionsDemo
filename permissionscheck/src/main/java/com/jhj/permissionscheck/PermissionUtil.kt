package com.jhj.permissionscheck

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import android.support.v4.app.AppOpsManagerCompat
import android.support.v4.content.ContextCompat
import java.util.*


/**
 * 权限请求工具类
 * Created by jhj on 17-8-9.
 */

object PermissionUtil {

    val isCamera: Boolean
        get() {
            var isCanUse = true
            var mCamera: Camera? = null
            try {
                mCamera = Camera.open()
                val mParameters = mCamera?.parameters
                mCamera.parameters = mParameters
            } catch (e: Exception) {
                isCanUse = false
            }

            if (mCamera != null) {
                try {
                    mCamera.release()
                } catch (e: Exception) {
                    e.printStackTrace()
                    return isCanUse
                }

            }
            return isCanUse
        }

    fun with(context: Context): DefaultRequest {
        return DefaultRequest(context)
    }


    class DefaultRequest internal constructor(private val context: Context) : PermissionActivity.PermissionListener {
        private var mPermissions: Array<out String>? = null
        private var mRequestCode: Int = 0
        private var mCallback: PermissionListener? = null

        fun requestPermissions(vararg permissions: String): DefaultRequest {
            this.mPermissions = permissions
            return this
        }

        fun requestCode(requestCode: Int): DefaultRequest {
            this.mRequestCode = requestCode
            return this
        }

        fun callback(callback: PermissionListener): DefaultRequest {
            this.mCallback = callback
            return this
        }

        fun prepare() {
            mPermissions?.let {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { //Android 6.0之前不用检测
                    mCallback?.onPermissionsAllowed(mRequestCode, Arrays.asList(*it))
                } else if (isPermissionsDenied(*it)) { //如果有权限被禁止，进行权限请求
                    requestPermissions()
                } else { // 所有权限都被允许，使用原生权限管理检验
                    val permissionList = getDeniedPermissions(context, *it)
                    if (!permissionList.isEmpty() && permissionList.isNotEmpty()) {
                        mCallback?.onPermissionDenied(mRequestCode, permissionList, Arrays.asList(*it))
                    } else {
                        mCallback?.onPermissionsAllowed(mRequestCode, Arrays.asList(*it))
                    }
                }
            }

        }


        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
            val permissionList = getDeniedPermissions(context, *permissions)
            mCallback?.onPermissionDenied(requestCode, permissionList, Arrays.asList(*permissions))
            mCallback?.onPermissionsAllowed(requestCode, Arrays.asList(*permissions))
        }

        /**
         * 检测权限是否被禁止
         *
         * @param permissions permissions
         * @return 被禁止的权限
         */
        private fun isPermissionsDenied(vararg permissions: String): Boolean {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return true
                }
            }
            return false
        }

        /**
         * 权限被禁，进行权限请求
         */
        private fun requestPermissions() {
            PermissionActivity.setPermissionListener(this)
            val intent = Intent(context, PermissionActivity::class.java)
            intent.putExtra(PermissionActivity.REQUEST_PERMISSIONS, mPermissions)
            intent.putExtra(PermissionActivity.REQUEST_CODE, mRequestCode)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        /**
         * 原生权限管理检验
         *
         * @param context     context
         * @param permissions permissions
         * @return 被禁权限
         */
        private fun getDeniedPermissions(context: Context, vararg permissions: String): List<String> {
            val list = ArrayList<String>()
            for (permission in permissions) {
                val op = AppOpsManagerCompat.permissionToOp(permission) ?: return list
                val result = AppOpsManagerCompat.noteProxyOp(context, op, context.packageName)
                if (result == AppOpsManagerCompat.MODE_IGNORED) {
                    list.add(permission)
                } else if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    list.add(permission)
                }
            }
            return list
        }
    }


}
