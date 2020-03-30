package com.hero;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.hero.adapter.AppleBean;
import com.hero.libhero.mydb.DbUtil;
import com.hero.libhero.mydb.LogUtil;
import com.hero.libhero.nfc.StringCharByteUtil;
import com.hero.libhero.okhttp.OkHttpUtil;
import com.hero.libhero.okhttp.https.MyCallBack;
import com.hero.libhero.utils.GlideUtil;
import com.hero.libhero.utils.JsonUtil;
import com.hero.libhero.utils.StatusBarUtils;
import com.hero.libhero.view.FlipTextView;
import com.hero.libhero.view.PayPassDialog;
import com.hero.libhero.view.PayPassView;
import com.hero.libhero.view.XToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

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

    @BindView(R.id.ftv)
    FlipTextView ftv;

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

        int height = StatusBarUtils.getStatusBarHeight(mContext);

        XToast.Config.get().setGravity(Gravity.CENTER); //显示在屏幕中央
        XToast.success(mContext, "状态栏高度=" + height).show();

        doGet();

        doPostBody();

        doPostJsonStrAsyn();

        initDb();

        pic();

        ftvInit();

        file();

        listToMapList();


        String text="123456779";
        StringCharByteUtil.strToHexStr(text);

    }


    public static byte[] int2ByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);

        LogUtil.e("==result.length=" + result.length);
        return result;
    }


    //相同的list 按照相同的字段 进行再分组
    private void listToMapList() {
        List<AppleBean> appleList = new ArrayList<>();//存放apple对象集合

        AppleBean apple1 = new AppleBean(1, "苹果1", new BigDecimal("3.25"), 10);
        AppleBean apple2 = new AppleBean(1, "苹果2", new BigDecimal("1.35"), 20);
        AppleBean apple3 = new AppleBean(1, "苹果3", new BigDecimal("1.35"), 15);

        AppleBean apple4 = new AppleBean(3, "荔枝", new BigDecimal("9.99"), 40);


        AppleBean apple5 = new AppleBean(2, "香蕉", new BigDecimal("2.89"), 30);
        AppleBean apple6 = new AppleBean(2, "香蕉2", new BigDecimal("3.89"), 30);

        appleList.add(apple6);
        appleList.add(apple1);
        appleList.add(apple2);
        appleList.add(apple5);
        appleList.add(apple3);
        appleList.add(apple4);

        //定义一个Map存放分组结果，key为分类名称，value为该分类出现的个数
        Map<Integer, List<AppleBean>> resultMap = new HashMap<>();
        for (AppleBean mUserBean : appleList) {
            if (resultMap.containsKey(mUserBean.getId())) {
                //map中存在此id，将数据存放当前key的map中
                resultMap.get(mUserBean.getId()).add(mUserBean);
            } else {
                //map中不存在，新建key，用来存放数据
                List<AppleBean> tmpList = new ArrayList<>();
                tmpList.add(mUserBean);
                resultMap.put(mUserBean.getId(), tmpList);
            }
        }


        //遍历Map集合的方法，输出List分组后的结果
        Set<Map.Entry<Integer, List<AppleBean>>> entrySet = resultMap.entrySet();
        for (Map.Entry<Integer, List<AppleBean>> entry : entrySet) {
            List<AppleBean> m = entry.getValue();
            LogUtil.e("分组:" + entry.getKey() + ":" + JsonUtil.objToString(m));
        }

    }


    private void file() {
        PermissionsUtil.requestPermission(MainActivity.this, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {

            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {

            }
        }, needPermissions);
    }

    //文字切换
    private void ftvInit() {
        int colorId = mActivity.getResources().getColor(R.color.colorPrimary);
        ftv.setColorId(colorId);
        ftv.setWait_Time(2000);

        List<String> stringList = new ArrayList<>();
        stringList.add("粘贴商品标题搜隐藏券拿返利");
        stringList.add("购物之前搜一搜,能省还能赚");
        stringList.add("第三行");
        ftv.setData(stringList);


    }

    private void pic() {
        String img_url = "http://img.hb.aicdn.com/43adcee6fa8d59ceb3219f8e7bfc818abcda59b22c8f1-r2zIW3";
        GlideUtil.loadGifOrImg(this, img_url, iv_01);
    }


    //Butterknife 自动生成的
    @OnClick({R.id.tv_res, R.id.tv_res2,
            R.id.tv_res3, R.id.tv_todownload,
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
                payDialog();
                break;
            case R.id.tv_res3:
                intent = new Intent(mContext, NfcAc.class);
                intent.putExtra("text", "温州浩兴科技有限公司");
                startActivity(intent);
                break;
        }
    }


    /**
     * 2 自定义方式
     */
    private void payDialog() {
        final PayPassDialog dialog = new PayPassDialog(mContext, R.style.dialog_pay_theme);
        //弹框自定义配置
        dialog.setAlertDialog(false)
                .setWindowSize(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f)
                .setOutColse(false)
                .setGravity(R.style.dialogOpenAnimation, Gravity.CENTER_VERTICAL);
        //组合控件自定义配置
        PayPassView payView = dialog.getPayViewPass();
        //payView.setForgetText("忘记支付密码?");
        payView.setHintText("请输入管理密码");
        payView.setTvHintSize(20);
//        payView.setForgetColor(getResources().getColor(R.color.colorAccent));
//        payView.setForgetSize(16);
        payView.setPayClickListener(new PayPassView.OnPayClickListener() {
            @Override
            public void onPassFinish(String passContent) {
                dialog.dismiss();
                XToast.success(mContext, passContent).show();
            }

            @Override
            public void onPayClose() {
                dialog.dismiss();
            }

            @Override
            public void onPayForget() {
                dialog.dismiss();
            }
        });
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
                Map<String, Object> map = new HashMap<>();
                map.put("platform", "android");
                map.put("vesion", "4.0.0");
                map.put("app", "apt");

                String http_url = "http://www.fastpaotui.com/App/CommonApi/PostRequestBody";

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
        Map<String, Object> map = new HashMap<>();
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
