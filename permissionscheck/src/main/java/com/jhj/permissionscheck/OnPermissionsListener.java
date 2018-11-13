package com.jhj.permissionscheck;

import java.util.List;

/**
 * 请求权限回调。
 * Created by jianhaojie on 2017/5/24.
 */

public interface OnPermissionsListener {

    void onPermissions(List<String> deniedPermissions, List<String> allPermissions);

}
