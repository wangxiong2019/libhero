package com.hero.libhero.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

public class PhoneUtil {

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
            phonenum = checkPhone(phonenum);

            Log.e("phonenum", phonenum);

            return phonenum;
        }
    }

    public static String checkPhone(String phonenum) {
        if (phonenum.contains(" ")) {
            phonenum = phonenum.replace(" ", "");
        }
        if (phonenum.contains("-")) {
            phonenum = phonenum.replace("-", "");
        }
        if (phonenum.contains("/")) {
            phonenum = phonenum.replace("/", "");
        }
        if (phonenum.contains("+86")) {
            phonenum = phonenum.replace("+86", "");
        }
        return phonenum;
    }

    public static String getName(Intent data, Activity mActivity) {

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
                        phonenum = c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)); //通过Cursor c2获得联系人电//给控件赋值
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
            if (EmojiFilterUtil.containsEmoji(phonenum)) {
                phonenum = EmojiFilterUtil.filterEmoji(phonenum);
            }

            Log.e("phonenum", phonenum);

            return phonenum;
        }
    }
}
