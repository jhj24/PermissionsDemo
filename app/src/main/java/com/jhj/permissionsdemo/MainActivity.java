package com.jhj.permissionsdemo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jhj.permissionscheck.PermissionListener;
import com.jhj.permissionscheck.PermissionUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtil.INSTANCE.with(MainActivity.this)
                        .requestPermissions(Manifest.permission.CAMERA
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.ACCESS_COARSE_LOCATION)
                        .requestCode(100)
                        .callback(new PermissionListener() {
                            @Override
                            public void onPermissionsAllowed(int requestCode, @NotNull List<String> grantPermissions) {
                                for (String grantPermission : grantPermissions) {
                                    Log.w("allow", grantPermission);
                                }
                            }

                            @Override
                            public void onPermissionDenied(int requestCode, @NotNull List<String> deniedPermissions, @NotNull List<String> allPermissions) {
                                for (String grantPermission : deniedPermissions) {
                                    if (PermissionUtil.INSTANCE.isCamera()) {
                                        Log.w("camera", grantPermission);
                                    }
                                    Log.w("denied", grantPermission);
                                }
                            }
                        })
                        .prepare();
            }
        });
    }
}
