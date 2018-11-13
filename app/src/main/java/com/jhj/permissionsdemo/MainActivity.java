package com.jhj.permissionsdemo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jhj.permissionscheck.OnPermissionsListener;
import com.jhj.permissionscheck.PermissionsRequest;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PermissionsRequest.Builder(MainActivity.this)
                        .requestPermissions(Manifest.permission.CAMERA
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.ACCESS_COARSE_LOCATION)
                        .callback(new OnPermissionsListener() {
                            @Override
                            public void onPermissions(List<String> deniedPermissions, List<String> allPermissions) {
                                if (deniedPermissions.size() > 0) {
                                    System.out.print("有权限被禁止");
                                }
                            }
                        })
                        .build();
            }
        });
    }


}
