package com.hero;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hero.libhero.mydb.LogUtil;
import com.hero.libhero.okhttp.OkHttpUtil;
import com.hero.libhero.okhttp.https.MyCallBack;
import com.hero.libhero.okhttp.https.ReqProgressCallBack;
import com.hero.libhero.utils.SharedUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hero.libhero.utils.GlobalUtil.getTwoPrice;

public class UploadAc extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_01)
    TextView tv_01;
    @BindView(R.id.tv_02)
    TextView tv_res2;
    @BindView(R.id.tv_03)
    TextView tv_res3;
    @BindView(R.id.tv_04)
    TextView tv04;


    @Override
    public int getLayout() {
        return R.layout.ac_download;
    }

    @Override
    public void initView() {



    }

    //上传文件 不带进度
    private void setPostParameAndFile() {
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

    //上传文件件 带进度
    private void uploadProgressParameAndFile() {
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
                    tv_res3.setText("失败" + res_msg);
                }

                @Override
                public void successBack(String res_data) {
                    tv_res3.setText("上传成功");
                }

                @Override
                public void progressBack(long total, long current, int percentage) {
                    float c = (float) (current / 1024.0 / 1024.0);
                    float t = (float) (total / 1024.0 / 1024.0);

                    tv_01.setText(percentage + "%" + "上传进度：" + getTwoPrice(c) + "MB/" + getTwoPrice(t) + "MB");


                    if (isFirst == false) {
                        isFirst=true;
                        progressBar.setMax(100);
                    }

                    progressBar.setProgress(percentage);


                }


            });
        }
    }


    @OnClick({R.id.tv_02, R.id.tv_03, R.id.tv_04})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_02:
                startDownload();
                break;
            case R.id.tv_03:
                break;
            case R.id.tv_04:
                break;
        }
    }

    boolean isFirst = false;



    private void startDownload() {

        uploadProgressParameAndFile();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
