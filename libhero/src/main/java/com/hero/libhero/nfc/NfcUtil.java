package com.hero.libhero.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.hero.libhero.mydb.LogUtil;
import com.hero.libhero.view.XToast;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;

/**
 * 创建 by hero
 * 时间 2020/3/27
 * 类名 nfc
 */
public class NfcUtil {

    public NfcAdapter mNfcAdapter;
    public IntentFilter[] mIntentFilter = null;
    public PendingIntent mPendingIntent = null;
    public String[][] mTechList = null;

    Activity mActivity;

    //////////////////初始化//////////////////////////
    public NfcUtil(Activity mActiv) {
        this.mActivity = mActiv;

        mNfcAdapter = NfcAdapter.getDefaultAdapter(mActivity);

        if (mNfcAdapter == null) {
            XToast.error(mActivity, "设备不支持NFC！").show();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            XToast.error(mActivity, "请在系统设置中先启用NFC功能！").show();
            IsToSet(mActivity);
            return;
        }

        mPendingIntent = PendingIntent.getActivity(mActivity, 0,
                new Intent(mActivity, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        try {
            ndef.addDataType("*/*");

        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mIntentFilter = new IntentFilter[]{ndef,};
        mTechList = new String[][]{
                {NfcA.class.getName()},
                {NfcB.class.getName()},
                {NfcF.class.getName()},
                {NfcV.class.getName()},
                {Ndef.class.getName()},
                {NdefFormatable.class.getName()},
                {IsoDep.class.getName()},
                {MifareUltralight.class.getName()},
                {MifareClassic.class.getName()},};


    }

    public NfcAdapter getmNfcAdapter() {
        return mNfcAdapter;
    }

    public void onPause() {
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(mActivity);
        }
    }

    public void onResume() {
        if (mNfcAdapter != null) {
            PendingIntent mPendingIntent = PendingIntent.getActivity(mActivity,
                    0, new Intent(mActivity, mActivity.getClass()), 0);
            mNfcAdapter.enableForegroundDispatch(mActivity, mPendingIntent,
                    null, null);
        }
    }

    private static void IsToSet(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("是否跳转到设置页面打开NFC功能");
//        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToSet(activity);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private static void goToSet(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivity(intent);
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivity(intent);
            return;
        }
    }


    ///////////////////NFCId//////////////////////

    /**
     * 读取nfcID
     */
    public String readNFCId(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String id = ByteArrayToHexString(tag.getId());
        return id;
    }

    /**
     * 将字节数组转换为字符串
     */
    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";
        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }


    ///////////////////////标签读写////////////////////////

    /**
     * 读取NFC的数据
     */
    public String readNFCFromTag(Intent intent) {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawArray != null) {
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            if (mNdefRecord != null) {
                return parseTextRecord(mNdefRecord);
            }
        }
        return "";
    }


    /**
     * 往nfc写入数据
     */
    public void writeNFCToTag(String data, Intent intent) throws IOException, FormatException {

        if (TextUtils.isEmpty(data)) {
            return;
        }

        NdefRecord ndefRecord = null;
        ndefRecord = createTextRecord(data);
        NdefRecord[] records = {ndefRecord};

        NdefMessage ndefMessage = new NdefMessage(records);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            NdefFormatable format = NdefFormatable.get(tag);
            format.connect();
            format.format(ndefMessage);
            if (format.isConnected()) {
                format.close();
            }
            return;
        }

        ndef.connect();
        if (!ndef.isWritable()) {
            LogUtil.e("当前设备不支持写入");
            return;
        }
        ndef.writeNdefMessage(ndefMessage);
        if (ndef.isConnected()) {
            ndef.close();
        }
        XToast.success(mActivity, "读写完毕").show();
    }


    /**
     * 解析NDEF文本数据，从第三个字节开始，后面的文本数据
     *
     * @param ndefRecord
     * @return
     */
    public static String parseTextRecord(NdefRecord ndefRecord) {
        /**
         * 判断数据是否为NDEF格式
         */
        //判断TNF
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        //判断可变的长度的类型
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            //获得字节数组，然后进行分析
            byte[] payload = ndefRecord.getPayload();
            //下面开始NDEF文本数据第一个字节，状态字节
            //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
            //其他位都是0，所以进行"位与"运算后就会保留最高位
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            LogUtil.e("parseTextRecord() textEncoding=" + textEncoding);
            //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
            int languageCodeLength = payload[0] & 0x3f;
            //下面开始NDEF文本数据第二个字节，语言编码
            //获得语言编码
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            LogUtil.e("parseTextRecord() languageCode=" + languageCode);
            //下面开始NDEF文本数据后面的字节，解析出文本
            String textRecord = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
            LogUtil.e("parseTextRecord() text=" + textRecord);
            return textRecord;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }


    /**
     * 创建NDEF文本数据
     *
     * @param text
     * @return
     */
    public static NdefRecord createTextRecord(String text) {
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = Charset.forName("UTF-8");
        //将文本转换为UTF-8格式
        byte[] textBytes = text.getBytes(utfEncoding);
        //设置状态字节编码最高位数为0
        int utfBit = 0;
        //定义状态字节
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        //设置第一个状态字节，先将状态码转换成字节
        data[0] = (byte) status;
        //设置语言编码，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1到langBytes.length的位置
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        //设置文本字节，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1 + langBytes.length
        //到textBytes.length的位置
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        //通过字节传入NdefRecord对象
        //NdefRecord.RTD_TEXT：传入类型 读写
        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return ndefRecord;
    }


    ///////////////////M1卡读写//////////////////////////

    /**
     * M1扇区默认是没有密码的，但有部分人闲不住要把密码改了，
     * 因此认证过程要加密码，一般认证KeyA就行。
     * 普通卡16个扇区64块，第一个扇区等闲不能操作。
     * 每个扇区4块，从0数起，第二扇区第一块索引就是8，
     * 每个扇区前3块存数据，最后一块一般存密码。
     * 监测是否支持MifareClassic
     *
     * @param intent
     * @param
     * @return
     */
    public boolean isMifareClassic(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String[] techList = tag.getTechList();
        boolean haveMifareUltralight = false;
        for (String tech : techList) {
            if (tech.contains("MifareClassic")) {
                haveMifareUltralight = true;
                break;
            }
        }
        if (!haveMifareUltralight) {
            //XToast.error(mActivity, "不支持MifareClassic").show();
            return false;
        }
        return true;
    }

    /**
     * 改写数据
     *
     * @param block
     * @param text
     */
    public boolean writeM1Block(Intent intent, int block, String text) throws IOException {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (isMifareClassic(intent) == false) {
            return false;
        }
        MifareClassic mTag = MifareClassic.get(tag);
        try {
            mTag.connect();
            boolean isAuth = false;
            if (m1Auth(mTag, block / 4)) {
                isAuth = true;
            }

            if (isAuth) {
                String text2 = StringCharByteUtil.strToHexStr(text);
                byte[] blockbyte = StringCharByteUtil.hexToByteArray(text2);
                //StringCharByteUtil.bytesToHexString(blockbyte);

                String text3 = "00";
                String text4 = "";
                if (blockbyte.length < 16) {//字节数组 一定要16位  如果不足16位 要用00占一位补齐
                    int num = 16 - blockbyte.length;
                    for (int i = 0; i < num; i++) {
                        text4 = text4 + text3;
                    }
                }
                LogUtil.e("text4=" + text4);
                String last_test = text4 + text2;
                byte[] last_bytes = StringCharByteUtil.hexToByteArray(last_test);

                // StringCharByteUtil.bytesToHexString(last_bytes);

                mTag.writeBlock(block, last_bytes);
                LogUtil.e("writeBlock：写入成功");
                return true;
            } else {
                LogUtil.e("writeBlock：写入失败");

                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();

            return false;
        } finally {
            //释放卡片
            mTag.close();

        }
    }


    /**
     * 读取M1卡片  某一扇区 某一块信息
     */
    public String readM1Block(Intent intent, int sectorIndex, int blockIndex) throws IOException {

        if (isMifareClassic(intent) == false) {
            return "";
        }
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        MifareClassic mTag = MifareClassic.get(tag);
        mTag.connect();
        // 读取TAG
        try {
            // 获取TAG中包含的扇区数
            int sectorCount = mTag.getSectorCount();
            for (int j = 0; j < sectorCount; j++) {

                if (j == sectorIndex) {
                    int bCount;//当前扇区的块数
                    int bIndex;//当前扇区第一块

                    boolean isAuth = m1Auth(mTag, sectorIndex);

                    if (isAuth) {
                        bCount = mTag.getBlockCountInSector(j);
                        bIndex = mTag.sectorToBlock(j);
                        for (int i = 0; i < bCount; i++) {

                            int pos = 4 * sectorIndex + blockIndex;
                            if (pos == bIndex) {
                                byte[] data2 = mTag.readBlock(pos);
                                //字节转十六进制
                                String hexStr = StringCharByteUtil.bytesToHexString(data2);
                                hexStr=hexStr.replaceAll("00 ","");
                                //十六进制转字符串
                                hexStr = StringCharByteUtil.hexToStr(hexStr);
                                Log.e("扇区：" + sectorIndex + "---位置：" + bIndex + "数据:", hexStr);

                                return hexStr;
                            }

                            bIndex++;
                        }
                    } else {
                        return "";
                    }
                }
            }
            return "";
        } catch (Exception e) {
            Log.e("读取数据错误", e.getMessage());
            e.printStackTrace();
        } finally {
            //释放卡片
            mTag.close();
        }
        return null;
    }

    //去掉字符串前面所有的 0
    private String clearZero(String str) {
        int len = str.length();//取得字符串的长度
        int index = 0;//预定义第一个非零字符串的位置

        char strs[] = str.toCharArray();// 将字符串转化成字符数组
        for (int i = 0; i < len; i++) {
            if ('0' != strs[i]) {
                index = i;// 找到非零字符串并跳出
                break;
            }
        }
        String strLast = str.substring(index, len);// 截取字符串

        return strLast;
    }


    /**
     * 密码校验 这个扇区是否可以写入
     */
    public static boolean m1Auth(MifareClassic mTag, int position) throws IOException {
        if (mTag.authenticateSectorWithKeyA(position, MifareClassic.KEY_DEFAULT)) {
            LogUtil.e("authenticateSectorWithKeyA");
            return true;
        } else if (mTag.authenticateSectorWithKeyB(position, MifareClassic.KEY_DEFAULT)) {
            LogUtil.e("authenticateSectorWithKeyB");
            return true;
        }
        return false;
    }

    ////////////////CPU卡读写//////////////////////


    /////////////////读取NTAG213/////////////////////////


    public boolean isMifareUltralight(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String[] techList = tag.getTechList();
        boolean haveMifareUltralight = false;
        for (String tech : techList) {
            if (tech.contains("MifareUltralight")) {
                haveMifareUltralight = true;
                break;
            }
        }
        if (!haveMifareUltralight) {
            return false;
        }
        return true;
    }

    int min_page = 4;
    int max_page = 39;

    public boolean writeStrNTAG213(Intent intent, int page, String text) {


        if (page < min_page) {
            XToast.error(mActivity, "page不能小于" + min_page).show();
            return false;
        }
        if (page > max_page) {
            XToast.error(mActivity, "page不能大于" + max_page).show();
            return false;
        }

        if (!isMifareUltralight(intent)) {
            return false;
        }
        try {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            MifareUltralight mTag = MifareUltralight.get(tag);
            mTag.connect();

            String text2 = StringCharByteUtil.strToHexStr(text);
            byte[] bytes = StringCharByteUtil.hexToByteArray(text2);
            StringCharByteUtil.bytesToHexString(bytes);

            int len = bytes.length;
            //一个 page  只能写入4个字节（相当于GB2312格式 两个汉字）
            int yu = len % 4;//取余
            LogUtil.e("取余=" + yu);

            String text3 = "00";
            String text4 = "";

            for (int i = 0; i < yu; i++) {
                text4 = text4 + text3;//不足4个字节 空字符补齐
            }
            LogUtil.e("text4=" + text4);
            String last_text = text4 + text2;
            LogUtil.e("last_text=" + last_text);
            byte[] last_bytes = StringCharByteUtil.hexToByteArray(last_text);

            int num = last_bytes.length / 4;
            LogUtil.e("num=" + num);

            for (int i = 0; i < num; i++) {
                int start = i * 4;
                byte[] bytes2 = SubArray(last_bytes, start, 4);
                mTag.writePage(page + i, bytes2);
            }
            mTag.close();
            LogUtil.e("写入成功:" + text);
            return true;
        } catch (IOException e) {
            return false;
        }

    }


    /**
     * 字节数组拆分
     *
     * @param paramArrayOfByte 原始数组
     * @param paramInt1        起始下标
     * @param paramInt2        要截取的长度
     * @return 处理后的数组
     */
    public static byte[] SubArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        byte[] arrayOfByte = new byte[paramInt2];
        int i = 0;
        while (true) {
            if (i >= paramInt2)
                return arrayOfByte;
            arrayOfByte[i] = paramArrayOfByte[(i + paramInt1)];
            i += 1;
        }
    }

    //清除4至39页
    public boolean clearNTAG4to39(Intent intent) {

        if (!isMifareUltralight(intent)) {
            return false;
        }
        try {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            MifareUltralight mTag = MifareUltralight.get(tag);
            mTag.connect();

            int text = 0;
            String text2 = StringCharByteUtil.numToHex(text);
            String text3 = "00";
            byte[] bb = StringCharByteUtil.hexToByteArray(text2);
            StringCharByteUtil.bytesToHexString(bb);
            String text4 = "";
            if (bb.length < 4) {
                int num = 4 - bb.length;
                for (int i = 0; i < num; i++) {
                    text4 = text4 + text3;
                }
            }
            LogUtil.e("text4=" + text4);
            bb = StringCharByteUtil.hexToByteArray(text4 + text2);

            for (int i = min_page; i < max_page; i++) {
                mTag.writePage(i, bb);
            }
            mTag.close();
            LogUtil.e("清除成功");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String readNTAG213(Intent intent) {

        if (!isMifareUltralight(intent)) {
            return "";
        }
        try {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            MifareUltralight mTag = MifareUltralight.get(tag);
            mTag.connect();


            byte[] data = mTag.readPages(min_page);

            String hexStr = StringCharByteUtil.bytesToHexString(data);// StringCharByteUtil.bytesToHexString2(data);
            hexStr=hexStr.replaceAll("00 ","");
            hexStr = StringCharByteUtil.hexToStr(hexStr);
            LogUtil.e("最后的readNTAG213=" + hexStr);


            mTag.close();
            return hexStr.trim();
        } catch (IOException e) {
            return "";
        }


    }

}
