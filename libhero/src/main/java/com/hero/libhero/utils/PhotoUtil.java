package com.hero.libhero.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hero.libhero.R;
import com.hero.libhero.mydb.LogUtil;
import com.hero.libhero.okhttp.OkHttpUtil;
import com.hero.libhero.okhttp.https.MyCallBack;
import com.hero.libhero.permissions.PermissionListener;
import com.hero.libhero.permissions.PermissionsUtil;
import com.hero.libhero.photopicker.PhotoPicker;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;


/**
 * Created by Administrator on 2016/11/5.
 */
public class PhotoUtil {

    public static final String Img_Key = "photopicker_imgpath";

    public static void SelectCamera(final Activity mActivity, final int PhotoCount, final int requestCode, final int requestCode2) {

        PermissionsUtil.requestPermission(mActivity, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                selectCameras(mActivity, PhotoCount, requestCode, requestCode2);

            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {

            }
        }, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA});


    }

    private static void selectCameras(final Activity mActivity, final int PhotoCount, final int requestCode, final int requestCode2) {
        View view = View.inflate(mActivity, R.layout.pop_select_pic, null);

        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(mActivity, R.style.common_dialog);
        dialog.setContentView(view);
        dialog.show();

        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);


        TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
        TextView tv_photo = (TextView) view.findViewById(R.id.tv_photo);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);

        tv_camera.getBackground().mutate().setAlpha(255);
        tv_photo.getBackground().mutate().setAlpha(255);
        tv_cancel.getBackground().mutate().setAlpha(255);

        //手机拍照
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    File filepath = new File(ActivityUtil.mSavePath + "/" + System.currentTimeMillis() + ".JPEG");
                    String imgpath = filepath.getAbsolutePath();


                    SharedUtil.putString(Img_Key, imgpath);

                    if (Build.VERSION.SDK_INT < 24) {//android  7.0以下
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filepath));
                        mActivity.startActivityForResult(intent, requestCode);
                    } else {


                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = FileProvider.getUriForFile(mActivity, ActivityUtil.getAppLastName() + ".fileprovider", filepath);//这里进行替换uri的获得方式
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag
                        mActivity.startActivityForResult(intent, requestCode);

                    }

                    Log.e("imgpath=00=", imgpath);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        //从手机选择
        tv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PhotoPicker.builder()
                        .setShowCamera(false)
                        .setPhotoCount(PhotoCount)
                        .setGridColumnCount(3)
                        .start(mActivity, requestCode2);

            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }


    //从相册选择照片后  获取它的路径
    public static String getPicPtah(Activity mActivity, Intent data, ImageView imageView) {
        String path = "";

        try {
            Uri originalUri = data.getData(); //获得图片的uri

            String[] proj = {MediaStore.Images.Media.DATA};


            ContentResolver cr = mActivity.getContentResolver();

            if (originalUri == null) {
                return path;
            }
            Cursor cursor = cr.query(originalUri, proj, null, null, null);
            if (cursor == null) {
                return path;
            } else {
                if (cursor.moveToFirst()) { //将Cursor移动到第一条记录
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    path = cursor.getString(column_index);
                    cursor.close(); //关闭Cursor c
                }
                if (!TextUtils.isEmpty(path)) {
                    GlideUtil.loadGifOrImg(mActivity, path, imageView);
                } else {
                    path = "";
                }

                return path;
            }
        } catch (NullPointerException e) {
            return path;
        }

    }

    /**
     * 获取相册路劲
     *
     * @return
     */
    public static String getAlbumPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();

    }


    public static String ImgToBase64(String imgpath) {
        String str64 = "";

        Bitmap bitmap = YaoSuo(imgpath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] b = baos.toByteArray();
        String size = String.format("%.2f", (double) b.length / 1024 / 1024);

        str64 = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("最后", size + "MB---" + str64.length());

        return str64;
    }


    private static Bitmap YaoSuo(String imgpath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imgpath);//此时返回bm为空
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        File file1 = new File(imgpath);
        Log.e("压缩前", "getWidth" + bitmap.getWidth() + "--getHeight" + bitmap.getHeight());

        Log.e("压缩前", "大小MB" + (double) file1.length() / 1024 / 1024);

        newOpts.inJustDecodeBounds = false;
        int w = bitmap.getWidth();//newOpts.outWidth;
        int h = bitmap.getHeight();//newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (w / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (h / hh);
        }
        // Log.e("压缩后be",  be+"--h"+h+"--w"+w);
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = 6;//设置缩放比例


        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgpath, newOpts);

        Log.e("比例压缩", "getWidth" + bitmap.getWidth() + "--getHeight" + bitmap.getHeight() + "--be" + be);


        //压缩完成后重新 输出到文件夹

        String newname = imgpath.substring(imgpath.lastIndexOf("/"));

        File file = new File(ActivityUtil.mSavePath + "/" + newname);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

            if (file.length() / 1024 > 500) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            }
            bos.flush();
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.e("压缩到", "大小kB" + (double) file.length() / 1024);

        return bitmap;
    }


    /*
     * 将base64字符串转换为bitmap  显示在界面上
     */
    public static Bitmap base64toBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    //压缩图片
    public String ImageToPath(String imgPath) {
        //Bitmap bitmap = BitmapFactory.decodeFile(imgPath, getBitmapOption(2));//imgPathToBitmap(imgPath);
        Bitmap bitmap = imgPathToBitmap(imgPath);
        if (bitmap != null) {
            return BitmapToOldPath(bitmap, imgPath);
        } else {
            return "";
        }
    }

    //2.图片按比例大小压缩方法
    public Bitmap imgPathToBitmap(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath);//此时返回bm为空
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            Log.e("压缩前", "getWidth" + bitmap.getWidth() + "--getHeight" + bitmap.getHeight());


            newOpts.inJustDecodeBounds = false;
            int w = bitmap.getWidth();//newOpts.outWidth;
            int h = bitmap.getHeight();//newOpts.outHeight;
            //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
            float hh = 800f;//这里设置高度为800f
            float ww = 480f;//这里设置宽度为480f
            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;//be=1表示不缩放
            if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
                be = (int) (w / ww);
            } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
                be = (int) (h / hh);
            }
            // Log.e("压缩后be",  be+"--h"+h+"--w"+w);
            if (be <= 0)
                be = 1;
            newOpts.inSampleSize = be;//设置缩放比例
            //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
            Log.e("压缩", "getWidth" + bitmap.getWidth() + "--getHeight" + bitmap.getHeight() + "--be" + be);
            return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
        } else {
            return null;
        }
    }


    //1.质量压缩方法
    public Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于400kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中


        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        Log.e("压缩后", (baos.toByteArray().length) / 1024 + "kb");
        return bitmap;
    }


    //替换原来的图片
    public String BitmapToOldPath(Bitmap bitmap, String oldimgPath) {
        String imgPath = "";

        String newname = oldimgPath.substring(oldimgPath.lastIndexOf("/"));

        File file = new File(ActivityUtil.mSavePath + "/" + newname);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

            if (file.length() / 1024 > 1024) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            }
            bos.flush();
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        imgPath = file.getAbsolutePath();
        Log.e("BitmapToPath", imgPath);

        return imgPath;
    }


    /////////////////////  以及返回imageid
    List<String> imgPathList;//上传图片 本地路径

    String http_url;//接口地址
    Activity mActivity;
    String file_parmar;//文件参数
    Map<String, Object> map_parmar;//参数
    FileBack fileBack;
    List<String> imgIDList;

    //图片类型 ：1head 2identity 3voice 4other 5complaint 6shopidentity 7insurance
    public PhotoUtil(
                     String http_url,
                     List<String> imgPathList,
                     String file_parmar,
                     Map<String, Object> map_parmar,
                     FileBack fileBack) {
        this.imgPathList = imgPathList;
        this.http_url = http_url;
        this.file_parmar = file_parmar;
        this.map_parmar = map_parmar;
        this.fileBack = fileBack;
        imgIDList = new ArrayList<>();
    }


    //上传多张图片
    public void UploadImg() {
        for (int i = 0; i < imgPathList.size(); i++) {
            getImgid2(imgPathList.get(i));
        }
    }


    public void getImgid2(String imgPath) {

        String newImgPath = ImageToPath(imgPath);

        File file = new File(newImgPath);

        if (file.exists()) {
            Log.e("新imgPath", "" + newImgPath);
            upload(newImgPath);
        } else {

            upload(imgPath);
        }
    }

    public interface FileBack {
        void getSuccessFile(List<String> resList);

        void getFailFile(String res_msg, int res_code);
    }

    private void upload(String newImgPath) {


        File file = new File(newImgPath);
        if (file.exists()) {
            LogUtil.e("文件存在--->file:" + newImgPath);

            Map<String, Object> map = new HashMap<>();
            map.put(file_parmar, file);

            if (null != map_parmar) {
                for (Map.Entry<String, Object> entry : map_parmar.entrySet()) {
                    map.put(entry.getKey(), entry.getValue());
                    LogUtil.e("参数:" + entry.getKey() + "=" + entry.getValue().toString());
                }
            }

            OkHttpUtil.setPostParameAndFile(http_url, map, new MyCallBack() {
                @Override
                public void failBack(String res_msg, int res_code) {

                    if (fileBack != null) {
                        fileBack.getFailFile(res_msg, res_code);
                    }
                }

                @Override
                public void successBack(String res_data) {

                    try {
                        if (fileBack != null) {
                            imgIDList.add(res_data);
                            if (imgPathList.size() == imgIDList.size()) {
                                fileBack.getSuccessFile(imgIDList);
                            }

                        }
                    } catch (Exception e) {
                        LogUtil.e("Exception=" + e.getMessage());
                        if (fileBack != null) {
                            fileBack.getFailFile(e.getMessage(), -1);
                        }
                    }
                }
            });
        }


    }

}
