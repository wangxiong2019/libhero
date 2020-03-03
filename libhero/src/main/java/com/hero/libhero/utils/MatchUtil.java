package com.hero.libhero.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/4/27.
 */
public class MatchUtil {

    //获取手机联系人
    public static String getPhone(Intent data, Activity mActivity) {

        String phonenum = "";
        Uri contactData = data.getData();
        if (contactData == null) {
            return phonenum;
        }
        ContentResolver cr = mActivity.getContentResolver();
        Cursor cursor = cr.query(contactData, null, null, null, null);

        if (cursor == null) {
            return phonenum;
        } else {


            if (cursor.moveToFirst()) { //将Cursor移动到第一条记录
                //String suspect = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); //通过Cursor c获得联系人名字
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));//通过Cursor c获得联系人id
                Cursor c2 = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
                //在ContactsContract.CommonDataKinds.Phone.CONTENT_URI里，// 通过上面获得的联系人id获得一个新的Cursor c2

                if (c2 != null) {
                    c2.moveToFirst();
                    try {
                        phonenum = c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); //通过Cursor c2获得联系人电//给控件赋值
                    } catch (CursorIndexOutOfBoundsException e) {
                        phonenum = "";
                    }

                    c2.close(); //关闭Cursor c2
                }

                cursor.close(); //关闭Cursor c

            }


            if (TextUtils.isEmpty(phonenum)) {
                phonenum = "";
            }
            if (phonenum.contains(" ")) {
                phonenum = phonenum.replace(" ", "");
            }
            if (phonenum.contains("-")) {
                phonenum = phonenum.replace("-", "");
            }

            Log.e("phonenum", phonenum);

            return phonenum;
        }
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
    public static boolean isNetTrue(Activity mActivity) {
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
