package com.hero;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.hero.libhero.okhttp.https.MyCallBack;
import com.hero.libhero.okhttp.OkHttpUtil;
import com.hero.libhero.okhttp.https.ReqProgressCallBack;
import com.hero.libhero.utils.ActivityUtil;
import com.hero.libhero.utils.JsonUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.hero.libhero.utils.GlobalUtil.getTwoPrice;

public class MainActivity extends AppCompatActivity {
    public static String apt_ip = "https://api.ipaotui.com/index.php?m=Api&c=Common&a=checkUpdate";//正式服务器地址

    TextView tv_res, tv_res2,tv_res3;
    public static  String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_res = findViewById(R.id.tv_res);
        tv_res2 = findViewById(R.id.tv_res2);
        tv_res3= findViewById(R.id.tv_res3);

        //baidu();

        //postData1();

        //postData2();

        PermissionsUtil.requestPermission(MainActivity.this, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
               file();
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {

            }
        }, needPermissions);
    }

    private void file(){
        //postFile1();
        postFile2();

        //dowmloadFile1();

        //downLoadProgressFile();
    }


    private void baidu(){
        String url = "https://www.baidu.com/";

        OkHttpUtil.doGetJsonStrAnsy(false, url, null, new MyCallBack() {

            @Override
            public void failBack(String res_msg, int res_code) {

            }

            @Override
            public void successBack(String res_data) {

            }
        });

    }

    private void postFile1() {
        String pic_path = Environment.getExternalStorageDirectory() + "/app_bingbinggou/1577351213426.JPEG";

        File file = new File(pic_path);
        if (file.exists()) {
            Log.e("file=", "文件存在" + pic_path);
            String http_url = "http://www.fastpaotui.com/App/FileApi/UploadPic";

            Map<String, Object> map = new HashMap<>();
            map.put("file_type", "wang");
            map.put("picture", file);

            OkHttpUtil.setPostParameAndFile(http_url, map, new MyCallBack() {
                @Override
                public void failBack(String res_msg, int res_code) {

                }

                @Override
                public void successBack(String res_data) {
                    tv_res2.setText("上传成功");
                }


            });
        }
    }
    private void postFile2() {
        String pic_path = Environment.getExternalStorageDirectory() + "/tencent/QQfile_recv/ipaotui_new.apk";

        File file = new File(pic_path);
        if (file.exists()) {
            Log.e("file=", "文件存在" + pic_path);
            String http_url = "http://www.fastpaotui.com/App/FileApi/UploadFile";

            Map<String, Object> map = new HashMap<>();
            map.put("file_type", "apk");
            map.put("file", file);

            OkHttpUtil.uploadProgressParameAndFile(http_url, map, new ReqProgressCallBack() {
                @Override
                public void failBack(String res_msg, int res_code) {

                }

                @Override
                public void successBack(String res_data) {
                    tv_res2.setText("上传成功");
                }

                @Override
                public void progressBack(long total, long current) {
                    tv_res3.setText("进度："+current+"/"+total);
                }


            });
        }
    }

    private void postData1() {
        //同步请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<>();
                map.put("platform", "android");
                map.put("vesion", "4.0.0");
                map.put("app", "apt");
                OkHttpUtil.doPostAsny(apt_ip, map, new MyCallBack() {
                    @Override
                    public void failBack(String res_msg, int res_code) {

                    }

                    @Override
                    public void successBack(final String res_data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//Log.e("res",res_data);
                                ResData resData = JsonUtil.dataToClass(res_data, ResData.class);
                                tv_res2.setText(resData.getData().toString());

                            }
                        });
                    }


                });

            }
        }).start();


    }

    private void postData2() {
        //异步请求
        Map<String, String> map = new HashMap<>();
        map.put("platform", "android");
        map.put("vesion", "4.0.0");
        map.put("app", "apt");
        String http_url = "http://www.fastpaotui.com/App/CommonApi/GetProvinceList";
        //String http_url = "http://192.168.0.5:8081/App/CommonApi/GetProvinceList";

        OkHttpUtil.doPostAsny(http_url, map, new MyCallBack() {
            @Override
            public void failBack(String res_msg, int res_code) {

            }

            @Override
            public void successBack(String res_data) {
                ResData resData = JsonUtil.dataToClass(res_data, ResData.class);

                List<DistrictBean> list = JsonUtil.dataToList(resData.getData().toString(), DistrictBean.class);
                tv_res.setText(list.get(0).getProvince_name() + "");

            }

        });
    }



    private void downLoadProgressFile(){
        String file_path="http://www.fastpaotui.com/uploadfile/wang/ipaotui_new.apk";
        ActivityUtil.IsHasSD();
        String save_path= ActivityUtil.mSavePath;

        OkHttpUtil.downLoadProgressFile(file_path, save_path, new ReqProgressCallBack() {
            @Override
            public void failBack(String res_msg, int res_code) {

            }

            @Override
            public void successBack(String res_data) {
                tv_res2.setText("下载完成");

            }

            @Override
            public void progressBack(long total, long current) {
                float c= (float) (current/1024.0/1024.0);
                float t= (float) (total/1024.0/1024.0);

                tv_res3.setText("进度："+getTwoPrice(c)+"MB/"+getTwoPrice(t)+"MB");
            }




        });
    }
}
