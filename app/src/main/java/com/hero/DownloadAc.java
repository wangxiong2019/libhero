package com.hero;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hero.libhero.mydb.LogUtil;
import com.hero.libhero.okhttp.OkHttpUtil;
import com.hero.libhero.okhttp.download.ProgressDownloader;
import com.hero.libhero.okhttp.download.ProgressListener;
import com.hero.libhero.okhttp.https.ReqProgressCallBack;
import com.hero.libhero.utils.ActivityUtil;
import com.hero.libhero.utils.SharedUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hero.libhero.utils.GlobalUtil.getTwoPrice;

public class DownloadAc extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_01)
    TextView tv_01;
    @BindView(R.id.tv_02)
    TextView tv_res2;
    @BindView(R.id.tv_03)
    TextView tv03;
    @BindView(R.id.tv_04)
    TextView tv04;


    @Override
    public int getLayout() {
        return R.layout.ac_download;
    }

    @Override
    public void initView() {

        breakPoints = SharedUtil.getLong("apk_long");
        initFile();

    }

    //下载文件 带进度
    private void downLoadProgressFile() {
        ActivityUtil.IsHasSD();
        String save_path = ActivityUtil.mSavePath;

        OkHttpUtil.downLoadProgressFile(file_path, save_path, new ReqProgressCallBack() {
            @Override
            public void failBack(String res_msg, int res_code) {
                tv_res2.setText(res_msg);
            }

            @Override
            public void successBack(String res_data) {
                tv_res2.setText("下载完成");

            }

            @Override
            public void progressBack(long total, long current,int percentage) {
                float c = (float) (current / 1024.0 / 1024.0);
                float t = (float) (total / 1024.0 / 1024.0);

                tv_res2.setText(percentage+"%下载进度：" + getTwoPrice(c) + "MB/" + getTwoPrice(t) + "MB");
            }


        });
    }

    String file_path = "http://www.fastpaotui.com/uploadfile/wang/ipaotui_new.apk";

    @OnClick({R.id.tv_02, R.id.tv_03, R.id.tv_04})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_02:
                startDownload();
                break;
            case R.id.tv_03:
                pauseDownload();
                break;
            case R.id.tv_04:
                continueDownload();
                break;
        }
    }

    long total;
    long lastBytes;
    int max;
    private long breakPoints;
    private ProgressDownloader downloader;

    private void initFile() {


        downloader = new ProgressDownloader(breakPoints, file_path, ActivityUtil.mSavePath, new ProgressListener() {
            @Override
            public void onPreExecute(long contentLength) {
                // 文件总长只需记录一次，要注意断点续传后的contentLength只是剩余部分的长度
                LogUtil.e(total + "==========" + contentLength);
                if (total == 0L) {
                    total = contentLength;
                    max = (int) (contentLength / 1024);
                    progressBar.setMax(max);

                }
            }

            @Override
            public void update(long totalBytes, boolean done) {
                // 注意加上断点的长度

                lastBytes = totalBytes + breakPoints;
                int current = (int) lastBytes / 1024;
                progressBar.setProgress(current);

                float aa = ((float) current / max);

                float pro = aa * 100;
                int Progress = (int) pro;
                LogUtil.e(+current + "/" + max + "=" + aa + "==========" + pro);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (done) {
                            Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();
                            tv_01.setText("下载完成");
                            lastBytes = 0;
                        } else {
                            tv_01.setText(Progress + "%");
                        }
                    }
                });


            }
        });
    }

    private void startDownload() {

        breakPoints = 0;
        lastBytes = 0;
        downloader.download();
    }

    void pauseDownload() {
        downloader.pause();
        // 存储此时的totalBytes，即断点位置。
        breakPoints = lastBytes;

        Toast.makeText(mContext, "下载暂停", Toast.LENGTH_SHORT).show();
    }

    void continueDownload() {
        downloader.download();
        Toast.makeText(mContext, "下载继续", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("退出,已下载：" + lastBytes);
        isDone();
    }

    private void isDone() {
        if (lastBytes > 0) {
            pauseDownload();
            SharedUtil.putLong("apk_long", lastBytes);
        }
    }
}
