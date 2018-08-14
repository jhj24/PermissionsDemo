package com.jhj.permissionsdemo;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jhj.permissionscheck.PermissionsListener;
import com.jhj.permissionscheck.PermissionsUtil;

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
                PermissionsUtil.with(MainActivity.this)
                        .requestPermissions(Manifest.permission.CAMERA
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.ACCESS_COARSE_LOCATION)
                        .callback(new PermissionsListener() {
                            @Override
                            public void onPermissionsAllowed(int requestCode, @NotNull List<String> grantPermissions) {
                                for (String grantPermission : grantPermissions) {
                                    Log.w("allow", grantPermission);
                                }
                                ImageUtil.openCamera(MainActivity.this,"/image",100);
                            }

                            @Override
                            public void onPermissionDenied(int requestCode, @NotNull List<String> deniedPermissions, @NotNull List<String> allPermissions) {
                                for (String grantPermission : deniedPermissions) {
                                    if (PermissionsUtil.isCamera()) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            Toast.makeText(MainActivity.this, "xxx", Toast.LENGTH_SHORT).show();
        }
    }
}
