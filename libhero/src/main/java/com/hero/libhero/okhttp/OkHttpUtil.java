package com.hero.libhero.okhttp;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.hero.libhero.mydb.LogUtil;
import com.hero.libhero.okhttp.cookie.SimpleCookieJar;
import com.hero.libhero.okhttp.https.HttpsUtils;
import com.hero.libhero.okhttp.https.MyCallBack;
import com.hero.libhero.okhttp.https.ReqProgressCallBack;
import com.hero.libhero.utils.JsonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import static android.content.ContentValues.TAG;

/**
 * @author qndroid
 * @function 用来发送get, post请求的工具类，包括设置一些请求的共用参数
 */
public class OkHttpUtil {
    private static OkHttpClient mOkHttpClient;
    public static Handler mMainHandler = new Handler(Looper.getMainLooper());


    private static int TIME_OUT = 45;


    public static OkHttpClient getmOkHttpClient() {
        return mOkHttpClient;
    }


    public static void initOkHttp() {

        if (mOkHttpClient == null) {

//            synchronized (OkHttpUtil.class) {
//                if (mOkHttpClient == null) {


            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            /**
             *  为所有请求添加请求头，看个人需求
             */
            //if (getHeaderMap() != null) {
            okHttpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request()
                            .newBuilder()
                            // 标明发送本次请求的客户端
                            .headers(getHeaders())
                            .build();
                    return chain.proceed(request);
                }
            });
            //}

            okHttpClientBuilder.cookieJar(new SimpleCookieJar());
            okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);//单位是秒
            okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
            okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
            okHttpClientBuilder.followRedirects(true);
            /**
             * trust all the https point
             */
            okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(),
                    HttpsUtils.initTrustManager());
            mOkHttpClient = okHttpClientBuilder.build();


//                }
//            }
        }


    }


    public static void doGetJsonStr(boolean isWenHao, String http_url, Map<String, String> map, MyCallBack myCallBack) {
        Request request = getStr(isWenHao, http_url, map);
        executeRequest(request, myCallBack);
    }

    public static void doGetJsonStrAsyn(boolean isWenHao, String http_url, Map<String, String> map, MyCallBack myCallBack) {
        Request request = getStr(isWenHao, http_url, map);
        enqueueRequest(request, myCallBack);
    }

    private static Request getStr(boolean isWenHao, String http_url, Map<String, String> map) {
        String str = "";
        if (null != map && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                str = str + entry.getKey() + "=" + entry.getValue() + "&";
            }
            str = str.substring(0, str.length() - 1);
            if (isWenHao) {
                http_url = http_url + "?" + str;//带问号的get请求
            } else {
                http_url = http_url + "&" + str;
            }
        }

        LogUtil.e("参数:"+ http_url);
        Request request = new Request.Builder()//创建Request 对象。
                .url(http_url)
                .get()
                .build();

        return request;
    }

    public static void doPostJsonStr(String http_url, Map<String, String> map, MyCallBack myCallBack) {
        Request request = getJsonStr(http_url, map);
        executeRequest(request, myCallBack);
    }

    public static void doPostJsonStrAsyn(String http_url, Map<String, String> map, MyCallBack myCallBack) {
        Request request = getJsonStr(http_url, map);
        enqueueRequest(request, myCallBack);
    }

    //后台可以使用Map 或对象接收
    private static Request getJsonStr(String http_url, Map<String, String> map) {
        //数据类型为json格式，
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String josnStr = JsonUtil.objToString(map);

        RequestBody jsonBody = RequestBody.create(JSON, josnStr);
        LogUtil.e("参数:http_url=" + http_url);
        LogUtil.e("参数:josnStr=" + josnStr);
        Request request = new Request.Builder()//创建Request 对象。
                .url(http_url)
                .post(jsonBody)//传递请求体
                .build();

        return request;
    }

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";

    /**
     * post,put,delete都需要body，但也都有body等于空的情况，
     * 此时也应该有body对象，但body中的内容为空
     */

    public static void doPostBody(String http_url, Map<String, String> map, MyCallBack myCallBack) {
        Request request = getBody(METHOD_POST, http_url, map);
        executeRequest(request, myCallBack);
    }


    public static void doPostBodyAsny(String http_url, Map<String, String> map, MyCallBack myCallBack) {
        Request request = getBody(METHOD_POST, http_url, map);
        enqueueRequest(request, myCallBack);
    }

    public static void doPutBodyAsny(String http_url, Map<String, String> map, MyCallBack myCallBack) {
        Request request = getBody(METHOD_PUT, http_url, map);
        enqueueRequest(request, myCallBack);
    }

    public static void doDeleteBodyAsny(String http_url, Map<String, String> map, MyCallBack myCallBack) {
        Request request = getBody(METHOD_DELETE, http_url, map);
        enqueueRequest(request, myCallBack);
    }


    //后台可以使用@RequestParam()接收
    private static Request getBody(String method, String http_url, Map<String, String> map) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (null != map && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                formBody.add(entry.getKey(), entry.getValue());
                LogUtil.e("参数:"+ entry.getKey() + "=" + entry.getValue());
            }
        }
        LogUtil.e("http_url:"+ http_url);
        RequestBody body = formBody.build();
        Request.Builder builder = new Request.Builder();
        if (method.equals(METHOD_POST)) {
            builder.post(body);
        } else if (method.equals(METHOD_PUT)) {
            builder.put(body);
        } else if (method.equals(METHOD_DELETE)) {
            builder.delete(body);
        }

        builder.url(http_url);

        //创建Request 对象。
        Request request = builder.build();

        return request;
    }

    ////////////////////////////文件操作/////////////////////////
    private static MediaType FILE_TYPE = null;//MediaType.parse("application/octet-stream");

    /**
     * 只有一个文件，且提交服务器时不用指定键，带键值对参数 不带上传进度
     */
    public static void setPostParameAndFile(String http_url, Map<String, Object> map,
                                            MyCallBack myCallBack) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);
        //追加参数
        for (String key : map.keySet()) {
            Object object = map.get(key);
            if (!(object instanceof File)) {
                builder.addFormDataPart(key, object.toString());
            } else {
                File file = (File) object;
                builder.addFormDataPart(key, file.getName(),
                        RequestBody.create(FILE_TYPE, file));
            }
        }
        //创建RequestBody
        RequestBody body = builder.build();
        //创建Request
        Request request = new Request.Builder()
                .url(http_url)
                .post(body)
                .build();

        enqueueRequest(request, myCallBack);
    }


    /**
     * 带参数带进度上传文件,带键值对参数
     */
    public static void uploadProgressParameAndFile(String http_url, Map<String, Object> map,
                                                   ReqProgressCallBack reqProgressCallBack) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);
        //追加参数
        for (String key : map.keySet()) {
            Object object = map.get(key);
            if (!(object instanceof File)) {
                builder.addFormDataPart(key, object.toString());
            } else {
                File file = (File) object;
                builder.addFormDataPart(key, file.getName(),
                        createProgressRequestBody(FILE_TYPE, file, reqProgressCallBack));
            }
        }
        //创建RequestBody
        RequestBody body = builder.build();
        //创建Request
        Request request = new Request.Builder()
                .url(http_url)
                .header("RANGE", "bytes=" + mAlreadyUpLength + "-" + mTotalLength)
                .post(body)
                .build();

        enqueueRequest(request, reqProgressCallBack);
    }

    /**
     * 下载文件
     *
     * @param fileUrl     文件url
     * @param saveFileDir 存储目标目录
     */

    public static void downLoadProgressFile(String fileUrl, final String saveFileDir,
                                            final ReqProgressCallBack myCallBack) {
        int index = fileUrl.lastIndexOf("/");
        String fileName = fileUrl.substring((index + 1));
        final File file = new File(saveFileDir, fileName);
        if (file.exists()) {
            file.delete();
            return;
        }
        final Request request = new Request.Builder().url(fileUrl).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e(e.getMessage());
                        String message = e.getMessage();
                        myCallBack.failBack(message, -1);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final int code = response.code();
                final boolean isSucess = response.isSuccessful();
                final String msg = response.message();
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    byte[] buf = new byte[2048];
                    long total = response.body().contentLength();
                    long current = 0;
                    int len = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        progressCallBack(total, current, myCallBack);
                    }
                    fos.flush();

                    if (isSucess) {
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                myCallBack.successBack("");
                            }
                        });
                    } else {
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                myCallBack.failBack(msg, code);
                            }
                        });
                    }


                } catch (IOException e) {
                    LogUtil.e(e.getMessage());
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            myCallBack.failBack(e.getMessage(), -1);
                        }
                    });

                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        LogUtil.e(e.getMessage());
                    }
                }

            }

        });
    }

    private static Map<String, String> headerMap;

    public static Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public static Headers getHeaders() {
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (getHeaderMap() != null) {
            for (Map.Entry<String, String> entry : getHeaderMap().entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
                LogUtil.e("请求头:"+ entry.getKey() + "=" + entry.getValue());
            }
        }

        Headers mHeader = mHeaderBuild.build();

        return mHeader;
    }


    /**
     * OkHttp的 execute 的方法是 同步方法
     * OkHttp的 enqueue 的方法是 异步方法
     */
    public static void executeRequest(Request request, final MyCallBack myCallBack) {


        final Call call = mOkHttpClient.newCall(request);
//            LogUtil.e("execute返回code", response.code() + "");
//            LogUtil.e("execute返回isSuccessful", response.isSuccessful() + "");
//            LogUtil.e("execute返回message", response.message() + "");
        try {
            final Response response = call.execute();
            if (response.isSuccessful()) {
                String res = response.body().string();
                myCallBack.successBack(res);
            } else {
                myCallBack.failBack(response.message(), response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void enqueueRequest(Request request, final MyCallBack myCallBack) {
        Call  call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {
                /**
                 * 此时还在非UI线程，因此要转发
                 */
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e(e.getMessage());
                        String message = e.getMessage();
                        myCallBack.failBack(message, -1);

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final int code = response.code();
                final boolean isSucess = response.isSuccessful();
                final String msg = response.message();
                LogUtil.e("enqueue返回code" + code + "");
                LogUtil.e("enqueue返回isSuccessful" + isSucess + "");
                LogUtil.e("enqueue返回message" + msg + "");

                final String res = response.body().string();
                LogUtil.e("enqueue返回data:"+ res);
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isSucess) {
                            myCallBack.successBack(res);
                        } else {
                            myCallBack.failBack(msg, code);
                        }
                    }
                });
            }
        });

    }


    /**
     * 创建带进度的RequestBody
     *
     * @param contentType MediaType
     * @param file        准备上传的文件
     * @param callBack    回调
     * @param //<T>
     * @return
     */

    public static long mAlreadyUpLength = 0;
    public static long mTotalLength = 0;



    private static RequestBody createProgressRequestBody(
            final MediaType contentType, final File file,
            final ReqProgressCallBack callBack) {
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        progressCallBack(remaining, current, callBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        return requestBody;
    }

    /**
     * 统一处理进度信息
     *
     * @param total    总计大小
     * @param current  当前进度
     * @param callBack
     * @param //<T>
     */
    private static void progressCallBack(final long total, final long current,
                                         final ReqProgressCallBack callBack) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    float aa = (float) current / total;
                    int percentage = (int) (aa * 100);

                    callBack.progressBack(total, current, percentage);
                }
            }
        });
    }
//    /**
//     * 指定cilent信任指定证书
//     *
//     * @param certificates
//     */
//    public static void setCertificates(InputStream... certificates) {
//        mOkHttpClient.newBuilder().sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null)).build();
//    }

}