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
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;

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
                {IsoDep.class.getName()},
                {NfcA.class.getName()},
                {NfcB.class.getName()},
                {MifareClassic.class.getName()},};

        LogUtil.e(" mTechLists" + NfcF.class.getName() + mTechList.length);


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
     * 每个扇区前3块存数据最后一块一般存密码。
     * 监测是否支持MifareClassic
     *
     * @param tag
     * @param
     * @return
     */
    public boolean isMifareClassic(Tag tag) {
        String[] techList = tag.getTechList();
        boolean haveMifareUltralight = false;
        for (String tech : techList) {
            if (tech.contains("MifareClassic")) {
                haveMifareUltralight = true;
                break;
            }
        }
        if (!haveMifareUltralight) {
            XToast.error(mActivity, "不支持MifareClassic").show();
            return false;
        }
        return true;
    }

    /**
     * 改写数据
     *
     * @param block
     * @param blockbyte
     */
    public boolean writeM1Block(Intent intent, int block, int bIndex, byte[] blockbyte) {

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        boolean isTrue = isMifareClassic(tag);
        if (isTrue == false) {
            return false;
        }
        LogUtil.e("blockbyte.length=" + blockbyte.length);

        MifareClassic mifareClassic = MifareClassic.get(tag);

        try {
            mifareClassic.connect();
            try {
                if (m1Auth(mifareClassic, block)) {
                    //写入第block个扇区 第block
                    mifareClassic.writeBlock(bIndex, blockbyte);//写入到第四块
                    LogUtil.e("writeBlock:" + "写入成功");
                } else {
                    LogUtil.e("没有找到密码");
                    return false;
                }
            } catch (IllegalArgumentException e) {
                LogUtil.e("IllegalArgumentException:" + e.getMessage());
                return false;
            }
        } catch (IOException e) {
            return false;
        } finally {
            try {
                mifareClassic.close();
            } catch (IOException e) {

            }
        }
        return true;
    }


    /**
     * 读扇区
     *
     * @return
     */
    public String readM1Block(Intent intent, int sectorIndex, int bIndex2) {

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        MifareClassic mfc = MifareClassic.get(tag);
        //读取TAG
        try {
            String metaInfo = "";
            //Enable I/O operations to the tag from this TagTechnology object.
            mfc.connect();
            int type = mfc.getType();//获取TAG的类型
            int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }
            metaInfo += "卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共" + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n";

            boolean auth = mfc.authenticateSectorWithKeyA(sectorIndex, MifareClassic.KEY_DEFAULT);
            if (auth) {
                metaInfo += "Sector " + sectorIndex + ":验证成功\n";
                byte[] data = mfc.readBlock(bIndex2);
                metaInfo += metaInfo + bytesToHexString(data) + "\n";
            } else {
                metaInfo += "Sector " + sectorIndex + ":验证失败:auth=" + auth + "\n";
            }
            LogUtil.e("metaInfo=" + metaInfo);
            return metaInfo;
        } catch (Exception e) {
            LogUtil.e("Exception=" + e.getMessage());
            XToast.error(mActivity, "Exception=" + e.getMessage()).show();
        } finally {
            if (mfc != null) {
                try {
                    mfc.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }


    public String readM1BlockAll(Intent intent) {

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        boolean isTrue = isMifareClassic(tag);
        if (isTrue == false) {
            return "";
        }


        MifareClassic mfc = MifareClassic.get(tag);
        //读取TAG
        try {
            String metaInfo = "";
            //Enable I/O operations to the tag from this TagTechnology object.
            mfc.connect();
            int type = mfc.getType();//获取TAG的类型
            int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }
            metaInfo += "卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共" + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n";


            //读取每一个扇区
            for (int j = 0; j < sectorCount; j++) {
                boolean auth = mfc.authenticateSectorWithKeyA(j, MifareClassic.KEY_DEFAULT);

                int bCount;
                int bIndex;
                if (auth) {
                    metaInfo += "Sector " + j + ":验证成功\n";
                    // 读取扇区中的块
                    bCount = mfc.getBlockCountInSector(j);
                    //读取每一个扇区的每一块
                    bIndex = mfc.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mfc.readBlock(bIndex);
                        metaInfo += "Block " + bIndex + " : "
                                + bytesToHexString(data) + "\n";
                        bIndex++;
                    }
                } else {
                    metaInfo += "Sector " + j + ":验证失败\n";
                }
            }

            LogUtil.e("metaInfo=" + metaInfo);

            return metaInfo;
        } catch (Exception e) {
            XToast.error(mActivity, e.getMessage()).show();
            return "Exception:" + e.getMessage();
        } finally {

            try {
                if (mfc != null) {
                    mfc.close();
                }
            } catch (IOException e) {
            }

        }
    }

    /**
     * 密码校验 这个扇区是否可以写入
     */
    public boolean m1Auth(MifareClassic mTag, int position) throws IOException {
        if (mTag.authenticateSectorWithKeyA(position, MifareClassic.KEY_DEFAULT)) {
            LogUtil.e("authenticateSectorWithKeyA");
            return true;
        } else if (mTag.authenticateSectorWithKeyB(position, MifareClassic.KEY_DEFAULT)) {
            LogUtil.e("authenticateSectorWithKeyB");
            return true;
        }
        return false;
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }
    ////////////////CPU卡读写//////////////////////
}
