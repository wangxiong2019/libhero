package com.hero;

import android.content.Intent;
import android.widget.TextView;

import com.hero.libhero.nfc.NfcUtil;
import com.hero.libhero.nfc.StringCharByteUtil;

import butterknife.BindView;

import static com.hero.libhero.nfc.StringCharByteUtil.hexStringToBytes;

/**
 * 创建 by hero
 * 时间 2020/3/26
 * 类名
 */
public class NfcAc3 extends BaseActivty {
    @BindView(R.id.tv_01)
    TextView tv_01;
    @BindView(R.id.tv_02)
    TextView tv_02;
    @BindView(R.id.tv_03)
    TextView tv_03;

    @BindView(R.id.tv_04)
    TextView tv_04;


    private NfcUtil nfcUtil;

    @Override
    public int getLayout() {
        return R.layout.ac_nfc;
    }


    @Override
    public void initView() {
        nfcUtil = new NfcUtil(mActivity);
        if (nfcUtil.getmNfcAdapter() == null) {
            tv_01.setText("不支持nfc功能");
        }


    }

    int sectorIndex = 4;//第几个扇区
    int bIndex = 3;//第几个扇区中的第几块
    String text = "";//"9966332211445567";

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String id = nfcUtil.readNFCId(intent);
        tv_01.setText("id=" + id);

        //readTag(intent);

        writeM1Block(intent);

        //readM1Block(intent);

        tv_04.setText("值：" + nfcUtil.readM1BlockAll(intent));
    }

    private void readTag(Intent intent) {
        String str = nfcUtil.readNFCFromTag(intent);
        tv_03.setText("标签数据：" + str);
    }

    private void readM1Block(Intent intent) {
        String str = nfcUtil.readM1Block(intent, sectorIndex, bIndex);
        tv_03.setText("扇区数据：\n" + str);
    }

    private void writeM1Block(Intent intent) {

        text = "ZLSCSXS000000000";
        //text = StringCharByteUtil.strToHexStr(text);
        byte[] bb = text.getBytes();
//        String cc = StringCharByteUtil.strToHexStr(text);
//        StringCharByteUtil.hexTobytes(cc);// text.getBytes();

        //bb=StringCharByteUtil.str2bytearray(text);
        bb = "9966332211445567".getBytes();


        if (nfcUtil.writeM1Block(intent, sectorIndex, bIndex, bb)) {
            tv_02.setText("" + "写入成功");
        } else {
            tv_02.setText("" + "写入失败");
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        nfcUtil.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        nfcUtil.onResume();
    }


}
