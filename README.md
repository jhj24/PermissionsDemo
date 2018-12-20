# PermissionsRequest
commit
### 使用
```
new PermissionsRequest.Builder(MainActivity.this)
                      .requestPermissions(Manifest.permission.CAMERA
                               , Manifest.permission.WRITE_EXTERNAL_STORAGE
                               , Manifest.permission.READ_EXTERNAL_STORAGE
                               , Manifest.permission.ACCESS_FINE_LOCATION
                               , Manifest.permission.ACCESS_COARSE_LOCATION)
                      .callback(new OnPermissionsListener() {
                           /**
                           * 请求完成回调
                           *
                           * @param deniedPermissions 被禁止的权限
                           * @param allPermissions 所请求的全部权限
                           */
                           @Override
                           public void onPermissionsResult(List<String> deniedPermissions, List<String> allPermissions) {
                               if (deniedPermissions.size() > 0) {
                                   System.out.print("有权限被禁止");
                               }
                           }
                      })
                      .build();
```

### Android 8.0

#### 简介

在 Android 8.0 之前，如果应用在运行时请求权限并且被授予该权限，系统会错误地将属于同一权限组并且在清单中注册的其他权限也一起授予应用。

- 以前，申请一个子权限会自动获取权限组中其他子权限。组内其他子权限可以直接使用。
- 现在，申请一个子权限，组内其他子权限不会自动获取。使用组内其他子权限的时候。需要再次申请。（但是这种情况不会弹出系统的权限申请框）如果不申请。会FC。

例如，假设某个应用在其清单中列出 READ_EXTERNAL_STORAGE 和 WRITE_EXTERNAL_STORAGE。应用请求 READ_EXTERNAL_STORAGE，并且用户授予了该权限。如果该应用针对的是 API 级别 24 或更低级别，系统还会同时授予 WRITE_EXTERNAL_STORAGE，因为该权限也属于同一 STORAGE 权限组并且也在清单中注册过。如果该应用针对的是 Android 8.0，则系统此时仅会授予 READ_EXTERNAL_STORAGE；不过，如果该应用后来又请求 WRITE_EXTERNAL_STORAGE，则系统会立即授予该权限，而不会提示用户。

#### 解决方案
同组权限一起申请。当我们申请权限时。申请同组的多个权限时，也只会弹出一次申请框。所以不如一起申请。

```
 //Storage权限
 protected static String[] ABS_STORAGE = new String[] {
      Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
      };
      
 //Phone权限
 protected static String[] ABS_PHONE = new String[] {
          Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE,
          Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG,
          Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS
      };

```
