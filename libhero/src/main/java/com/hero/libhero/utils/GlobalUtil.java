package com.hero.libhero.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/4/27.
 */
public class GlobalUtil {


    public static String clearLastZero(String score){
        if (score.indexOf(".") > 0) {
            //正则表达
            score = score.replaceAll("0+?$", "");//去掉后面无用的零
            score = score.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }

        return score;
    }


    private static final int TIME = 1000;
    private static long lastClickTime = 0;
    /**
     * 处理快速双击，多击事件，在TIME时间内只执行一次事件
     * if (GlobalUtil.isFastDoubleClick() == false) {
     *                     Log.e(TAG, "禁止双击");
     *  }
     */
    public static boolean isFastDoubleClick() {
        long currentTime = System.currentTimeMillis();
        long timeInterval = currentTime - lastClickTime;
        if (0 < timeInterval && timeInterval < TIME) {
            return true;
        }
        lastClickTime = currentTime;
        return false;
    }


    /**
     * 安卓自带复制功能
     * @param context
     * @param msg
     */
    public static void copyText(Context context,String msg){
        copyText(context,msg,true);
    }

    public static String COPY_LAST_TEXT="COPY_LAST_TEXT";
    public static void copyText(Context context,String msg,boolean isShowTip){

        SharedUtil.putString(COPY_LAST_TEXT,msg);
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager)context. getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", msg);
// 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        if (isShowTip){
            Toast.makeText(context,"复制成功",Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 获取剪切板的内容
     * @param context
     * @return
     */
    public static String getCopyString(Context context){
        // 获取系统剪贴板
        try{
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 返回数据
            ClipData clipData = clipboard.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                // 从数据集中获取（粘贴）第一条文本数据
                return clipData.getItemAt(0).getText().toString();
            }
        }catch (Exception e){

        }

        return null;

    }


    /**
     * 清除剪切板的内容
     * @param context
     */
    public static void  clearClipboard(Context context){
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            try {
                manager.setPrimaryClip(manager.getPrimaryClip());
                manager.setText(null);
            } catch (Exception e) {

            }
        }

    }


    /**
     *  设置edittext只能输入小数点后两位
     */
    public static void afterDotTwo(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 限制最多能输入9位整数
                if (s.toString().contains(".")) {
                    if (s.toString().indexOf(".") > 7) {
                        s = s.toString().subSequence(0,7) + s.toString().substring(s.toString().indexOf("."));
                        editText.setText(s);
                        editText.setSelection(7);
                    }
                }else {
                    if (s.toString().length() > 5){
                        s = s.toString().subSequence(0,5);
                        editText.setText(s);
                        editText.setSelection(5);
                    }
                }
                // 判断小数点后只能输入两位
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                //如果第一个数字为0，第二个不为点，就不允许输入
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().trim() != null && !editText.getText().toString().trim().equals("")) {
                    if (editText.getText().toString().trim().substring(0, 1).equals(".")) {
                        editText.setText("0" + editText.getText().toString().trim());
                        editText.setSelection(1);
                    }
                }
            }
        });
    }



    /**
     * 超过1万的转 以万为单位
     */
    public static String getPersonNumber(String number){
        if (TextUtils.isDigitsOnly(number)){
            Integer count = Integer.parseInt(number);
            if (count>=100000){
                return  count/10000+"万";
            }else if (count>=10000){
                if (count/1000%10>0){
                    return count/10000+"."+count/1000%10+"万";
                }else {
                    return count/10000+"万";
                }

            }else {
                return number;
            }

        }

        return "";
    }


    //浮点数 保留两位小数
    public static String getTwoPrice(float price) {
        if (TextUtils.isEmpty(price + "")) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
        String TwoPrice = df.format(price);
        return TwoPrice;
    }

    public static String getThreePrice(float price) {
        if (TextUtils.isEmpty(price + "")) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("0.000");//格式化小数，不足的补0
        String TwoPrice = df.format(price);
        return TwoPrice;
    }


    //比较两个 List 的值是否相等
    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }

    /**
     * 判断网络有无
     */
    public static boolean isNetTrue(Context mActivity) {
        boolean isNet = false;
        ConnectivityManager conManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            isNet = conManager.getActiveNetworkInfo().isAvailable();
        }
        return isNet;
    }

    /**
     * 判断是否为合法IP
     *
     * @return the ip
     */
    public static boolean isIpTrue(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    /**
     * 判断是否为合法子网掩码
     *
     * @return the ip
     */
    public static boolean isMask(String mask) {

        String reg = "/^(254|252|248|240|224|192|128|0)\\.0\\.0\\.0$|" +
                "^(255\\.(254|252|248|240|224|192|128|0)\\.0\\.0)$|" +
                "^(255\\.255\\.(254|252|248|240|224|192|128|0)\\.0)$|" +
                "^(255\\.255\\.255\\.(254|252|248|240|224|192|128|0))$/";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(mask);
        return matcher.matches();
    }

    /**
     * 手机正则表达式
     *
     * @return boolean
     */

    public static boolean isMobile(String mobile) {
        boolean flag = false;
        if (TextUtils.isEmpty(mobile)) {
            return false;
        }
        if (mobile.length() == 0) {
            return false;
        }
        String[] mobiles = mobile.split(",");
        int len = mobiles.length;
        if (len == 1) {
            //return Pattern.matches("^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,1,3,5-8])|(18[0-9])|(19[0-9]))\\d{8}$", mobile);
            return true;
        } else {

            for (int i = 0; i < len; i++) {
                if (isMobile(mobiles[i])) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }
        return flag;

    }

    // 校验18位身份证号码
    public static boolean isIDCard18(final String value) {
        if (value == null || value.length() != 18)
            return false;
        if (!value.matches("[\\d]+[X]?"))
            return false;
        String code = "10X98765432";
        int weight[] = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
                8, 4, 2, 1};
        int nSum = 0;
        for (int i = 0; i < 17; ++i) {
            nSum += (int) (value.charAt(i) - '0') * weight[i];
        }
        int nCheckNum = nSum % 11;
        char chrValue = value.charAt(17);
        char chrCode = code.charAt(nCheckNum);
        if (chrValue == chrCode)
            return true;
        if (nCheckNum == 2 && (chrValue + ('a' - 'A') == chrCode))
            return true;
        return false;
    }


    public static boolean isNum(String str) {

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;

    }

    //替换 （）以及括号里的内容  例：华厦世纪（厦门）地产--华厦世纪地产
    public static String ClearBracket(String company) {

        String bracket = company.substring(company.indexOf("<"), company.indexOf(">") + 1);
        company = company.replace(bracket, "");
        return company;
    }

    public static boolean isabc(String alpha) {
        if (alpha == null) return false;
        return alpha.matches("[a-z]+");
    }

    public static boolean isABC(String alpha) {
        if (alpha == null) return false;
        return alpha.matches("[A-Z]+");
    }

    public static boolean isABCabc(String alpha) {
        if (alpha == null) return false;
        return alpha.matches("[a-zA-Z]+");
    }

    public static boolean isChinese(String chineseContent) {
        if (chineseContent == null) return false;
        return chineseContent.matches("[\u4e00-\u9fa5]");
    }


    public static void setLvHeight(ListView listView) {
        if (listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int listItemHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            listItemHeight = listItem.getMeasuredHeight();
            totalHeight += listItemHeight;
        }
        Log.e("listItemHeight", "" + listItemHeight);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height = height;

        listView.setLayoutParams(params);
    }


}
