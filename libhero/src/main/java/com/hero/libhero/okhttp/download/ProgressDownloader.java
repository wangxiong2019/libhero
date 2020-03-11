package com.hero.libhero.okhttp.download;

import com.hero.libhero.okhttp.OkHttpUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ProgressDownloader {
    public static final String TAG = "ProgressDownloader";
    ProgressListener progressListener;

    private String url;
    private OkHttpClient client;
    private File file;
    private Call call;


    public ProgressDownloader(long breakPoints, String fileUrl, String saveFileDir, ProgressListener progressListener) {
        this.url = fileUrl;
        this.progressListener = progressListener;
        //在下载、暂停后的继续下载中可复用同一个client对象
        client = OkHttpUtil.getmOkHttpClient();

        int index = fileUrl.lastIndexOf("/");
        String fileName = fileUrl.substring((index + 1));
        file = new File(saveFileDir, fileName);
        boolean exist = file.exists();
        if (exist == true && breakPoints == 0) {
            file.delete();
        } else if (exist == true && breakPoints > 0) {
            startsPoint = breakPoints;
            download();
        } else if (exist == false && breakPoints > 0) {
            startsPoint = 0;
        } else if (exist == false && breakPoints == 0) {
            startsPoint = 0;
        }
    }

    //每次下载需要新建新的Call对象
    private Call newCall(long startPoints) {
        Request request = new Request.Builder()
                .url(url)
                .header("RANGE", "bytes=" + startPoints + "-")//断点续传要用到的，指示下载的区间
                .build();
        return client.newCall(request);
    }


    // startsPoint指定开始下载的点
    long startsPoint;

    public void download() {
        call = newCall(startsPoint);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                save(response, startsPoint);
            }
        });
    }

    public void pause() {
        if (call != null) {
            call.cancel();
        }
    }

    private void save(Response response, long startsPoint) {
        ResponseBody body = response.body();
        InputStream in = body.byteStream();
        FileChannel channelOut = null;
        // 随机访问文件，可以指定断点续传的起始位置
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rwd");
            //Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，
            // 亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
            channelOut = randomAccessFile.getChannel();
            // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，
            // 在这里指定下载位置。
            MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                    startsPoint, body.contentLength());
            byte[] buffer = new byte[2048];
            int len;
            while ((len = in.read(buffer)) != -1) {
                mappedBuffer.put(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
