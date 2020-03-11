package com.hero;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.hero.libhero.mydb.DbUtil;
import com.hero.libhero.mydb.LogUtil;
import com.hero.libhero.okhttp.OkHttpUtil;
import com.hero.libhero.okhttp.https.MyCallBack;
import com.hero.libhero.okhttp.https.ReqProgressCallBack;
import com.hero.libhero.utils.ActivityUtil;
import com.hero.libhero.utils.GlideUtil;
import com.hero.libhero.utils.JsonUtil;
import com.hero.libhero.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hero.libhero.utils.GlobalUtil.getTwoPrice;

public class MainActivity extends BaseActivty {


    /**
     * 自动绑定控件
     * 1.安装插件 Butterknife Zelezny
     * 2.将光标放到布局文件：R.layout.activity_main，然后右键Generate就有了
     * 3.点击选择  generate butterknife injectios
     * 这里需要注意的是在勾选控件的界面上，有一个CreateViewHolder ,
     * 很明显这个是专门为ListView或者RecyclerView的适配器专门提供的。
     */
    @BindView(R.id.tv_res)
    TextView tv_res;
    @BindView(R.id.tv_res0)
    TextView tv_res0;
    @BindView(R.id.tv_res1)
    TextView tv_res1;
    @BindView(R.id.tv_res2)
    TextView tv_res2;
    @BindView(R.id.tv_res3)
    TextView tv_res3;
    @BindView(R.id.iv_01)
    ImageView iv_01;



    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        //设置侵入式状态栏
        StatusBarUtils.translucent(mActivity);
        //设置状态栏字体黑色
        StatusBarUtils.setStatusBarLightMode(mActivity);

        //EventBus    1.注册事件
        EventBus.getDefault().register(this);

        doGet();

        doPostBody();

        doPostJsonStrAsyn();

        initDb();

        pic();

        PermissionsUtil.requestPermission(MainActivity.this, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {

            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {

            }
        }, needPermissions);
    }

    private void pic(){
        String img_url="http://img.hb.aicdn.com/43adcee6fa8d59ceb3219f8e7bfc818abcda59b22c8f1-r2zIW3";
        GlideUtil.loadGifOrImg(this,img_url,iv_01);
    }


    //Butterknife 自动生成的
    @OnClick({R.id.tv_res, R.id.tv_res2,
            R.id.tv_res3,R.id.tv_todownload,
            R.id.tv_toupload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_res:
                intent = new Intent(mContext, EventBusAc.class);
                startActivity(intent);
                break;
            case R.id.tv_toupload:
                intent = new Intent(mContext, UploadAc.class);
                startActivity(intent);
                break;
            case R.id.tv_todownload:
                intent = new Intent(mContext, DownloadAc.class);
                startActivity(intent);
                break;
            case R.id.tv_res2:
                break;
            case R.id.tv_res3:
                break;
        }
    }

    DbUtil dbUtil;
    List<UserBean> userBeanList;

    //数据库 示例
    private void initDb() {

        dbUtil = DbUtil.getInstance();
        userBeanList = new ArrayList<>();
        UserBean userBean;

        for (int i = 0; i < 4; i++) {
            userBean = new UserBean();
            userBean.setAge(13 + i);
            userBean.setName("张三");
            userBean.setSex("男");
            userBeanList.add(userBean);
        }
        for (int i = 0; i < 4; i++) {
            userBean = new UserBean();
            userBean.setAge(13);
            userBean.setName("张三");
            userBean.setSex("女");
            userBeanList.add(userBean);
        }
        userBean = new UserBean();
        userBean.setAge(23);
        userBean.setName("李四");
        userBean.setSex("女");
        userBeanList.add(userBean);


        userBean = new UserBean();
        userBean.setAge(33);
        userBean.setName("张三疯");
        userBean.setSex("男");
        userBeanList.add(userBean);

        LogUtil.e("list0=" + userBeanList.size());

        //删除整个表的数据
        dbUtil.deleteList(UserBean.class);

        Download1 download1 = new Download1();
        download1.execute();


    }




    private class Download1 extends AsyncTask<String, Integer, String> {

        public Download1() {

        }

        @Override
        protected String doInBackground(String... params) {
            //新增
            dbUtil.saveOrUpdate(userBeanList);
            return "完成";
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            super.onProgressUpdate(value);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //查询整个表
            List<UserBean> list = dbUtil.selectList(UserBean.class);
            LogUtil.e("list=" + list.size());

            tv_res.setText(list.size() + "个");

            //查询单个
            UserBean userBean = (UserBean) dbUtil.selectClassByKey(UserBean.class, "name", "张三");

            //修改
            //userBean.setName("张小三");
            //dbUtil.saveOrUpdate(userBean);


            //通过id 删除单个
            //dbUtil.deleteById(UserBean.class, userBean.getId());


            //模糊查询
            List<UserBean> list2 = dbUtil.selectClassByKeyLike(UserBean.class, "name", "%三%");
            LogUtil.e(list2.size() + "==list2=" + JsonUtil.objToString(list2));


            //多条件查询
            Map<String, Object> map = new HashMap<>();
            map.put("age", 13);
            map.put("sex", "女");
            List<UserBean> list3 = dbUtil.selectClassByMap(UserBean.class, "name", "张三", map);
            LogUtil.e(list3.size() + "==list3=" + JsonUtil.objToString(list3));
        }
    }





    private void doGet() {
        String url = "https://www.baidu.com/";

        OkHttpUtil.doGetJsonStrAsyn(false, url, null, new MyCallBack() {

            @Override
            public void failBack(String res_msg, int res_code) {

            }

            @Override
            public void successBack(String res_data) {

            }
        });

    }





    private void doPostBody() {
        //同步请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<>();
                map.put("platform", "android");
                map.put("vesion", "4.0.0");
                map.put("app", "apt");

                String http_url="http://www.fastpaotui.com/App/CommonApi/PostRequestBody";

                OkHttpUtil.doPostBody(http_url, map, new MyCallBack() {
                    @Override
                    public void failBack(String res_msg, int res_code) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_res0.setText(res_msg);
                            }
                        });
                    }

                    @Override
                    public void successBack(final String res_data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ResData resData = JsonUtil.dataToClass(res_data, ResData.class);
                                tv_res0.setText(resData.getData().toString());

                            }
                        });
                    }


                });

            }
        }).start();


    }

    private void doPostJsonStrAsyn() {
        //异步请求
        Map<String, String> map = new HashMap<>();
        map.put("platform", "android");
        map.put("vesion", "4.0.0");
        map.put("app", "apt");
        String http_url = "http://www.fastpaotui.com/App/CommonApi/PostJson";
        //String http_url = "http://192.168.0.5:8081/App/CommonApi/GetProvinceList";

        OkHttpUtil.doPostJsonStrAsyn(http_url, map, new MyCallBack() {
            @Override
            public void failBack(String res_msg, int res_code) {
                tv_res1.setText(res_msg);
            }

            @Override
            public void successBack(String res_data) {

                ResData resData = JsonUtil.dataToClass(res_data, ResData.class);
                tv_res1.setText(resData.getData().toString());

            }

        });
    }




    //EventBus   3.实现事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(WxCodeEB event) {
        if (event != null) {
            LogUtil.e("接收到事件");
            String code = event.getCode();
            tv_res3.setText("code=" + code);
        }
    }
}
